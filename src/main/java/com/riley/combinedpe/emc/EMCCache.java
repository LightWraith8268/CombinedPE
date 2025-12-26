package com.riley.combinedpe.emc;

import com.google.gson.*;
import com.riley.combinedpe.CombinedPE;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * Caches calculated EMC values to disk to avoid re-scanning on every world load
 *
 * Cache structure:
 * - Saved to: config/combinedpe/emc_cache.json
 * - Contains: EMC values, sources, metadata
 * - Versioned for compatibility checking
 *
 * Cache invalidation:
 * - Manual deletion of cache file
 * - Config option to force re-scan
 * - Version mismatch
 */
public class EMCCache {

    private static final Path CACHE_FILE = Paths.get("config", "combinedpe", "emc_cache.json");
    private static final int CACHE_VERSION = 1;
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    /**
     * Cache data structure
     */
    public static class CacheData {
        public int version = CACHE_VERSION;
        public String minecraftVersion;
        public String scanTimestamp;
        public Map<String, CachedEMCEntry> emcValues = new HashMap<>();

        public static class CachedEMCEntry {
            public double value;
            public String source;

            public CachedEMCEntry(double value, String source) {
                this.value = value;
                this.source = source;
            }
        }
    }

    /**
     * Check if cache file exists and is valid
     */
    public static boolean cacheExists() {
        if (!Files.exists(CACHE_FILE)) {
            return false;
        }

        try {
            CacheData cache = loadCacheData();
            if (cache.version != CACHE_VERSION) {
                CombinedPE.LOGGER.warn("EMC cache version mismatch (expected {}, found {})",
                    CACHE_VERSION, cache.version);
                return false;
            }
            return true;
        } catch (Exception e) {
            CombinedPE.LOGGER.warn("Failed to read EMC cache, will re-scan", e);
            return false;
        }
    }

    /**
     * Load EMC values from cache
     * @return Map of Item to Double EMC values, or null if cache invalid
     */
    public static Map<Item, Double> loadFromCache() {
        try {
            CacheData cache = loadCacheData();

            if (cache.version != CACHE_VERSION) {
                CombinedPE.LOGGER.warn("Cache version mismatch, invalidating cache");
                return null;
            }

            Map<Item, Double> emcValues = new HashMap<>();
            int loadedCount = 0;
            int skippedCount = 0;

            for (Map.Entry<String, CacheData.CachedEMCEntry> entry : cache.emcValues.entrySet()) {
                String itemId = entry.getKey();
                ResourceLocation itemLocation = ResourceLocation.tryParse(itemId);

                if (itemLocation == null) {
                    CombinedPE.LOGGER.warn("Invalid item ID in cache: {}", itemId);
                    skippedCount++;
                    continue;
                }

                Item item = BuiltInRegistries.ITEM.get(itemLocation);
                if (item == null || item == net.minecraft.world.item.Items.AIR) {
                    CombinedPE.LOGGER.debug("Item from cache no longer exists: {}", itemId);
                    skippedCount++;
                    continue;
                }

                emcValues.put(item, entry.getValue().value);
                loadedCount++;
            }

            CombinedPE.LOGGER.info("Loaded {} EMC values from cache (scanned: {}, skipped: {})",
                loadedCount, cache.scanTimestamp, skippedCount);

            return emcValues;

        } catch (Exception e) {
            CombinedPE.LOGGER.error("Failed to load EMC cache", e);
            return null;
        }
    }

    /**
     * Load EMC sources from cache for reporting
     */
    public static Map<Item, String> loadSourcesFromCache() {
        try {
            CacheData cache = loadCacheData();
            Map<Item, String> sources = new HashMap<>();

            for (Map.Entry<String, CacheData.CachedEMCEntry> entry : cache.emcValues.entrySet()) {
                String itemId = entry.getKey();
                ResourceLocation itemLocation = ResourceLocation.tryParse(itemId);

                if (itemLocation == null) continue;

                Item item = BuiltInRegistries.ITEM.get(itemLocation);
                if (item == null || item == net.minecraft.world.item.Items.AIR) continue;

                sources.put(item, entry.getValue().source);
            }

            return sources;

        } catch (Exception e) {
            CombinedPE.LOGGER.error("Failed to load sources from cache", e);
            return new HashMap<>();
        }
    }

    /**
     * Save EMC values to cache
     */
    public static void saveToCache(Map<Item, Double> emcValues, Map<Item, String> emcSources) {
        try {
            // Create cache directory if needed
            Files.createDirectories(CACHE_FILE.getParent());

            CacheData cache = new CacheData();
            cache.minecraftVersion = net.minecraft.SharedConstants.getCurrentVersion().getName();
            cache.scanTimestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

            // Convert Item map to String-keyed map for JSON
            for (Map.Entry<Item, Double> entry : emcValues.entrySet()) {
                Item item = entry.getKey();
                String itemId = BuiltInRegistries.ITEM.getKey(item).toString();
                String source = emcSources.getOrDefault(item, "unknown");

                cache.emcValues.put(itemId, new CacheData.CachedEMCEntry(entry.getValue(), source));
            }

            // Write to file
            try (Writer writer = Files.newBufferedWriter(CACHE_FILE)) {
                GSON.toJson(cache, writer);
            }

            CombinedPE.LOGGER.info("Saved {} EMC values to cache: {}",
                emcValues.size(), CACHE_FILE.toAbsolutePath());

        } catch (Exception e) {
            CombinedPE.LOGGER.error("Failed to save EMC cache", e);
        }
    }

    /**
     * Delete the cache file to force a re-scan
     */
    public static boolean invalidateCache() {
        try {
            if (Files.exists(CACHE_FILE)) {
                Files.delete(CACHE_FILE);
                CombinedPE.LOGGER.info("EMC cache invalidated");
                return true;
            }
            return false;
        } catch (Exception e) {
            CombinedPE.LOGGER.error("Failed to delete cache file", e);
            return false;
        }
    }

    /**
     * Load cache data from file
     */
    private static CacheData loadCacheData() throws IOException {
        try (Reader reader = Files.newBufferedReader(CACHE_FILE)) {
            return GSON.fromJson(reader, CacheData.class);
        }
    }

    /**
     * Get cache file path
     */
    public static Path getCacheFile() {
        return CACHE_FILE;
    }
}
