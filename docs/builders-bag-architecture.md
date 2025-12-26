# Builder's Bag Architecture Analysis (1.12.2 → 1.21.1 Port)

## Overview
Builder's Bag is a building-focused mod that provides intelligent bags that store blocks and resources, automatically providing them when building. **Not a backpack replacement** - specifically designed for building workflows.

## Core Concepts

### Five Bag Tiers (All Configurable)
1. **Basic Bag**
2. **Advanced Bag**
3. **Superior Bag**
4. **Masterful Bag**
5. **Ultimate Bag**

Each tier has configurable:
- Storage capacity
- Feature unlocks (modules)
- Performance characteristics

## Module System

### 1. **Supplier Module**
**Purpose:** Provides blocks to building tools automatically

**Integrations:**
- Better Builder's Wands - wands pull blocks from bag
- Botania Wand of Shifting Crust - pulls blocks from bag
- Default module, available on all tiers

**Implementation Notes:**
- Acts as an IItemHandler capability provider
- Monitors player actions (block placement, tool usage)
- Provides blocks from inventory when needed
- Must handle block state preservation (metadata, NBT)

### 2. **Container Modules**
**Purpose:** Pull blocks from external storage systems

**Supported Containers (1.12.2):**
- Vanilla chests
- Ender Chest (vanilla)
- Ender Storage ender chests
- Botania items (Rod of the Lands, Rod of the Depths, Black Hole Talisman, Ender Hand)
- Backpacks from other mods

**Implementation Notes:**
- Needs to query IItemHandler capabilities on containers
- Must handle different inventory types
- May need distance/chunk loading considerations
- **For our mod:** Add Refined Storage integration here

### 3. **Crafting Module**
**Purpose:** Auto-craft items when needed

**Features:**
- Crafts items when supplier module requests them
- Prevents bag overflow (dumps excess to player inventory)
- Integrates with supplier module
- Default unlock: Masterful Bag

**Implementation Notes:**
- Recipe matching system
- Crafting grid simulation
- Resource tracking and reservation
- **For our mod:** Should integrate with EMC system (use EMC to fill missing ingredients)

### 4. **Chisel Module**
**Purpose:** Chisel integration for block variants

