# JEI Integration Plan - Phase 6

## Overview

Integrate Just Enough Items (JEI) to provide recipe viewing and EMC value display for CombinedPE items and recipes.

**Version Target**: JEI 19.19.6+ for NeoForge 1.21.1
**Priority**: P2 (Phase 6)
**Status**: Planning

---

## Goals

### Primary Goals
1. **EMC Value Display**: Show EMC values for all items in JEI tooltips
2. **Recipe Book Integration**: All CombinedPE recipes visible in JEI
3. **Bag Recipe Category**: Custom category showing bag crafting tree
4. **Transmutation Integration**: Show ProjectE transmutation as recipes

### Secondary Goals
1. **Recipe Transfer**: Quick-fill recipes from JEI to Enhanced Workbench
2. **Search Integration**: Make bags searchable by type/tier
3. **Info Pages**: Add information pages for mod features

---

## Technical Architecture

### Plugin Structure

```
src/main/java/com/riley/combinedpe/integration/jei/
├── CombinedPEJeiPlugin.java        (Main plugin class)
├── EMCValueHelper.java              (EMC value calculation)
├── category/
│   ├── BagCraftingCategory.java    (Bag recipe category)
│   └── TransmutationCategory.java  (ProjectE transmutation)
└── transfer/
    └── WorkbenchRecipeTransfer.java (Enhanced Workbench support)
```

### Key Classes

#### 1. CombinedPEJeiPlugin
Main entry point implementing `IModPlugin`

**Methods to implement**:
- `getPluginUid()` - Unique plugin identifier
- `registerCategories()` - Register custom recipe categories
- `registerRecipes()` - Register recipes to display
- `registerRecipeCatalysts()` - Link blocks to recipe categories
- `registerRecipeTransferHandlers()` - Transfer support

#### 2. EMCValueHelper
Utility for getting EMC values

**Methods**:
- `getEMCValue(ItemStack)` - Get EMC from ProjectE
- `formatEMCValue(long)` - Format EMC for display
- `addEMCTooltip(List<Component>, ItemStack)` - Add tooltip line

#### 3. BagCraftingCategory
Custom recipe category for bag crafting

**Purpose**: Show tier upgrade paths and stack upgrade recipes

#### 4. TransmutationCategory
Show ProjectE transmutation as recipes

**Purpose**: Display EMC costs and learned items

---

## Implementation Phases

### Phase 6.1: Basic Integration ✓
**Goal**: Get JEI dependency and basic plugin structure

**Tasks**:
1. Add JEI dependency to build.gradle.kts
2. Download JEI JAR to libs/
3. Create CombinedPEJeiPlugin skeleton
4. Register plugin with JEI
5. Verify mod loads with JEI

**Files**:
- `build.gradle.kts` (add dependency)
- `gradle.properties` (JEI version)
- `CombinedPEJeiPlugin.java` (new)

**Testing**: Launch game, verify no crashes, check JEI loads

---

### Phase 6.2: EMC Tooltips ✓
**Goal**: Display EMC values in item tooltips

**Tasks**:
1. Create EMCValueHelper utility
2. Integrate with ProjectE EMC API
3. Register tooltip provider with JEI
4. Format EMC values nicely (1.5K, 12.3M, etc.)
5. Add EMC icon/indicator

**Implementation**:
```java
@Override
public void registerRecipes(IRecipeRegistration registration) {
    // Add EMC value to all items
    registration.addIngredientInfo(
        getAllItems(),
        VanillaTypes.ITEM_STACK,
        Component.literal("EMC: " + getEMCValue())
    );
}
```

**Testing**: Hover over items in JEI, verify EMC shows

---

### Phase 6.3: Recipe Book Integration ✓
**Goal**: All CombinedPE recipes visible in JEI

**Tasks**:
1. Register all bag crafting recipes
2. Register Enhanced Workbench recipe
3. Register stack upgrade recipes
4. Register EMC Linker recipe
5. Test recipe search

