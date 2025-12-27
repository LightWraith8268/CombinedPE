# Textures Needed for CombinedPE

## Critical - Phase 5 (Polish & Testing)

### Block Textures Needed

#### EMC Linker Block
**Path**: `src/main/resources/assets/combinedpe/textures/block/emc_linker.png`

**Current Status**: ❌ Missing (using placeholder reference)
**Priority**: P1 - Critical for release
**Dimensions**: 16×16 pixels (standard Minecraft block texture)

**Design Requirements**:
- Should convey "EMC" and "linking/connection" concept
- Glowing appearance (block has light level 7)
- Distinct from other ProjectE blocks
- Professional, cohesive with ProjectE aesthetic

**Suggested Design Elements**:
- EMC symbol or energy pattern
- Directional indicator (block is directional)
- Glowing energy core
- Metallic/technological appearance
- Color scheme: Purple/Blue (EMC colors) + Gold/Yellow accents

**Current Model Reference**:
- Model: `assets/combinedpe/models/block/emc_linker.json`
- Cube: 10×10×10 centered in block space
- All faces use `#all` texture (single texture for all sides)

#### Enhanced Workbench Block
**Path**: `src/main/resources/assets/combinedpe/textures/block/enhanced_workbench.png`

**Current Status**: ⚠️ Unknown (need to verify)
**Priority**: P2 - Important for polish

---

## Item Textures Status

### Existing Textures ✅
All item textures present in `src/main/resources/assets/combinedpe/textures/item/`:
- ✅ 65 Builder's Bag textures (all tiers × all types)
- ✅ 4 Stack Upgrade textures (I-IV)
- ✅ Enhanced Workbench item texture

### Missing Textures ❌
None - all item models reference existing textures

---

## Texture Creation Workflow

### Option 1: Commission Artist
1. Provide design requirements and reference images
2. Review and iterate on drafts
3. Finalize at 16×16 PNG format
4. Place in appropriate texture directory
5. Test in-game appearance

### Option 2: Use ProjectE/Minecraft Style Generator
1. Use existing ProjectE textures as base
2. Modify with image editing software (GIMP, Photoshop, Aseprite)
3. Add unique elements (EMC symbol, glow effects)
4. Export as 16×16 PNG
5. Test in-game

### Option 3: Placeholder/Temporary
1. Use solid color or simple pattern
2. Add glowing border effect
3. Mark as temporary in changelog
4. Replace in future update

---

## Testing Checklist

After adding textures:
- [ ] Verify texture loads in-game (no missing texture)
- [ ] Check all 6 block faces display correctly
- [ ] Verify directional variants look correct
- [ ] Test lighting effects (glow level 7)
- [ ] Confirm texture fits Minecraft/ProjectE aesthetic
- [ ] Check item form matches block appearance
- [ ] Test in different resource packs (if applicable)

---

## Implementation Steps

1. **Create texture file**:
   ```bash
   mkdir -p src/main/resources/assets/combinedpe/textures/block
   # Add emc_linker.png (16×16)
   ```

2. **Verify model references**:
   - Block model: `models/block/emc_linker.json` → `"all": "combinedpe:block/emc_linker"`
   - Item model: `models/item/emc_linker.json` → references block model

3. **Rebuild and test**:
   ```bash
   ./gradlew build -x test
   # Launch Minecraft and verify texture displays
   ```

4. **Commit**:
   ```bash
   git add src/main/resources/assets/combinedpe/textures/block/emc_linker.png
   git commit -m "Add EMC Linker block texture"
   ```

---

## Future Texture Needs

### Phase 6+ (Lower Priority)

- Additional block textures (if new blocks added)
- GUI backgrounds (if custom GUIs needed)
- Particle textures (if particle effects added)
- Achievement/advancement icons
- Alternative texture variants (optional)

---

**Last Updated**: 2025-12-27
**Status**: EMC Linker texture critical for Phase 5 completion
