package com.riley.combinedpe.bag;

import net.minecraft.util.StringRepresentable;

/**
 * Tiers for Builder's Bag
 * Each tier has different capacity and module availability
 */
public enum BagTier implements StringRepresentable {
    BASIC("basic", 54, 0x8B4513),      // Brown - 54 slots (6 rows)
    ADVANCED("advanced", 108, 0x4169E1),  // Royal Blue - 108 slots (12 rows)
    SUPERIOR("superior", 162, 0x9370DB),  // Medium Purple - 162 slots (18 rows)
    MASTERFUL("masterful", 216, 0xFFD700), // Gold - 216 slots (24 rows)
    ULTIMATE("ultimate", 270, 0xFF1493); // Deep Pink - 270 slots (30 rows)

    private final String name;
    private final int capacity;
    private final int color;
    private final int stackMultiplier;

    BagTier(String name, int capacity, int color) {
        this.name = name;
        this.capacity = capacity;
        this.color = color;
        // Stack multipliers for virtual stacking (exponential progression)
        // Basic=1x, Advanced=4x, Superior=16x, Masterful=64x, Ultimate=256x
        this.stackMultiplier = (int) Math.pow(4, this.ordinal());
    }

    /**
     * Get the tier's inventory capacity (in slots)
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * Get the tier's display color (for rendering)
     */
    public int getColor() {
        return color;
    }

    /**
     * Get the tier's stack multiplier for virtual stacking
     * Basic=1x (64), Advanced=4x (256), Superior=16x (1024),
     * Masterful=64x (4096), Ultimate=256x (16384)
     */
    public int getStackMultiplier() {
        return stackMultiplier;
    }

    /**
     * Get the maximum stack size for this tier (without upgrades)
     */
    public int getMaxStackSize() {
        return 64 * stackMultiplier;
    }

    /**
     * Get the tier's serialized name
     */
    @Override
    public String getSerializedName() {
        return name;
    }

    /**
     * Check if this tier has the supplier module enabled
     */
    public boolean hasSupplierModule() {
        // All tiers have supplier module
        return true;
    }

    /**
     * Check if this tier has the container module enabled
     */
    public boolean hasContainerModule() {
        // Advanced and above
        return this.ordinal() >= ADVANCED.ordinal();
    }

    /**
     * Check if this tier has the crafting module enabled
     */
    public boolean hasCraftingModule() {
        // Masterful and above
        return this.ordinal() >= MASTERFUL.ordinal();
    }

    /**
     * Check if this tier has the EMC provider module enabled
     */
    public boolean hasEMCProviderModule() {
        // Ultimate only
        return this == ULTIMATE;
    }

    /**
     * Get tier by name (case-insensitive)
     */
    public static BagTier byName(String name) {
        for (BagTier tier : values()) {
            if (tier.name.equalsIgnoreCase(name)) {
                return tier;
            }
        }
        return BASIC; // Default to basic
    }

    /**
     * Get display name for tier
     */
    public String getDisplayName() {
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }
}
