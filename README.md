# CombinedPE

A unified Minecraft mod combining ProjectE ecosystem mods with a dynamic EMC registration system that can automatically assign EMC values to items from ANY mod in a modpack.

**Target Platform:** NeoForge 1.21.1+ (forward compatible)
**Author:** Riley E. Antrobus (personal use only)

## Features

### Dynamic EMC Registration System
The killer feature that makes this mod valuable: **automatic EMC assignment for any item from any mod**.

- Scans all registered items from all loaded mods on world load
- Calculates EMC values based on crafting, smelting, and smithing recipes
- Tag-based inference for items without recipes
- Configurable overrides and blacklists
- Generates reports of assigned values

### Builder's Bag (Ported from 1.12.2)
- Bag item that stores building blocks
- Auto-refill from bag when placing blocks
- Multiple bag tiers with increasing capacity
- EMC Provider: Bag can pull blocks from EMC (transmute on demand)
- Refined Storage Integration: Bag can pull from RS network

### Refined Storage Integration
- RS Grid can show EMC values
- RS can auto-craft using EMC as a "resource"
- External Storage can expose EMC as item source
- Pattern encoding for EMC transmutation

## Dependencies

### Required
- **NeoForge** 1.21.1-21.1.82 or higher
- **ProjectE** 1.21.1-4.0.0 or higher
- **FTB ProjectEX** 1.21.1-5.0.0 or higher

### Optional
- **JEI** - Recipe viewer integration
- **Refined Storage** - RS integration features

## Building

```bash
./gradlew build
```

The built JAR will be in `build/libs/`.

## Development Setup

1. Clone the repository
2. Import into your IDE (IntelliJ IDEA or Eclipse)
3. Run `./gradlew genIntellijRuns` or `./gradlew eclipse`
4. Use the generated run configurations

## License

All Rights Reserved - Personal Use Only

## Credits

- **sinkillerj/pupnewfster** - ProjectE
- **FTB Team** - FTB ProjectEX
- **Tschipp** - Original Builder's Bag (1.12.2)
- **TagnumElite** - ProjectE Integration inspiration
