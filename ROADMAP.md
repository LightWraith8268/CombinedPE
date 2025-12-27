# CombinedPE Development Roadmap

## Current Version: 1.7.6
**Status:** ✅ Build Complete - Ready for Testing

---

## Phase 1: Foundation ✅ COMPLETE

### Core Infrastructure
- ✅ Project setup (NeoForge 1.21.1)
- ✅ Gradle build configuration
- ✅ Basic mod structure
- ✅ ProjectE API integration

### Builder's Bags System
- ✅ 13 bag types implemented
- ✅ 5 tier system (Basic → Ultimate)
- ✅ 65 unique bags total
- ✅ All textures created
- ✅ Complete localization
- ✅ Stack upgrade system (4 tiers)
- ✅ Type filtering logic
- ✅ Capability registration (IItemHandler)

### Enhanced Workbench
- ✅ Block and block entity
- ✅ Custom crafting GUI (3×3)
- ✅ Menu system
- ✅ Recipe support
- ✅ Texture and localization

### Build System
- ✅ All compilation errors fixed
- ✅ Successful build (181KB jar)
- ✅ Testing documentation created

---

## Phase 2: EMC System ✅ COMPLETE

### EMC Calculation
- ✅ Recipe scanner (crafting, smelting, smithing)
- ✅ Recursive EMC calculation
- ✅ Circular dependency detection
- ✅ EMC caching system
- ✅ Unit tests for calculations

### EMC Provider
- ✅ Block provider handler
- ✅ Ultimate bag integration
- ✅ Auto-transmutation from player EMC
- ✅ Knowledge checks
- ✅ EMC deduction
- ✅ Client-server sync

### ProjectE Integration
- ✅ API access verified
- ✅ EMC value retrieval
- ✅ Knowledge provider integration
- ✅ Transmutation support

---

## Phase 3: Testing & Polish 🔄 IN PROGRESS

### Current Task: Comprehensive Testing
**Status:** In Progress (CombinedPE-311)
**Priority:** P1

**Testing Checklist:**
- ⏳ Verify mod loads without crashes
- ⏳ Test all 65 bags in creative menu
  - Proper names display
  - Proper icons display
- ⏳ Test bag functionality
  - Open GUI
  - Insert items
  - Extract items
  - Type filtering works correctly
- ⏳ Test Enhanced Workbench
  - Place block
  - Open crafting GUI
  - Craft vanilla recipes
  - Shift-click quick crafting
- ⏳ Test stack upgrades
  - Apply to bags
  - Stack limits increase correctly
- ⏳ Test EMC provider (Ultimate bags)
  - Auto-transmutation works
  - EMC deduction from player
  - Knowledge checks
- ⏳ Test all recipes
  - Bag crafting recipes
  - Enhanced Workbench recipe
  - Stack upgrade recipes
- ⏳ Performance testing
  - No lag opening bags
  - No lag using workbench
  - No memory leaks

**Testing Files:**
- `TESTING_CHECKLIST.md` - Detailed test plan
- `TESTING_SUMMARY.md` - Build status and notes

---

## Phase 4: Refined Storage Integration (PLANNED)

### External Storage
- [ ] RS External Storage for EMC access (CombinedPE-1g2) ✅ CLOSED
- [ ] Test with Refined Storage items (CombinedPE-52g) ✅ CLOSED
- [ ] Grid EMC value display (CombinedPE-vdl) ✅ CLOSED

### Integration Features
- [ ] EMC value tooltip in RS grid
- [ ] Auto-import from EMC system
- [ ] Auto-export to EMC system
- [ ] Pattern support for transmutation

**Dependencies:**
- Refined Storage 2.0.0+ for NeoForge 1.21.1
- Successful Phase 3 testing

---

## Phase 5: JEI/EMI Integration (PLANNED)

### Recipe Integration
- [ ] JEI/EMI plugin (CombinedPE-hxd)
- [ ] Show EMC values in recipes
- [ ] Transmutation recipe category
- [ ] Bag crafting recipe category
- [ ] Recipe transfer support

