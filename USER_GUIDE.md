# CombinedPE User Guide

## Overview

CombinedPE is a comprehensive Minecraft mod that combines and enhances the ProjectE ecosystem with new features, including Builder's Bags, Enhanced Workbench, and Refined Storage integration.

**Version**: 1.7.7
**Minecraft**: 1.21.1
**Mod Loader**: NeoForge 21.1.82

---

## Features

### Builder's Bags (65 Unique Bags)

Store massive quantities of items in specialized bags with type filtering.

**13 Bag Types**:
- Materials - Stone, wood, basic resources
- Food - Edible items and ingredients
- Ore - Raw ores and ingots
- Tool - Pickaxes, shovels, axes, hoes
- Mob Drop - Items dropped by mobs
- Liquid - Buckets and fluid containers
- Redstone - Redstone components and circuits
- Potion - Potions and brewing ingredients
- Enchanting - Books, enchanting materials
- Trade - Emeralds, trade goods
- Combat - Weapons, armor, shields
- Adventure - Maps, compasses, exploration items
- Treasure - Rare and valuable items

**5 Tier Levels**:
- Basic - 9 slots, 64 stack size
- Advanced - 18 slots, 128 stack size
- Superior - 27 slots, 256 stack size
- Masterful - 36 slots, 512 stack size
- Ultimate - 54 slots, 1024 stack size + EMC auto-transmutation

**Stack Upgrades** (4 tiers):
- Stack Upgrade I-IV: Further increase stack sizes within bags

---

### Enhanced Workbench

A convenient 3×3 crafting table that keeps your recipes accessible.

**Features**:
- Standard 3×3 crafting grid
- Shift-click quick crafting support
- All vanilla recipes supported
- Placed as a block in the world

**How to Use**:
1. Craft Enhanced Workbench (8 planks + Basic Materials Bag)
2. Place in world
3. Right-click to open crafting GUI
4. Craft as normal

---

### Refined Storage Integration (NEW in v1.7.7)

Access your ProjectE EMC balance through Refined Storage External Storage!

**Features**:
- EMC Linker block for configuration
- Show specific items from EMC in RS Grid
- Auto-transmute items using your EMC balance
- Items extracted automatically deduct EMC
- Builder's Bag inventory access via RS

---

## EMC Linker - Complete Guide

### What is EMC Linker?

EMC Linker is a configuration block that tells Refined Storage External Storage which item to show from your ProjectE EMC balance.

**Why is this needed?**

Without EMC Linker, RS would try to show ALL items you've learned in ProjectE (potentially 1000+ items), causing severe performance issues. EMC Linker limits it to ONE specific item per External Storage block.

### Crafting Recipe

```
[Iron] [Diamond] [Iron]
[Diamond] [Ender Pearl] [Diamond]
[Iron] [Diamond] [Iron]
```

**Materials**:
- 4 Iron Ingots
- 4 Diamonds
- 1 Ender Pearl

### Setup Instructions

**Step 1: Place RS External Storage**
- Place a Refined Storage External Storage block
- Face it in any direction

**Step 2: Place EMC Linker**
- Place EMC Linker adjacent to the External Storage
- Can be on any side (top, bottom, sides)
- The EMC Linker is directional - it "points" toward the External Storage

**Step 3: Configure the Link**
- Hold the item you want to link (e.g., Diamond)
- Right-click the EMC Linker
- Message appears: "Linked to [item name]"
- The RS External Storage will now show that item

**Step 4: Connect to RS Network**
- Connect the External Storage to your RS network
- Open RS Grid
- The linked item will appear with quantity based on your EMC balance

### Usage Tips

**Clearing a Link**:
- Sneak + Right-click the EMC Linker
- Message: "Link cleared"
- External Storage will now show nothing

**Multiple Items**:
- Need multiple items? Use multiple EMC Linkers + External Storage blocks
- Each External Storage can show ONE item type
- Example setup:
  - EMC Linker #1 → Diamonds
  - EMC Linker #2 → Iron Ingots
  - EMC Linker #3 → Gold Ingots

**EMC Requirements**:
- You must have the item "learned" in ProjectE
- You must have enough EMC balance to create at least 1 item
- Quantity shown = EMC balance ÷ item EMC value

**Extracting Items**:
- Request items through RS Grid as normal
- Items are auto-transmuted from your EMC
- EMC is automatically deducted from your balance
- Works with autocrafting patterns!

### Visual Appearance

- Small glowing cube (10×10×10 pixels)
- Light level 7 (glows in the dark)
- Rotates to face the External Storage
- Purple/Blue energy appearance (placeholder texture in v1.7.7)

### Troubleshooting

**Item not showing in RS Grid?**
- Check EMC Linker has a link configured (right-click to verify)
- Verify you have the item learned in ProjectE
- Check you have enough EMC balance
- Ensure External Storage is connected to RS network

**"Not enough EMC" message?**
- Your EMC balance is too low for even 1 item
- Check item EMC value with ProjectE Transmutation Tablet
- Add more EMC to your player account

**Link keeps clearing?**
- Each EMC Linker stores links per-dimension
- Moving to different dimension requires new link
- Check you're in the correct dimension

