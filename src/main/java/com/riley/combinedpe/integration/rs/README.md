# Refined Storage 2.0 Integration

## Overview

This package contains integration code for Refined Storage 2.0, allowing players to use RS External Storage blocks to extract items using their ProjectE EMC balance.

## Current Status

**Implementation:** ✅ ENABLED
**Version:** 1.7.4

## How It Works

When an RS External Storage block is placed and connected to a Refined Storage network:

1. **Player Detection**: Finds the nearest player within 16 blocks
2. **Knowledge Access**: Uses that player's ProjectE knowledge (learned items)
3. **Full Inventory Listing**: Shows ALL learned items in RS Grid
4. **Real-Time Quantity**: Each item's quantity is calculated from:
   - Item's EMC value
   - Player's current EMC balance
   - Formula: `quantity = playerEMC / itemEMC`
5. **Extraction (Transmutation)**: When RS extracts items:
   - Deducts EMC cost from player's balance
   - Returns the transmuted items
6. **Insertion (EMC Conversion)**: When RS inserts items:
   - Converts items to EMC value
   - Adds EMC to player's balance
   - Learns the item if not already known
   - Only accepts items with EMC values

## Usage

**Already Enabled!** Just install CombinedPE alongside Refined Storage 2.0.

### In-Game Setup:

1. Place an RS External Storage block
2. Connect it to your RS network
3. Stand within 16 blocks of the External Storage
4. Open your RS Grid

**Result:** You'll see ALL your learned ProjectE items with quantities based on your EMC!

### Example - Extraction (Transmutation):

- **Player EMC:** 1,000,000
- **Learned Items:**
  - Dirt (1 EMC) → Shows 1,000,000 available
  - Cobblestone (1 EMC) → Shows 1,000,000 available
  - Iron Ingot (256 EMC) → Shows 3,906 available
  - Diamond (8,192 EMC) → Shows 122 available

Extract 100 Iron Ingots → EMC decreases by 25,600

### Example - Insertion (EMC Conversion):

- **Player EMC:** 10,000
- Insert 64 Diamonds (8,192 EMC each) into RS
- RS routes to External Storage (if priority is high)
- Diamonds convert to EMC: 64 × 8,192 = 524,288 EMC
- **New Player EMC:** 534,288
- Diamond is learned (if not already known)

**Pro Tip:** Set External Storage priority higher than regular storage to auto-convert items to EMC!

## Files

- `EMCExternalStorageProvider.java` - Implements RS's ExternalStorageProvider interface
- `RefinedStorageIntegration.java` - Registration and initialization logic
- `README.md` - This file

## Design Notes

### Bidirectional EMC Flow

**Extraction (EMC → Items):**
- Iterator returns ALL learned items with calculated quantities
- Iterates through player's ProjectE knowledge
- Calculates max quantity per item: `playerEMC / itemEMC`
- Deducts EMC when items are extracted

**Insertion (Items → EMC):**
- Accepts any item with an EMC value
- Converts items to EMC: `itemCount × itemEMC`
- Adds EMC to player's balance
- Auto-learns unknown items
- Rejects items without EMC values

This allows RS to act as both a transmutation interface AND an EMC bank!

### Player Association

The External Storage finds the nearest player within 16 blocks. This means:
- Place External Storage near where you work
- Works with any block (not just Transmutation Table)
- Multiple players can use different External Storages

### EMC Deduction

EMC is deducted when RS actually extracts items (not when querying). The transaction is atomic - either the full amount is extracted and EMC deducted, or nothing happens.

## Testing

When enabled, test:
1. Place RS External Storage
2. Stand within 16 blocks
3. Open RS grid
4. Search for an item you've learned
5. Extract it - should consume EMC

## Compatibility

**Requires:**
- Refined Storage 2.0.0-beta.2 or later
- ProjectE 1.21.1-4.0.0 or later
- NeoForge 21.1.x

**Optional:**
- Works standalone without RS installed (integration just skips initialization)

---

**Version:** 1.7.2
**Author:** Riley E. Antrobus
**License:** All Rights Reserved
