package com.riley.combinedpe.integration.rs;

import com.refinedmods.refinedstorage.api.resource.ResourceAmount;
import com.refinedmods.refinedstorage.api.storage.external.ExternalStorageProvider;
import com.refinedmods.refinedstorage.common.support.resource.ItemResource;
import com.riley.combinedpe.bag.ItemBuildersBag;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * RS External Storage provider for Builder's Bag inventory access.
 *
 * When attached to a block, this provider allows Refined Storage to see and
 * interact with the nearest player's equipped Builder's Bag inventory.
 *
 * Design approach:
 * - Finds nearest player within 16 blocks
 * - Looks for Builder's Bag in player's hands (main or offhand)
 * - Exposes bag's inventory through RS External Storage
 * - Supports insert and extract operations
 * - Multiple bags show as separate storage sources
 *
 * This allows players to access their bag contents through RS Grid!
 */
public class BagExternalStorageProvider implements ExternalStorageProvider {

    private final ServerLevel level;
    private final BlockPos pos;

    public BagExternalStorageProvider(ServerLevel level, BlockPos pos) {
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
     * Get the player's Builder's Bag item handler.
     * Checks main hand first, then offhand.
     */
    @Nullable
    private IItemHandler getBagHandler(ServerPlayer player) {
        // Check main hand
        ItemStack mainHand = player.getMainHandItem();
        if (mainHand.getItem() instanceof ItemBuildersBag) {
            return ItemBuildersBag.getItemHandler(mainHand);
        }

        // Check offhand
        ItemStack offHand = player.getOffhandItem();
        if (offHand.getItem() instanceof ItemBuildersBag) {
            return ItemBuildersBag.getItemHandler(offHand);
        }

        return null;
    }

    @Override
    public Iterator<ResourceAmount> iterator() {
        List<ResourceAmount> resources = new ArrayList<>();

        // Find nearest player
        ServerPlayer player = findNearestPlayer();
        if (player == null) {
            return resources.iterator();
        }

        // Get bag handler
        IItemHandler handler = getBagHandler(player);
        if (handler == null) {
            return resources.iterator();
        }

        // List all items in the bag
        for (int slot = 0; slot < handler.getSlots(); slot++) {
            ItemStack stack = handler.getStackInSlot(slot);
            if (stack.isEmpty()) {
                continue;
            }

            // Convert to RS ItemResource
            ItemResource itemResource = ItemResource.ofItemStack(stack);

            // Add to resource list with current quantity
            resources.add(new ResourceAmount(itemResource, stack.getCount()));
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

        IItemHandler handler = getBagHandler(player);
        if (handler == null) {
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

        // Limit to int range for safety
        int toInsert = (int) Math.min(amount, Integer.MAX_VALUE);
        stack.setCount(toInsert);

        // Try to insert into all slots
        long totalInserted = 0;
        ItemStack remaining = stack.copy();

        for (int slot = 0; slot < handler.getSlots(); slot++) {
            if (remaining.isEmpty()) {
                break;
            }

            ItemStack notInserted = handler.insertItem(
                slot,
                remaining,
                action != com.refinedmods.refinedstorage.api.core.Action.EXECUTE
            );

            int inserted = remaining.getCount() - notInserted.getCount();
            totalInserted += inserted;
            remaining = notInserted;
        }

        return totalInserted;
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

        IItemHandler handler = getBagHandler(player);
        if (handler == null) {
            return 0;
        }

        // Resource should be an ItemResource
        if (!(resource instanceof ItemResource itemResource)) {
            return 0;
        }

        // Convert to ItemStack for matching
        ItemStack targetStack = itemResource.toItemStack();
        if (targetStack.isEmpty()) {
            return 0;
        }

        // Limit to int range for safety
        int toExtract = (int) Math.min(amount, Integer.MAX_VALUE);

        // Try to extract from all slots
        long totalExtracted = 0;
        int remaining = toExtract;

        for (int slot = 0; slot < handler.getSlots(); slot++) {
            if (remaining <= 0) {
                break;
            }

            ItemStack slotStack = handler.getStackInSlot(slot);
            if (slotStack.isEmpty()) {
                continue;
            }

            // Check if slot contains the target item
            if (!ItemStack.isSameItemSameComponents(slotStack, targetStack)) {
                continue;
            }

            // Extract what we can from this slot
            ItemStack extracted = handler.extractItem(
                slot,
                remaining,
                action != com.refinedmods.refinedstorage.api.core.Action.EXECUTE
            );

            if (!extracted.isEmpty()) {
                totalExtracted += extracted.getCount();
                remaining -= extracted.getCount();
            }
        }

        return totalExtracted;
    }
}
