# Bag GUI Design Specification
## CombinedPE - Modern Scrollable Interface

### Updated Slot Counts (Doubled)

| Tier | Slots | Rows | Stack Multiplier | Max per Slot |
|------|-------|------|------------------|--------------|
| Basic | 54 | 6 | 1x | 64 items |
| Advanced | 108 | 12 | 4x | 256 items |
| Superior | 162 | 18 | 16x | 1,024 items |
| Masterful | 216 | 24 | 64x | 4,096 items |
| Ultimate | 270 | 30 | 256x | 16,384 items |

### Storage Capacity Examples

**Basic Bag (54 slots):**
- No upgrade: 54 × 64 = 3,456 items max
- Stack Upgrade IV: 54 × 1,024 = 55,296 items max
- Infinite: 54 × ∞ = Unlimited (EMC)

**Ultimate Bag (270 slots):**
- No upgrade: 270 × 16,384 = 4,423,680 items max
- Stack Upgrade IV: 270 × 262,144 = 70,778,880 items max
- Infinite: 270 × ∞ = Unlimited (EMC)

---

## GUI Design: Wide Scrollable Interface

### Problem:
- Ultimate bag has 270 slots (30 rows)
- Can't fit on screen at once
- Need modern, user-friendly interface

### Solution: Scrollable Grid with Wide Layout

#### Layout Option A: Wide Grid (Recommended)

```
┌────────────────────────────────────────────────────────────────────┐
│  Ultimate Builder's Bag                                  [?] [X]   │
├────────────────────────────────────────────────────────────────────┤
│                                                                    │
│  Bag Inventory (270 slots) - Scroll: Mouse wheel or drag bar  ▲   │
│  ┌────────────────────────────────────────────────────────┐    │   │
│  │ [Dirt 2048] [Stone 1024] [Cobble 512] [Oak 256] [...] │    │   │
│  │ [Glass 128] [Sand 64]    [Gravel 32]  [Clay 16]  [...] │    │   │
│  │ [Iron 8]    [Gold 4]     [Diamond 2]  [Emerald 1][...] │    ║   │
│  │ [...]       [...]        [...]        [...]       [...] │    ║   │
│  │ [...]       [...]        [...]        [...]       [...] │    ║   │
│  │ [...]       [...]        [...]        [...]       [...] │    ║   │
│  └────────────────────────────────────────────────────────┘    │   │
│  ═══════════════════════════════════════════════════════════    ▼   │
│  Showing rows 1-6 of 30                                            │
│                                                                    │
├────────────────────────────────────────────────────────────────────┤
│  Player Inventory (36 slots)                                      │
│  [Hotbar: 9 slots ...................................]            │
│  [Main Inventory: 27 slots ......................]                │
├────────────────────────────────────────────────────────────────────┤
│  Modules:  ☑ Dank Mode  ☑ Container  ☑ Crafting  ☑ Infinite      │
│  Upgrades: [Stack IV ████] [Infinite ∞]                           │
│                                                                    │
│  Personal EMC: 51,200,000  ████████████████████░ 92%              │
│  Learned Items: 847/1,243  ━━━━━━━━━━━━━━━░░░░░ 68%              │
└────────────────────────────────────────────────────────────────────┘
```

**Dimensions:**
- Width: 12-14 slots wide (432-504 pixels)
- Visible height: 6-8 rows at a time
- Scrollbar on right side
- Total grid: 9 columns × 30 rows = 270 slots

---

#### Layout Option B: Compact with Search (Alternative)

```
┌──────────────────────────────────────────────────────────┐
│  Ultimate Builder's Bag                        [?] [X]   │
├──────────────────────────────────────────────────────────┤
│  Search: [_________________] 🔍  Sort: [A-Z ▼]      ▲   │
│  ┌──────────────────────────────────────────────┐    │   │
│  │ [Dirt 2K]  [Stone 1K] [Cobble 512] [Oak...]│    │   │
│  │ [Glass...] [Sand 64]  [Gravel...] [Clay...]│    │   │
│  │ [Iron...] [Gold...]  [Diamond...] [Emer...]│    ║   │
│  │ [...]     [...]      [...]        [...]    │    ║   │
│  │ [...]     [...]      [...]        [...]    │    ║   │
│  └──────────────────────────────────────────────┘    │   │
│  ═══════════════════════════════════════════════════    ▼   │
│  Page 1 of 45  [◄] [►]                               │
│                                                        │
│  Quick Stats:                                          │
│  • 127 unique items  • 892,456 total items            │
│  • 143 empty slots   • 52% full                       │
├──────────────────────────────────────────────────────────┤
│  Player Inventory [36 slots........................]    │
├──────────────────────────────────────────────────────────┤
│  ☑ Dank  ☑ Container  ☑ Craft  ☑ Infinite  [Settings] │
│  EMC: 51.2M ████████████████████░ 92%                  │
└──────────────────────────────────────────────────────────┘
```

