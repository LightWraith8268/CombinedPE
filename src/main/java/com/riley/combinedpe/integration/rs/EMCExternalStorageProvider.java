package com.riley.combinedpe.integration.rs;

import com.refinedmods.refinedstorage.api.resource.ResourceAmount;
import com.refinedmods.refinedstorage.api.storage.external.ExternalStorageProvider;
import com.refinedmods.refinedstorage.common.support.resource.ItemResource;
import com.riley.combinedpe.compat.ProjectECompat;
import moze_intel.projecte.api.ItemInfo;
import moze_intel.projecte.api.capabilities.IKnowledgeProvider;
import moze_intel.projecte.api.capabilities.PECapabilities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * RS External Storage provider for ProjectE EMC access.
 *
 * When attached to a block (typically near a Transmutation Table), this provider
 * allows Refined Storage to bidirectionally interact with the nearest player's EMC.
 *
 * Design approach:
 * - Finds nearest player within 16 blocks
 * - Uses that player's ProjectE knowledge and EMC balance
 * - Lists ALL learned items in RS Grid with quantities based on available EMC
 * - On extract: deducts EMC cost from player's balance (transmutation)
 * - On insert: converts items to EMC and learns them if unknown
 *
 * This provides full EMC integration - transmute items from EMC or convert items
 * to EMC, all through the RS Grid interface. Set External Storage priority high
 * to prefer EMC conversion over regular storage.
 */
public class EMCExternalStorageProvider implements ExternalStorageProvider {

    private final ServerLevel level;
    private final BlockPos pos;

    public EMCExternalStorageProvider(ServerLevel level, BlockPos pos) {
        this.level = level;
        this.pos = pos;
    }

    /**
     * Find the nearest player to the External Storage block.
     */
    @Nullable
    private ServerPlayer findNearestPlayer() {
        // Search within 16 blocks (RS External Storage typical range)
        AABB searchBox = new AABB(pos).inflate(16.0);
        List<ServerPlayer> nearbyPlayers = level.getEntitiesOfClass(
            ServerPlayer.class,
            searchBox,
            player -> player.distanceToSqr(pos.getX(), pos.getY(), pos.getZ()) <= 256.0 // 16^2
        );

        if (nearbyPlayers.isEmpty()) {
            return null;
        }

        // Return closest player
        ServerPlayer closest = nearbyPlayers.get(0);
        double closestDist = closest.distanceToSqr(pos.getX(), pos.getY(), pos.getZ());

        for (ServerPlayer player : nearbyPlayers) {
            double dist = player.distanceToSqr(pos.getX(), pos.getY(), pos.getZ());
            if (dist < closestDist) {
                closest = player;
                closestDist = dist;
            }
        }

        return closest;
    }

    /**
     * Get the player's knowledge provider capability.
     */
    @Nullable
    private IKnowledgeProvider getKnowledge(ServerPlayer player) {
        return player.getCapability(PECapabilities.KNOWLEDGE_CAPABILITY);
    }

    /**
     * Calculate maximum quantity of an item that can be extracted based on player's EMC.
     */
    private long calculateMaxQuantity(IKnowledgeProvider knowledge, ItemStack stack) {
        if (!ProjectECompat.hasEMC(stack)) {
            return 0;
        }

        long emcPerItem = ProjectECompat.getEMCValue(stack);
        if (emcPerItem <= 0) {
            return 0;
        }

        BigInteger playerEMC = knowledge.getEmc();
        BigInteger emcPerItemBig = BigInteger.valueOf(emcPerItem);

        // Calculate how many items the player can afford
        long maxQuantity = playerEMC.divide(emcPerItemBig).longValue();

        // Cap at Integer.MAX_VALUE for safety
        return Math.min(maxQuantity, Integer.MAX_VALUE);
    }