---

## Builder's Bags - Detailed Guide

### Using Bags

**Opening a Bag**:
- Right-click while holding the bag
- Opens inventory GUI showing available slots

**Storing Items**:
- Drag items into bag slots
- Items must match the bag type filter
- Stack sizes can exceed normal limits (based on tier)

**Type Filtering**:
Each bag type only accepts specific items:
- Materials Bag: Won't accept food, tools, etc.
- Food Bag: Only accepts edible items
- Tool Bag: Only accepts tools (pickaxes, shovels, etc.)

**Quick Insert**:
- Shift-click items to quickly insert into bags
- Works from any inventory to bag
- Only inserts items matching bag type

**Food Bags - Special Feature**:
- Right-click while hungry to eat from bag
- Automatically finds food in bag
- No need to open GUI
- Consumes 1 food item from bag

### Upgrading Bags

**Tier Upgrades**:
- Craft higher tier bags using lower tier bags
- Cannot downgrade tiers
- Inventory transfers automatically

**Stack Upgrades**:
- Right-click bag with Stack Upgrade item
- Increases max stack size in that bag
- 4 upgrade tiers available (I-IV)
- Can apply multiple upgrades to same bag

### Ultimate Bags - EMC Provider

Ultimate tier bags have a special feature: **EMC Auto-Transmutation**

**How it works**:
1. Try to extract item from Ultimate bag
2. If item not in bag but you know it in ProjectE
3. Mod checks your EMC balance
4. If enough EMC, automatically transmutes item
5. Item appears in the bag
6. EMC is deducted from your account

**Requirements**:
- Ultimate tier bag only
- Player must have item "learned" in ProjectE
- Player must have enough EMC balance
- Player must be within 16 blocks of the bag

**Example**:
- You have Ultimate Materials Bag (empty)
- Try to take 64 Stone
- You have Stone learned + enough EMC
- Result: 64 Stone appears, EMC deducted

---

## Enhanced Workbench Guide

### Features

- 3×3 crafting grid (same as crafting table)
- Convenient block placement
- Shift-click support
- All vanilla recipes

### Usage

1. Place Enhanced Workbench block
2. Right-click to open GUI
3. Place items in 3×3 grid
4. Craft result appears in output slot
5. Shift-click output to quick-craft

### Tips

- Place near storage for easy crafting
- No different from regular crafting table functionally
- More convenient form factor

---

## FAQ

**Q: Do bags work in other players' inventories?**
A: Yes, each bag stores its own inventory regardless of who holds it.

**Q: Can I dye bags different colors?**
A: Not yet - planned for future update.

**Q: What happens if I break a full bag?**
A: The bag item keeps its inventory - items are not lost.

**Q: Can I use bags in automated systems?**
A: Yes! Bags work with hoppers, pipes, and other item transfer systems.

**Q: Does RS integration work with Builder's Bags?**
A: Yes! Use RS External Storage next to a bag to access its inventory.

**Q: How much EMC can Ultimate bags transmute?**
A: Up to your entire EMC balance - no hard limit per bag.

**Q: Can I link multiple External Storage to same EMC Linker?**
A: No - each External Storage needs its own EMC Linker.

**Q: Do EMC Linkers work across dimensions?**
A: Links are per-dimension. You'll need to re-link after dimension travel.

---

## Compatibility

**Required Mods**:
- ProjectE 1.21.1-4.0.0 or higher

**Optional Mods**:
- Refined Storage 2.0.0+ (for RS integration)
- JEI/EMI (for recipe viewing - planned Phase 6)

**Known Compatible Mods**:
- Most storage mods (via standard item handler capability)
- Most automation mods (hoppers, pipes, etc.)

**Known Issues**:
- None currently

---

## Performance

**Optimized For**:
- Large mod packs
- Multiplayer servers
- Low-end computers

**Performance Features**:
- Efficient item filtering
- Smart EMC caching
- Minimal network packets
- RS integration uses item-specific links (not full EMC listing)

---

## Support & Feedback

**Bug Reports**: GitHub Issues
**Feature Requests**: GitHub Discussions
**Questions**: GitHub Discussions

**Version**: 1.7.7
**Last Updated**: 2025-12-27

---

## Changelog

### v1.7.7 (2025-12-27) - Phase 4 Complete
- ✅ Refined Storage integration
- ✅ EMC Linker block system
- ✅ Fixed texture display bug (70 item models)
- ✅ Fixed world hanging performance issue
- ✅ Added helpful tooltips to EMC Linker
- ✅ Complete RS External Storage support

### v1.7.6 (2025-12-27)
- Fixed compilation errors (85 total)
- Complete localization for 65 bags
- Testing documentation

### v1.7.5
- Enhanced Workbench implementation
- Workbench GUI system

### v1.6.0
- EMC Provider system
- Ultimate bag auto-transmutation

### v1.5.0
- Recipe scanner
- EMC calculation engine

---

## Credits

**Mod Author**: Riley E. Antrobus
**Based On**: ProjectE ecosystem
**Special Thanks**: ProjectE team, Refined Storage team

**License**: All Rights Reserved
