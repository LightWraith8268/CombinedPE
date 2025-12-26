package com.riley.combinedpe;

import net.neoforged.neoforge.common.ModConfigSpec;

/**
 * Configuration handler for CombinedPE
 */
public class Config {

    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    // Dynamic EMC Registration Settings
    public static final ModConfigSpec.BooleanValue DYNAMIC_EMC_ENABLED;
    public static final ModConfigSpec.BooleanValue SCAN_ON_WORLD_LOAD;
    public static final ModConfigSpec.BooleanValue GENERATE_REPORT;

    // Inference Rules
    public static final ModConfigSpec.DoubleValue CRAFTING_MULTIPLIER;
    public static final ModConfigSpec.DoubleValue SMELTING_MULTIPLIER;
    public static final ModConfigSpec.DoubleValue SMITHING_MULTIPLIER;

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
    }

    public static final ModConfigSpec SPEC = BUILDER.build();
}
