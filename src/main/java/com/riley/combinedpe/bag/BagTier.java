package com.riley.combinedpe.bag;

import net.minecraft.util.StringRepresentable;

/**
 * Tiers for Builder's Bag
 * Each tier has different capacity and module availability
 */
public enum BagTier implements StringRepresentable {
    BASIC("basic", 27, 0x8B4513),      // Brown
    ADVANCED("advanced", 54, 0x4169E1),  // Royal Blue
    SUPERIOR("superior", 81, 0x9370DB),  // Medium Purple
    MASTERFUL("masterful", 108, 0xFFD700), // Gold
    ULTIMATE("ultimate", 135, 0xFF1493); // Deep Pink

    private final String name;
    private final int capacity;
    private final int color;

    BagTier(String name, int capacity, int color) {
        this.name = name;
        this.capacity = capacity;
        this.color = color;
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
