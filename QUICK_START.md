# CombinedPE Quick Start Guide

## For Users: Installing and Testing

### Installation
1. Download `combinedpe-1.7.6.jar` from `build/libs/`
2. Copy to `.minecraft/mods/` folder
3. Ensure you have:
   - Minecraft 1.21.1
   - NeoForge 21.1.82
   - ProjectE 1.21.1-4.0.0 (required dependency)
4. Launch Minecraft

### Basic Usage

#### Builder's Bags
**What are they?**
- 65 specialized storage bags (13 types × 5 tiers)
- Each type only accepts specific items (Materials bags accept blocks, Food bags accept food, etc.)
- Higher tiers have more storage capacity

**How to use:**
1. Find bags in Creative menu under "CombinedPE" tab
2. Right-click to open bag GUI
3. Insert items - only matching types will be accepted
4. Ultimate tier bags can transmute items from your ProjectE EMC!

**Bag Types:**
- Materials (blocks, planks, stone)
- Food (edible items)
- Ore (ores, ingots, nuggets)
- Tool (pickaxes, axes, shovels)
- Mob Drop (bones, gunpowder, etc.)
- Liquid (buckets, bottles)
- Redstone (redstone components)
- Potion (potions and splash potions)
- Enchanting (books, enchanting tables)
- Trade (emeralds, villager items)
- Combat (weapons, armor)
- Adventure (maps, compasses)
- Treasure (diamonds, netherite, etc.)

**Tiers:**
- Basic (smallest)
- Advanced
- Superior
- Masterful
- Ultimate (largest + EMC provider)

#### Enhanced Workbench
**What is it?**
- Crafting table that can pull materials from bags in your inventory
- 3×3 crafting grid like vanilla

**How to use:**
1. Place Enhanced Workbench block
2. Right-click to open GUI
3. Craft normally - it will check bags for ingredients!

#### Stack Upgrades
**What are they?**
- Items that increase stack sizes in bags
- 4 tiers (I, II, III, IV)

**How to use:**
1. Craft stack upgrade
2. Apply to bag (feature pending implementation)
3. Stack limits increase

### Testing Checklist
See `TESTING_CHECKLIST.md` for comprehensive testing guide.

Quick tests:
- ✓ Open a Materials bag, try inserting cobblestone (should work)
- ✓ Try inserting food into Materials bag (should reject)
- ✓ Open Food bag, insert bread (should work)
- ✓ Place Enhanced Workbench, craft a stick
- ✓ Get an Ultimate bag, try the EMC provider feature

---

## For Developers: Building and Contributing

### Prerequisites
- Java 21 (Eclipse Adoptium)
- Git
- Gradle (wrapper included)

### Quick Build
```bash
# Clone repository
git clone https://github.com/LightWraith8268/CombinedPE.git
cd CombinedPE

# Build mod
./gradlew build

# Output: build/libs/combinedpe-1.7.6.jar
```

### Development Setup
```bash
# Generate IDE files
./gradlew genIntellijRuns  # For IntelliJ IDEA
./gradlew genEclipseRuns   # For Eclipse
./gradlew genVSCodeRuns    # For VS Code

# Run test client
./gradlew runClient

# Run test server
./gradlew runServer
```

### Project Structure
```
CombinedPE/
├── src/main/java/com/riley/combinedpe/
│   ├── bag/              # Builder's Bags system
│   ├── core/             # Registry, items, blocks
│   ├── event/            # Event handlers
│   ├── network/          # Client-server packets
│   └── workbench/        # Enhanced Workbench
├── src/main/resources/
│   ├── assets/combinedpe/
│   │   ├── textures/     # All textures
│   │   ├── lang/         # Localization
│   │   └── models/       # Block/item models
│   └── data/combinedpe/
│       └── recipe/       # Crafting recipes
├── TESTING_CHECKLIST.md  # Testing guide
├── TESTING_SUMMARY.md    # Build summary
├── ROADMAP.md           # Development roadmap
└── build.gradle         # Build configuration
```

### Making Changes
```bash
# Create feature branch
git checkout -b feature/my-feature

# Make changes, test
./gradlew build

# Commit
git add .
git commit -m "Description of changes"

# Push
git push origin feature/my-feature
```

### Using Beads Workflow
```bash
# Check available work
bd ready

# Show issue details
bd show CombinedPE-xxx

# Start working on issue
bd update CombinedPE-xxx --status=in_progress

# Close when done
bd close CombinedPE-xxx

# Sync with git
bd sync
```

### Code Style
- Follow existing code patterns
- Use descriptive variable names
- Add JavaDoc comments for public methods
- Keep methods focused and concise

### Testing
- Build must succeed: `./gradlew build`
- Test in actual Minecraft client
- Verify no console errors
- Document all issues found

---

## Troubleshooting

### Build Fails
```bash
# Clean and rebuild
./gradlew clean build

# Check Java version
java -version  # Should be Java 21
```

### Mod Doesn't Load
- Verify Minecraft 1.21.1
- Verify NeoForge 21.1.82
- Verify ProjectE is installed
- Check logs in `.minecraft/logs/latest.log`

### Items Show Wrong Names
- Check `src/main/resources/assets/combinedpe/lang/en_us.json`
- Rebuild mod after changes
- Clear Minecraft cache

### Textures Missing (Purple/Black)
- Check textures exist in `src/main/resources/assets/combinedpe/textures/item/`
- Verify file names match item registry names
- Rebuild mod

---

## Getting Help

**Documentation:**
- `ROADMAP.md` - Development plan and milestones
- `TESTING_CHECKLIST.md` - Comprehensive testing guide
- `TESTING_SUMMARY.md` - Current build status

**Issue Tracking:**
```bash
bd list --status=open     # See all open issues
bd ready                  # See what can be worked on
bd stats                  # Project statistics
```

**Community:**
- GitHub Issues: Report bugs and feature requests
- GitHub Discussions: Ask questions and share ideas

---

## Quick Reference

### Gradle Commands
```bash
./gradlew build           # Build mod
./gradlew clean          # Clean build files
./gradlew runClient      # Test in Minecraft
./gradlew tasks          # List all tasks
```

### Git Commands
```bash
git status               # Check changes
git add .                # Stage all changes
git commit -m "msg"      # Commit changes
git push                 # Push to remote
bd sync                  # Sync beads workflow
```

### File Locations
- Built jar: `build/libs/combinedpe-1.7.6.jar`
- Source code: `src/main/java/com/riley/combinedpe/`
- Textures: `src/main/resources/assets/combinedpe/textures/`
- Recipes: `src/main/resources/data/combinedpe/recipe/`
- Localization: `src/main/resources/assets/combinedpe/lang/en_us.json`

---

**Last Updated:** 2025-12-27
**Version:** 1.7.6
**Status:** Ready for Testing