**Dependencies:**
- JEI 19.19.2.120+ or EMI equivalent
- Recipe scanner complete ✅

---

## Phase 6: ProjectEX Port (PLANNED)

### Research Phase
- [ ] Research ProjectEX features (CombinedPE-0lm)
- [ ] Analyze ProjectEX architecture
- [ ] Identify 1.21.1 compatibility issues
- [ ] Plan porting strategy

### Implementation
- [ ] Port core ProjectEX features
- [ ] Update to NeoForge API
- [ ] Test integration with CombinedPE
- [ ] Merge into unified mod

**Priority:** P1 Epic (CombinedPE-8v7)

---

## Phase 7: Advanced Features (PLANNED)

### Configuration
- [ ] In-game config GUI (CombinedPE-ens) - P3
- [ ] Config syncing client/server
- [ ] Per-world config options

### Performance
- [ ] Performance optimization (CombinedPE-25n) - P2
- [ ] Memory usage profiling
- [ ] Render optimization
- [ ] Network packet optimization

### Polish
- [ ] ProjectE Retexture assets (CombinedPE-c38) - P3 (optional)
- [ ] Custom sounds for bags
- [ ] Particle effects for transmutation
- [ ] Achievements/advancements

---

## Phase 8: Multi-Version Support (FUTURE)

### Fabric Port
- [ ] Configure multi-loader build (CombinedPE-qo0) - P2
- [ ] Test Fabric compatibility
- [ ] Maintain feature parity

### Version Compatibility
- [ ] Test on 1.21.2 (CombinedPE-8u0) - P4
- [ ] Test on 1.21.3 as released
- [ ] Address breaking changes (CombinedPE-q0y) - P4
- [ ] Maintain backwards compatibility (CombinedPE-kr9) - P4

---

## Issue Tracking

**Total Issues:** 33
- Open: 11
- In Progress: 1 (Testing)
- Blocked: 1
- Closed: 22

**Priority Breakdown:**
- P0 (Critical): 0
- P1 (High): 2 (Testing, ProjectEX port)
- P2 (Medium): 3 (Multi-loader, JEI, Performance)
- P3 (Low): 2 (Config GUI, Retextures)
- P4 (Backlog): 3 (Version compatibility)

**Ready to Work:** 10 issues (no blockers)

---

## Recent Milestones

### v1.7.6 (Current - 2025-12-27)
- Fixed all compilation errors (85 total)
- Updated to 65-bag system throughout
- Added complete localization
- Created testing documentation
- **Status:** Ready for in-game testing

### v1.7.5 (Previous)
- Enhanced Workbench implementation
- Workbench GUI system
- Recipe integration

### v1.6.0
- EMC Provider system
- Ultimate bag auto-transmutation
- ProjectE knowledge integration

### v1.5.0
- Recipe scanner implementation
- EMC calculation engine
- Circular dependency detection

---

## Next Actions

**Immediate (Waiting on User):**
1. In-game testing of v1.7.6
2. Verify all 65 bags work correctly
3. Test Enhanced Workbench functionality
4. Document any issues found

**Short Term (After Testing):**
1. Fix any bugs found in testing
2. Begin JEI/EMI integration
3. Research ProjectEX porting requirements
4. Plan Refined Storage integration

**Long Term:**
1. Complete ProjectEX port
2. Multi-loader support (Fabric)
3. Performance optimization
4. Version compatibility updates

---

## Dependencies

### Runtime Dependencies
- Minecraft 1.21.1
- NeoForge 21.1.82
- ProjectE 1.21.1-4.0.0 (core functionality)

### Optional Dependencies
- JEI 19.19.2.120 (recipe integration)
- Refined Storage 2.0.0+ (storage integration)
- ProjectEX 1.21.1-5.0.0 (planned port/merge)

### Development Dependencies
- Java 21 (Eclipse Adoptium)
- Gradle 8.x
- IntelliJ IDEA / VS Code

---

**Last Updated:** 2025-12-27
**Next Review:** After v1.7.6 testing complete
