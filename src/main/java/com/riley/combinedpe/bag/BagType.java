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

        net.minecraft.world.item.Item item = stack.getItem();

        switch (this) {
            case MATERIALS:
                // Building blocks: blocks that can be placed
                return item instanceof net.minecraft.world.item.BlockItem
                    || stack.is(net.minecraft.tags.ItemTags.PLANKS)
                    || stack.is(net.minecraft.tags.ItemTags.LOGS)
                    || stack.is(net.minecraft.tags.ItemTags.STONE_CRAFTING_MATERIALS)
                    || stack.is(net.minecraft.tags.ItemTags.TERRACOTTA);

            case FOOD:
                // Food items only (EXCLUSIVE - nothing else stores food)
                return item.components().has(net.minecraft.core.component.DataComponents.FOOD);

            case ORE:
                // Raw ores and processed minerals (NOT gems/valuables - those go to TREASURE)
                return stack.is(net.minecraft.tags.ItemTags.COAL_ORES)
                    || stack.is(net.minecraft.tags.ItemTags.IRON_ORES)
                    || stack.is(net.minecraft.tags.ItemTags.GOLD_ORES)
                    || stack.is(net.minecraft.tags.ItemTags.COPPER_ORES)
                    || item == net.minecraft.world.item.Items.RAW_IRON
                    || item == net.minecraft.world.item.Items.RAW_GOLD
                    || item == net.minecraft.world.item.Items.RAW_COPPER
                    || item == net.minecraft.world.item.Items.IRON_INGOT
                    || item == net.minecraft.world.item.Items.GOLD_INGOT
                    || item == net.minecraft.world.item.Items.COPPER_INGOT
                    || item == net.minecraft.world.item.Items.COAL
                    || item == net.minecraft.world.item.Items.QUARTZ;

            case TOOL:
                // Non-combat tools only (pickaxe, axe, hoe, shovel, shears, fishing rod)
                return item instanceof net.minecraft.world.item.PickaxeItem
                    || item instanceof net.minecraft.world.item.AxeItem
                    || item instanceof net.minecraft.world.item.HoeItem
                    || item instanceof net.minecraft.world.item.ShovelItem
                    || item instanceof net.minecraft.world.item.ShearsItem
                    || item instanceof net.minecraft.world.item.FishingRodItem
                    || item instanceof net.minecraft.world.item.FlintAndSteelItem;

            case MOB_DROP:
                // Common mob drops (NOT ender pearls - goes to ADVENTURE)
                return item == net.minecraft.world.item.Items.ROTTEN_FLESH
                    || item == net.minecraft.world.item.Items.BONE
                    || item == net.minecraft.world.item.Items.STRING
                    || item == net.minecraft.world.item.Items.SPIDER_EYE
                    || item == net.minecraft.world.item.Items.GUNPOWDER
                    || item == net.minecraft.world.item.Items.BLAZE_ROD
                    || item == net.minecraft.world.item.Items.GHAST_TEAR
                    || item == net.minecraft.world.item.Items.SLIME_BALL
                    || item == net.minecraft.world.item.Items.MAGMA_CREAM
                    || item == net.minecraft.world.item.Items.PHANTOM_MEMBRANE
                    || item == net.minecraft.world.item.Items.RABBIT_FOOT
                    || item == net.minecraft.world.item.Items.LEATHER
                    || item == net.minecraft.world.item.Items.FEATHER
                    || item == net.minecraft.world.item.Items.PRISMARINE_SHARD
                    || item == net.minecraft.world.item.Items.PRISMARINE_CRYSTALS;

            case LIQUID:
                // Buckets and fluid containers (EXCLUSIVE)
                return item instanceof net.minecraft.world.item.BucketItem
                    || item instanceof net.minecraft.world.item.MilkBucketItem
                    || item == net.minecraft.world.item.Items.GLASS_BOTTLE
                    || item == net.minecraft.world.item.Items.HONEY_BOTTLE;

            case REDSTONE:
                // Redstone components (placeable items) + redstone dust
                return item == net.minecraft.world.item.Items.REDSTONE
                    || item == net.minecraft.world.item.Items.REDSTONE_TORCH
                    || item == net.minecraft.world.item.Items.REPEATER
                    || item == net.minecraft.world.item.Items.COMPARATOR
                    || item == net.minecraft.world.item.Items.OBSERVER
                    || item == net.minecraft.world.item.Items.PISTON
                    || item == net.minecraft.world.item.Items.STICKY_PISTON
                    || item == net.minecraft.world.item.Items.DISPENSER
                    || item == net.minecraft.world.item.Items.DROPPER
                    || item == net.minecraft.world.item.Items.HOPPER
                    || item == net.minecraft.world.item.Items.REDSTONE_LAMP
                    || item == net.minecraft.world.item.Items.LEVER
                    || item == net.minecraft.world.item.Items.STONE_BUTTON
                    || item == net.minecraft.world.item.Items.STONE_PRESSURE_PLATE
                    || item == net.minecraft.world.item.Items.TRIPWIRE_HOOK
                    || item == net.minecraft.world.item.Items.DAYLIGHT_DETECTOR
                    || item == net.minecraft.world.item.Items.TARGET
                    || stack.is(net.minecraft.tags.ItemTags.REDSTONE_ORES);

            case POTION:
                // Potions and brewing ingredients ONLY (NOT for combat use)
                return item instanceof net.minecraft.world.item.PotionItem
                    || item instanceof net.minecraft.world.item.LingeringPotionItem
                    || item instanceof net.minecraft.world.item.SplashPotionItem
                    || item == net.minecraft.world.item.Items.BREWING_STAND
                    || item == net.minecraft.world.item.Items.BLAZE_POWDER
                    || item == net.minecraft.world.item.Items.NETHER_WART
                    || item == net.minecraft.world.item.Items.FERMENTED_SPIDER_EYE
                    || item == net.minecraft.world.item.Items.GLISTERING_MELON_SLICE
                    || item == net.minecraft.world.item.Items.GOLDEN_CARROT
                    || item == net.minecraft.world.item.Items.GLOWSTONE_DUST
                    || item == net.minecraft.world.item.Items.DRAGON_BREATH;

            case ENCHANTING:
                // Books and enchantment items (NOT enchanted books - goes to TREASURE)
                return item == net.minecraft.world.item.Items.BOOK
                    || item == net.minecraft.world.item.Items.BOOKSHELF
                    || item == net.minecraft.world.item.Items.ENCHANTING_TABLE
                    || item == net.minecraft.world.item.Items.ANVIL
                    || item == net.minecraft.world.item.Items.GRINDSTONE
                    || item == net.minecraft.world.item.Items.EXPERIENCE_BOTTLE
                    || stack.is(net.minecraft.tags.ItemTags.LAPIS_ORES)
                    || item == net.minecraft.world.item.Items.LAPIS_LAZULI;

            case TRADE:
                // Tradeable items (emeralds and trade goods) - EXCLUSIVE for emeralds
                return item == net.minecraft.world.item.Items.EMERALD
                    || item == net.minecraft.world.item.Items.EMERALD_BLOCK
                    || stack.is(net.minecraft.tags.ItemTags.VILLAGER_PLANTABLE_SEEDS)
                    || stack.is(net.minecraft.tags.ItemTags.EMERALD_ORES);

            case COMBAT:
                // Combat-only gear (weapons, armor, combat consumables)
                return item instanceof net.minecraft.world.item.SwordItem
                    || item instanceof net.minecraft.world.item.BowItem
                    || item instanceof net.minecraft.world.item.CrossbowItem
                    || item instanceof net.minecraft.world.item.ArmorItem
                    || item instanceof net.minecraft.world.item.ShieldItem
                    || item == net.minecraft.world.item.Items.ARROW
                    || item == net.minecraft.world.item.Items.SPECTRAL_ARROW
                    || item == net.minecraft.world.item.Items.TIPPED_ARROW
                    || item == net.minecraft.world.item.Items.GOLDEN_APPLE
                    || item == net.minecraft.world.item.Items.ENCHANTED_GOLDEN_APPLE;

            case ADVENTURE:
                // Exploration items (torches, navigation, teleportation, flight)
                return item == net.minecraft.world.item.Items.TORCH
                    || item == net.minecraft.world.item.Items.SOUL_TORCH
                    || item == net.minecraft.world.item.Items.LANTERN
                    || item == net.minecraft.world.item.Items.SOUL_LANTERN
                    || item instanceof net.minecraft.world.item.MapItem
                    || item instanceof net.minecraft.world.item.CompassItem
                    || item == net.minecraft.world.item.Items.CLOCK
                    || item == net.minecraft.world.item.Items.SPYGLASS
                    || item == net.minecraft.world.item.Items.ENDER_EYE
                    || item == net.minecraft.world.item.Items.ENDER_PEARL
                    || item == net.minecraft.world.item.Items.TOTEM_OF_UNDYING
                    || item == net.minecraft.world.item.Items.ELYTRA
                    || item == net.minecraft.world.item.Items.FIREWORK_ROCKET;

            case TREASURE:
                // Valuable items (gems, netherite, unique drops, enchanted books)
                return item == net.minecraft.world.item.Items.DIAMOND
                    || item == net.minecraft.world.item.Items.DIAMOND_BLOCK
                    || item == net.minecraft.world.item.Items.NETHERITE_INGOT
                    || item == net.minecraft.world.item.Items.NETHERITE_SCRAP
                    || item == net.minecraft.world.item.Items.ANCIENT_DEBRIS
                    || item == net.minecraft.world.item.Items.NETHERITE_BLOCK
                    || item == net.minecraft.world.item.Items.NETHER_STAR
                    || item == net.minecraft.world.item.Items.DRAGON_EGG
                    || item == net.minecraft.world.item.Items.TRIDENT
                    || item == net.minecraft.world.item.Items.HEART_OF_THE_SEA
                    || item == net.minecraft.world.item.Items.AMETHYST_SHARD
                    || item instanceof net.minecraft.world.item.EnchantedBookItem
                    || stack.is(net.minecraft.tags.ItemTags.DIAMOND_ORES);

            default:
                return true; // Fallback to universal storage
        }
    }
}