**Features:**
- Works with Chisel mod
- Performance optimizations needed (known issue #25)
- Caching system required

**Implementation Notes:**
- **Not priority for initial port** (Chisel availability on 1.21.1 unclear)
- May be replaced with vanilla stone cutter integration
- Could expand to other block transformation mods

### 5. **Little Tiles Module**
**Purpose:** Integration with Little Tiles mod

**Features:**
- Provides blocks for Little Tiles construction
- Default unlock: Advanced Bag

**Implementation Notes:**
- **Not priority for initial port** (Little Tiles availability on 1.21.1 unclear)
- May be optional or removed entirely

## Technical Architecture (Inferred)

### Core Classes (1.12.2 - to be confirmed)
```
ItemBuildersBag - Main bag item
  - NBT storage for inventory
  - Tier configuration
  - Module enablement

ContainerBuildersBag - Container for GUI
  - Inventory management
  - Module slot handling
  - Config UI

GuiBuildersBag - Client GUI
  - Rendering
  - Module configuration interface
  - Visual feedback

TileEntityBuildersBag (unlikely - bag is item-based)

Capability System:
  - IItemHandler for inventory
  - Custom capabilities for modules
```

### Key Systems

#### 1. **Inventory System**
- NBT-based storage (bag is an item, not a block)
- Tiered capacity (configurable per tier)
- Stack limit handling
- Filter system (block-only or configurable)

#### 2. **Module System**
- Modular feature unlocks
- Per-bag configuration persistence
- Module communication (supplier ↔ crafting)

#### 3. **Event Handling**
- Block placement events (provide blocks)
- Tool usage events (wand integration)
- Crafting events (auto-craft trigger)

#### 4. **Capability Providers**
- IItemHandler for external access
- Custom capabilities for module APIs
- Integration points for other mods

## Known Issues (from GitHub)

1. **Issue #25:** Chisel module performance optimization needed
2. **Issue #14:** Bag caching system (client-side)
3. **Issue #20:** CraftTweaker2 crash compatibility
4. **Issue #8:** Items spilling back to inventory (sync issue)
5. **Issue #5:** Backpack integration request

## NeoForge 1.21.1 Porting Considerations

### API Changes (1.12.2 → 1.21.1)

#### 1. **ItemStack Data Storage - CRITICAL CHANGE**
- **1.12.2:** NBT-based (`NBTTagCompound` attached to stack)
- **1.21.1:** **Data Components** system (key-value map)
  - `DataComponentType<T>` - typed component registration
  - Access via `ItemStack.get(component)`, `set()`, `has()`, `update()`, `remove()`
  - Components registered via `DeferredRegister.DataComponents`
  - Default values set in `Item.Properties.component()`
- **Action:**
  - Create custom `DataComponentType` for bag inventory
  - Register in `DeferredRegister`
  - Use codecs for serialization (NetworkCodec, Codec)
- **Example:**
  ```java
  public static final DataComponentType<BagInventory> BAG_INVENTORY =
    DataComponentType.<BagInventory>builder()
      .persistent(BagInventory.CODEC)  // For disk serialization
      .networkSynchronized(BagInventory.STREAM_CODEC)  // For network sync
      .build();
  ```

#### 2. **Capability System**
- **1.12.2:** `ICapabilityProvider`, `LazyOptional`
- **1.21.1:** Capability attachments + capability cache
  - `Capabilities.ItemHandler.ITEM` - for item-based handlers
  - `Capabilities.ItemHandler.BLOCK` - for block-based handlers
  - `Capabilities.ItemHandler.ENTITY` - for entity-based handlers
  - Capabilities use context objects (e.g., `ItemContext`, `BlockContext`)
- **Action:**
  - Implement `IItemHandler` for bag inventory
  - Register capability provider for Builder's Bag item
  - Use `CapabilityCache` for performance
- **Example:**
  ```java
  @Override
  public @Nullable IItemHandler getCapability(ItemStack stack, ItemContext context) {
    if (stack.has(BAG_INVENTORY)) {
      return new BagItemHandler(stack);
    }
    return null;
  }
  ```

#### 3. **Container/GUI System**
- **1.12.2:** `Container` + `GuiScreen`
- **1.21.1:** `AbstractContainerMenu` + `Screen`
  - Menus registered via `DeferredRegister.MenuTypes`
  - Network sync handled by menu
  - Rendering in client-side Screen class
- **Action:**
  - Create `BuildersBagMenu extends AbstractContainerMenu`
  - Create `BuildersBagScreen extends AbstractContainerScreen`
  - Register `MenuType` with menu factory

#### 4. **Item Handlers**
- **1.12.2:** Forge `IItemHandler`
- **1.21.1:** NeoForge `IItemHandler` (interface similar, registration different)
  - Still has `insertItem()`, `extractItem()`, `getStackInSlot()`, etc.
  - Registration through capability system
- **Action:**
  - Implement `IItemHandler` for bag (similar to 1.12.2)
  - Register via capability provider

#### 5. **Event System**
- **1.12.2:** `@SubscribeEvent` with Forge events
- **1.21.1:** `@SubscribeEvent` with NeoForge events
  - Event classes renamed (e.g., `PlayerInteractEvent.RightClickBlock`)
  - Event bus mostly compatible
- **Action:**
  - Update event class imports
  - Verify event behavior matches expectations

#### 6. **Attachment System (NEW in 1.21.1)**
- **Purpose:** Store arbitrary data on blocks, entities, chunks, stacks
- **Alternative to:** Custom capabilities in 1.12.2
- **Usage:**
  ```java
  public static final AttachmentType<BagModules> BAG_MODULES =
    AttachmentType.builder(() -> new BagModules())
      .serialize(BagModules.CODEC)
      .copyOnDeath()
      .build();

  // Usage:
  stack.getData(BAG_MODULES);  // Get
  stack.setData(BAG_MODULES, modules);  // Set
  stack.hasData(BAG_MODULES);  // Check
  ```
- **Decision:** Use data components for bag inventory, attachments for module state

### Integration Targets (Our Mod-Specific)

#### ProjectE Integration
- **Supplier Module:** Provide blocks from transmutation tablet
- **Crafting Module:** Use EMC to create missing ingredients
- **Container Module:** Pull from transmutation tables, condensers
- **EMC Display:** Show EMC cost in bag GUI

#### Refined Storage Integration (Phase 4)
- **Container Module:** Pull from RS network
- **Crafting Module:** Request crafting from RS system
- **Display Module:** Show items available in RS network
- **Storage Module:** Push excess to RS network

## Porting Strategy

### Phase 3.1: Core Bag Item (Priority 1)
- [ ] Create `ItemBuildersBag` with tiers
- [ ] NBT-based inventory system
- [ ] Basic open/close functionality
- [ ] Tier configuration system

### Phase 3.2: GUI and Container (Priority 1)
- [ ] Create `BuildersBagMenu` (container)
- [ ] Create `BuildersBagScreen` (GUI)
- [ ] Inventory rendering
- [ ] Module configuration UI

### Phase 3.3: Supplier Module (Priority 1)
- [ ] Block provider capability
- [ ] Block placement event handling
- [ ] Tool integration hooks
- [ ] ProjectE transmutation integration

### Phase 3.4: Container Module (Priority 2)
- [ ] External inventory scanning
- [ ] IItemHandler integration
- [ ] Refined Storage network access
- [ ] Distance/chunk considerations

### Phase 3.5: Crafting Module (Priority 2)
- [ ] Recipe matching system
- [ ] Auto-crafting logic
- [ ] EMC-based ingredient filling
- [ ] Overflow handling

### Phase 3.6: Module System Framework (Priority 2)
- [ ] Module base class/interface
- [ ] Module registration
- [ ] Module configuration persistence
- [ ] Module communication API

## Configuration Structure (Planned)

```toml
[builders_bag]
  [builders_bag.tiers]
    # Tier 1: Basic Bag
    [[builders_bag.tiers.basic]]
      capacity = 27  # slots
      enabled_modules = ["supplier"]

    # Tier 2: Advanced Bag
    [[builders_bag.tiers.advanced]]
      capacity = 54
      enabled_modules = ["supplier", "container", "little_tiles"]

    # Tier 3: Superior Bag
    [[builders_bag.tiers.superior]]
      capacity = 81
      enabled_modules = ["supplier", "container"]

    # Tier 4: Masterful Bag
    [[builders_bag.tiers.masterful]]
      capacity = 108
      enabled_modules = ["supplier", "container", "crafting"]

    # Tier 5: Ultimate Bag
    [[builders_bag.tiers.ultimate]]
      capacity = 135
      enabled_modules = ["supplier", "container", "crafting", "emc_provider"]

  [builders_bag.modules]
    # Module-specific configuration
    [builders_bag.modules.supplier]
      enabled = true
      auto_refill = true

    [builders_bag.modules.container]
      enabled = true
      scan_radius = 16  # blocks
      include_ender_chests = true

    [builders_bag.modules.crafting]
      enabled = true
      use_emc_for_missing = true  # Our addition
      overflow_to_inventory = true

    [builders_bag.modules.emc_provider]
      enabled = true
      pull_from_transmutation_tablet = true
      emc_cost_display = true
```

## Research Tasks Completed
- ✅ Feature analysis from CurseForge documentation
- ✅ Module system understanding
- ✅ Integration points identified
- ✅ Known issues reviewed

## Research Tasks Remaining
- [ ] Confirm class structure (need actual source code)
- [ ] NeoForge 1.21.1 capability system research
- [ ] Modern menu/screen system examples
- [ ] Item handler attachment points

## Notes
- Original mod is **LGPL-3.0** licensed (compatible with open source)
- Module system is highly extensible
- Performance considerations noted (caching, optimization)
- Strong focus on mod integration (many compatibility hooks)
