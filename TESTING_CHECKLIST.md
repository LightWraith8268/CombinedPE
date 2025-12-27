# CombinedPE v1.7.6 Testing Checklist

**Mod Version:** 1.7.6
**Minecraft Version:** 1.21.1
**NeoForge Version:** 21.1.82
**Build:** combinedpe-1.7.6.jar (181KB)

## Pre-Testing Setup

- [ ] Install mod in `.minecraft/mods/` folder
- [ ] Launch Minecraft 1.21.1 with NeoForge 21.1.82
- [ ] Verify mod loads without crashes
- [ ] Check logs for any errors/warnings

## Creative Menu Testing

### Builder's Bags (65 total)
Test that all bags appear with proper names and icons:

**Materials Bags (5)**
- [ ] Basic Materials Bag - proper name and icon
- [ ] Advanced Materials Bag - proper name and icon
- [ ] Superior Materials Bag - proper name and icon
- [ ] Masterful Materials Bag - proper name and icon
- [ ] Ultimate Materials Bag - proper name and icon

**Food Bags (5)**
- [ ] Basic Food Bag - proper name and icon
- [ ] Advanced Food Bag - proper name and icon
- [ ] Superior Food Bag - proper name and icon
- [ ] Masterful Food Bag - proper name and icon
- [ ] Ultimate Food Bag - proper name and icon

**Ore Bags (5)**
- [ ] Basic Ore Bag - proper name and icon
- [ ] Advanced Ore Bag - proper name and icon
- [ ] Superior Ore Bag - proper name and icon
- [ ] Masterful Ore Bag - proper name and icon
- [ ] Ultimate Ore Bag - proper name and icon

**Tool Bags (5)**
- [ ] Basic Tool Bag - proper name and icon
- [ ] Advanced Tool Bag - proper name and icon
- [ ] Superior Tool Bag - proper name and icon
- [ ] Masterful Tool Bag - proper name and icon
- [ ] Ultimate Tool Bag - proper name and icon

**Mob Drop Bags (5)**
- [ ] Basic Mob Drop Bag - proper name and icon
- [ ] Advanced Mob Drop Bag - proper name and icon
- [ ] Superior Mob Drop Bag - proper name and icon
- [ ] Masterful Mob Drop Bag - proper name and icon
- [ ] Ultimate Mob Drop Bag - proper name and icon

**Liquid Bags (5)**
- [ ] Basic Liquid Bag - proper name and icon
- [ ] Advanced Liquid Bag - proper name and icon
- [ ] Superior Liquid Bag - proper name and icon
- [ ] Masterful Liquid Bag - proper name and icon
- [ ] Ultimate Liquid Bag - proper name and icon

**Redstone Bags (5)**
- [ ] Basic Redstone Bag - proper name and icon
- [ ] Advanced Redstone Bag - proper name and icon
- [ ] Superior Redstone Bag - proper name and icon
- [ ] Masterful Redstone Bag - proper name and icon
- [ ] Ultimate Redstone Bag - proper name and icon

**Potion Bags (5)**
- [ ] Basic Potion Bag - proper name and icon
- [ ] Advanced Potion Bag - proper name and icon
- [ ] Superior Potion Bag - proper name and icon
- [ ] Masterful Potion Bag - proper name and icon
- [ ] Ultimate Potion Bag - proper name and icon

**Enchanting Bags (5)**
- [ ] Basic Enchanting Bag - proper name and icon
- [ ] Advanced Enchanting Bag - proper name and icon
- [ ] Superior Enchanting Bag - proper name and icon
- [ ] Masterful Enchanting Bag - proper name and icon
- [ ] Ultimate Enchanting Bag - proper name and icon

**Trade Bags (5)**
- [ ] Basic Trade Bag - proper name and icon
- [ ] Advanced Trade Bag - proper name and icon
- [ ] Superior Trade Bag - proper name and icon
- [ ] Masterful Trade Bag - proper name and icon
- [ ] Ultimate Trade Bag - proper name and icon

**Combat Bags (5)**
- [ ] Basic Combat Bag - proper name and icon
- [ ] Advanced Combat Bag - proper name and icon
- [ ] Superior Combat Bag - proper name and icon
- [ ] Masterful Combat Bag - proper name and icon
- [ ] Ultimate Combat Bag - proper name and icon

