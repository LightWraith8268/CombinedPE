package com.riley.combinedpe.core;

import com.riley.combinedpe.CombinedPE;
import com.riley.combinedpe.bag.BagTier;
import com.riley.combinedpe.bag.ItemBuildersBag;
import com.riley.combinedpe.bag.ItemStackUpgrade;
import com.riley.combinedpe.bag.StackUpgradeTier;
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

    // Stack upgrade items (4 tiers)
    public static final DeferredHolder<Item, ItemStackUpgrade> STACK_UPGRADE_I =
        ITEMS.register("stack_upgrade_i", () -> new ItemStackUpgrade(StackUpgradeTier.TIER_I, new Item.Properties()));

    public static final DeferredHolder<Item, ItemStackUpgrade> STACK_UPGRADE_II =
        ITEMS.register("stack_upgrade_ii", () -> new ItemStackUpgrade(StackUpgradeTier.TIER_II, new Item.Properties()));

    public static final DeferredHolder<Item, ItemStackUpgrade> STACK_UPGRADE_III =
        ITEMS.register("stack_upgrade_iii", () -> new ItemStackUpgrade(StackUpgradeTier.TIER_III, new Item.Properties()));

    public static final DeferredHolder<Item, ItemStackUpgrade> STACK_UPGRADE_IV =
        ITEMS.register("stack_upgrade_iv", () -> new ItemStackUpgrade(StackUpgradeTier.TIER_IV, new Item.Properties()));

}
