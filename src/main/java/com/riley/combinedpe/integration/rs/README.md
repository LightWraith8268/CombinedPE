# Refined Storage 2.0 Integration

## Overview

This package contains integration code for Refined Storage 2.0, allowing players to use RS External Storage blocks to extract items using their ProjectE EMC balance.

## Current Status

**Implementation:** ✅ ENABLED
**Version:** 1.7.2

## How It Works

When an RS External Storage block is placed and connected to a Refined Storage network:

1. **Player Detection**: Finds the nearest player within 16 blocks
2. **Knowledge Access**: Uses that player's ProjectE knowledge (learned items)
3. **Full Inventory Listing**: Shows ALL learned items in RS Grid
4. **Real-Time Quantity**: Each item's quantity is calculated from:
   - Item's EMC value
   - Player's current EMC balance
   - Formula: `quantity = playerEMC / itemEMC`
5. **Extraction**: When RS extracts items:
   - Deducts EMC cost from player's balance
   - Returns the transmuted items
6. **Read-Only**: Inserting items into the EMC storage is blocked

## Usage

**Already Enabled!** Just install CombinedPE alongside Refined Storage 2.0.

### In-Game Setup:

1. Place an RS External Storage block
2. Connect it to your RS network
3. Stand within 16 blocks of the External Storage
4. Open your RS Grid

**Result:** You'll see ALL your learned ProjectE items with quantities based on your EMC!

### Example:

- **Player EMC:** 1,000,000
- **Learned Items:**
  - Dirt (1 EMC) → Shows 1,000,000 available
  - Cobblestone (1 EMC) → Shows 1,000,000 available
  - Iron Ingot (256 EMC) → Shows 3,906 available
  - Diamond (8,192 EMC) → Shows 122 available

As you extract items, your EMC decreases and quantities update in real-time!

## Files

- `EMCExternalStorageProvider.java` - Implements RS's ExternalStorageProvider interface
- `RefinedStorageIntegration.java` - Registration and initialization logic
- `README.md` - This file

## Design Notes

### Full Item Listing

The iterator returns ALL learned items with their calculated quantities:
- Iterates through player's ProjectE knowledge (`IKnowledgeProvider.getKnowledge()`)
- Filters items that have EMC values
- Calculates max quantity per item: `playerEMC / itemEMC`
- Returns as `ResourceAmount` list for RS Grid display

This provides a complete view of available EMC transmutations directly in RS!

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
