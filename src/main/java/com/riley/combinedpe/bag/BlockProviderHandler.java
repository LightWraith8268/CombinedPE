package com.riley.combinedpe.bag;

import com.riley.combinedpe.CombinedPE;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

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
