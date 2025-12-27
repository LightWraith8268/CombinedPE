# Refined Storage 2.0 Integration Plan

## Overview
Integration between CombinedPE and Refined Storage 2.0 to allow EMC access through the RS storage network.

## User Context
- Refined Storage 2.0.0 is installed in the user's modpack
- User wants to access ProjectE EMC through RS External Storage

## Research Completed (v1.6.2)

### RS 2.0 External Storage API

**Key Interfaces:**

1. `ExternalStorageProvider` (extends `InsertableStorage`, `ExtractableStorage`)
   - `Iterator<ResourceAmount> iterator()` - Lists all available resources
   - `long insert(ResourceKey resource, long amount, Action action)` - Insert items
   - `long extract(ResourceKey resource, long amount, Action action)` - Extract items

2. `ExternalStorageProviderFactory` (functional interface)
   - `ExternalStorageProvider create(ServerLevel level, BlockPos pos, Direction direction)` - Creates provider instances

3. Registration
   - `RefinedStorageApi.addExternalStorageProviderFactory(factory)` - Register custom external storage

**Resource Types:**

- `ItemResource` - Record class for item resources
  - `ItemResource(Item item, DataComponentPatch components)`
  - `ItemResource.ofItemStack(ItemStack)` - Create from ItemStack
  - `ItemResource.toItemStack(long amount)` - Convert to ItemStack

- `ResourceKey` - Interface for resource identification
- `ResourceAmount` - Wrapper for resource + quantity

### Implementation Challenges

1. **Missing Dependency**
   - RS 2.0.0 is not available as a Maven dependency
   - Would need to add as local jar or compileOnly dependency
   - API is still in beta, may change

2. **ProjectE Knowledge Access**
   - ProjectE's IKnowledgeProvider doesn't expose a way to iterate learned items
   - Would need to track learned items separately or use reflection
   - EMC balance can change dynamically

3. **Player Association**
   - RS External Storage is placed in world, not player-specific
   - Need to determine which player's EMC to access
   - Current approach: Find nearest player within 16 blocks

## Implementation Options

### Option 1: Full Integration (Complex)
**Pros:**
- Complete RS integration
- EMC accessible from RS grid
- Can craft with EMC through RS

**Cons:**
- Requires RS 2.0 as dependency
- Complex player association logic
- ProjectE API limitations for knowledge iteration
- Significant testing required

**Estimated Complexity:** High (3-5 days development + testing)

### Option 2: Minimal Integration (Simple)
**Pros:**
- Easy to implement
- No external dependencies
- Focused on core functionality

**Cons:**
- Limited RS integration
- Players must use ProjectE transmutation table

**Implementation:**
- Just ensure EMC-registered items work in RS storage
- No special external storage provider
- Basic compatibility testing

**Estimated Complexity:** Low (1-2 hours testing)

### Option 3: Deferred Integration
**Pros:**
- Wait for RS 2.0 stable release
- Let ProjectE API mature
- Community may provide integration mods

**Cons:**
- No immediate RS integration
- Users must wait for feature

## Recommended Approach

**Short-term (v1.7.0):** Option 2 - Minimal Integration
- Test that CombinedPE items work in RS storage
- Ensure EMC values display correctly (if RS shows them)
- Document compatibility

**Long-term (v2.0.0):** Option 1 - Full Integration
- Wait for RS 2.0 stable release
- Implement full External Storage provider
- Add RS dependency properly
- Comprehensive testing

## Current Status

**Files Created:**
- `src/main/java/com/riley/combinedpe/integration/rs/EMCExternalStorageProvider.java`
  - Skeleton implementation
  - Missing proper RS dependency
  - Incomplete knowledge iteration
  - Needs substantial work

**Next Steps:**
1. Decide on integration approach
2. If Option 2: Remove skeleton file, do basic testing
3. If Option 1: Add RS 2.0 dependency, complete implementation
4. Update beads issues accordingly

## Dependencies Required (Option 1)

```kotlin
// build.gradle.kts
dependencies {
    // ... existing dependencies

    // Refined Storage 2.0 - compileOnly for optional integration
    compileOnly("com.refinedmods:refinedstorage:2.0.0-beta.2") // Version TBD
    // OR as local jar:
    compileOnly(files("libs/refinedstorage-neoforge-2.0.0-beta.2.jar"))
}
```

## Testing Plan (Option 1)

1. **Basic Functionality**
   - Place RS External Storage next to nothing
   - Verify EMC items appear in RS grid
   - Test extraction (transmutation) from RS

2. **Player Association**
   - Test with multiple players
   - Verify nearest player's EMC is used
   - Test when no players nearby

3. **EMC Balance**
   - Extract items, verify EMC deducted
   - Verify quantity limits based on EMC
   - Test with insufficient EMC

4. **Edge Cases**
   - Player moves away during extraction
   - EMC balance changes during operation
   - Multiple External Storages accessing same EMC

## References

- [RS 2.0 JavaDoc](https://refinedmods.com/javadoc/refinedstorage2/)
- [RS 2.0 GitHub](https://github.com/refinedmods/refinedstorage2)
- [Issue #976 - Custom ExternalStorageProviders](https://github.com/refinedmods/refinedstorage2/issues/976)
- [ProjectE API Docs](libs/ProjectE-1.21.1-PE1.1.0.jar)

---

**Document Status:** Research complete, awaiting implementation decision
**Last Updated:** 2025-12-26
**Version:** 1.6.2
