package com.riley.combinedpe.bag;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Container menu for Builder's Bag
 * Handles inventory slots and item transfer logic
 */
public class BagMenu extends AbstractContainerMenu {

    private static final int SLOTS_PER_ROW = 18; // 2 chests wide
    private static final int PLAYER_INVENTORY_ROWS = 3;
    private static final int PLAYER_INVENTORY_COLUMNS = 9;
    private static final int HOTBAR_SLOTS = 9;

    private final ItemStack bagStack;
    private final BagInventory bagInventory;
    private final BagTier tier;
    private final int totalSlots;

    public BagMenu(int containerId, Inventory playerInventory, ItemStack bagStack) {
        super(com.riley.combinedpe.core.ModMenuTypes.BAG_MENU.get(), containerId);
        this.bagStack = bagStack;
        this.bagInventory = ItemBuildersBag.getInventory(bagStack);

        // Get tier from bag item
        if (bagStack.getItem() instanceof ItemBuildersBag bagItem) {
            this.tier = bagItem.getTier();
        } else {
            this.tier = BagTier.BASIC; // Fallback
        }

        this.totalSlots = bagInventory.getSlots();

        // Add bag inventory slots
        addBagSlots();

        // Add player inventory slots (below bag inventory)
        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
    }

    /**
     * Add all bag inventory slots to the menu
     */
    private void addBagSlots() {
        int slotIndex = 0;
        int totalRows = getTotalRows();

        for (int row = 0; row < totalRows; row++) {
            for (int col = 0; col < SLOTS_PER_ROW; col++) {
                if (slotIndex >= totalSlots) break;

                // Virtual Y position (screen will handle actual rendering with scrolling)
                int x = 8 + col * 18;
                int y = 18 + row * 18;

                this.addSlot(new BagSlot(bagInventory, bagStack, slotIndex++, x, y));
            }
        }
    }

    /**
     * Add player inventory slots (3 rows × 9 columns)
     */
    private void addPlayerInventory(Inventory playerInventory) {
        int startY = getPlayerInventoryYOffset();

        for (int row = 0; row < PLAYER_INVENTORY_ROWS; row++) {
            for (int col = 0; col < PLAYER_INVENTORY_COLUMNS; col++) {
                int slotIndex = col + row * PLAYER_INVENTORY_COLUMNS + HOTBAR_SLOTS;
                int x = 8 + col * 18;
                int y = startY + row * 18;
                this.addSlot(new Slot(playerInventory, slotIndex, x, y));
            }
        }
    }

    /**
     * Add player hotbar slots (1 row × 9 columns)
     */
    private void addPlayerHotbar(Inventory playerInventory) {
        int startY = getPlayerInventoryYOffset() + 58; // 3 rows + 4px gap

        for (int col = 0; col < HOTBAR_SLOTS; col++) {
            int x = 8 + col * 18;
            this.addSlot(new Slot(playerInventory, col, x, startY));
        }
    }

    /**
     * Get the number of rows needed for the bag inventory
     */
    private int getTotalRows() {
        return (int) Math.ceil(totalSlots / (double) SLOTS_PER_ROW);
    }

    /**
     * Get the Y offset for player inventory (below bag slots area)
     */
    private int getPlayerInventoryYOffset() {
        // 18px header + (6 visible rows × 18px) + 14px padding
        return 18 + (6 * 18) + 14;
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        ItemStack resultStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            resultStack = slotStack.copy();

            int bagSlotCount = totalSlots;
            int playerInventoryStart = bagSlotCount;
            int playerInventoryEnd = playerInventoryStart + 36; // 27 + 9 hotbar

            if (index < bagSlotCount) {
                // Moving from bag to player inventory
                if (!this.moveItemStackTo(slotStack, playerInventoryStart, playerInventoryEnd, true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                // Moving from player inventory to bag
                if (!this.moveItemStackTo(slotStack, 0, bagSlotCount, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (slotStack.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (slotStack.getCount() == resultStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, slotStack);
        }

        return resultStack;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        // Check if player still has the bag in inventory
        return player.getInventory().contains(bagStack);
    }

    /**
     * Get the bag's tier
     */
    public BagTier getTier() {
        return tier;
    }

    /**
     * Get the total number of bag slots
     */
    public int getTotalSlots() {
        return totalSlots;
    }

    /**
     * Get the bag inventory
     */
    public BagInventory getBagInventory() {
        return bagInventory;
    }

    /**
     * Get the bag ItemStack
     */
    public ItemStack getBagStack() {
        return bagStack;
    }
}
