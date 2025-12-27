package com.riley.combinedpe.bag;

import com.riley.combinedpe.registry.ModDataComponents;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

/**
 * IItemHandler wrapper for Builder's Bag inventory
 * Allows other mods to interact with the bag through the standard capability system
 */
public class BagItemHandler implements IItemHandler {

    private final ItemStack bagStack;

    public BagItemHandler(ItemStack bagStack) {
        if (!(bagStack.getItem() instanceof ItemBuildersBag)) {
            throw new IllegalArgumentException("BagItemHandler requires a Builder's Bag ItemStack");
        }
        this.bagStack = bagStack;
    }

    /**
     * Get the bag's inventory from the data component
     */
    private BagInventory getInventory() {
        return ItemBuildersBag.getInventory(bagStack);
    }

    /**
     * Update the bag's inventory data component
     */
    private void setInventory(BagInventory inventory) {
        ItemBuildersBag.setInventory(bagStack, inventory);
    }

    /**
     * Get the maximum stack size for a slot based on tier and upgrades
     */
    private int getMaxStackSize() {
        StackUpgradeTier upgrade = bagStack.getOrDefault(
            ModDataComponents.STACK_UPGRADE.get(),
            StackUpgradeTier.NONE
        );

        if (bagStack.getItem() instanceof ItemBuildersBag bagItem) {
            BagTier tier = bagItem.getTier();
            int baseMax = tier.getMaxStackSize();
            int upgradeMultiplier = upgrade.getMultiplier();
            return baseMax * upgradeMultiplier;
        }

        return 64;
    }

    @Override
    public int getSlots() {
        return getInventory().getSlots();
    }

    @Override
    public @NotNull ItemStack getStackInSlot(int slot) {
        if (slot < 0 || slot >= getSlots()) {
            return ItemStack.EMPTY;
        }
        return getInventory().getStackInSlot(slot);
    }

    @Override
    public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        }

        if (slot < 0 || slot >= getSlots()) {
            return stack;
        }

        // Prevent bag-in-bag
        if (stack.getItem() instanceof ItemBuildersBag) {
            return stack;
        }

        BagInventory inventory = getInventory();
        ItemStack existing = inventory.getStackInSlot(slot);

        // Calculate max stack size based on tier and upgrade
        int maxStackSize = getMaxStackSize();

        if (existing.isEmpty()) {
            // Empty slot - insert up to maxStackSize
            int toInsert = Math.min(stack.getCount(), maxStackSize);
            ItemStack newStack = stack.copy();
            newStack.setCount(toInsert);

            if (!simulate) {
                BagInventory newInventory = inventory.setStackInSlot(slot, newStack);
                setInventory(newInventory);
            }

            // Return remainder
            if (stack.getCount() > toInsert) {
                ItemStack remainder = stack.copy();
                remainder.setCount(stack.getCount() - toInsert);
                return remainder;
            }
            return ItemStack.EMPTY;

        } else if (ItemStack.isSameItemSameComponents(existing, stack)) {
            // Same item - try to stack
            int space = maxStackSize - existing.getCount();
            if (space <= 0) {
                return stack; // Slot full
            }

            int toInsert = Math.min(stack.getCount(), space);
            if (!simulate) {
                ItemStack newStack = existing.copy();
                newStack.setCount(existing.getCount() + toInsert);
                BagInventory newInventory = inventory.setStackInSlot(slot, newStack);
                setInventory(newInventory);
            }

            // Return remainder
            if (stack.getCount() > toInsert) {
                ItemStack remainder = stack.copy();
                remainder.setCount(stack.getCount() - toInsert);
                return remainder;
            }
            return ItemStack.EMPTY;

        } else {
            // Different item - can't insert
            return stack;
        }
    }

    @Override
    public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (amount <= 0) {
            return ItemStack.EMPTY;
        }

        if (slot < 0 || slot >= getSlots()) {
            return ItemStack.EMPTY;
        }

        BagInventory inventory = getInventory();
        ItemStack existing = inventory.getStackInSlot(slot);

        if (existing.isEmpty()) {
            return ItemStack.EMPTY;
        }

        int toExtract = Math.min(amount, existing.getCount());
        ItemStack extracted = existing.copy();
        extracted.setCount(toExtract);

        if (!simulate) {
            ItemStack remaining = existing.copy();
            remaining.setCount(existing.getCount() - toExtract);

            BagInventory newInventory = inventory.setStackInSlot(
                slot,
                remaining.isEmpty() ? ItemStack.EMPTY : remaining
            );
            setInventory(newInventory);
        }

        return extracted;
    }

    @Override
    public int getSlotLimit(int slot) {
        return getMaxStackSize();
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        // Prevent bag-in-bag nesting
        if (stack.getItem() instanceof ItemBuildersBag) {
            return false;
        }

        // Check type-specific filtering
        if (bagStack.getItem() instanceof ItemBuildersBag bagItem) {
            return bagItem.getType().canStore(stack);
        }

        return true;
    }
}
