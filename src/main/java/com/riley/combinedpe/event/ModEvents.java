package com.riley.combinedpe.event;

import com.riley.combinedpe.CombinedPE;
import com.riley.combinedpe.command.CombinedPECommand;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

/**
 * Event handlers for CombinedPE mod
 */
@EventBusSubscriber(modid = CombinedPE.MOD_ID)
public class ModEvents {

    /**
     * Register commands
     */
    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        CombinedPECommand.register(event.getDispatcher(), event.getBuildContext());
        CombinedPE.LOGGER.info("Registered CombinedPE commands");
    }
}
