"""
Generate all 65 bag textures (13 types × 5 tiers) for CombinedPE mod

Creates 16x16 pixel art textures with:
- Type-specific colors
- Tier progression visual indicators
- Minecraft-style aesthetic
"""

from PIL import Image, ImageDraw
import os

# Output directory
output_dir = "src/main/resources/assets/combinedpe/textures/item"
os.makedirs(output_dir, exist_ok=True)

# Bag types with colors (from BagType.java)
BAG_TYPES = {
    "materials": (0x8B, 0x45, 0x13),      # Brown
    "food": (0xFF, 0x63, 0x47),           # Tomato Red
    "ore": (0xC0, 0xC0, 0xC0),            # Silver
    "tool": (0xFF, 0xD7, 0x00),           # Gold
    "mob_drop": (0x8B, 0x45, 0x13),       # Brown
    "liquid": (0x1E, 0x90, 0xFF),         # Dodger Blue
    "redstone": (0xFF, 0x00, 0x00),       # Red
    "potion": (0x93, 0x70, 0xDB),         # Medium Purple
    "enchanting": (0x7B, 0x68, 0xEE),     # Medium Slate Blue
    "trade": (0x32, 0xCD, 0x32),          # Lime Green
    "combat": (0xDC, 0x14, 0x3C),         # Crimson
    "adventure": (0x22, 0x8B, 0x22),      # Forest Green
    "treasure": (0xFF, 0xD7, 0x00),       # Gold
}

# Tiers
TIERS = ["basic", "advanced", "superior", "masterful", "ultimate"]

def lighten(color, factor=1.3):
    """Lighten a color for highlights"""
    return tuple(min(255, int(c * factor)) for c in color)

def darken(color, factor=0.7):
    """Darken a color for shadows"""
    return tuple(int(c * factor) for c in color)

def create_bag_texture(bag_type, tier, primary_color):
    """Create a 16x16 bag texture"""
    img = Image.new('RGBA', (16, 16), (0, 0, 0, 0))
    draw = ImageDraw.Draw(img)

    light_color = lighten(primary_color)
    dark_color = darken(primary_color)
    very_dark = darken(primary_color, 0.5)

    # === BASE BAG SHAPE ===
    # Bag body (rounded rectangle)
    draw.rectangle([4, 3, 11, 13], fill=primary_color)
    draw.rectangle([3, 5, 12, 11], fill=primary_color)

    # Top opening
    draw.rectangle([5, 2, 10, 3], fill=dark_color)

    # Side shadows (left darker, right lighter)
    for y in range(5, 12):
        draw.point((3, y), fill=dark_color)
        draw.point((12, y), fill=light_color)

    # Bottom shadow
    draw.rectangle([4, 12, 11, 13], fill=dark_color)

    # Corners (rounded)
    draw.point((4, 4), fill=dark_color)
    draw.point((11, 4), fill=light_color)
    draw.point((4, 12), fill=very_dark)
    draw.point((11, 12), fill=dark_color)

    # === TIER-SPECIFIC DETAILS ===

    if tier == "basic":
        # Simple strap
        draw.rectangle([6, 1, 9, 2], fill=very_dark)
        draw.line([(6, 2), (6, 4)], fill=very_dark)
        draw.line([(9, 2), (9, 4)], fill=very_dark)

    elif tier == "advanced":
        # Better strap + container indicator
        draw.rectangle([6, 0, 9, 2], fill=very_dark)
        draw.line([(6, 2), (6, 5)], fill=very_dark)
        draw.line([(9, 2), (9, 5)], fill=very_dark)

        # Container symbol (small chest icon)
        draw.rectangle([6, 7, 9, 9], fill=light_color)
        draw.line([(7, 8), (8, 8)], fill=dark_color)

    elif tier == "superior":
        # Enhanced strap + glow effect
        draw.rectangle([6, 0, 9, 2], fill=light_color)
        draw.line([(6, 2), (5, 5)], fill=light_color)
        draw.line([(9, 2), (10, 5)], fill=light_color)

        # Enhanced indicator (diamond pattern)
        draw.point((7, 6), fill=(255, 255, 255))
        draw.point((8, 7), fill=(255, 255, 255))
        draw.point((7, 8), fill=(255, 255, 255))
        draw.point((6, 7), fill=(255, 255, 255))

    elif tier == "masterful":
        # Masterful strap + crafting pattern
        draw.rectangle([6, 0, 9, 2], fill=(255, 215, 0))  # Gold
        draw.line([(5, 2), (5, 6)], fill=(255, 215, 0))
        draw.line([(10, 2), (10, 6)], fill=(255, 215, 0))

        # Crafting grid (3x3 tiny)
        for x in range(6, 9):
            for y in range(7, 10):
                if (x + y) % 2 == 0:
                    draw.point((x, y), fill=(255, 215, 0))

    elif tier == "ultimate":
        # Ultimate strap + EMC glow
        draw.rectangle([6, 0, 9, 2], fill=(255, 20, 147))  # Deep Pink
        draw.line([(5, 2), (5, 6)], fill=(255, 20, 147))
        draw.line([(10, 2), (10, 6)], fill=(255, 20, 147))

        # EMC glow particles (bright spots)
        emc_color = (255, 192, 203)  # Pink glow
        draw.point((5, 7), fill=emc_color)
        draw.point((10, 7), fill=emc_color)
        draw.point((7, 10), fill=emc_color)
        draw.point((8, 10), fill=emc_color)

        # Central EMC symbol
        draw.rectangle([7, 7, 8, 8], fill=(255, 255, 255, 200))

    # === TYPE-SPECIFIC ICONS (small, bottom-right corner) ===

    if bag_type == "food":
        # Apple icon
        draw.rectangle([10, 11, 11, 12], fill=(255, 0, 0))  # Red
        draw.point((10, 10), fill=(0, 128, 0))  # Green stem

    elif bag_type == "ore":
        # Pickaxe tip
        draw.line([(10, 10), (11, 11)], fill=(192, 192, 192))
        draw.point((11, 10), fill=(160, 160, 160))

    elif bag_type == "tool":
        # Sword icon
        draw.line([(11, 9), (11, 12)], fill=(192, 192, 192))
        draw.point((11, 8), fill=(139, 69, 19))  # Brown handle

    elif bag_type == "mob_drop":
        # Bone icon
        draw.line([(10, 11), (12, 11)], fill=(255, 255, 255))
        draw.point((10, 10), fill=(255, 255, 255))
        draw.point((12, 12), fill=(255, 255, 255))

    elif bag_type == "liquid":
        # Water drop
        draw.point((11, 10), fill=(0, 0, 255))
        draw.line([(10, 11), (12, 11)], fill=(0, 0, 255))
        draw.point((11, 12), fill=(0, 0, 255))

    elif bag_type == "redstone":
        # Redstone torch
        draw.point((11, 12), fill=(139, 69, 19))  # Brown base
        draw.point((11, 11), fill=(255, 0, 0))    # Red top
        draw.point((11, 10), fill=(255, 100, 100))  # Light red glow

    elif bag_type == "potion":
        # Potion bottle
        draw.line([(11, 10), (11, 12)], fill=(128, 0, 128))
        draw.point((11, 9), fill=(64, 64, 64))  # Cork

    elif bag_type == "enchanting":
        # Book icon
        draw.rectangle([10, 10, 12, 12], fill=(139, 69, 19))  # Brown book
        draw.line([(11, 10), (11, 12)], fill=(255, 215, 0))  # Gold spine

    elif bag_type == "trade":
        # Emerald
        draw.point((11, 10), fill=(0, 255, 0))
        draw.line([(10, 11), (12, 11)], fill=(0, 255, 0))
        draw.point((11, 12), fill=(0, 200, 0))

    elif bag_type == "combat":
        # Sword crossing shield
        draw.line([(10, 10), (12, 12)], fill=(192, 192, 192))  # Sword
        draw.rectangle([11, 11, 12, 12], fill=(100, 100, 100))  # Shield

    elif bag_type == "adventure":
        # Compass
        draw.rectangle([10, 10, 12, 12], fill=(139, 69, 19))
        draw.point((11, 11), fill=(255, 0, 0))  # Red needle

    elif bag_type == "treasure":
        # Diamond
        draw.point((11, 10), fill=(0, 255, 255))
        draw.line([(10, 11), (12, 11)], fill=(0, 255, 255))
        draw.point((11, 12), fill=(0, 200, 200))

    return img

