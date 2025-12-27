package com.riley.combinedpe.link;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

/**
 * Base block for all EMC Link types.
 *
 * Interaction:
 * - Right-click with item: Set as filter
 * - Right-click with empty hand: Clear filter
 * - Shift-right-click: Show info
 */
public abstract class LinkBaseBlock extends BaseEntityBlock {

    public LinkBaseBlock() {
        super(BlockBehaviour.Properties.of()
                .strength(5.0F, 6.0F)
                .sound(SoundType.STONE)
                .requiresCorrectToolForDrops()
        );
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (level.isClientSide()) {
            return ItemInteractionResult.SUCCESS;
        }

        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (!(blockEntity instanceof LinkBaseBlockEntity link)) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        // Shift-click shows info regardless of held item
        if (player.isShiftKeyDown()) {
            showLinkInfo(player, link);
            return ItemInteractionResult.SUCCESS;
        }

        // Click with item sets filter
        if (!stack.isEmpty()) {
            link.setSelectedItem(stack);
            player.displayClientMessage(
                    Component.literal("Link set to: " + stack.getHoverName().getString()),
                    true
            );
            return ItemInteractionResult.SUCCESS;
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (!(blockEntity instanceof LinkBaseBlockEntity link)) {
            return InteractionResult.PASS;
        }

        // Shift-click shows info
        if (player.isShiftKeyDown()) {
            showLinkInfo(player, link);
            return InteractionResult.SUCCESS;
        }

        // Empty hand clears filter
        link.setSelectedItem(ItemStack.EMPTY);
        player.displayClientMessage(
                Component.literal("Link filter cleared"),
                true
        );
        return InteractionResult.SUCCESS;
    }

    /**
     * Show link information to the player
     */
    private void showLinkInfo(Player player, LinkBaseBlockEntity link) {
        player.displayClientMessage(Component.literal("=== EMC Link Info ==="), false);
        player.displayClientMessage(Component.literal("Owner: " + link.getOwnerName()), false);
        player.displayClientMessage(Component.literal("Tier: " + link.getTier().getName()), false);

        ItemStack filter = link.getSelectedItem();
        if (!filter.isEmpty()) {
            player.displayClientMessage(
                    Component.literal("Filter: " + filter.getHoverName().getString()),
                    false
            );
        } else {
            player.displayClientMessage(Component.literal("Filter: None (set by clicking with item)"), false);
        }
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        if (placer != null && level.getBlockEntity(pos) instanceof LinkBaseBlockEntity link) {
            link.setOwner(placer.getUUID(), placer.getScoreboardName());
        }
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public abstract BlockEntity newBlockEntity(BlockPos pos, BlockState state);
}
