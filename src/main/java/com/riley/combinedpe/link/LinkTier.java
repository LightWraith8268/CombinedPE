package com.riley.combinedpe.link;

/**
 * Defines the tiers for EMC Link blocks.
 * Each tier has different throughput rates and visual styling.
 */
public enum LinkTier {
    /**
     * Basic tier - slowest throughput
     * - 1 item/second (20 ticks per operation)
     * - 100 RF/tick for Energy Link
     * - Brown color theme
     */
    BASIC("basic", 20, 100, 0x8B4513),

    /**
     * Advanced tier - medium throughput
     * - 8 items/second (2.5 ticks per operation, rounded to 3 ticks)
     * - 1,000 RF/tick for Energy Link
     * - Blue color theme
     */
    ADVANCED("advanced", 3, 1000, 0x4169E1),

    /**
     * Ultimate tier - INFINITE throughput
     * - No tick delay (0 ticks = instant/unlimited)
     * - 10,000 RF/tick for Energy Link
     * - Purple/Pink color theme
     */
    ULTIMATE("ultimate", 0, 10000, 0xFF1493);

    private final String name;
    private final int ticksPerOperation;
    private final int energyPerTick;
    private final int color;

    LinkTier(String name, int ticksPerOperation, int energyPerTick, int color) {
        this.name = name;
        this.ticksPerOperation = ticksPerOperation;
        this.energyPerTick = energyPerTick;
        this.color = color;
    }

    /**
     * Get the tier name (for registry/ID purposes)
     */
    public String getName() {
        return name;
    }

    /**
     * Get how many ticks must pass between operations
     */
    public int getTicksPerOperation() {
        return ticksPerOperation;
    }

    /**
     * Get the energy transfer rate for Energy Links (RF/tick)
     */
    public int getEnergyPerTick() {
        return energyPerTick;
    }

    /**
     * Get the color theme for this tier (RGB)
     */
    public int getColor() {
        return color;
    }

    /**
     * Get tier from name (for NBT deserialization)
     */
    public static LinkTier fromName(String name) {
        for (LinkTier tier : values()) {
            if (tier.name.equals(name)) {
                return tier;
            }
        }
        return BASIC; // Default fallback
    }
}