**Features:**
- Search/filter items by name
- Sort options (A-Z, count, EMC value, learned status)
- Page navigation
- Quick stats panel
- Compact view

---

## Technical Implementation

### Screen Class Structure

```java
public class BagScreen extends AbstractContainerScreen<BagMenu> {

    // Scrolling
    private float scrollOffset = 0.0F;
    private boolean isScrolling = false;

    // Dimensions
    private static final int SLOTS_PER_ROW = 9;
    private static final int VISIBLE_ROWS = 6;
    private static final int SLOT_SIZE = 18;
    private static final int SCROLLBAR_WIDTH = 12;

    // GUI sizing
    @Override
    protected void init() {
        super.init();

        int totalRows = getTotalRows();

        // Wide layout: 9 slots per row
        this.imageWidth = SLOTS_PER_ROW * SLOT_SIZE + 16 + SCROLLBAR_WIDTH;
        this.imageHeight = calculateHeight(totalRows);

        // Center on screen
        this.leftPos = (this.width - this.imageWidth) / 2;
        this.topPos = (this.height - this.imageHeight) / 2;
    }

    private int getTotalRows() {
        int capacity = getBagCapacity();
        return (int) Math.ceil(capacity / (float) SLOTS_PER_ROW);
    }

    private int calculateHeight(int totalRows) {
        // Header + visible slots + player inventory + footer
        return 20 + (VISIBLE_ROWS * SLOT_SIZE) + 8 + 76 + 40;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        int totalRows = getTotalRows();
        if (totalRows > VISIBLE_ROWS) {
            // Scroll 1 row at a time
            float scroll = (float) delta / totalRows;
            this.scrollOffset = Mth.clamp(
                this.scrollOffset - scroll,
                0.0F,
                1.0F
            );
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, delta);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        // Render background
        this.renderBackground(graphics);

        // Render scrollable slots
        renderScrollableSlots(graphics, mouseX, mouseY);

        // Render scrollbar
        renderScrollbar(graphics);

        // Render player inventory
        super.render(graphics, mouseX, mouseY, partialTick);

        // Render tooltips
        this.renderTooltip(graphics, mouseX, mouseY);
    }

    private void renderScrollableSlots(GuiGraphics graphics, int mouseX, int mouseY) {
        int totalRows = getTotalRows();
        int firstVisibleRow = (int) (scrollOffset * (totalRows - VISIBLE_ROWS));

        // Enable scissor for clipping
        enableScissor(
            this.leftPos + 8,
            this.topPos + 20,
            SLOTS_PER_ROW * SLOT_SIZE,
            VISIBLE_ROWS * SLOT_SIZE
        );

        // Render visible slots
        for (int row = 0; row < VISIBLE_ROWS; row++) {
            int actualRow = firstVisibleRow + row;
            if (actualRow >= totalRows) break;

            for (int col = 0; col < SLOTS_PER_ROW; col++) {
                int slotIndex = actualRow * SLOTS_PER_ROW + col;
                if (slotIndex >= getBagCapacity()) break;

                renderSlot(graphics, slotIndex, col, row);
            }
        }

        // Disable scissor
        disableScissor();
    }

    private void renderScrollbar(GuiGraphics graphics) {
        int totalRows = getTotalRows();
        if (totalRows <= VISIBLE_ROWS) return; // No scrollbar needed

        int scrollbarX = this.leftPos + this.imageWidth - SCROLLBAR_WIDTH - 4;
        int scrollbarY = this.topPos + 20;
        int scrollbarHeight = VISIBLE_ROWS * SLOT_SIZE;

        // Background track
        graphics.fill(
            scrollbarX,
            scrollbarY,
            scrollbarX + SCROLLBAR_WIDTH,
            scrollbarY + scrollbarHeight,
            0xFF8B8B8B
        );

        // Scrollbar thumb
        int thumbHeight = Math.max(20,
            scrollbarHeight * VISIBLE_ROWS / totalRows);
        int thumbY = scrollbarY +
            (int) (scrollOffset * (scrollbarHeight - thumbHeight));

        graphics.fill(
            scrollbarX + 1,
            thumbY,
            scrollbarX + SCROLLBAR_WIDTH - 1,
            thumbY + thumbHeight,
            isScrolling ? 0xFFFFFFFF : 0xFFC0C0C0
        );
    }
}
```

