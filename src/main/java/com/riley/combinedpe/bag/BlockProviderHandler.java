package com.riley.combinedpe.bag;

import com.riley.combinedpe.CombinedPE;
import com.riley.combinedpe.integration.projecte.ProjectECompat;
import moze_intel.projecte.api.capabilities.IKnowledgeProvider;
import moze_intel.projecte.api.capabilities.PECapabilities;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

import java.math.BigInteger;

/**
 * Handles automatic block provision from Builder's Bags
 *
 * When a player places a block and the stack depletes, this handler:
 * 1. Searches all bags in the player's inventory
 * 2. Finds bags with the Supplier Module enabled
 * 3. Extracts matching items from the bag
 * 4. Refills the player's hand automatically
 */
@EventBusSubscriber(modid = CombinedPE.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class BlockProviderHandler {

    /**
     * Listen for right-click block placement events
     * This fires AFTER the block is placed, allowing us to refill the hand
     */
    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();

        // Only process on server side
        if (player.level().isClientSide) {
            return;
        }

        // Get the item stack that was used
        ItemStack usedStack = player.getItemInHand(event.getHand());

        // If the stack is now empty or nearly depleted, try to refill from bags
        if (usedStack.isEmpty() || usedStack.getCount() <= 1) {
            tryRefillFromBags(player, event.getHand());
        }
    }

    /**
     * Try to refill the player's hand from available Builder's Bags
     */
    private static void tryRefillFromBags(Player player, net.minecraft.world.InteractionHand hand) {
        ItemStack currentStack = player.getItemInHand(hand);
        Inventory inventory = player.getInventory();

        // Remember what item we need (if stack is empty, we won't know what to refill)
        // In this case, we need to track it differently - this is a simplified version
        if (currentStack.isEmpty()) {
            // TODO: Track last placed block type for empty hand refill
            return;
        }

        // Search all inventory slots for Builder's Bags with Supplier Module
        for (int slot = 0; slot < inventory.getContainerSize(); slot++) {
            ItemStack stack = inventory.getItem(slot);

            // Check if this is a Builder's Bag
            if (!ItemBuildersBag.isBag(stack)) {
                continue;
            }

            // Check if this bag has the Supplier Module
            BagTier tier = ItemBuildersBag.getTier(stack);
            if (!tier.hasSupplierModule()) {
                continue;
            }

            // Try to extract matching items from this bag
            BagInventory bagInventory = ItemBuildersBag.getInventory(stack);
            ExtractionResult result = extractMatchingItem(bagInventory, currentStack, 64);

            if (!result.extracted.isEmpty()) {
                // Successfully extracted - refill the hand
                currentStack.grow(result.extracted.getCount());

                // Update the bag's inventory with the modified version
                ItemBuildersBag.setInventory(stack, result.updatedInventory);

                CombinedPE.LOGGER.debug("Refilled {} x{} from bag in slot {}",
                    result.extracted.getItem(), result.extracted.getCount(), slot);

                // We found and used items, stop searching
                return;
            }
        }

        // If we couldn't find items in any bag, try EMC transmutation
        // This only works with Ultimate tier bags that have EMC Provider Module
        tryTransmuteFromEMC(player, hand, currentStack);
    }

    /**
     * Try to transmute items from the player's EMC using an Ultimate tier bag
     */
    private static void tryTransmuteFromEMC(Player player, net.minecraft.world.InteractionHand hand, ItemStack currentStack) {
        // Skip if stack is empty (can't determine what to transmute)
        if (currentStack.isEmpty()) {
            return;
        }

        // Check if player has an Ultimate bag with EMC Provider Module
        Inventory inventory = player.getInventory();
        boolean hasUltimateBag = false;

        for (int slot = 0; slot < inventory.getContainerSize(); slot++) {
            ItemStack stack = inventory.getItem(slot);

            if (ItemBuildersBag.isBag(stack)) {
                BagTier tier = ItemBuildersBag.getTier(stack);
                if (tier.hasEMCProviderModule()) {
                    hasUltimateBag = true;
                    break;
                }
            }
        }

        if (!hasUltimateBag) {
            return; // No Ultimate bag, can't use EMC
        }

        // Check if the item has EMC value
        if (!ProjectECompat.hasEMC(currentStack)) {
            CombinedPE.LOGGER.debug("Item {} has no EMC value, cannot transmute", currentStack.getItem());
            return;
        }

        long emcPerItem = ProjectECompat.getEMCValue(currentStack);
        int desiredCount = 64; // Try to get a full stack
        long totalEMCNeeded = emcPerItem * desiredCount;

        // Get player's knowledge capability
        IKnowledgeProvider knowledge = player.getCapability(PECapabilities.KNOWLEDGE_CAPABILITY);
        if (knowledge == null) {
            CombinedPE.LOGGER.debug("Player has no ProjectE knowledge capability");
            return;
        }

        // Check if player has learned this item
        if (!knowledge.hasKnowledge(currentStack)) {
            CombinedPE.LOGGER.debug("Player hasn't learned {}, cannot transmute", currentStack.getItem());
            return;
        }

        // Check if player has enough EMC
        BigInteger playerEMC = knowledge.getEmc();
        BigInteger emcNeeded = BigInteger.valueOf(totalEMCNeeded);

        if (playerEMC.compareTo(emcNeeded) < 0) {
            // Not enough EMC for full stack, try to get as many as possible
            long affordableCount = playerEMC.divide(BigInteger.valueOf(emcPerItem)).longValue();
            if (affordableCount <= 0) {
                CombinedPE.LOGGER.debug("Player has insufficient EMC to transmute {}", currentStack.getItem());
                return;
            }
            desiredCount = (int) Math.min(affordableCount, 64);
            totalEMCNeeded = emcPerItem * desiredCount;
            emcNeeded = BigInteger.valueOf(totalEMCNeeded);
        }

        // Deduct EMC from player
        BigInteger newEMC = playerEMC.subtract(emcNeeded);
        knowledge.setEmc(newEMC);

        // Sync EMC to client
        if (player instanceof net.minecraft.server.level.ServerPlayer serverPlayer) {
            knowledge.syncEmc(serverPlayer);
        }

        // Create the transmuted items
        ItemStack transmuted = currentStack.copy();
        transmuted.setCount(desiredCount);

        // Add to player's hand
        currentStack.grow(desiredCount);

        CombinedPE.LOGGER.info("Transmuted {} x{} using {} EMC from player's knowledge",
            transmuted.getItem(), desiredCount, totalEMCNeeded);
    }

    /**
     * Result of an extraction operation
     */
    private record ExtractionResult(ItemStack extracted, BagInventory updatedInventory) {}

    /**
     * Extract matching items from a bag's inventory
     *
     * @param inventory The bag's inventory
     * @param template The item to match
     * @param maxCount Maximum items to extract
     * @return ExtractionResult containing extracted items and updated inventory
     */
    private static ExtractionResult extractMatchingItem(BagInventory inventory, ItemStack template, int maxCount) {
        int remaining = maxCount;
        ItemStack result = ItemStack.EMPTY;
        BagInventory updatedInventory = inventory;

        // Search all slots in the bag
        for (int slot = 0; slot < inventory.getSlots(); slot++) {
            ItemStack slotStack = updatedInventory.getStackInSlot(slot);

            // Skip empty slots or non-matching items
            if (slotStack.isEmpty()) {
                continue;
            }

            if (!ItemStack.isSameItemSameComponents(slotStack, template)) {
                continue;
            }

            // Calculate how much we can take from this slot
            int toExtract = Math.min(remaining, slotStack.getCount());

            // Initialize result stack if this is the first extraction
            if (result.isEmpty()) {
                result = slotStack.copy();
                result.setCount(toExtract);
            } else {
                result.grow(toExtract);
            }

            // Remove items from the bag slot (creates new inventory instance)
            ItemStack newSlotStack = slotStack.copy();
            newSlotStack.shrink(toExtract);

            if (newSlotStack.isEmpty()) {
                updatedInventory = updatedInventory.setStackInSlot(slot, ItemStack.EMPTY);
            } else {
                updatedInventory = updatedInventory.setStackInSlot(slot, newSlotStack);
            }

            remaining -= toExtract;

            // If we've extracted enough, stop
            if (remaining <= 0) {
                break;
            }
        }

        return new ExtractionResult(result, updatedInventory);
    }
}