**Implementation**:
```java
@Override
public void registerRecipes(IRecipeRegistration registration) {
    RecipeManager manager = level.getRecipeManager();

    // Get all CombinedPE recipes
    List<RecipeHolder<?>> recipes = manager.getAllRecipesFor(RecipeType.CRAFTING)
        .stream()
        .filter(r -> isCombinedPERecipe(r))
        .toList();

    registration.addRecipes(RecipeTypes.CRAFTING, recipes);
}
```

**Testing**: Search for "Builder's Bag" in JEI, verify all 65 bags show

---

### Phase 6.4: Custom Bag Category ✓
**Goal**: Visual bag crafting tree

**Tasks**:
1. Create BagCraftingCategory class
2. Implement IRecipeCategory interface
3. Design category UI (background, slots)
4. Show tier upgrade paths visually
5. Register category with JEI

**Visual Design**:
```
Basic Bag + Materials → Advanced Bag
Advanced Bag + Materials → Superior Bag
Superior Bag + Materials → Masterful Bag
Masterful Bag + Materials → Ultimate Bag
```

**Testing**: Open JEI, verify Bag Crafting category exists

---

### Phase 6.5: Enhanced Workbench Transfer ✓
**Goal**: Click recipes in JEI to fill Enhanced Workbench

**Tasks**:
1. Create WorkbenchRecipeTransfer class
2. Implement IRecipeTransferHandler
3. Link to Enhanced Workbench GUI
4. Test shift-click recipe transfer
5. Handle errors gracefully

**Implementation**:
```java
public class WorkbenchRecipeTransfer implements IRecipeTransferHandler<...> {
    @Override
    public RecipeTransferError transferRecipe(...) {
        // Transfer items from player inventory to workbench grid
        // Return error if missing items
    }
}
```

**Testing**: Open Enhanced Workbench, click JEI recipe, verify items fill grid

---

### Phase 6.6: Transmutation Category (Optional) 🔄
**Goal**: Show ProjectE transmutation as recipes

**Status**: Lower priority - may defer to Phase 7

**Tasks**:
1. Query ProjectE for learned items
2. Create transmutation recipe wrapper
3. Show EMC costs
4. Link to Transmutation Table
5. Test with player knowledge

**Note**: This may overlap with ProjectE's own JEI plugin

---

## Dependencies

### Runtime Dependencies
- JEI 19.19.6+ for NeoForge 1.21.1
- ProjectE 1.21.1-4.0.0 (for EMC values)

### Build Dependencies
```gradle
// JEI integration
compileOnly("mezz.jei:jei-1.21.1-neoforge-api:19.19.6.235")
runtimeOnly("mezz.jei:jei-1.21.1-neoforge:19.19.6.235")
```

### Optional Dependencies
- EMI (alternative to JEI) - Phase 7+

---

## Testing Plan

### Unit Tests
- EMC value formatting (1000 → "1.0K")
- Recipe filtering (only CombinedPE recipes)
- Tooltip generation

### Integration Tests
1. **Mod loads with JEI**
   - No crashes during startup
   - Plugin registers successfully

2. **EMC tooltips work**
   - Hover over diamond → Shows EMC
   - Hover over bag → Shows EMC
   - Hover over no-EMC item → No tooltip

3. **Recipe search works**
   - Search "bag" → All 65 bags appear
   - Search "workbench" → Enhanced Workbench appears
   - Search "upgrade" → Stack upgrades appear

4. **Custom category displays**
   - Bag Crafting category tab exists
   - Shows tier upgrade tree
   - Navigation works

5. **Recipe transfer works**
   - Click recipe in JEI
   - Items fill Enhanced Workbench grid
   - Missing items show error

### Manual Testing Checklist
- [ ] JEI mod loads without errors
- [ ] All 65 bags appear in JEI
- [ ] EMC values show in tooltips
- [ ] Bag recipes searchable
- [ ] Enhanced Workbench recipes work
- [ ] Recipe transfer functions
- [ ] Custom categories display correctly
- [ ] No performance issues