### Menu/Container Class

```java
public class BagMenu extends AbstractContainerMenu {

    private final BagInventory bagInventory;
    private final int totalSlots;

    public BagMenu(int containerId, Inventory playerInv, BagInventory bagInv) {
        super(ModMenuTypes.BAG_MENU.get(), containerId);
        this.bagInventory = bagInv;
        this.totalSlots = bagInv.getSlots();

        // Add all bag slots (will be rendered with scrolling)
        int index = 0;
        for (int row = 0; row < getTotalRows(); row++) {
            for (int col = 0; col < 9; col++) {
                if (index >= totalSlots) break;

                // Virtual positions (screen handles actual rendering)
                this.addSlot(new BagSlot(bagInv, index++,
                    8 + col * 18, 20 + row * 18));
            }
        }

        // Add player inventory slots (fixed position)
        addPlayerInventory(playerInv);
        addPlayerHotbar(playerInv);
    }

    private int getTotalRows() {
        return (int) Math.ceil(totalSlots / 9.0);
    }
}
```

---

## Advanced Features

### 1. Search & Filter
```java
private String searchQuery = "";
private List<Integer> filteredSlots = new ArrayList<>();

private void updateSearch(String query) {
    this.searchQuery = query.toLowerCase();
    filteredSlots.clear();

    if (query.isEmpty()) {
        // Show all slots
        for (int i = 0; i < bagInventory.getSlots(); i++) {
            filteredSlots.add(i);
        }
    } else {
        // Filter by name
        for (int i = 0; i < bagInventory.getSlots(); i++) {
            ItemStack stack = bagInventory.getStackInSlot(i);
            if (!stack.isEmpty()) {
                String itemName = stack.getHoverName()
                    .getString().toLowerCase();
                if (itemName.contains(query)) {
                    filteredSlots.add(i);
                }
            }
        }
    }
}
```

### 2. Sort Options
```java
public enum SortMode {
    NAME_ASC,     // A-Z
    NAME_DESC,    // Z-A
    COUNT_ASC,    // Least to most
    COUNT_DESC,   // Most to least
    EMC_ASC,      // Cheapest first
    EMC_DESC,     // Most expensive first
    LEARNED       // Learned items first
}

private void sortSlots(SortMode mode) {
    List<ItemStack> items = new ArrayList<>();
    // ... collect items, sort, redistribute
}
```

### 3. Quick Actions
```java
// Shift+Click slot = Quick deposit/withdraw
// Ctrl+Click = Move single item
// Middle-click = Quick info
// Alt+Click = Lock/unlock slot
```

---

## Responsive Design

### Small Screens (< 1024px width)
- Reduce to 6 slots per row
- Increase visible rows to 9
- Taller, narrower layout

### Medium Screens (1024-1920px)
- 9 slots per row (default)
- 6 visible rows

### Large Screens (> 1920px)
- 12 slots per row
- 8 visible rows
- Show more at once

---

## Performance Optimizations

1. **Render only visible slots** - Don't render off-screen slots
2. **Slot caching** - Cache rendered slot textures
3. **Lazy loading** - Load item info on-demand
4. **Batch rendering** - Render all slots in single pass
5. **Scissor clipping** - Prevent overdraw outside scroll area

---

## Accessibility

- **Keyboard navigation**: Arrow keys to scroll
- **Tab navigation**: Tab through slots
- **Screen reader**: Announce slot contents
- **High contrast**: Option for colorblind users
- **Zoom**: Scale GUI with Minecraft GUI scale

---

## Summary

✅ **Doubled slot counts** - 54, 108, 162, 216, 270 slots
✅ **Wide scrollable layout** - Fits on screen with scrolling
✅ **Modern UI** - Clean, responsive design
✅ **Search & filter** - Find items quickly
✅ **Performance optimized** - Smooth scrolling
✅ **Accessible** - Keyboard/screen reader support

**Recommended: Layout Option A (Wide Grid)**
- Simple, clean interface
- Easy scrolling
- Shows module/EMC info
- Familiar to Minecraft players
