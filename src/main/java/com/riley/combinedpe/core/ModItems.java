package com.riley.combinedpe.core;

import com.riley.combinedpe.CombinedPE;
import com.riley.combinedpe.bag.BagTier;
import com.riley.combinedpe.bag.BagType;
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

    // ========== Specialized Bags (13 types × 5 tiers = 65 items) ==========

    // Materials Bags (building blocks)
    public static final DeferredHolder<Item, ItemBuildersBag> BASIC_MATERIALS_BAG =
        ITEMS.register("basic_materials_bag", () -> new ItemBuildersBag(BagType.MATERIALS, BagTier.BASIC, new Item.Properties()));
    public static final DeferredHolder<Item, ItemBuildersBag> ADVANCED_MATERIALS_BAG =
        ITEMS.register("advanced_materials_bag", () -> new ItemBuildersBag(BagType.MATERIALS, BagTier.ADVANCED, new Item.Properties()));
    public static final DeferredHolder<Item, ItemBuildersBag> SUPERIOR_MATERIALS_BAG =
        ITEMS.register("superior_materials_bag", () -> new ItemBuildersBag(BagType.MATERIALS, BagTier.SUPERIOR, new Item.Properties()));
    public static final DeferredHolder<Item, ItemBuildersBag> MASTERFUL_MATERIALS_BAG =
        ITEMS.register("masterful_materials_bag", () -> new ItemBuildersBag(BagType.MATERIALS, BagTier.MASTERFUL, new Item.Properties()));
    public static final DeferredHolder<Item, ItemBuildersBag> ULTIMATE_MATERIALS_BAG =
        ITEMS.register("ultimate_materials_bag", () -> new ItemBuildersBag(BagType.MATERIALS, BagTier.ULTIMATE, new Item.Properties()));

    // Food Bags
    public static final DeferredHolder<Item, ItemBuildersBag> BASIC_FOOD_BAG =
        ITEMS.register("basic_food_bag", () -> new ItemBuildersBag(BagType.FOOD, BagTier.BASIC, new Item.Properties()));
    public static final DeferredHolder<Item, ItemBuildersBag> ADVANCED_FOOD_BAG =
        ITEMS.register("advanced_food_bag", () -> new ItemBuildersBag(BagType.FOOD, BagTier.ADVANCED, new Item.Properties()));
    public static final DeferredHolder<Item, ItemBuildersBag> SUPERIOR_FOOD_BAG =
        ITEMS.register("superior_food_bag", () -> new ItemBuildersBag(BagType.FOOD, BagTier.SUPERIOR, new Item.Properties()));
    public static final DeferredHolder<Item, ItemBuildersBag> MASTERFUL_FOOD_BAG =
        ITEMS.register("masterful_food_bag", () -> new ItemBuildersBag(BagType.FOOD, BagTier.MASTERFUL, new Item.Properties()));
    public static final DeferredHolder<Item, ItemBuildersBag> ULTIMATE_FOOD_BAG =
        ITEMS.register("ultimate_food_bag", () -> new ItemBuildersBag(BagType.FOOD, BagTier.ULTIMATE, new Item.Properties()));

    // Ore Bags
    public static final DeferredHolder<Item, ItemBuildersBag> BASIC_ORE_BAG =
        ITEMS.register("basic_ore_bag", () -> new ItemBuildersBag(BagType.ORE, BagTier.BASIC, new Item.Properties()));
    public static final DeferredHolder<Item, ItemBuildersBag> ADVANCED_ORE_BAG =
        ITEMS.register("advanced_ore_bag", () -> new ItemBuildersBag(BagType.ORE, BagTier.ADVANCED, new Item.Properties()));
    public static final DeferredHolder<Item, ItemBuildersBag> SUPERIOR_ORE_BAG =
        ITEMS.register("superior_ore_bag", () -> new ItemBuildersBag(BagType.ORE, BagTier.SUPERIOR, new Item.Properties()));
    public static final DeferredHolder<Item, ItemBuildersBag> MASTERFUL_ORE_BAG =
        ITEMS.register("masterful_ore_bag", () -> new ItemBuildersBag(BagType.ORE, BagTier.MASTERFUL, new Item.Properties()));
    public static final DeferredHolder<Item, ItemBuildersBag> ULTIMATE_ORE_BAG =
        ITEMS.register("ultimate_ore_bag", () -> new ItemBuildersBag(BagType.ORE, BagTier.ULTIMATE, new Item.Properties()));

    // Tool Bags
    public static final DeferredHolder<Item, ItemBuildersBag> BASIC_TOOL_BAG =
        ITEMS.register("basic_tool_bag", () -> new ItemBuildersBag(BagType.TOOL, BagTier.BASIC, new Item.Properties()));
    public static final DeferredHolder<Item, ItemBuildersBag> ADVANCED_TOOL_BAG =
        ITEMS.register("advanced_tool_bag", () -> new ItemBuildersBag(BagType.TOOL, BagTier.ADVANCED, new Item.Properties()));
    public static final DeferredHolder<Item, ItemBuildersBag> SUPERIOR_TOOL_BAG =
        ITEMS.register("superior_tool_bag", () -> new ItemBuildersBag(BagType.TOOL, BagTier.SUPERIOR, new Item.Properties()));
    public static final DeferredHolder<Item, ItemBuildersBag> MASTERFUL_TOOL_BAG =
        ITEMS.register("masterful_tool_bag", () -> new ItemBuildersBag(BagType.TOOL, BagTier.MASTERFUL, new Item.Properties()));
    public static final DeferredHolder<Item, ItemBuildersBag> ULTIMATE_TOOL_BAG =
        ITEMS.register("ultimate_tool_bag", () -> new ItemBuildersBag(BagType.TOOL, BagTier.ULTIMATE, new Item.Properties()));

    // Mob Drop Bags
    public static final DeferredHolder<Item, ItemBuildersBag> BASIC_MOB_DROP_BAG =
        ITEMS.register("basic_mob_drop_bag", () -> new ItemBuildersBag(BagType.MOB_DROP, BagTier.BASIC, new Item.Properties()));
    public static final DeferredHolder<Item, ItemBuildersBag> ADVANCED_MOB_DROP_BAG =
        ITEMS.register("advanced_mob_drop_bag", () -> new ItemBuildersBag(BagType.MOB_DROP, BagTier.ADVANCED, new Item.Properties()));
    public static final DeferredHolder<Item, ItemBuildersBag> SUPERIOR_MOB_DROP_BAG =
        ITEMS.register("superior_mob_drop_bag", () -> new ItemBuildersBag(BagType.MOB_DROP, BagTier.SUPERIOR, new Item.Properties()));
    public static final DeferredHolder<Item, ItemBuildersBag> MASTERFUL_MOB_DROP_BAG =
        ITEMS.register("masterful_mob_drop_bag", () -> new ItemBuildersBag(BagType.MOB_DROP, BagTier.MASTERFUL, new Item.Properties()));
    public static final DeferredHolder<Item, ItemBuildersBag> ULTIMATE_MOB_DROP_BAG =
        ITEMS.register("ultimate_mob_drop_bag", () -> new ItemBuildersBag(BagType.MOB_DROP, BagTier.ULTIMATE, new Item.Properties()));

    // Liquid Bags
    public static final DeferredHolder<Item, ItemBuildersBag> BASIC_LIQUID_BAG =
        ITEMS.register("basic_liquid_bag", () -> new ItemBuildersBag(BagType.LIQUID, BagTier.BASIC, new Item.Properties()));
    public static final DeferredHolder<Item, ItemBuildersBag> ADVANCED_LIQUID_BAG =
        ITEMS.register("advanced_liquid_bag", () -> new ItemBuildersBag(BagType.LIQUID, BagTier.ADVANCED, new Item.Properties()));
    public static final DeferredHolder<Item, ItemBuildersBag> SUPERIOR_LIQUID_BAG =
        ITEMS.register("superior_liquid_bag", () -> new ItemBuildersBag(BagType.LIQUID, BagTier.SUPERIOR, new Item.Properties()));
    public static final DeferredHolder<Item, ItemBuildersBag> MASTERFUL_LIQUID_BAG =
        ITEMS.register("masterful_liquid_bag", () -> new ItemBuildersBag(BagType.LIQUID, BagTier.MASTERFUL, new Item.Properties()));
    public static final DeferredHolder<Item, ItemBuildersBag> ULTIMATE_LIQUID_BAG =
        ITEMS.register("ultimate_liquid_bag", () -> new ItemBuildersBag(BagType.LIQUID, BagTier.ULTIMATE, new Item.Properties()));

    // Redstone Bags
    public static final DeferredHolder<Item, ItemBuildersBag> BASIC_REDSTONE_BAG =
        ITEMS.register("basic_redstone_bag", () -> new ItemBuildersBag(BagType.REDSTONE, BagTier.BASIC, new Item.Properties()));
    public static final DeferredHolder<Item, ItemBuildersBag> ADVANCED_REDSTONE_BAG =
        ITEMS.register("advanced_redstone_bag", () -> new ItemBuildersBag(BagType.REDSTONE, BagTier.ADVANCED, new Item.Properties()));
    public static final DeferredHolder<Item, ItemBuildersBag> SUPERIOR_REDSTONE_BAG =
        ITEMS.register("superior_redstone_bag", () -> new ItemBuildersBag(BagType.REDSTONE, BagTier.SUPERIOR, new Item.Properties()));
    public static final DeferredHolder<Item, ItemBuildersBag> MASTERFUL_REDSTONE_BAG =
        ITEMS.register("masterful_redstone_bag", () -> new ItemBuildersBag(BagType.REDSTONE, BagTier.MASTERFUL, new Item.Properties()));
    public static final DeferredHolder<Item, ItemBuildersBag> ULTIMATE_REDSTONE_BAG =
        ITEMS.register("ultimate_redstone_bag", () -> new ItemBuildersBag(BagType.REDSTONE, BagTier.ULTIMATE, new Item.Properties()));

    // Potion Bags
    public static final DeferredHolder<Item, ItemBuildersBag> BASIC_POTION_BAG =
        ITEMS.register("basic_potion_bag", () -> new ItemBuildersBag(BagType.POTION, BagTier.BASIC, new Item.Properties()));
    public static final DeferredHolder<Item, ItemBuildersBag> ADVANCED_POTION_BAG =
        ITEMS.register("advanced_potion_bag", () -> new ItemBuildersBag(BagType.POTION, BagTier.ADVANCED, new Item.Properties()));
    public static final DeferredHolder<Item, ItemBuildersBag> SUPERIOR_POTION_BAG =
        ITEMS.register("superior_potion_bag", () -> new ItemBuildersBag(BagType.POTION, BagTier.SUPERIOR, new Item.Properties()));
    public static final DeferredHolder<Item, ItemBuildersBag> MASTERFUL_POTION_BAG =
        ITEMS.register("masterful_potion_bag", () -> new ItemBuildersBag(BagType.POTION, BagTier.MASTERFUL, new Item.Properties()));
    public static final DeferredHolder<Item, ItemBuildersBag> ULTIMATE_POTION_BAG =
        ITEMS.register("ultimate_potion_bag", () -> new ItemBuildersBag(BagType.POTION, BagTier.ULTIMATE, new Item.Properties()));

    // Enchanting Bags
    public static final DeferredHolder<Item, ItemBuildersBag> BASIC_ENCHANTING_BAG =
        ITEMS.register("basic_enchanting_bag", () -> new ItemBuildersBag(BagType.ENCHANTING, BagTier.BASIC, new Item.Properties()));
    public static final DeferredHolder<Item, ItemBuildersBag> ADVANCED_ENCHANTING_BAG =
        ITEMS.register("advanced_enchanting_bag", () -> new ItemBuildersBag(BagType.ENCHANTING, BagTier.ADVANCED, new Item.Properties()));
    public static final DeferredHolder<Item, ItemBuildersBag> SUPERIOR_ENCHANTING_BAG =
        ITEMS.register("superior_enchanting_bag", () -> new ItemBuildersBag(BagType.ENCHANTING, BagTier.SUPERIOR, new Item.Properties()));
    public static final DeferredHolder<Item, ItemBuildersBag> MASTERFUL_ENCHANTING_BAG =
        ITEMS.register("masterful_enchanting_bag", () -> new ItemBuildersBag(BagType.ENCHANTING, BagTier.MASTERFUL, new Item.Properties()));
    public static final DeferredHolder<Item, ItemBuildersBag> ULTIMATE_ENCHANTING_BAG =
        ITEMS.register("ultimate_enchanting_bag", () -> new ItemBuildersBag(BagType.ENCHANTING, BagTier.ULTIMATE, new Item.Properties()));

    // Trade Bags
    public static final DeferredHolder<Item, ItemBuildersBag> BASIC_TRADE_BAG =
        ITEMS.register("basic_trade_bag", () -> new ItemBuildersBag(BagType.TRADE, BagTier.BASIC, new Item.Properties()));
    public static final DeferredHolder<Item, ItemBuildersBag> ADVANCED_TRADE_BAG =
        ITEMS.register("advanced_trade_bag", () -> new ItemBuildersBag(BagType.TRADE, BagTier.ADVANCED, new Item.Properties()));
    public static final DeferredHolder<Item, ItemBuildersBag> SUPERIOR_TRADE_BAG =
        ITEMS.register("superior_trade_bag", () -> new ItemBuildersBag(BagType.TRADE, BagTier.SUPERIOR, new Item.Properties()));
    public static final DeferredHolder<Item, ItemBuildersBag> MASTERFUL_TRADE_BAG =
        ITEMS.register("masterful_trade_bag", () -> new ItemBuildersBag(BagType.TRADE, BagTier.MASTERFUL, new Item.Properties()));
    public static final DeferredHolder<Item, ItemBuildersBag> ULTIMATE_TRADE_BAG =
        ITEMS.register("ultimate_trade_bag", () -> new ItemBuildersBag(BagType.TRADE, BagTier.ULTIMATE, new Item.Properties()));

    // Combat Bags
    public static final DeferredHolder<Item, ItemBuildersBag> BASIC_COMBAT_BAG =
        ITEMS.register("basic_combat_bag", () -> new ItemBuildersBag(BagType.COMBAT, BagTier.BASIC, new Item.Properties()));
    public static final DeferredHolder<Item, ItemBuildersBag> ADVANCED_COMBAT_BAG =
        ITEMS.register("advanced_combat_bag", () -> new ItemBuildersBag(BagType.COMBAT, BagTier.ADVANCED, new Item.Properties()));
    public static final DeferredHolder<Item, ItemBuildersBag> SUPERIOR_COMBAT_BAG =
        ITEMS.register("superior_combat_bag", () -> new ItemBuildersBag(BagType.COMBAT, BagTier.SUPERIOR, new Item.Properties()));
    public static final DeferredHolder<Item, ItemBuildersBag> MASTERFUL_COMBAT_BAG =
        ITEMS.register("masterful_combat_bag", () -> new ItemBuildersBag(BagType.COMBAT, BagTier.MASTERFUL, new Item.Properties()));
    public static final DeferredHolder<Item, ItemBuildersBag> ULTIMATE_COMBAT_BAG =
        ITEMS.register("ultimate_combat_bag", () -> new ItemBuildersBag(BagType.COMBAT, BagTier.ULTIMATE, new Item.Properties()));

    // Adventure Bags
    public static final DeferredHolder<Item, ItemBuildersBag> BASIC_ADVENTURE_BAG =
        ITEMS.register("basic_adventure_bag", () -> new ItemBuildersBag(BagType.ADVENTURE, BagTier.BASIC, new Item.Properties()));
    public static final DeferredHolder<Item, ItemBuildersBag> ADVANCED_ADVENTURE_BAG =
        ITEMS.register("advanced_adventure_bag", () -> new ItemBuildersBag(BagType.ADVENTURE, BagTier.ADVANCED, new Item.Properties()));
    public static final DeferredHolder<Item, ItemBuildersBag> SUPERIOR_ADVENTURE_BAG =
        ITEMS.register("superior_adventure_bag", () -> new ItemBuildersBag(BagType.ADVENTURE, BagTier.SUPERIOR, new Item.Properties()));
    public static final DeferredHolder<Item, ItemBuildersBag> MASTERFUL_ADVENTURE_BAG =
        ITEMS.register("masterful_adventure_bag", () -> new ItemBuildersBag(BagType.ADVENTURE, BagTier.MASTERFUL, new Item.Properties()));
    public static final DeferredHolder<Item, ItemBuildersBag> ULTIMATE_ADVENTURE_BAG =
        ITEMS.register("ultimate_adventure_bag", () -> new ItemBuildersBag(BagType.ADVENTURE, BagTier.ULTIMATE, new Item.Properties()));

    // Treasure Bags
    public static final DeferredHolder<Item, ItemBuildersBag> BASIC_TREASURE_BAG =
        ITEMS.register("basic_treasure_bag", () -> new ItemBuildersBag(BagType.TREASURE, BagTier.BASIC, new Item.Properties()));
    public static final DeferredHolder<Item, ItemBuildersBag> ADVANCED_TREASURE_BAG =
        ITEMS.register("advanced_treasure_bag", () -> new ItemBuildersBag(BagType.TREASURE, BagTier.ADVANCED, new Item.Properties()));
    public static final DeferredHolder<Item, ItemBuildersBag> SUPERIOR_TREASURE_BAG =
        ITEMS.register("superior_treasure_bag", () -> new ItemBuildersBag(BagType.TREASURE, BagTier.SUPERIOR, new Item.Properties()));
    public static final DeferredHolder<Item, ItemBuildersBag> MASTERFUL_TREASURE_BAG =
        ITEMS.register("masterful_treasure_bag", () -> new ItemBuildersBag(BagType.TREASURE, BagTier.MASTERFUL, new Item.Properties()));
    public static final DeferredHolder<Item, ItemBuildersBag> ULTIMATE_TREASURE_BAG =
        ITEMS.register("ultimate_treasure_bag", () -> new ItemBuildersBag(BagType.TREASURE, BagTier.ULTIMATE, new Item.Properties()));

    // Stack upgrade items (4 tiers)
    public static final DeferredHolder<Item, ItemStackUpgrade> STACK_UPGRADE_I =
        ITEMS.register("stack_upgrade_i", () -> new ItemStackUpgrade(StackUpgradeTier.TIER_I, new Item.Properties()));

    public static final DeferredHolder<Item, ItemStackUpgrade> STACK_UPGRADE_II =
        ITEMS.register("stack_upgrade_ii", () -> new ItemStackUpgrade(StackUpgradeTier.TIER_II, new Item.Properties()));

    public static final DeferredHolder<Item, ItemStackUpgrade> STACK_UPGRADE_III =
        ITEMS.register("stack_upgrade_iii", () -> new ItemStackUpgrade(StackUpgradeTier.TIER_III, new Item.Properties()));

    public static final DeferredHolder<Item, ItemStackUpgrade> STACK_UPGRADE_IV =
        ITEMS.register("stack_upgrade_iv", () -> new ItemStackUpgrade(StackUpgradeTier.TIER_IV, new Item.Properties()));

    // Enhanced Workbench block item
    public static final DeferredHolder<Item, net.minecraft.world.item.BlockItem> ENHANCED_WORKBENCH =
        ITEMS.register("enhanced_workbench", () -> new net.minecraft.world.item.BlockItem(
            ModBlocks.ENHANCED_WORKBENCH.get(), new Item.Properties()
        ));

    // EMC Linker block item (with helpful tooltips)
    public static final DeferredHolder<Item, net.minecraft.world.item.BlockItem> EMC_LINKER =
        ITEMS.register("emc_linker", () -> new com.riley.combinedpe.integration.rs.ItemEMCLinker(
            ModBlocks.EMC_LINKER.get(), new Item.Properties()
        ));

}
