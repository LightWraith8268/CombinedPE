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
        event.registerItem(
            Capabilities.ItemHandler.ITEM,
            (stack, context) -> {
                if (stack.getItem() instanceof ItemBuildersBag) {
                    return new BagItemHandler(stack);
                }
                return null;
            },
            // Register for all bag tiers
            com.riley.combinedpe.core.ModItems.BASIC_BAG.get(),
            com.riley.combinedpe.core.ModItems.ADVANCED_BAG.get(),
            com.riley.combinedpe.core.ModItems.SUPERIOR_BAG.get(),
            com.riley.combinedpe.core.ModItems.MASTERFUL_BAG.get(),
            com.riley.combinedpe.core.ModItems.ULTIMATE_BAG.get()
        );

        CombinedPE.LOGGER.info("Registered IItemHandler capability for Builder's Bags");
    }
}
