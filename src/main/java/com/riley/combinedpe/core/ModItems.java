package com.riley.combinedpe.core;

import com.riley.combinedpe.CombinedPE;
import com.riley.combinedpe.bag.BagTier;
import com.riley.combinedpe.bag.ItemBuildersBag;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Registry for CombinedPE items
 */
public class ModItems {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(Registries.ITEM, CombinedPE.MOD_ID);

    // Builder's Bag items (5 tiers)
    public static final DeferredHolder<Item, ItemBuildersBag> BASIC_BAG =
        ITEMS.register("basic_bag", () -> new ItemBuildersBag(BagTier.BASIC, new Item.Properties()));

    public static final DeferredHolder<Item, ItemBuildersBag> ADVANCED_BAG =
        ITEMS.register("advanced_bag", () -> new ItemBuildersBag(BagTier.ADVANCED, new Item.Properties()));

    public static final DeferredHolder<Item, ItemBuildersBag> SUPERIOR_BAG =
        ITEMS.register("superior_bag", () -> new ItemBuildersBag(BagTier.SUPERIOR, new Item.Properties()));

    public static final DeferredHolder<Item, ItemBuildersBag> MASTERFUL_BAG =
        ITEMS.register("masterful_bag", () -> new ItemBuildersBag(BagTier.MASTERFUL, new Item.Properties()));

    public static final DeferredHolder<Item, ItemBuildersBag> ULTIMATE_BAG =
        ITEMS.register("ultimate_bag", () -> new ItemBuildersBag(BagTier.ULTIMATE, new Item.Properties()));

}