    @Override
    public Iterator<ResourceAmount> iterator() {
        List<ResourceAmount> resources = new ArrayList<>();

        // Check if this External Storage has an EMC link configured
        EMCLink.LinkData link = EMCLink.getLink(level, pos);
        if (link == null) {
            // No link configured - return empty (prevents performance issues)
            return resources.iterator();
        }

        // Find the player who owns this link
        ServerPlayer player = level.getServer().getPlayerList().getPlayer(link.playerUUID);
        if (player == null) {
            // Player offline - return empty
            return resources.iterator();
        }

        // Get player's knowledge
        IKnowledgeProvider knowledge = getKnowledge(player);
        if (knowledge == null) {
            return resources.iterator();
        }

        // Check if player has learned the linked item
        ItemInfo itemInfo = ItemInfo.fromStack(link.linkedItem);
        if (!knowledge.hasKnowledge(itemInfo)) {
            // Player hasn't learned this item yet - return empty
            return resources.iterator();
        }

        // Calculate how many of the linked item the player can afford
        long maxQuantity = calculateMaxQuantity(knowledge, link.linkedItem);
        if (maxQuantity > 0) {
            // Show the linked item with quantity based on player's EMC
            ItemResource itemResource = ItemResource.ofItemStack(link.linkedItem);
            resources.add(new ResourceAmount(itemResource, maxQuantity));
        }

        return resources.iterator();
    }

    @Override
    public long insert(com.refinedmods.refinedstorage.api.resource.ResourceKey resource,
                      long amount,
                      com.refinedmods.refinedstorage.api.core.Action action,
                      com.refinedmods.refinedstorage.api.storage.Actor actor) {
        ServerPlayer player = findNearestPlayer();
        if (player == null) {
            return 0;
        }

        IKnowledgeProvider knowledge = getKnowledge(player);
        if (knowledge == null) {
            return 0;
        }

        // Resource should be an ItemResource
        if (!(resource instanceof ItemResource itemResource)) {
            return 0;
        }

        // Convert to ItemStack
        ItemStack stack = itemResource.toItemStack();
        if (stack.isEmpty()) {
            return 0;
        }

        // Check if item has EMC value
        if (!ProjectECompat.hasEMC(stack)) {
            return 0; // Can't convert items without EMC
        }

        // Limit to int range for safety
        long toInsert = Math.min(amount, Integer.MAX_VALUE);

        if (action == com.refinedmods.refinedstorage.api.core.Action.EXECUTE) {
            // Convert items to EMC
            long emcPerItem = ProjectECompat.getEMCValue(stack);
            long totalEMCGained = emcPerItem * toInsert;

            // Add EMC to player
            BigInteger currentEMC = knowledge.getEmc();
            BigInteger newEMC = currentEMC.add(BigInteger.valueOf(totalEMCGained));
            knowledge.setEmc(newEMC);
            knowledge.syncEmc(player);

            // Learn the item if not already known
            ItemInfo itemInfo = ItemInfo.fromStack(stack);
            if (!knowledge.hasKnowledge(itemInfo)) {
                knowledge.addKnowledge(itemInfo);
                knowledge.syncKnowledgeChange(player, itemInfo, true);
            }
        }

        // Successfully "inserted" (converted to EMC)
        return toInsert;
    }

    @Override
    public long extract(com.refinedmods.refinedstorage.api.resource.ResourceKey resource,
                       long amount,
                       com.refinedmods.refinedstorage.api.core.Action action,
                       com.refinedmods.refinedstorage.api.storage.Actor actor) {
        ServerPlayer player = findNearestPlayer();
        if (player == null) {
            return 0;
        }

        IKnowledgeProvider knowledge = getKnowledge(player);
        if (knowledge == null) {
            return 0;
        }

        // Resource should be an ItemResource
        if (!(resource instanceof ItemResource itemResource)) {
            return 0;
        }

        // Convert to ItemStack to check EMC and knowledge
        ItemStack stack = itemResource.toItemStack();
        if (stack.isEmpty()) {
            return 0;
        }

        // Check if player has learned this item
        ItemInfo itemInfo = ItemInfo.fromStack(stack);
        if (!knowledge.hasKnowledge(itemInfo)) {
            return 0;
        }

        // Check if item has EMC value
        if (!ProjectECompat.hasEMC(stack)) {
            return 0;
        }

        // Calculate maximum quantity based on EMC
        long maxQuantity = calculateMaxQuantity(knowledge, stack);
        if (maxQuantity <= 0) {
            return 0;
        }

        // Limit extraction to what's available
        long actualAmount = Math.min(amount, maxQuantity);

        if (action == com.refinedmods.refinedstorage.api.core.Action.EXECUTE) {
            // Deduct EMC from player
            long emcPerItem = ProjectECompat.getEMCValue(stack);
            long totalEMCCost = emcPerItem * actualAmount;

            BigInteger playerEMC = knowledge.getEmc();
            BigInteger newEMC = playerEMC.subtract(BigInteger.valueOf(totalEMCCost));

            knowledge.setEmc(newEMC);
            knowledge.syncEmc(player);
        }

        return actualAmount;
    }
}