# Generate all 65 bag textures
print("Generating bag textures...")
count = 0

for bag_type, color in BAG_TYPES.items():
    for tier in TIERS:
        filename = f"{tier}_{bag_type}_bag.png"
        filepath = os.path.join(output_dir, filename)

        img = create_bag_texture(bag_type, tier, color)
        img.save(filepath)

        count += 1
        print(f"  [{count}/65] Created {filename}")

print(f"\n✅ Successfully created {count} bag textures!")
print(f"📁 Output directory: {output_dir}")

# Generate Enhanced Workbench texture (bonus)
print("\nGenerating Enhanced Workbench texture...")
workbench_img = Image.new('RGBA', (16, 16), (0, 0, 0, 0))
draw = ImageDraw.Draw(workbench_img)

# Crafting table style (brown wood with grid)
wood_color = (139, 90, 43)
light_wood = (160, 110, 60)
dark_wood = (100, 65, 30)

# Wood background
draw.rectangle([0, 0, 15, 15], fill=wood_color)

# Wood grain
for i in range(0, 16, 4):
    draw.line([(i, 0), (i, 15)], fill=light_wood)

# Crafting grid (3x3)
grid_color = (80, 50, 20)
for x in range(5, 12, 3):
    draw.line([(x, 4), (x, 11)], fill=grid_color)
for y in range(4, 12, 3):
    draw.line([(5, y), (11, y)], fill=grid_color)

# EMC glow indicator (corner)
emc_glow = (255, 20, 147)
draw.point((14, 1), fill=emc_glow)
draw.point((14, 2), fill=emc_glow)
draw.point((13, 1), fill=emc_glow)

workbench_img.save(os.path.join(output_dir, "enhanced_workbench.png"))
print("  Created enhanced_workbench.png")

print("\n✅ All textures generated successfully!")
print(f"📊 Total files created: {count + 1}")
