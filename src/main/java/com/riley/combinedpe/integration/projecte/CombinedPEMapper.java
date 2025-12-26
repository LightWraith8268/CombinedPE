package com.riley.combinedpe.integration.projecte;

import com.riley.combinedpe.CombinedPE;
import com.riley.combinedpe.emc.DynamicEMCMapper;
import moze_intel.projecte.api.mapper.IEMCMapper;
import moze_intel.projecte.api.mapper.collector.IMappingCollector;
import moze_intel.projecte.api.nss.NSSItem;
import moze_intel.projecte.api.nss.NormalizedSimpleStack;
import net.minecraft.core.RegistryAccess;
import net.minecraft.server.ReloadableServerResources;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.Item;

import java.util.Map;

/**
 * ProjectE EMC mapper for CombinedPE
 *
 * Registers dynamically calculated EMC values with ProjectE's EMC system.
 * This allows our calculated values to be used by ProjectE for transmutation.
 *
 * Registration flow:
 * 1. ProjectE loads and calls addMappings() during resource reload
 * 2. We provide our calculated EMC values
 *    - Config overrides use setValueAfter() to override even hardcoded ProjectE values
 *    - Calculated values use setValueBefore() to fill gaps without overriding ProjectE
 * 3. ProjectE's graph algorithm incorporates our values
 * 4. Final EMC values become available in-game
 */
public class CombinedPEMapper implements IEMCMapper<NormalizedSimpleStack, Long> {

    /**
     * Check if this mapper should be active
     */
    @Override
    public boolean isAvailable() {
        // Only active if we have calculated EMC values
        Map<Item, Long> emcValues = DynamicEMCMapper.getDiscoveredEMCAsLong();
        return emcValues != null && !emcValues.isEmpty();
    }

    /**
     * Add our EMC mappings to ProjectE's collector
     */
    @Override
    public void addMappings(
        IMappingCollector<NormalizedSimpleStack, Long> collector,
        ReloadableServerResources serverResources,
        RegistryAccess registryAccess,
        ResourceManager resourceManager
    ) {
        Map<Item, Long> emcValues = DynamicEMCMapper.getDiscoveredEMCAsLong();
        Map<Item, String> emcSources = DynamicEMCMapper.getEMCSources();

        if (emcValues == null || emcValues.isEmpty()) {
            CombinedPE.LOGGER.warn("CombinedPEMapper called but no EMC values available");
            return;
        }

        int overrideCount = 0;
        int calculatedCount = 0;

        for (Map.Entry<Item, Long> entry : emcValues.entrySet()) {
            Item item = entry.getKey();
            Long emcValue = entry.getValue();

            // Skip invalid values
            if (emcValue == null || emcValue <= 0) {
                continue;
            }

            try {
                // Create NormalizedSimpleStack from item
                NormalizedSimpleStack nss = NSSItem.createItem(item);

                // Determine if this is a config override or calculated value
                String source = emcSources.get(item);
                boolean isOverride = "config_override".equals(source);

                if (isOverride) {
                    // Config overrides use setValueAfter to override even hardcoded ProjectE values
                    collector.setValueAfter(nss, emcValue);
                    overrideCount++;
                } else {
                    // Calculated values use setValueBefore to fill gaps without overriding ProjectE
                    collector.setValueBefore(nss, emcValue);
                    calculatedCount++;
                }

            } catch (Exception e) {
                CombinedPE.LOGGER.error("Failed to register EMC for item: {}", item, e);
            }
        }

        CombinedPE.LOGGER.info("CombinedPEMapper registered {} calculated EMC values and {} overrides with ProjectE",
            calculatedCount, overrideCount);
    }

    /**
     * Mapper configuration name (for ProjectE's config system)
     */
    @Override
    public String getName() {
        return "CombinedPE";
    }

    /**
     * Translation key for configuration UI
     */
    @Override
    public String getTranslationKey() {
        return "combinedpe.mapper.name";
    }

    /**
     * Description for configuration UI
     */
    @Override
    public String getDescription() {
        return "Dynamically calculated EMC values from CombinedPE";
    }
}
