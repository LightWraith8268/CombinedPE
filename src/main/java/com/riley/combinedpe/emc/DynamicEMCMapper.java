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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Dynamic EMC mapper - scans and calculates EMC values for modded items
 *
 * Phase 2 implementation (COMPLETE):
 * - Recipe scanner (crafting, smelting, smithing) ✓
 * - Tag-based inference ✓
 * - Configuration overrides and blacklist ✓
 * - Report generation ✓
 * - EMC cache system (fast world loading) ✓
 * - ProjectE registration (CombinedPEMapper via ServiceLoader) ✓
 */
@EventBusSubscriber(modid = CombinedPE.MOD_ID)
public class DynamicEMCMapper {

    private static RecipeEMCCalculator calculator;
    private static final Map<Item, Double> discoveredEMC = new HashMap<>();

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
     * This is when we scan and calculate EMC values (or load from cache)
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

        // Check if force re-scan is enabled
        boolean forceRescan = Config.FORCE_RESCAN.get();
        if (forceRescan) {
            CombinedPE.LOGGER.info("Force re-scan enabled, invalidating cache...");
            EMCCache.invalidateCache();
            // Reset the config option so it doesn't re-scan every time
            // Note: Config values are read-only at runtime, user must manually set it back to false
        }

        // Try to load from cache first
        if (!forceRescan && EMCCache.cacheExists()) {
            CombinedPE.LOGGER.info("Loading EMC values from cache...");
            Map<Item, Double> cachedEMC = EMCCache.loadFromCache();

            if (cachedEMC != null && !cachedEMC.isEmpty()) {
                discoveredEMC.clear();
                discoveredEMC.putAll(cachedEMC);
                CombinedPE.LOGGER.info("Successfully loaded {} EMC values from cache", cachedEMC.size());
                // TODO: Phase 2.4 - Register cached values with ProjectE
                return;
            } else {
                CombinedPE.LOGGER.warn("Cache load failed, performing full scan...");
            }
        }

        // No cache or force re-scan: perform full scan
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
        int recipeBasedEMC = 0;
        int tagBasedEMC = 0;
        int overriddenEMC = 0;
        int blacklistedItems = 0;

        long startTime = System.currentTimeMillis();

        // Track EMC sources for report
        Map<Item, String> emcSources = new HashMap<>();
        List<String> blacklistedItemIds = new ArrayList<>();

        // Load configuration overrides and blacklist
        Map<String, Long> emcOverrides = Config.getEMCOverrides();
        CombinedPE.LOGGER.info("Loaded {} EMC overrides from config", emcOverrides.size());

        // Iterate through all registered items
        for (Item item : BuiltInRegistries.ITEM) {
            totalItems++;

            ItemStack stack = new ItemStack(item);
            String itemId = BuiltInRegistries.ITEM.getKey(item).toString();

            // Check blacklist first
            if (Config.isBlacklisted(itemId)) {
                blacklistedItems++;
                blacklistedItemIds.add(itemId);
                CombinedPE.LOGGER.debug("Skipping blacklisted item: {}", itemId);
                continue;
            }

            // Skip items that already have EMC
            if (ProjectECompat.hasEMC(stack)) {
                itemsWithEMC++;
                continue;
            }

            // Check for config override first (highest priority)
            if (emcOverrides.containsKey(itemId)) {
                long overrideValue = emcOverrides.get(itemId);
                discoveredEMC.put(item, (double) overrideValue);
                emcSources.put(item, "config_override");
                newEMCAssignments++;
                overriddenEMC++;

                CombinedPE.LOGGER.info("Applied EMC override for {}: {} (from config)",
                    itemId, overrideValue);
                continue;
            }

            // Try to calculate EMC from recipes
            double calculatedEMC = calculator.calculateEMC(stack);

            if (calculatedEMC > 0.0) {
                // Store recipe-based EMC
                discoveredEMC.put(item, calculatedEMC);
                emcSources.put(item, "recipe");
                newEMCAssignments++;
                recipeBasedEMC++;

                CombinedPE.LOGGER.info("Discovered EMC for {} from recipe: {} (will register as {})",
                    itemId, calculatedEMC, Math.round(calculatedEMC));
            } else {
                // No recipe found, try tag-based inference
                double inferredEMC = TagEMCInferrer.inferEMCFromTags(item);

                if (inferredEMC > 0.0) {
                    // Store tag-inferred EMC
                    discoveredEMC.put(item, inferredEMC);
                    emcSources.put(item, "tag_inference");
                    newEMCAssignments++;
                    tagBasedEMC++;

                    CombinedPE.LOGGER.info("Inferred EMC for {} from tags: {} (will register as {})",
                        itemId, inferredEMC, Math.round(inferredEMC));
                }
            }
        }

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        CombinedPE.LOGGER.info("=== Dynamic EMC Scan Complete ===");
        CombinedPE.LOGGER.info("Total items scanned: {}", totalItems);
        CombinedPE.LOGGER.info("Items with existing EMC: {}", itemsWithEMC);
        CombinedPE.LOGGER.info("Blacklisted items: {}", blacklistedItems);
        CombinedPE.LOGGER.info("New EMC values discovered: {}", newEMCAssignments);
        CombinedPE.LOGGER.info("  - From config overrides: {}", overriddenEMC);
        CombinedPE.LOGGER.info("  - From recipes: {}", recipeBasedEMC);
        CombinedPE.LOGGER.info("  - From tags: {}", tagBasedEMC);
        CombinedPE.LOGGER.info("Scan duration: {}ms", duration);

        // Save to cache for next world load
        CombinedPE.LOGGER.info("Saving EMC values to cache...");
        EMCCache.saveToCache(discoveredEMC, emcSources);

        // Generate report if enabled
        if (Config.GENERATE_REPORT.get()) {
            EMCReportGenerator.ReportData reportData = new EMCReportGenerator.ReportData();
            reportData.totalItems = totalItems;
            reportData.itemsWithExistingEMC = itemsWithEMC;
            reportData.blacklistedItems = blacklistedItems;
            reportData.newEMCAssignments = newEMCAssignments;
            reportData.overriddenEMC = overriddenEMC;
            reportData.recipeBasedEMC = recipeBasedEMC;
            reportData.tagBasedEMC = tagBasedEMC;
            reportData.scanDurationMs = duration;
            reportData.discoveredEMC = new HashMap<>(discoveredEMC);
            reportData.configOverrides = emcOverrides;
            reportData.blacklistedItemIds = blacklistedItemIds;
            reportData.emcSources = emcSources;

            EMCReportGenerator.generateReport(reportData);
        }

        // Note: EMC values are registered with ProjectE via CombinedPEMapper
        // (ServiceLoader-based IEMCMapper registered in META-INF/services)
        // ProjectE will call CombinedPEMapper.addMappings() during resource reload
    }

    /**
     * Get all discovered EMC values (as doubles for fractional precision)
     */
    public static Map<Item, Double> getDiscoveredEMC() {
        return new HashMap<>(discoveredEMC);
    }

    /**
     * Get all discovered EMC values rounded to long (for ProjectE registration)
     */
    public static Map<Item, Long> getDiscoveredEMCAsLong() {
        Map<Item, Long> rounded = new HashMap<>();
        for (Map.Entry<Item, Double> entry : discoveredEMC.entrySet()) {
            rounded.put(entry.getKey(), Math.round(entry.getValue()));
        }
        return rounded;
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
