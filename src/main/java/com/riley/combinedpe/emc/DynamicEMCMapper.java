package com.riley.combinedpe.emc;

import com.riley.combinedpe.CombinedPE;
import com.riley.combinedpe.Config;
import com.riley.combinedpe.integration.projecte.ProjectECompat;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.LevelEvent;

import java.util.HashMap;
import java.util.Map;

/**
 * Dynamic EMC mapper - scans and calculates EMC values for modded items
 *
 * Phase 2 implementation:
 * - Recipe scanner (crafting, smelting, smithing) ✓
 * - Tag-based inference (TODO: Phase 2.2)
 * - Configuration overrides (TODO: Phase 2.3)
 * - EMC value registration with ProjectE
 */
@EventBusSubscriber(modid = CombinedPE.MOD_ID)
public class DynamicEMCMapper {

    private static RecipeEMCCalculator calculator;
    private static final Map<Item, Long> discoveredEMC = new HashMap<>();

    /**
     * Initialize the dynamic EMC system
     */
    public static void initialize() {
        if (!Config.DYNAMIC_EMC_ENABLED.get()) {
            CombinedPE.LOGGER.info("Dynamic EMC registration is disabled in config");
            return;
        }

        CombinedPE.LOGGER.info("Dynamic EMC mapper initialized - waiting for world load");
    }

    /**
     * Triggered when a level (world) loads
     * This is when we scan and calculate EMC values
     */
    @SubscribeEvent
    public static void onWorldLoad(LevelEvent.Load event) {
        if (event.getLevel() instanceof Level level && level.isClientSide()) {
            // Only run on server side
            return;
        }

        if (!Config.SCAN_ON_WORLD_LOAD.get()) {
            CombinedPE.LOGGER.info("World load EMC scan disabled in config");
            return;
        }

        if (!ProjectECompat.isProjectELoaded()) {
            CombinedPE.LOGGER.warn("ProjectE not loaded, skipping EMC calculation");
            return;
        }

        Level level = (Level) event.getLevel();
        CombinedPE.LOGGER.info("World loaded, starting dynamic EMC calculation...");

        calculator = new RecipeEMCCalculator(level);
        scanAndCalculateEMC(level);
    }

    /**
     * Scan all registered items and calculate EMC values
     */
    private static void scanAndCalculateEMC(Level level) {
        int totalItems = 0;
        int itemsWithEMC = 0;
        int newEMCAssignments = 0;

        long startTime = System.currentTimeMillis();

        // Iterate through all registered items
        for (Item item : BuiltInRegistries.ITEM) {
            totalItems++;

            ItemStack stack = new ItemStack(item);

            // Skip items that already have EMC
            if (ProjectECompat.hasEMC(stack)) {
                itemsWithEMC++;
                continue;
            }

            // Calculate EMC from recipes
            long calculatedEMC = calculator.calculateEMC(stack);

            if (calculatedEMC > 0) {
                // Store the calculated EMC (Phase 2.2 will register with ProjectE)
                discoveredEMC.put(item, calculatedEMC);
                newEMCAssignments++;

                CombinedPE.LOGGER.info("Discovered EMC for {}: {}",
                    BuiltInRegistries.ITEM.getKey(item), calculatedEMC);
            }
        }

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        CombinedPE.LOGGER.info("=== Dynamic EMC Scan Complete ===");
        CombinedPE.LOGGER.info("Total items scanned: {}", totalItems);
        CombinedPE.LOGGER.info("Items with existing EMC: {}", itemsWithEMC);
        CombinedPE.LOGGER.info("New EMC values calculated: {}", newEMCAssignments);
        CombinedPE.LOGGER.info("Scan duration: {}ms", duration);

        // TODO: Phase 2.2 - Register calculated values with ProjectE
        // TODO: Phase 2.3 - Generate report file
    }

    /**
     * Get all discovered EMC values
     */
    public static Map<Item, Long> getDiscoveredEMC() {
        return new HashMap<>(discoveredEMC);
    }

    /**
     * Clear all calculated EMC values (for testing/reload)
     */
    public static void clearCache() {
        discoveredEMC.clear();
        if (calculator != null) {
            calculator.clearCache();
        }
    }
}
