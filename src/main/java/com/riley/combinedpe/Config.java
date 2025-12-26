package com.riley.combinedpe;

import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Configuration handler for CombinedPE
 */
public class Config {

    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    // Dynamic EMC Registration Settings
    public static final ModConfigSpec.BooleanValue DYNAMIC_EMC_ENABLED;
    public static final ModConfigSpec.BooleanValue SCAN_ON_WORLD_LOAD;
    public static final ModConfigSpec.BooleanValue GENERATE_REPORT;
    public static final ModConfigSpec.BooleanValue FORCE_RESCAN;

    // Inference Rules
    public static final ModConfigSpec.DoubleValue CRAFTING_MULTIPLIER;
    public static final ModConfigSpec.DoubleValue SMELTING_MULTIPLIER;
    public static final ModConfigSpec.DoubleValue SMITHING_MULTIPLIER;

    // EMC Overrides and Blacklist
    public static final ModConfigSpec.ConfigValue<List<? extends String>> EMC_OVERRIDE_ENTRIES;
    public static final ModConfigSpec.ConfigValue<List<? extends String>> BLACKLISTED_ITEMS;

    static {
        BUILDER.comment("Dynamic EMC Registration Settings").push("dynamic_registration");

        DYNAMIC_EMC_ENABLED = BUILDER
                .comment("Enable dynamic EMC calculation and registration")
                .define("enabled", true);

        SCAN_ON_WORLD_LOAD = BUILDER
                .comment("Scan and register EMC values when world loads")
                .define("scan_on_world_load", true);

        GENERATE_REPORT = BUILDER
                .comment("Generate a report of assigned EMC values")
                .define("generate_report", true);

        FORCE_RESCAN = BUILDER
                .comment(
                    "Force a complete re-scan on next world load (ignores cache)",
                    "After re-scan completes, this will automatically reset to false",
                    "Use this to refresh EMC values after adding new mods or changing recipes"
                )
                .define("force_rescan", false);

        BUILDER.pop();

        BUILDER.comment("Inference Rules for EMC Calculation").push("inference_rules");

        CRAFTING_MULTIPLIER = BUILDER
                .comment("Multiplier for items crafted via crafting recipes")
                .defineInRange("crafting_multiplier", 1.0, 0.1, 10.0);

        SMELTING_MULTIPLIER = BUILDER
                .comment("Multiplier for items processed via smelting (slight bonus for processing)")
                .defineInRange("smelting_multiplier", 1.1, 0.1, 10.0);

        SMITHING_MULTIPLIER = BUILDER
                .comment("Multiplier for items upgraded via smithing")
                .defineInRange("smithing_multiplier", 1.0, 0.1, 10.0);

        BUILDER.pop();

        BUILDER.comment("EMC Value Overrides").push("emc_overrides");

        EMC_OVERRIDE_ENTRIES = BUILDER
                .comment(
                    "Custom EMC values for specific items",
                    "Format: \"modid:itemname=emc_value\"",
                    "Examples:",
                    "  \"minecraft:dirt=1\"",
                    "  \"minecraft:diamond=8192\"",
                    "These values override both recipe-based and tag-based calculations"
                )
                .defineListAllowEmpty(
                    "overrides",
                    () -> Arrays.asList(),
                    obj -> obj instanceof String
                );

        BUILDER.pop();

        BUILDER.comment("Blacklist Configuration").push("blacklist");

        BLACKLISTED_ITEMS = BUILDER
                .comment(
                    "Items that should never receive EMC values",
                    "Format: \"modid:itemname\"",
                    "Examples:",
                    "  \"minecraft:bedrock\"",
                    "  \"minecraft:command_block\"",
                    "  \"minecraft:structure_block\"",
                    "Blacklisted items will not be assigned EMC through any method",
                    "By default, no items are blacklisted - users can add items as needed"
                )
                .defineListAllowEmpty(
                    "items",
                    () -> Arrays.asList(),
                    obj -> obj instanceof String
                );

        BUILDER.pop();
    }

    public static final ModConfigSpec SPEC = BUILDER.build();

    /**
     * Parse EMC override entries into a map
     * @return Map of item ID to EMC value
     */
    public static Map<String, Long> getEMCOverrides() {
        Map<String, Long> overrides = new HashMap<>();

        for (String entry : EMC_OVERRIDE_ENTRIES.get()) {
            if (entry == null || entry.trim().isEmpty()) {
                continue;
            }

            // Parse format: "modid:itemname=emc_value"
            String[] parts = entry.split("=", 2);
            if (parts.length != 2) {
                CombinedPE.LOGGER.warn("Invalid EMC override format (expected 'item=value'): {}", entry);
                continue;
            }

            String itemId = parts[0].trim();
            String valueStr = parts[1].trim();

            try {
                long emcValue = Long.parseLong(valueStr);
                overrides.put(itemId, emcValue);
                CombinedPE.LOGGER.debug("Loaded EMC override: {} = {}", itemId, emcValue);
            } catch (NumberFormatException e) {
                CombinedPE.LOGGER.warn("Invalid EMC value for {}: {}", itemId, valueStr);
            }
        }

        return overrides;
    }

    /**
     * Check if an item is blacklisted
     * @param itemId The item's resource location as string (e.g., "minecraft:dirt")
     * @return true if the item should not receive EMC
     */
    public static boolean isBlacklisted(String itemId) {
        return BLACKLISTED_ITEMS.get().contains(itemId);
    }
}
