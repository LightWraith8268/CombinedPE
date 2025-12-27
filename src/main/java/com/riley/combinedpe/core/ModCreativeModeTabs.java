package com.riley.combinedpe.core;

import com.riley.combinedpe.CombinedPE;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Creative mode tabs for CombinedPE
 */
public class ModCreativeModeTabs {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CombinedPE.MOD_ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> COMBINEDPE_TAB =
            CREATIVE_MODE_TABS.register("combinedpe", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.combinedpe"))
                    .icon(() -> new ItemStack(ModItems.ULTIMATE_TREASURE_BAG.get()))
                    .displayItems((parameters, output) -> {
                        // Blocks
                        output.accept(ModBlocks.ENHANCED_WORKBENCH.get());
                        output.accept(ModBlocks.EMC_LINKER.get());

                        // Builder's Bags - organized by type, then tier
                        // Materials Bags
                        output.accept(ModItems.BASIC_MATERIALS_BAG.get());
                        output.accept(ModItems.ADVANCED_MATERIALS_BAG.get());
                        output.accept(ModItems.SUPERIOR_MATERIALS_BAG.get());
                        output.accept(ModItems.MASTERFUL_MATERIALS_BAG.get());
                        output.accept(ModItems.ULTIMATE_MATERIALS_BAG.get());

                        // Food Bags
                        output.accept(ModItems.BASIC_FOOD_BAG.get());
                        output.accept(ModItems.ADVANCED_FOOD_BAG.get());
                        output.accept(ModItems.SUPERIOR_FOOD_BAG.get());
                        output.accept(ModItems.MASTERFUL_FOOD_BAG.get());
                        output.accept(ModItems.ULTIMATE_FOOD_BAG.get());

                        // Ore Bags
                        output.accept(ModItems.BASIC_ORE_BAG.get());
                        output.accept(ModItems.ADVANCED_ORE_BAG.get());
                        output.accept(ModItems.SUPERIOR_ORE_BAG.get());
                        output.accept(ModItems.MASTERFUL_ORE_BAG.get());
                        output.accept(ModItems.ULTIMATE_ORE_BAG.get());

                        // Tool Bags
                        output.accept(ModItems.BASIC_TOOL_BAG.get());
                        output.accept(ModItems.ADVANCED_TOOL_BAG.get());
                        output.accept(ModItems.SUPERIOR_TOOL_BAG.get());
                        output.accept(ModItems.MASTERFUL_TOOL_BAG.get());
                        output.accept(ModItems.ULTIMATE_TOOL_BAG.get());

                        // Mob Drop Bags
                        output.accept(ModItems.BASIC_MOB_DROP_BAG.get());
                        output.accept(ModItems.ADVANCED_MOB_DROP_BAG.get());
                        output.accept(ModItems.SUPERIOR_MOB_DROP_BAG.get());
                        output.accept(ModItems.MASTERFUL_MOB_DROP_BAG.get());
                        output.accept(ModItems.ULTIMATE_MOB_DROP_BAG.get());

                        // Liquid Bags
                        output.accept(ModItems.BASIC_LIQUID_BAG.get());
                        output.accept(ModItems.ADVANCED_LIQUID_BAG.get());
                        output.accept(ModItems.SUPERIOR_LIQUID_BAG.get());
                        output.accept(ModItems.MASTERFUL_LIQUID_BAG.get());
                        output.accept(ModItems.ULTIMATE_LIQUID_BAG.get());

                        // Redstone Bags
                        output.accept(ModItems.BASIC_REDSTONE_BAG.get());
                        output.accept(ModItems.ADVANCED_REDSTONE_BAG.get());
                        output.accept(ModItems.SUPERIOR_REDSTONE_BAG.get());
                        output.accept(ModItems.MASTERFUL_REDSTONE_BAG.get());
                        output.accept(ModItems.ULTIMATE_REDSTONE_BAG.get());

                        // Potion Bags
                        output.accept(ModItems.BASIC_POTION_BAG.get());
                        output.accept(ModItems.ADVANCED_POTION_BAG.get());
                        output.accept(ModItems.SUPERIOR_POTION_BAG.get());
                        output.accept(ModItems.MASTERFUL_POTION_BAG.get());
                        output.accept(ModItems.ULTIMATE_POTION_BAG.get());

                        // Enchanting Bags
                        output.accept(ModItems.BASIC_ENCHANTING_BAG.get());
                        output.accept(ModItems.ADVANCED_ENCHANTING_BAG.get());
                        output.accept(ModItems.SUPERIOR_ENCHANTING_BAG.get());
                        output.accept(ModItems.MASTERFUL_ENCHANTING_BAG.get());
                        output.accept(ModItems.ULTIMATE_ENCHANTING_BAG.get());

                        // Trade Bags
                        output.accept(ModItems.BASIC_TRADE_BAG.get());
                        output.accept(ModItems.ADVANCED_TRADE_BAG.get());
                        output.accept(ModItems.SUPERIOR_TRADE_BAG.get());
                        output.accept(ModItems.MASTERFUL_TRADE_BAG.get());
                        output.accept(ModItems.ULTIMATE_TRADE_BAG.get());

                        // Combat Bags
                        output.accept(ModItems.BASIC_COMBAT_BAG.get());
                        output.accept(ModItems.ADVANCED_COMBAT_BAG.get());
                        output.accept(ModItems.SUPERIOR_COMBAT_BAG.get());
                        output.accept(ModItems.MASTERFUL_COMBAT_BAG.get());
                        output.accept(ModItems.ULTIMATE_COMBAT_BAG.get());

                        // Adventure Bags
                        output.accept(ModItems.BASIC_ADVENTURE_BAG.get());
                        output.accept(ModItems.ADVANCED_ADVENTURE_BAG.get());
                        output.accept(ModItems.SUPERIOR_ADVENTURE_BAG.get());
                        output.accept(ModItems.MASTERFUL_ADVENTURE_BAG.get());
                        output.accept(ModItems.ULTIMATE_ADVENTURE_BAG.get());

                        // Treasure Bags
                        output.accept(ModItems.BASIC_TREASURE_BAG.get());
                        output.accept(ModItems.ADVANCED_TREASURE_BAG.get());
                        output.accept(ModItems.SUPERIOR_TREASURE_BAG.get());
                        output.accept(ModItems.MASTERFUL_TREASURE_BAG.get());
                        output.accept(ModItems.ULTIMATE_TREASURE_BAG.get());

                        // Stack Upgrades
                        output.accept(ModItems.STACK_UPGRADE_I.get());
                        output.accept(ModItems.STACK_UPGRADE_II.get());
                        output.accept(ModItems.STACK_UPGRADE_III.get());
                        output.accept(ModItems.STACK_UPGRADE_IV.get());
                    })
                    .build());
}
