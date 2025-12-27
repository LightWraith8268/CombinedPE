package com.riley.combinedpe.event;

import com.riley.combinedpe.CombinedPE;
import com.riley.combinedpe.bag.BagItemHandler;
import com.riley.combinedpe.bag.ItemBuildersBag;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

/**
 * Event handler for registering capabilities
 */
@EventBusSubscriber(modid = CombinedPE.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class CapabilitySetup {

    /**
     * Register capabilities for Builder's Bag items
     * This allows other mods (RS, AE2, etc.) to interact with bag inventories
     */
    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        // Register capability for all bags (13 types × 5 tiers = 65 bags)
        // We register by checking instanceof ItemBuildersBag, so this covers all bags automatically
        event.registerItem(
            Capabilities.ItemHandler.ITEM,
            (stack, context) -> {
                if (stack.getItem() instanceof ItemBuildersBag) {
                    return new BagItemHandler(stack);
                }
                return null;
            },
            // Materials Bags
            com.riley.combinedpe.core.ModItems.BASIC_MATERIALS_BAG.get(),
            com.riley.combinedpe.core.ModItems.ADVANCED_MATERIALS_BAG.get(),
            com.riley.combinedpe.core.ModItems.SUPERIOR_MATERIALS_BAG.get(),
            com.riley.combinedpe.core.ModItems.MASTERFUL_MATERIALS_BAG.get(),
            com.riley.combinedpe.core.ModItems.ULTIMATE_MATERIALS_BAG.get(),
            // Food Bags
            com.riley.combinedpe.core.ModItems.BASIC_FOOD_BAG.get(),
            com.riley.combinedpe.core.ModItems.ADVANCED_FOOD_BAG.get(),
            com.riley.combinedpe.core.ModItems.SUPERIOR_FOOD_BAG.get(),
            com.riley.combinedpe.core.ModItems.MASTERFUL_FOOD_BAG.get(),
            com.riley.combinedpe.core.ModItems.ULTIMATE_FOOD_BAG.get(),
            // Ore Bags
            com.riley.combinedpe.core.ModItems.BASIC_ORE_BAG.get(),
            com.riley.combinedpe.core.ModItems.ADVANCED_ORE_BAG.get(),
            com.riley.combinedpe.core.ModItems.SUPERIOR_ORE_BAG.get(),
            com.riley.combinedpe.core.ModItems.MASTERFUL_ORE_BAG.get(),
            com.riley.combinedpe.core.ModItems.ULTIMATE_ORE_BAG.get(),
            // Tool Bags
            com.riley.combinedpe.core.ModItems.BASIC_TOOL_BAG.get(),
            com.riley.combinedpe.core.ModItems.ADVANCED_TOOL_BAG.get(),
            com.riley.combinedpe.core.ModItems.SUPERIOR_TOOL_BAG.get(),
            com.riley.combinedpe.core.ModItems.MASTERFUL_TOOL_BAG.get(),
            com.riley.combinedpe.core.ModItems.ULTIMATE_TOOL_BAG.get(),
            // Mob Drop Bags
            com.riley.combinedpe.core.ModItems.BASIC_MOB_DROP_BAG.get(),
            com.riley.combinedpe.core.ModItems.ADVANCED_MOB_DROP_BAG.get(),
            com.riley.combinedpe.core.ModItems.SUPERIOR_MOB_DROP_BAG.get(),
            com.riley.combinedpe.core.ModItems.MASTERFUL_MOB_DROP_BAG.get(),
            com.riley.combinedpe.core.ModItems.ULTIMATE_MOB_DROP_BAG.get(),
            // Liquid Bags
            com.riley.combinedpe.core.ModItems.BASIC_LIQUID_BAG.get(),
            com.riley.combinedpe.core.ModItems.ADVANCED_LIQUID_BAG.get(),
            com.riley.combinedpe.core.ModItems.SUPERIOR_LIQUID_BAG.get(),
            com.riley.combinedpe.core.ModItems.MASTERFUL_LIQUID_BAG.get(),
            com.riley.combinedpe.core.ModItems.ULTIMATE_LIQUID_BAG.get(),
            // Redstone Bags
            com.riley.combinedpe.core.ModItems.BASIC_REDSTONE_BAG.get(),
            com.riley.combinedpe.core.ModItems.ADVANCED_REDSTONE_BAG.get(),
            com.riley.combinedpe.core.ModItems.SUPERIOR_REDSTONE_BAG.get(),
            com.riley.combinedpe.core.ModItems.MASTERFUL_REDSTONE_BAG.get(),
            com.riley.combinedpe.core.ModItems.ULTIMATE_REDSTONE_BAG.get(),
            // Potion Bags
            com.riley.combinedpe.core.ModItems.BASIC_POTION_BAG.get(),
            com.riley.combinedpe.core.ModItems.ADVANCED_POTION_BAG.get(),
            com.riley.combinedpe.core.ModItems.SUPERIOR_POTION_BAG.get(),
            com.riley.combinedpe.core.ModItems.MASTERFUL_POTION_BAG.get(),
            com.riley.combinedpe.core.ModItems.ULTIMATE_POTION_BAG.get(),
            // Enchanting Bags
            com.riley.combinedpe.core.ModItems.BASIC_ENCHANTING_BAG.get(),
            com.riley.combinedpe.core.ModItems.ADVANCED_ENCHANTING_BAG.get(),
            com.riley.combinedpe.core.ModItems.SUPERIOR_ENCHANTING_BAG.get(),
            com.riley.combinedpe.core.ModItems.MASTERFUL_ENCHANTING_BAG.get(),
            com.riley.combinedpe.core.ModItems.ULTIMATE_ENCHANTING_BAG.get(),
            // Trade Bags
            com.riley.combinedpe.core.ModItems.BASIC_TRADE_BAG.get(),
            com.riley.combinedpe.core.ModItems.ADVANCED_TRADE_BAG.get(),
            com.riley.combinedpe.core.ModItems.SUPERIOR_TRADE_BAG.get(),
            com.riley.combinedpe.core.ModItems.MASTERFUL_TRADE_BAG.get(),
            com.riley.combinedpe.core.ModItems.ULTIMATE_TRADE_BAG.get(),
            // Combat Bags
            com.riley.combinedpe.core.ModItems.BASIC_COMBAT_BAG.get(),
            com.riley.combinedpe.core.ModItems.ADVANCED_COMBAT_BAG.get(),
            com.riley.combinedpe.core.ModItems.SUPERIOR_COMBAT_BAG.get(),
            com.riley.combinedpe.core.ModItems.MASTERFUL_COMBAT_BAG.get(),
            com.riley.combinedpe.core.ModItems.ULTIMATE_COMBAT_BAG.get(),
            // Adventure Bags
            com.riley.combinedpe.core.ModItems.BASIC_ADVENTURE_BAG.get(),
            com.riley.combinedpe.core.ModItems.ADVANCED_ADVENTURE_BAG.get(),
            com.riley.combinedpe.core.ModItems.SUPERIOR_ADVENTURE_BAG.get(),
            com.riley.combinedpe.core.ModItems.MASTERFUL_ADVENTURE_BAG.get(),
            com.riley.combinedpe.core.ModItems.ULTIMATE_ADVENTURE_BAG.get(),
            // Treasure Bags
            com.riley.combinedpe.core.ModItems.BASIC_TREASURE_BAG.get(),
            com.riley.combinedpe.core.ModItems.ADVANCED_TREASURE_BAG.get(),
            com.riley.combinedpe.core.ModItems.SUPERIOR_TREASURE_BAG.get(),
            com.riley.combinedpe.core.ModItems.MASTERFUL_TREASURE_BAG.get(),
            com.riley.combinedpe.core.ModItems.ULTIMATE_TREASURE_BAG.get()
        );

        CombinedPE.LOGGER.info("Registered IItemHandler capability for all 65 Builder's Bags");
    }
}
