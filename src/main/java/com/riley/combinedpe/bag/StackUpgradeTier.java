package com.riley.combinedpe.bag;

import net.minecraft.util.StringRepresentable;

/**
 * Stack upgrade tiers for increasing virtual stack capacity
 */
public enum StackUpgradeTier implements StringRepresentable {
    NONE("none", 1),        // No upgrade: base stack size
    TIER_I("tier_i", 4),    // Stack Upgrade I: 4x (256 items)
    TIER_II("tier_ii", 16), // Stack Upgrade II: 16x (1,024 items)
    TIER_III("tier_iii", 64), // Stack Upgrade III: 64x (4,096 items)
    TIER_IV("tier_iv", 256); // Stack Upgrade IV: 256x (16,384 items)

    private final String name;
    private final int multiplier;

    StackUpgradeTier(String name, int multiplier) {
        this.name = name;
        this.multiplier = multiplier;
    }

    /**
     * Get the stack multiplier for this tier
     */
    public int getMultiplier() {
        return multiplier;
    }

    /**
     * Get the maximum stack size with this upgrade applied
     * @param baseStackSize Base stack size (usually 64)
     * @return Max stack size with upgrade
     */
    public int getMaxStackSize(int baseStackSize) {
        return baseStackSize * multiplier;
    }

    /**
     * Get display name for tooltip
     */
    public String getDisplayName() {
        return switch (this) {
            case NONE -> "No Upgrade";
            case TIER_I -> "Stack Upgrade I";
            case TIER_II -> "Stack Upgrade II";
            case TIER_III -> "Stack Upgrade III";
            case TIER_IV -> "Stack Upgrade IV";
        };
    }

    /**
     * Get the serialized name
     */
    @Override
    public String getSerializedName() {
        return name;
    }

    /**
     * Get tier by name (case-insensitive)
     */
    public static StackUpgradeTier byName(String name) {
        for (StackUpgradeTier tier : values()) {
            if (tier.name.equalsIgnoreCase(name)) {
                return tier;
            }
        }
        return NONE;
    }

    /**
     * Get the next upgrade tier (or null if at max)
     */
    public StackUpgradeTier getNext() {
        return switch (this) {
            case NONE -> TIER_I;
            case TIER_I -> TIER_II;
            case TIER_II -> TIER_III;
            case TIER_III -> TIER_IV;
            case TIER_IV -> null; // Max tier
        };
    }
}