**Adventure Bags (5)**
- [ ] Basic Adventure Bag - proper name and icon
- [ ] Advanced Adventure Bag - proper name and icon
- [ ] Superior Adventure Bag - proper name and icon
- [ ] Masterful Adventure Bag - proper name and icon
- [ ] Ultimate Adventure Bag - proper name and icon

**Treasure Bags (5)**
- [ ] Basic Treasure Bag - proper name and icon
- [ ] Advanced Treasure Bag - proper name and icon
- [ ] Superior Treasure Bag - proper name and icon
- [ ] Masterful Treasure Bag - proper name and icon
- [ ] Ultimate Treasure Bag - proper name and icon

### Stack Upgrades (4)
- [ ] Stack Upgrade I - proper name and icon
- [ ] Stack Upgrade II - proper name and icon
- [ ] Stack Upgrade III - proper name and icon
- [ ] Stack Upgrade IV - proper name and icon

### Blocks (1)
- [ ] Enhanced Workbench - proper name and icon

## Functional Testing

### Builder's Bags
- [ ] Can open bag GUI by right-clicking
- [ ] Bag GUI displays with correct size (varies by tier)
- [ ] Can insert items matching bag type
- [ ] Cannot insert items not matching bag type
- [ ] Items persist when closing/reopening bag
- [ ] Multiple bags of same type work independently
- [ ] Stack upgrades can be applied to bags
- [ ] Stack limits increase correctly with upgrades

### Enhanced Workbench
- [ ] Block can be placed in world
- [ ] Right-clicking opens crafting GUI
- [ ] 3×3 crafting grid functions
- [ ] Can craft vanilla recipes
- [ ] Result appears in output slot
- [ ] Can take result and ingredients are consumed
- [ ] Can shift-click result for quick crafting

### Bag Type Filtering
Test that each bag type correctly accepts/rejects items:

**Materials Bag** - Should accept:
- [ ] Blocks (cobblestone, stone, dirt, etc.)
- [ ] Planks, logs, stone materials, terracotta

**Food Bag** - Should accept:
- [ ] Food items (bread, cooked meat, etc.)

**Ore Bag** - Should accept:
- [ ] Raw ores, ore blocks, ingots, nuggets

**Tool Bag** - Should accept:
- [ ] Tools (pickaxe, axe, shovel, hoe, etc.)

**Mob Drop Bag** - Should accept:
- [ ] Bones, gunpowder, string, leather, etc.

**Liquid Bag** - Should accept:
- [ ] Buckets (water, lava, milk)
- [ ] Bottles (water bottles, potions)

**Redstone Bag** - Should accept:
- [ ] Redstone dust, repeaters, comparators, etc.

**Potion Bag** - Should accept:
- [ ] Potions, splash potions, lingering potions

**Enchanting Bag** - Should accept:
- [ ] Enchanted books, enchanting tables, bookshelves

**Trade Bag** - Should accept:
- [ ] Emeralds, villager spawn eggs

**Combat Bag** - Should accept:
- [ ] Weapons, armor, shields

**Adventure Bag** - Should accept:
- [ ] Maps, compasses, spyglasses

**Treasure Bag** - Should accept:
- [ ] Diamonds, netherite, gold blocks, etc.

## Recipe Testing

### Enhanced Workbench Recipe
- [ ] Recipe appears in JEI/REI (if installed)
- [ ] Can craft Enhanced Workbench
- [ ] Recipe uses correct materials

### Bag Recipes
- [ ] Basic tier bags can be crafted
- [ ] Advanced tier bags can be crafted
- [ ] Superior tier bags can be crafted
- [ ] Masterful tier bags can be crafted
- [ ] Ultimate tier bags can be crafted
- [ ] Recipes appear in JEI/REI (if installed)

## Known Issues to Watch For

- [ ] Any texture rendering issues (missing textures = purple/black checkerboard)
- [ ] Any localization issues (items showing technical IDs)
- [ ] Any crashes when opening bags
- [ ] Any crashes when using Enhanced Workbench
- [ ] Any console errors/warnings

## Performance Testing

- [ ] No lag when opening bag GUIs
- [ ] No lag when using Enhanced Workbench
- [ ] No memory leaks after extended use

## Notes Section

**Issues Found:**
```
[Add any issues discovered during testing here]
```

**Additional Observations:**
```
[Add any other notes or observations here]
```

---

**Testing Date:** _______________
**Tester:** _______________
**Status:** [ ] Pass [ ] Fail [ ] Partial
