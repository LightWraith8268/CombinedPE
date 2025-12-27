package com.riley.combinedpe.integration.rs;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;

import java.util.List;

/**
 * Custom BlockItem for EMC Linker with tooltips.
 * Provides helpful usage information to players.
 */
public class ItemEMCLinker extends BlockItem {

    public ItemEMCLinker(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);

        // Main description
        tooltipComponents.add(Component.literal("Configure Refined Storage External Storage")
            .withStyle(ChatFormatting.GRAY));

        // Empty line for spacing
        tooltipComponents.add(Component.empty());

        // Usage instructions - header
        tooltipComponents.add(Component.literal("How to use:")
            .withStyle(ChatFormatting.GOLD));

        // Step 1
        tooltipComponents.add(Component.literal("1. Place next to RS External Storage")
            .withStyle(ChatFormatting.GRAY));

        // Step 2
        tooltipComponents.add(Component.literal("2. Right-click with item to link")
            .withStyle(ChatFormatting.GRAY));

        // Step 3
        tooltipComponents.add(Component.literal("3. RS Grid shows that item from EMC")
            .withStyle(ChatFormatting.GRAY));

        // Empty line for spacing
        tooltipComponents.add(Component.empty());

        // Advanced usage
        tooltipComponents.add(Component.literal("Sneak + Right-click to clear link")
            .withStyle(ChatFormatting.DARK_GRAY)
            .withStyle(ChatFormatting.ITALIC));

        // Requirement note
        tooltipComponents.add(Component.literal("Requires: ProjectE knowledge + EMC balance")
            .withStyle(ChatFormatting.DARK_AQUA)
            .withStyle(ChatFormatting.ITALIC));
    }
}
