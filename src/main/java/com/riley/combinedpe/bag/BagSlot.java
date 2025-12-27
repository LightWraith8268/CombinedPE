package com.riley.combinedpe.bag;

import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import org.jetbrains.annotations.NotNull;

/**
 * Custom slot for bag inventory
 * Handles virtual inventory backed by data component
 */
public class BagSlot extends Slot {

    private final BagInventory bagInventory;
    private final ItemStack bagStack;
    private final int slotIndex;

    public BagSlot(BagInventory bagInventory, ItemStack bagStack, int slotIndex, int x, int y) {
        super(createDummyContainer(bagInventory.getSlots()), slotIndex, x, y);
        this.bagInventory = bagInventory;
        this.bagStack = bagStack;
        this.slotIndex = slotIndex;
    }

    /**
     * Create a dummy container for the slot
     * We don't use this for actual storage, just for slot positioning
     */
    private static Container createDummyContainer(int size) {
        return new SimpleContainer(size);
    }

    @Override
    public @NotNull ItemStack getItem() {
        return bagInventory.getStackInSlot(slotIndex);
    }

    @Override
    public void set(@NotNull ItemStack stack) {
        BagInventory newInventory = bagInventory.setStackInSlot(slotIndex, stack);
        bagStack.set(com.riley.combinedpe.registry.ModDataComponents.BAG_INVENTORY.get(), newInventory);
        this.setChanged();
    }

    @Override
    public void onTake(@NotNull net.minecraft.world.entity.player.Player player, @NotNull ItemStack stack) {
        // Clear the slot
        set(ItemStack.EMPTY);
        super.onTake(player, stack);
    }

    @Override
    public @NotNull ItemStack remove(int amount) {
        ItemStack currentStack = getItem();
        if (currentStack.isEmpty()) {
            return ItemStack.EMPTY;
        }

        ItemStack removed = currentStack.split(amount);
        set(currentStack);
        return removed;
    }

    @Override
    public int getMaxStackSize() {
        // Get stack upgrade tier from bag
        StackUpgradeTier upgrade = bagStack.getOrDefault(
            com.riley.combinedpe.registry.ModDataComponents.STACK_UPGRADE.get(),
            StackUpgradeTier.NONE
        );

        // Calculate max stack size based on tier's natural multiplier and upgrade
        if (bagStack.getItem() instanceof ItemBuildersBag bagItem) {
            BagTier bagTier = bagItem.getTier();
            int baseMax = bagTier.getMaxStackSize(); // Tier's natural max (64, 256, 1024, etc.)
            int upgradeMultiplier = upgrade.getMultiplier();
            return baseMax * upgradeMultiplier;
        }

        return 64; // Fallback
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        // Prevent placing bags inside bags (infinite nesting prevention)
        return !(stack.getItem() instanceof ItemBuildersBag);
    }
}
