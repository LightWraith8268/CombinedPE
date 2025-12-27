#!/usr/bin/env python3
"""
Generate 16x16 item textures for CombinedPE mod
"""
from PIL import Image, ImageDraw

def create_bag_texture(filename, primary_color, accent_color, tier_name):
    """Create a bag texture with tier-appropriate styling"""
    img = Image.new('RGBA', (16, 16), (0, 0, 0, 0))
    pixels = img.load()

    # Bag outline (dark brown)
    outline = (74, 47, 24, 255)

    # Define the bag shape (simple pouch design)
    # Top opening
    for x in range(5, 11):
        pixels[x, 2] = outline

    # Top edges
    for y in range(3, 4):
        pixels[4, y] = outline
        pixels[11, y] = outline

    # Main body outline
    for y in range(4, 13):
        pixels[3, y] = outline
        pixels[12, y] = outline

    # Bottom
    for x in range(4, 12):
        pixels[x, 13] = outline
    pixels[4, 12] = outline
    pixels[11, 12] = outline
    pixels[5, 12] = outline
    pixels[10, 12] = outline

    # Fill main body with primary color
    for y in range(4, 12):
        for x in range(4, 12):
            if pixels[x, y] == (0, 0, 0, 0):
                # Add shading gradient
                shade_factor = 1.0 - (y - 4) * 0.1
                shaded = tuple(int(c * shade_factor) for c in primary_color[:3]) + (255,)
                pixels[x, y] = shaded

    # Add accent stripe (tier indicator)
    accent_y = 7
    for x in range(5, 11):
        pixels[x, accent_y] = accent_color

    # Add tier-specific details
    if tier_name == "ultimate":
        # Add EMC glow effect
        for x in range(6, 10):
            pixels[x, 9] = accent_color
    elif tier_name == "masterful":
        # Add crafting grid pattern
        pixels[6, 9] = accent_color
        pixels[8, 9] = accent_color
        pixels[9, 9] = accent_color
    elif tier_name == "superior":
        # Add container icon
        pixels[7, 9] = accent_color
        pixels[8, 9] = accent_color
    elif tier_name == "advanced":
        # Add simple container
        pixels[7, 9] = accent_color

    # Top opening (darker)
    for x in range(5, 11):
        pixels[x, 3] = tuple(int(c * 0.5) for c in outline[:3]) + (255,)

    img.save(filename)
    print(f"Created {filename}")


def create_upgrade_texture(filename, tier_num, color):
    """Create a stack upgrade texture"""
    img = Image.new('RGBA', (16, 16), (0, 0, 0, 0))
    pixels = img.load()

    # Create a stacked items icon
    outline = (60, 60, 60, 255)

    # Draw three stacked squares getting smaller
    # Bottom square (largest)
    for x in range(3, 13):
        pixels[x, 11] = outline
        pixels[x, 12] = outline
    for y in range(7, 13):
        pixels[3, y] = outline
        pixels[12, y] = outline

    # Fill bottom
    for y in range(8, 12):
        for x in range(4, 12):
            shade = tuple(int(c * 0.7) for c in color[:3]) + (255,)
            pixels[x, y] = shade

    # Middle square
    for x in range(5, 11):
        pixels[x, 5] = outline
        pixels[x, 6] = outline
    for y in range(4, 7):
        pixels[5, y] = outline
        pixels[10, y] = outline

    # Fill middle
    for y in range(5, 6):
        for x in range(6, 10):
            pixels[x, y] = color

    # Top square (smallest)
    for x in range(7, 9):
        pixels[x, 2] = outline
        pixels[x, 3] = outline
    pixels[7, 2] = color
    pixels[8, 2] = color

    # Add tier indicator (Roman numeral style dots)
    indicator_color = (255, 255, 100, 255)
    if tier_num >= 1:
        pixels[7, 9] = indicator_color
    if tier_num >= 2:
        pixels[8, 9] = indicator_color
    if tier_num >= 3:
        pixels[9, 9] = indicator_color
    if tier_num >= 4:
        pixels[10, 9] = indicator_color

    img.save(filename)
    print(f"Created {filename}")


# Output directory
output_dir = "src/main/resources/assets/combinedpe/textures/item/"

# Create Builder's Bag textures
bags = [
    ("basic_bag.png", (139, 69, 19), (101, 67, 33), "basic"),          # Brown
    ("advanced_bag.png", (65, 105, 225), (30, 144, 255), "advanced"),  # Royal Blue
    ("superior_bag.png", (147, 112, 219), (138, 43, 226), "superior"), # Medium Purple
    ("masterful_bag.png", (255, 215, 0), (255, 193, 37), "masterful"), # Gold
    ("ultimate_bag.png", (255, 20, 147), (255, 105, 180), "ultimate"), # Deep Pink
]

for filename, primary, accent, tier in bags:
    create_bag_texture(output_dir + filename, primary, accent, tier)

# Create Stack Upgrade textures
upgrades = [
    ("stack_upgrade_i.png", 1, (100, 200, 100)),    # Light green
    ("stack_upgrade_ii.png", 2, (100, 150, 255)),   # Light blue
    ("stack_upgrade_iii.png", 3, (200, 100, 255)),  # Light purple
    ("stack_upgrade_iv.png", 4, (255, 200, 50)),    # Gold
]

for filename, tier, color in upgrades:
    create_upgrade_texture(output_dir + filename, tier, color)

print("\n✓ All textures created successfully!")
print(f"Textures saved to: {output_dir}")
