package com.riley.combinedpe;

import com.mojang.logging.LogUtils;
import com.riley.combinedpe.core.ModBlocks;
import com.riley.combinedpe.core.ModCreativeModeTabs;
import com.riley.combinedpe.core.ModItems;
import com.riley.combinedpe.core.ModMenuTypes;
import com.riley.combinedpe.registry.ModDataComponents;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import org.slf4j.Logger;

/**
 * CombinedPE - A unified mod combining ProjectE ecosystem mods with dynamic EMC registration
 *
 * @author Riley E. Antrobus
 */
@Mod(CombinedPE.MOD_ID)
public class CombinedPE {

    public static final String MOD_ID = "combinedpe";
    public static final Logger LOGGER = LogUtils.getLogger();

    public CombinedPE(IEventBus modEventBus, ModContainer modContainer) {
        LOGGER.info("CombinedPE initializing...");

        // Register deferred registers
        ModBlocks.BLOCKS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModMenuTypes.MENU_TYPES.register(modEventBus);
        ModCreativeModeTabs.CREATIVE_MODE_TABS.register(modEventBus);
        ModDataComponents.REGISTRAR.register(modEventBus);

        // Register the common setup method
        modEventBus.addListener(this::commonSetup);

        // Register mod configuration
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);

        LOGGER.info("CombinedPE initialized!");
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("CombinedPE common setup starting...");

        // Initialize dynamic EMC system
        event.enqueueWork(() -> {
            com.riley.combinedpe.emc.DynamicEMCMapper.initialize();
        });

        LOGGER.info("CombinedPE common setup complete!");
    }
}
