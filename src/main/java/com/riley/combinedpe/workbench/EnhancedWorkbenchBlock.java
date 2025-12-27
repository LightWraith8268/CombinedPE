package com.riley.combinedpe.workbench;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CraftingTableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

/**
 * Enhanced Workbench - Crafting table that can access items from bags in player inventory
 *
 * Features:
 * - Standard 3x3 crafting grid
 * - Can pull ingredients from ANY bag in player inventory
 * - JEI integration for recipe transfer
 * - Vanilla Minecraft aesthetic
 */
public class EnhancedWorkbenchBlock extends CraftingTableBlock {

    private static final Component CONTAINER_TITLE = Component.translatable("container.combinedpe.enhanced_workbench");

    public EnhancedWorkbenchBlock(Properties properties) {
        super(properties);
    }

    /**
     * Open the Enhanced Workbench menu when right-clicked
     */
    @Override
    protected @NotNull InteractionResult useWithoutItem(
            @NotNull BlockState state,
            @NotNull Level level,
            @NotNull BlockPos pos,
            @NotNull Player player,
            @NotNull BlockHitResult hit) {

        if (!level.isClientSide) {
            player.openMenu(state.getMenuProvider(level, pos));
        }

        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    /**
     * Provide the Enhanced Workbench menu
     */
    @Override
    public MenuProvider getMenuProvider(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos) {
        return new SimpleMenuProvider(
            (containerId, playerInventory, player) ->
                new EnhancedWorkbenchMenu(containerId, playerInventory, ContainerLevelAccess.create(level, pos)),
            CONTAINER_TITLE
        );
    }
}
