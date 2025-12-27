package com.riley.combinedpe.bag;

/**
 * Different types of specialized bags, each with unique features and item filters.
 * All types share the same 5-tier progression (Basic, Advanced, Superior, Masterful, Ultimate)
 * and slot counts (108, 216, 432, 864, 1728).
 */
public enum BagType {
    /**
     * Materials Bag - Building blocks and construction materials
     * - Supplier Module: Auto-provides blocks when building
     * - Container Module: Pulls from external storage/RS
     * - Chisel/Stonecutter: Convert block variants
     * - Crafting Module: Auto-craft blocks
     * - EMC Provider: Pull blocks from EMC
     */
    MATERIALS("materials", 0x8B4513, "Building Blocks"),

    /**
     * Food Bag - Food items only
     * - Quick Eat: Right-click to eat from bag
     * - Auto-Feed: Eats when hungry
     * - Nutrition Balancing: Prioritizes best food
     * - EMC Food: Generate from EMC
     */
    FOOD("food", 0xFF6347, "Food Items"),

    /**
     * Ore Bag - Ores, ingots, gems, dusts
     * - Auto-Smelt: Convert ores to ingots
     * - Doubling: 2x ore processing
     * - Fortune Simulation: Apply fortune effect
     * - EMC Ore Gen: Generate from EMC
     */
    ORE("ore", 0xC0C0C0, "Ores & Minerals"),

    /**
     * Tool Bag - Tools, weapons, armor
     * - Auto-Repair: Repair using materials
     * - Enchant Preservation: Keep enchantments
     * - Auto-Upgrade: Upgrade materials
     * - EMC Repair: Repair using EMC
     */
    TOOL("tool", 0xFFD700, "Tools & Equipment"),

    /**
     * Mob Drop Bag - Mob drops and loot
     * - Auto-Loot: Collect nearby drops
     * - Drop Multiplier: 2x drops
     * - Rare Drop Boost: Better rare drops
     * - EMC Conversion: Convert to EMC (Ultimate only)
     */
    MOB_DROP("mob_drop", 0x8B4513, "Mob Drops"),

    /**
     * Liquid Bag - Buckets and fluids
     * - Fluid Storage: Store fluids directly
     * - Auto-Fill: Fill from sources
     * - Infinite Water: Free water generation
     * - EMC Fluids: Generate from EMC
     */
    LIQUID("liquid", 0x1E90FF, "Liquids & Buckets"),

    /**
     * Redstone Bag - All placeable redstone items
     * Stores: dust, torches, repeaters, comparators, observers, pistons,
     * dispensers, droppers, hoppers, sensors, etc.
     * - Quick Place: Right-click to place
     * - Auto-Wire: Auto-place redstone
     * - EMC Components: Generate from EMC
     */
    REDSTONE("redstone", 0xFF0000, "Redstone Components"),

    /**
     * Potion Bag - Potions and brewing ingredients
     * - Auto-Brew: Auto-brew potions
     * - Effect Stacking: Stack effects
     * - EMC Brewing: Brew using EMC
     */
    POTION("potion", 0x9370DB, "Potions & Brewing"),

    /**
     * Enchanting Bag - Books and enchantment items
     * - XP Storage: Store XP in bag
     * - Auto-Enchant: Auto-enchant items
     * - Enchant Transfer: Move enchants between items
     * - EMC Enchanting: Enchant using EMC
     */
    ENCHANTING("enchanting", 0x7B68EE, "Enchantments & XP"),

    /**
     * Trade Bag - Tradeable items
     * - Villager Scanner: Show wanted trades
     * - Auto-Trade: Trade automatically
     * - Trade History: Track best deals
     * - EMC Trading: Buy trades with EMC
     */
    TRADE("trade", 0x32CD32, "Tradeable Items"),

    /**
     * Combat Bag - Weapons, potions, combat items
     * - Quick Swap: Hotkey weapon swap
     * - Auto-Buff: Auto-apply potions before combat
     * - Ammo Supply: Infinite arrows/ammo
     * - EMC Arsenal: Generate weapons from EMC
     */
    COMBAT("combat", 0xDC143C, "Combat Gear"),

    /**
     * Adventure Bag - Exploration items
     * - Map Integration: Show waypoints
     * - Torch Auto-Place: Auto-place torches
     * - Totem Protection: Built-in totem of undying
     * - EMC Survival: Emergency items from EMC
     */
    ADVENTURE("adventure", 0x228B22, "Exploration Gear"),

    /**
     * Treasure Bag - Valuable items
     * - Auto-Appraisal: Show item values
     * - Duplicate Detection: Highlight duplicates
     * - EMC Valuation: Show EMC worth
     */
    TREASURE("treasure", 0xFFD700, "Valuables & Loot");

    private final String name;
    private final int color;
    private final String category;

    BagType(String name, int color, String category) {
        this.name = name;
        this.color = color;
        this.category = category;
    }

    /**
     * Get the bag type's registry name
     */
    public String getName() {
        return name;
    }

    /**
     * Get the bag type's theme color (for GUI/textures)
     */
    public int getColor() {
        return color;
    }

    /**
     * Get the category description (for tooltips)
     */
    public String getCategory() {
        return category;
    }

    /**
     * Get display name (capitalized)
     */
    public String getDisplayName() {
        // Convert snake_case to Title Case
        String[] parts = name.split("_");
        StringBuilder result = new StringBuilder();
        for (String part : parts) {
            result.append(Character.toUpperCase(part.charAt(0)))
                  .append(part.substring(1))
                  .append(" ");
        }
        return result.toString().trim();
    }

    /**
     * Get by name (for deserialization)
     */
    public static BagType byName(String name) {
        for (BagType type : values()) {
            if (type.name.equalsIgnoreCase(name)) {
                return type;
            }
        }
        return MATERIALS; // Default to materials bag
    }

    /**
     * Check if this bag type can store a specific item
     */
    public boolean canStore(net.minecraft.world.item.ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }

        // Will be implemented per-type with tag/category checking
        // For now, return true (universal storage)
        // TODO: Implement type-specific filtering
        return true;
    }
}
