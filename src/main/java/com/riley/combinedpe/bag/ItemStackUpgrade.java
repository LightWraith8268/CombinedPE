package com.riley.combinedpe.bag;

import com.riley.combinedpe.registry.ModDataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Stack upgrade item that can be applied to Builder's Bags
 * Increases the maximum stack size per slot
 */
public class ItemStackUpgrade extends Item {

    private final StackUpgradeTier tier;

    public ItemStackUpgrade(StackUpgradeTier tier, Properties properties) {
        super(properties.stacksTo(16)); // Stack up to 16 upgrades
        this.tier = tier;
    }

    /**
     * Get the upgrade tier
     */
    public StackUpgradeTier getTier() {
        return tier;
    }

    /**
     * Handle right-click - check if holding a bag in other hand
     */
    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack upgradeStack = player.getItemInHand(hand);
        ItemStack otherHandStack = player.getItemInHand(hand == InteractionHand.MAIN_HAND ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND);

        if (!level.isClientSide) {
            // Check if other hand has a bag
            if (otherHandStack.getItem() instanceof ItemBuildersBag) {
                StackUpgradeTier currentUpgrade = otherHandStack.getOrDefault(
                    ModDataComponents.STACK_UPGRADE.get(),
                    StackUpgradeTier.NONE
                );

                // Check if this upgrade is valid (must be next tier)
                if (currentUpgrade.getNext() == tier) {
                    // Apply upgrade
                    otherHandStack.set(ModDataComponents.STACK_UPGRADE.get(), tier);

                    // Consume one upgrade item
                    if (!player.isCreative()) {
                        upgradeStack.shrink(1);
                    }

                    player.displayClientMessage(
                        Component.literal("Applied " + tier.getDisplayName() + " to bag!").withStyle(style -> style.withColor(0x00FF00)),
                        true
                    );

                    return InteractionResultHolder.success(upgradeStack);
                } else if (currentUpgrade.ordinal() >= tier.ordinal()) {
                    // Already has this upgrade or higher
                    player.displayClientMessage(
                        Component.literal("Bag already has this upgrade or higher!").withStyle(style -> style.withColor(0xFF0000)),
                        true
                    );
                    return InteractionResultHolder.fail(upgradeStack);
                } else {
                    // Wrong tier (skipping tiers)
                    player.displayClientMessage(
                        Component.literal("Must apply upgrades in order! (Current: " + currentUpgrade.getDisplayName() + ")")
                            .withStyle(style -> style.withColor(0xFFFF00)),
                        true
                    );
                    return InteractionResultHolder.fail(upgradeStack);
                }
            } else {
                player.displayClientMessage(
                    Component.literal("Hold a Builder's Bag in your other hand!").withStyle(style -> style.withColor(0xFFFF00)),
                    true
                );
            }
        }

        return InteractionResultHolder.pass(upgradeStack);
    }

    /**
     * Add tooltip information
     */
    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);

        tooltipComponents.add(Component.literal(tier.getDisplayName()).withStyle(style -> style.withColor(0x00FF00)));
        tooltipComponents.add(Component.literal("Multiplier: " + tier.getMultiplier() + "x"));
        tooltipComponents.add(Component.literal("Max Stack: " + BagInventory.formatItemCount(tier.getMaxStackSize(64))));
        tooltipComponents.add(Component.literal(""));
        tooltipComponents.add(Component.literal("Hold a bag in your other hand").withStyle(style -> style.withItalic(true).withColor(0x808080)));
        tooltipComponents.add(Component.literal("and right-click to apply").withStyle(style -> style.withItalic(true).withColor(0x808080)));
    }
}