---

## Known Issues & Limitations

### Limitations
1. **Dynamic EMC values**: EMC values calculated at runtime may not match JEI cache
2. **Player-specific knowledge**: Can't show player's learned items in JEI (global view)
3. **Ultimate bag auto-transmute**: Can't visualize in JEI (runtime behavior)

### Potential Issues
1. **Recipe conflicts**: Multiple mods may register same recipes
2. **Performance**: Large recipe lists may slow JEI
3. **Version compatibility**: JEI API may change between versions

---

## Future Enhancements (Phase 7+)

### EMI Support
- Parallel implementation for EMI (alternative to JEI)
- Share common code between JEI and EMI

### Advanced Features
- **Recipe history**: Track recently used recipes
- **Favorites**: Bookmark favorite recipes
- **Recipe notes**: Add custom notes to recipes
- **Export/Import**: Share recipe collections

### Integration Features
- **REI support**: Roughly Enough Items compatibility
- **Crafting guide integration**: Link to external wikis
- **Achievement integration**: Show required achievements

---

## File Structure

```
src/main/java/com/riley/combinedpe/integration/jei/
├── CombinedPEJeiPlugin.java              (Main plugin - implements IModPlugin)
├── EMCValueHelper.java                    (EMC utility methods)
├── category/
│   ├── BagCraftingCategory.java          (Custom category for bags)
│   ├── TransmutationCategory.java        (ProjectE transmutation recipes)
│   └── recipe/
│       ├── BagUpgradeRecipe.java         (Recipe wrapper for bag upgrades)
│       └── TransmutationRecipe.java      (Recipe wrapper for transmutation)
└── transfer/
    ├── WorkbenchRecipeTransfer.java      (Enhanced Workbench recipe transfer)
    └── WorkbenchRecipeTransferInfo.java  (Transfer configuration)
```

```
src/main/resources/assets/combinedpe/jei/
├── textures/
│   ├── gui/
│   │   ├── bag_crafting_background.png   (Category background)
│   │   └── emc_icon.png                  (EMC value icon)
│   └── icons/
│       └── recipe_transfer.png           (Transfer button)
└── lang/
    └── en_us.json                        (JEI-specific translations)
```

---

## Success Criteria

**Phase 6 is complete when**:
1. ✅ JEI dependency added and builds successfully
2. ✅ Plugin registers without errors
3. ✅ EMC values display in item tooltips
4. ✅ All 65 bags appear in JEI recipe search
5. ✅ Recipe transfer works with Enhanced Workbench
6. ✅ No crashes or performance issues
7. ✅ User documentation updated with JEI features

**Stretch goals** (optional):
- Custom Bag Crafting category implemented
- Transmutation recipes displayed
- Advanced search filters
- Recipe bookmarks

---

## Timeline Estimate

**Phase 6.1** (Basic Integration): 1-2 hours
**Phase 6.2** (EMC Tooltips): 2-3 hours
**Phase 6.3** (Recipe Integration): 1-2 hours
**Phase 6.4** (Custom Category): 3-4 hours
**Phase 6.5** (Recipe Transfer): 2-3 hours
**Phase 6.6** (Transmutation): 4-5 hours (optional)

**Total**: 9-14 hours (core features)
**Total with optional**: 13-19 hours

---

## References

- [JEI Wiki](https://github.com/mezz/JustEnoughItems/wiki)
- [NeoForge JEI Tutorial](https://www.youtube.com/watch?v=15N9APaxJhE)
- [Kaupenjoe NeoForge Tutorials](https://github.com/Tutorials-By-Kaupenjoe/NeoForge-Tutorial-1.21.X)
- [JEI API Documentation](https://docs.neoforged.net/docs/1.21.1/)

---

**Last Updated**: 2025-12-27
**Status**: Ready to begin Phase 6.1
**Next Step**: Add JEI dependency to build.gradle.kts
