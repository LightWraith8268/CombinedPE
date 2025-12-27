package com.riley.combinedpe.client;

import com.riley.combinedpe.bag.BagScreen;
import com.riley.combinedpe.core.ModMenuTypes;
import com.riley.combinedpe.workbench.EnhancedWorkbenchScreen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

/**
 * Client-side setup for CombinedPE
 */
@EventBusSubscriber(modid = com.riley.combinedpe.CombinedPE.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetup {

    /**
     * Register GUI screens for menu types
     */
    @SubscribeEvent
    public static void onRegisterScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenuTypes.BAG_MENU.get(), BagScreen::new);
        event.register(ModMenuTypes.ENHANCED_WORKBENCH.get(), EnhancedWorkbenchScreen::new);
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        // Client-side initialization
    }
}
