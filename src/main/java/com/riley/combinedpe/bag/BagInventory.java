package com.riley.combinedpe.bag;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

/**
 * Data component for storing Builder's Bag inventory
 * This replaces NBT-based storage from 1.12.2
 */
public record BagInventory(NonNullList<ItemStack> items) {

    /**
     * Codec for saving/loading to disk (persistent storage)
     */
    public static final Codec<BagInventory> CODEC = RecordCodecBuilder.create(instance ->
        instance.group(
            ItemStack.CODEC.listOf().xmap(
                list -> {
                    NonNullList<ItemStack> nonNullList = NonNullList.create();
                    nonNullList.addAll(list);
                    return nonNullList;
                },
                nonNullList -> nonNullList
            ).fieldOf("items").forGetter(BagInventory::items)
        ).apply(instance, BagInventory::new)
    );

    /**
     * Stream codec for network synchronization
     */
    public static final StreamCodec<RegistryFriendlyByteBuf, BagInventory> STREAM_CODEC = StreamCodec.composite(
        ItemStack.STREAM_CODEC.apply(ByteBufCodecs.list()),
        inv -> inv.items,
        list -> {
            NonNullList<ItemStack> nonNullList = NonNullList.create();
            nonNullList.addAll(list);
            return new BagInventory(nonNullList);
        }
    );

    /**
     * Create empty inventory with specified capacity
     */
    public static BagInventory empty(int capacity) {
        NonNullList<ItemStack> items = NonNullList.withSize(capacity, ItemStack.EMPTY);
        return new BagInventory(items);
    }

    /**
     * Create inventory from existing items
     */
    public static BagInventory of(NonNullList<ItemStack> items) {
        return new BagInventory(items);
    }

    /**
     * Get the number of slots in this inventory
     */
    public int getSlots() {
        return items.size();
    }

    /**
     * Get the stack in a specific slot
     */
    public ItemStack getStackInSlot(int slot) {
        if (slot < 0 || slot >= items.size()) {
            return ItemStack.EMPTY;
        }
        return items.get(slot);
    }

    /**
     * Set the stack in a specific slot
     * Returns a new BagInventory instance (immutable pattern)
     */
    public BagInventory setStackInSlot(int slot, ItemStack stack) {
        if (slot < 0 || slot >= items.size()) {
            return this;
        }

        NonNullList<ItemStack> newItems = NonNullList.create();
        newItems.addAll(items);
        newItems.set(slot, stack.copy());
        return new BagInventory(newItems);
    }

    /**
     * Check if the inventory is empty
     */
    public boolean isEmpty() {
        for (ItemStack stack : items) {
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Get the total number of items in the inventory
     */
    public int getTotalItemCount() {
        int count = 0;
        for (ItemStack stack : items) {
            count += stack.getCount();
        }
        return count;
    }

    /**
     * Create a copy of this inventory
     */
    public BagInventory copy() {
        NonNullList<ItemStack> copiedItems = NonNullList.create();
        for (ItemStack stack : items) {
            copiedItems.add(stack.copy());
        }
        return new BagInventory(copiedItems);
    }
}
