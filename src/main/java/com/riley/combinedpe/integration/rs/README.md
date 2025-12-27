# Refined Storage 2.0 Integration

## Overview

This package contains integration code for Refined Storage 2.0, allowing players to use RS External Storage blocks to extract items using their ProjectE EMC balance.

## Current Status

**Implementation:** Complete but commented out
**Reason:** RS 2.0 dependency not included in build to keep mod lightweight

## How It Works

When enabled:
1. Player places RS External Storage block next to any block
2. RS External Storage finds nearest player (within 16 blocks)
3. Player can extract items they've learned in ProjectE knowledge
4. Extraction quantity limited by player's EMC balance
5. EMC automatically deducted on extraction

## How to Enable

### Step 1: Add RS Dependency

Download Refined Storage 2.0 jar and place in `libs/` directory, then add to `build.gradle.kts`:

```kotlin
dependencies {
    // ... existing dependencies

    // Refined Storage 2.0 - Optional integration
    compileOnly(files("libs/refinedstorage-neoforge-2.0.0-beta.X.jar"))
}
```

### Step 2: Uncomment Registration Code

In `RefinedStorageIntegration.java`, uncomment the registration code (lines marked with TODO).

### Step 3: Build and Test

```bash
./gradlew build
```

The integration will automatically activate when RS is detected at runtime.

## Files

- `EMCExternalStorageProvider.java` - Implements RS's ExternalStorageProvider interface
- `RefinedStorageIntegration.java` - Registration and initialization logic
- `README.md` - This file

## Design Notes

### Why This Approach?

Unlike trying to list all learned items (complex), we use RS's query-based approach:
- RS queries: "Do you have item X?"
- We respond: "Yes, up to N quantity (based on EMC)"
- RS extracts: We transmute using player's EMC

This is simpler and more efficient.

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

**Version:** 1.7.0 (when enabled)
**Author:** Riley E. Antrobus
**License:** All Rights Reserved
