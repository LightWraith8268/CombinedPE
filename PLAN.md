# CombinedPE - Project Plan

## Overview

A unified Minecraft mod combining ProjectE ecosystem mods with a dynamic EMC registration system that can automatically assign EMC values to items from ANY mod in a modpack.

**Target Platform:** NeoForge 1.21.1+ (forward compatible)
**Author:** Riley E. Antrobus (personal use only)

---

## Source Mods Being Combined

| Mod | Source | Current Status | Integration Approach |
|-----|--------|----------------|---------------------|
| ProjectE | [sinkillerj/ProjectE](https://github.com/sinkillerj/ProjectE) | 1.21.1 NeoForge ready | **Dependency** - already maintained |
| FTB ProjectEX | [FTBTeam/FTB-ProjectEX](https://github.com/FTBTeam/FTB-ProjectEX) | 1.21.1 NeoForge ready | **Dependency** - already maintained |
| ProjectE Integration | [TagnumElite/ProjectE-Integration](https://github.com/TagnumElite/ProjectE-Integration) | Outdated | **Merge & Enhance** - supersede with dynamic system |
| Builder's Bag | [Tschipp/BuildersBag](https://github.com/Tschipp/BuildersBag) | 1.12.2 only | **Full Port** - major effort |
| Builder's Bag ProjectE Addon | Part of BuildersBag | 1.12.2 only | **Full Port** - include in Builder's Bag port |
| ProjectE Retexture | CurseForge texture pack | Assets only | **Include Assets** - bundle textures |
| ProjectE Extended AdvancedAE | Unknown | Unknown | **Research Required** - may skip or reimplement |

---

## Architecture Decision: Dependencies vs Merged

**Decision: Hybrid Approach**

### As Dependencies (use their JARs)
- **ProjectE** - Core EMC system, actively maintained by sinkillerj/pupnewfster
- **FTB ProjectEX** - Upgraded items, actively maintained by FTB team

**Rationale:** These mods receive regular updates. Using them as dependencies means:
- Automatic bug fixes from upstream
- No need to maintain core EMC logic
- Less code to manage

### As Merged Code (port into CombinedPE)
- **Builder's Bag** - Dead since 2020, must port ourselves
- **ProjectE Integration** - Superseded by our dynamic EMC system
- **Builder's Bag ProjectE Addon** - Part of Builder's Bag port

**Rationale:** These mods are abandoned or we're replacing their functionality.

---

## Core Features

### 1. Dynamic EMC Registration System (NEW - Key Feature)

The killer feature that makes this mod valuable: **automatic EMC assignment for any item from any mod**.

#### How It Works

```
1. On world load, scan all registered items from all loaded mods
2. For items without EMC values:
   a. Check crafting recipes - sum ingredient EMC / output count
   b. Check smelting recipes - input EMC + fuel factor
   c. Check smithing recipes - base + addition EMC
   d. Check tag-based inference (e.g., "forge:ingots/copper" -> derive from copper ore)
   e. Check configurable overrides (user-defined values)
3. Register calculated EMC values with ProjectE's API
4. Generate report of assigned values for user review
```

#### Configuration System

```toml
# config/combinedpe-emc.toml

[dynamic_registration]
enabled = true
scan_on_world_load = true
generate_report = true

[inference_rules]
# Multipliers for recipe types
crafting_multiplier = 1.0
smelting_multiplier = 1.1  # Slight bonus for processed items
smithing_multiplier = 1.0

[tag_defaults]
# Default EMC for items matching tags (if no recipe found)
"forge:ingots" = 256
"forge:gems" = 512
"forge:dusts" = 128
"c:raw_materials" = 128

[overrides]
# Manual EMC assignments (highest priority)
"refinedstorage:controller" = 50000
"refinedstorage:disk_drive" = 25000

[blacklist]
# Items that should NEVER get EMC
items = [
    "minecraft:command_block",
    "minecraft:barrier",
    "minecraft:bedrock"
]
mod_ids = [
    "cheatingmod"  # Blacklist entire mods
]
```

### 2. Builder's Bag (Ported from 1.12.2)

#### Original Features to Port
- Bag item that stores building blocks
- Auto-refill from bag when placing blocks
- Multiple bag tiers with increasing capacity
- Filter system for block types
- Integration with building tools

#### New Features to Add
- **EMC Provider**: Bag can pull blocks from EMC (transmute on demand)
- **Refined Storage Integration**: Bag can pull from RS network
- **Sorting**: Auto-sort bag contents
- **Favorites**: Pin frequently used blocks

### 3. Refined Storage EMC Integration (NEW)

Since you use Refined Storage, this integration is essential:

- RS Grid can show EMC values
- RS can auto-craft using EMC as a "resource"
- External Storage can expose EMC as item source
- Pattern encoding for EMC transmutation

---

## Project Structure

```
CombinedPE/
├── build.gradle.kts              # NeoForge build configuration
├── settings.gradle.kts
├── gradle.properties             # Version management
├── PLAN.md                       # This file
├── CHANGELOG.md
│
├── src/main/java/com/riley/combinedpe/
│   │
│   ├── CombinedPE.java           # Main mod class
│   ├── Config.java               # Configuration handling
│   │
│   ├── core/                     # Shared utilities
│   │   ├── Registration.java     # Deferred registers
│   │   ├── NetworkHandler.java   # Packet handling
│   │   └── CreativeTab.java      # Creative mode tab
│   │
│   ├── emc/                      # Dynamic EMC system
│   │   ├── DynamicEMCMapper.java       # Main scanner/calculator
│   │   ├── RecipeEMCCalculator.java    # Recipe-based EMC derivation
│   │   ├── TagEMCInferrer.java         # Tag-based defaults
│   │   ├── EMCConfigOverrides.java     # User overrides
│   │   ├── EMCBlacklist.java           # Blocked items
│   │   ├── EMCReportGenerator.java     # Debug output
│   │   └── api/
│   │       └── IEMCProvider.java       # API for addons
│   │
│   ├── buildersbag/              # Builder's Bag port
│   │   ├── item/
│   │   │   ├── BuildersBagItem.java
│   │   │   └── BagTier.java
│   │   ├── container/
│   │   │   ├── BagContainer.java
│   │   │   └── BagSlot.java
│   │   ├── screen/
│   │   │   └── BagScreen.java
│   │   ├── capability/
│   │   │   └── BagItemHandler.java
│   │   ├── provider/                    # Block providers
│   │   │   ├── IBlockProvider.java
│   │   │   ├── InventoryProvider.java
│   │   │   ├── EMCProvider.java         # NEW: Pull from EMC
│   │   │   └── RSProvider.java          # NEW: Pull from RS
│   │   └── network/
│   │       └── BagSyncPacket.java
│   │
│   ├── integration/              # Mod integrations
│   │   ├── projecte/
│   │   │   └── ProjectECompat.java
│   │   ├── refinedstorage/
│   │   │   ├── RSCompat.java
│   │   │   ├── EMCExternalStorage.java
│   │   │   └── EMCGridWidget.java
│   │   └── jei/
│   │       └── JEIPlugin.java
│   │
│   └── data/                     # Data generators
│       ├── ModRecipeProvider.java
│       ├── ModItemModelProvider.java
│       ├── ModBlockStateProvider.java
│       └── ModLangProvider.java
│
├── src/main/resources/
│   ├── META-INF/
│   │   ├── neoforge.mods.toml    # Mod metadata
│   │   └── accesstransformer.cfg # If needed
│   │
│   ├── assets/combinedpe/
│   │   ├── textures/
│   │   │   ├── item/             # Bag textures
│   │   │   └── gui/              # GUI textures
│   │   ├── models/
│   │   │   └── item/
│   │   ├── lang/
│   │   │   └── en_us.json
│   │   └── blockstates/
│   │
│   └── data/combinedpe/
│       ├── recipes/
│       ├── tags/
│       └── emc/                  # Default EMC mappings
│           └── defaults.json
│
└── src/test/java/                # Unit tests for EMC calculation
```

---

## Implementation Phases

### Phase 1: Project Setup (Week 1)
- [ ] Initialize NeoForge 1.21.1 mod project
- [ ] Set up Gradle build with dependencies (ProjectE, ProjectEX)
- [ ] Configure multi-loader build (for future Fabric support if desired)
- [ ] Create basic mod structure and registration system
- [ ] Verify ProjectE API access works

### Phase 2: Dynamic EMC System (Week 2-3)
- [ ] Implement recipe scanner (CraftingRecipe, SmeltingRecipe, etc.)
- [ ] Implement tag-based EMC inference
- [ ] Create configuration system (TOML parsing)
- [ ] Build EMC report generator
- [ ] Add blacklist functionality
- [ ] Write unit tests for EMC calculations
- [ ] Test with Refined Storage items

### Phase 3: Builder's Bag Port (Week 3-5)
- [ ] Study original 1.12.2 source code
- [ ] Port bag item and tiers
- [ ] Port container and GUI
- [ ] Implement NeoForge capabilities (IItemHandler)
- [ ] Port block provider system
- [ ] Add EMC provider (transmute blocks on demand)
- [ ] Add textures and models

### Phase 4: Refined Storage Integration (Week 5-6)
- [ ] RS External Storage for EMC access
- [ ] RS Grid EMC value display
- [ ] RS crafting with EMC
- [ ] Builder's Bag RS provider

### Phase 5: Polish & Testing (Week 6-7)
- [ ] Include ProjectE Retexture assets (optional toggle)
- [ ] JEI/EMI integration
- [ ] In-game config GUI
- [ ] Performance optimization
- [ ] Comprehensive testing in modpack environment

### Phase 6: Multi-Version Support (Ongoing)
- [ ] Test on 1.21.2, 1.21.3 as they release
- [ ] Address any breaking changes
- [ ] Maintain backwards compatibility

---

## Technical Details

### Dependencies

```kotlin
// build.gradle.kts
dependencies {
    // NeoForge
    implementation("net.neoforged:neoforge:${neoforge_version}")

    // ProjectE (required)
    compileOnly("moze_intel.projecte:ProjectE:${projecte_version}")
    runtimeOnly("moze_intel.projecte:ProjectE:${projecte_version}")

    // FTB ProjectEX (required)
    compileOnly("dev.ftb.mods:ftb-projectex:${projectex_version}")
    runtimeOnly("dev.ftb.mods:ftb-projectex:${projectex_version}")

    // Refined Storage (optional integration)
    compileOnly("com.refinedmods.refinedstorage:refinedstorage-neoforge:${rs_version}")

    // JEI (optional integration)
    compileOnly("mezz.jei:jei-${mc_version}-neoforge:${jei_version}")
}
```

### ProjectE API Usage

```java
// Registering custom EMC values
public class DynamicEMCMapper {

    public void registerCalculatedEMC(Item item, long emcValue) {
        // Use ProjectE's API to register EMC
        EMCHelper.registerCustomEMC(item, emcValue);
    }

    public long calculateFromRecipe(CraftingRecipe recipe) {
        long totalInputEMC = 0;
        for (Ingredient ingredient : recipe.getIngredients()) {
            ItemStack[] stacks = ingredient.getItems();
            if (stacks.length > 0) {
                long ingredientEMC = EMCHelper.getEmcValue(stacks[0]);
                totalInputEMC += ingredientEMC;
            }
        }
        int outputCount = recipe.getResultItem().getCount();
        return totalInputEMC / outputCount;
    }
}
```

### Refined Storage Integration

```java
// External Storage that exposes EMC as items
public class EMCExternalStorage implements IExternalStorage {

    @Override
    public Collection<ItemStack> getStacks() {
        // Return all items player has learned in transmutation table
        // Limited by current EMC balance
    }

    @Override
    public long insert(ItemStack stack, long amount, Action action) {
        // Convert items to EMC
        long emcValue = EMCHelper.getEmcValue(stack);
        if (emcValue > 0) {
            addEMC(emcValue * amount);
            return amount;
        }
        return 0;
    }

    @Override
    public long extract(ItemStack stack, long amount, Action action) {
        // Transmute EMC into items
        long emcValue = EMCHelper.getEmcValue(stack);
        long cost = emcValue * amount;
        if (hasEMC(cost)) {
            removeEMC(cost);
            return amount;
        }
        return 0;
    }
}
```

---

## Versioning Strategy

```
CombinedPE-1.21.1-1.0.0.jar
         ^      ^
         |      +-- Mod version (semver)
         +-- Minecraft version target

1.0.0 - Initial release
1.1.0 - New features
1.0.1 - Bug fixes
2.0.0 - Breaking changes / major overhaul
```

**Forward Compatibility:**
- Target 1.21.1 as baseline
- Use `versionRange="[1.21.1,1.22)"` in mods.toml
- Avoid deprecated APIs where possible
- Test on newer MC versions before they go stable

---

## Testing Checklist

### EMC System
- [ ] Items with crafting recipes get correct EMC
- [ ] Items with smelting recipes get correct EMC
- [ ] Tag-based defaults apply correctly
- [ ] Blacklisted items never get EMC
- [ ] Config overrides take priority
- [ ] Report generates correctly
- [ ] No performance impact on world load (async scanning)

### Builder's Bag
- [ ] Bag stores and retrieves blocks
- [ ] Auto-refill works when placing blocks
- [ ] All tiers function correctly
- [ ] EMC provider transmutes blocks correctly
- [ ] RS provider pulls from network (if RS present)
- [ ] GUI renders and functions properly

### Integration
- [ ] ProjectE transmutation table recognizes our EMC values
- [ ] ProjectEX items work alongside our features
- [ ] RS grid shows EMC values (if RS integration enabled)
- [ ] JEI shows EMC info in tooltips

---

## Future Expansion Ideas

### Potential Additional Integrations
- Applied Energistics 2 (similar to RS integration)
- Mekanism (QIO system)
- Create (processing recipes for EMC calculation)
- Thermal Series
- Industrial Foregoing
- Botania (mana <-> EMC conversion?)

### Potential New Features
- EMC wireless transmitter (access EMC anywhere)
- EMC chunk loader (uses EMC as fuel)
- Auto-learning system (learn items by crafting/obtaining)
- EMC display overlay (show EMC values in world)

---

## Notes & Gotchas

### NeoForge 1.21.1 Specifics
- Use `DeferredRegister` for all registrations
- Capabilities system changed significantly from Forge
- Data components replaced NBT for item data
- Resource locations require explicit namespace

### Potential Issues
- **Recipe recursion**: Item A needs B, B needs A - need cycle detection
- **Large modpacks**: Scanning 10,000+ items needs optimization
- **Mod load order**: Must scan AFTER all mods register items
- **Multiplayer sync**: EMC values must sync to clients

---

## Resources

### Documentation
- [NeoForge Docs](https://docs.neoforged.net/)
- [ProjectE Wiki](https://github.com/sinkillerj/ProjectE/wiki)
- [Kaupenjoe NeoForge Tutorials](https://github.com/Tutorials-By-Kaupenjoe/NeoForge-Tutorial-1.21.X)

### Source Code References
- [ProjectE Source](https://github.com/sinkillerj/ProjectE)
- [FTB ProjectEX Source](https://github.com/FTBTeam/FTB-ProjectEX)
- [Builder's Bag Source (1.12.2)](https://github.com/Tschipp/BuildersBag)
- [ProjectE Integration Source](https://github.com/TagnumElite/ProjectE-Integration)

### Discord/Community
- [NeoForged Discord](https://discord.neoforged.net/)
- [ProjectE Discord](https://discord.gg/fkpxV5Z)

---

*Plan created: December 2025*
*Last updated: December 2025*
