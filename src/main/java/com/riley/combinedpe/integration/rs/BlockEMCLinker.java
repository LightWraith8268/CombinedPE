package com.riley.combinedpe.integration.rs;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

/**
 * EMC Linker Block.
 *
 * Place this block adjacent to a Refined Storage External Storage to configure
 * which item the External Storage should provide via EMC transmutation.
 *
 * Usage:
 * 1. Place EMC Linker next to RS External Storage (any side)
 * 2. Right-click linker with the item you want to link (e.g., Diamond)
 * 3. External Storage will now show virtually unlimited diamonds (based on your EMC)
 * 4. Extracting items from RS Grid deducts EMC from your balance
 * 5. Inserting items to RS Grid converts them to EMC
 *
 * Features:
 * - Directional block (points toward the External Storage it's linking)
 * - Sneak + right-click to clear link
 * - Right-click with item to set link
 * - Displays current link in chat when clicked
 * - Automatically clears link when broken
 *
 * Visual design:
 * - Small block (10×10×10 pixels)
 * - Glowing texture
 * - Shows linked item as particle effect (future enhancement)
 */
public class BlockEMCLinker extends DirectionalBlock {

    // Codec for block serialization (required by DirectionalBlock)
    public static final MapCodec<BlockEMCLinker> CODEC = simpleCodec(BlockEMCLinker::new);

    // Block shape: 10×10×10 centered cube (smaller than full block)
    private static final VoxelShape SHAPE = Block.box(3, 3, 3, 13, 13, 13);

    public BlockEMCLinker(Properties properties) {
        super(properties);
        this.registerDefaultState(
            this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
        );
    }

    @Override
    protected MapCodec<? extends DirectionalBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        // Face toward the player
        return this.defaultBlockState()
            .setValue(FACING, context.getNearestLookingDirection().getOpposite());
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level,
                                        @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return SHAPE;
    }

    @SuppressWarnings("deprecation")
    @Override
    protected @NotNull ItemInteractionResult useItemOn(@NotNull ItemStack stack, @NotNull BlockState state,
                                                        @NotNull Level level, @NotNull BlockPos pos,
                                                        @NotNull Player player, @NotNull InteractionHand hand,
                                                        @NotNull BlockHitResult hitResult) {
        if (level.isClientSide) {
            return ItemInteractionResult.SUCCESS;
        }

        ServerPlayer serverPlayer = (ServerPlayer) player;

        // Get the position this linker is pointing toward (where External Storage should be)
        Direction facing = state.getValue(FACING);
        BlockPos targetPos = pos.relative(facing);

        // Sneak + right-click to clear link
        if (player.isShiftKeyDown()) {
            EMCLink.removeLink(level, targetPos);
            serverPlayer.sendSystemMessage(
                Component.literal("EMC Link cleared for position ")
                    .append(Component.literal(targetPos.toShortString()).withStyle(s -> s.withColor(0xFFAA00)))
            );
            return ItemInteractionResult.SUCCESS;
        }

        // Get held item
        ItemStack heldItem = player.getItemInHand(hand);

        if (heldItem.isEmpty()) {
            // No item held - show current link
            EMCLink.LinkData link = EMCLink.getLink(level, targetPos);
            if (link == null) {
                serverPlayer.sendSystemMessage(
                    Component.literal("No EMC Link configured. Hold an item and right-click to set link.")
                        .withStyle(s -> s.withColor(0xAAAAAA))
                );
            } else {
                serverPlayer.sendSystemMessage(
                    Component.literal("Current EMC Link: ")
                        .append(link.linkedItem.getDisplayName())
                        .append(Component.literal(" (Owner: " + link.playerUUID + ")"))
                        .withStyle(s -> s.withColor(0x55FF55))
                );
            }
            return ItemInteractionResult.SUCCESS;
        }

        // Set link to held item
        EMCLink.setLink(level, targetPos, serverPlayer.getUUID(), heldItem);
        serverPlayer.sendSystemMessage(
            Component.literal("EMC Link set to ")
                .append(heldItem.getDisplayName())
                .append(Component.literal(" for position "))
                .append(Component.literal(targetPos.toShortString()).withStyle(s -> s.withColor(0xFFAA00)))
                .withStyle(s -> s.withColor(0x55FF55))
        );

        return ItemInteractionResult.SUCCESS;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onRemove(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos,
                         @NotNull BlockState newState, boolean movedByPiston) {
        if (!state.is(newState.getBlock())) {
            // Block was broken - clear the link it was pointing to
            Direction facing = state.getValue(FACING);
            BlockPos targetPos = pos.relative(facing);
            EMCLink.removeLink(level, targetPos);
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }
}
