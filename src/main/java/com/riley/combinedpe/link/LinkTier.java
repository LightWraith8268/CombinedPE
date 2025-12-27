package com.riley.combinedpe.link;

/**
 * Defines the tiers for EMC Link blocks.
 * Each tier has different throughput rates and visual styling.
 *
 * Progression: 1/sec → 4/sec → 10/sec → 20/sec → 64/sec → 256/sec → 1024/sec → infinite
 */
public enum LinkTier {
    /**
     * Basic tier - entry level
     * - 1 item/second (20 ticks per operation)
     * - 100 RF/tick for Energy Link
     * - Brown color theme (#8B4513)
     */
    BASIC("basic", 20, 100, 0x8B4513),

    /**
     * Advanced tier - early automation
     * - 4 items/second (5 ticks per operation)
     * - 400 RF/tick for Energy Link
     * - Blue color theme (#4169E1)
     */
    ADVANCED("advanced", 5, 400, 0x4169E1),

    /**
     * Superior tier - mid-tier automation
     * - 10 items/second (2 ticks per operation)
     * - 1,000 RF/tick for Energy Link
     * - Purple color theme (#9370DB)
     */
    SUPERIOR("superior", 2, 1000, 0x9370DB),

    /**
     * Masterful tier - high-speed automation
     * - 20 items/second (1 tick per operation)
     * - 2,500 RF/tick for Energy Link
     * - Gold color theme (#FFD700)
     */
    MASTERFUL("masterful", 1, 2500, 0xFFD700),

    /**
     * Eminent tier - very high speed
     * - 64 items/tick max (every tick, max 64 items)
     * - 5,000 RF/tick for Energy Link
     * - Cyan color theme (#00CED1)
     */
    EMINENT("eminent", 1, 64, 5000, 0x00CED1),

    /**
     * Supreme tier - extreme speed
     * - 256 items/tick max (every tick, max 256 items)
     * - 10,000 RF/tick for Energy Link
     * - Magenta color theme (#FF00FF)
     */
    SUPREME("supreme", 1, 256, 10000, 0xFF00FF),

    /**
     * Transcendent tier - near-infinite speed
     * - 1024 items/tick max (every tick, max 1024 items)
     * - 25,000 RF/tick for Energy Link
     * - Orange color theme (#FF8C00)
     */
    TRANSCENDENT("transcendent", 1, 1024, 25000, 0xFF8C00),

    /**
     * Ultimate tier - INFINITE throughput
     * - No limit (processes any amount instantly)
     * - 100,000 RF/tick for Energy Link
     * - Deep Pink color theme (#FF1493)
     */
    ULTIMATE("ultimate", 0, 0, 100000, 0xFF1493);

    private final String name;
    private final int ticksPerOperation;
    private final int maxItemsPerOperation; // Max items per operation (0 = unlimited)
    private final int energyPerTick;
    private final int color;

    LinkTier(String name, int ticksPerOperation, int energyPerTick, int color) {
        this(name, ticksPerOperation, 0, energyPerTick, color);
    }

    LinkTier(String name, int ticksPerOperation, int maxItemsPerOperation, int energyPerTick, int color) {
        this.name = name;
        this.ticksPerOperation = ticksPerOperation;
        this.maxItemsPerOperation = maxItemsPerOperation;
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
     * Get the maximum items that can be processed per operation (0 = unlimited)
     */
    public int getMaxItemsPerOperation() {
        return maxItemsPerOperation;
    }

    /**
     * Check if this tier has unlimited throughput
     */
    public boolean isUnlimited() {
        return ticksPerOperation == 0 && maxItemsPerOperation == 0;
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
