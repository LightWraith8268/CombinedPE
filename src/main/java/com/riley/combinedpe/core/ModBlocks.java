package com.riley.combinedpe.core;

import com.riley.combinedpe.CombinedPE;
import com.riley.combinedpe.integration.rs.BlockEMCLinker;
import com.riley.combinedpe.workbench.EnhancedWorkbenchBlock;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Registry for CombinedPE blocks
 */
public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(Registries.BLOCK, CombinedPE.MOD_ID);

    // Enhanced Workbench - Crafting table that can access items from bags
    public static final DeferredHolder<Block, EnhancedWorkbenchBlock> ENHANCED_WORKBENCH =
        BLOCKS.register("enhanced_workbench", () -> new EnhancedWorkbenchBlock(
            BlockBehaviour.Properties.of()
                .strength(2.5F)
                .sound(net.minecraft.world.level.block.SoundType.WOOD)
        ));

    // EMC Linker - Configure RS External Storage to show specific EMC items
    public static final DeferredHolder<Block, BlockEMCLinker> EMC_LINKER =
        BLOCKS.register("emc_linker", () -> new BlockEMCLinker(
            BlockBehaviour.Properties.of()
                .strength(1.5F)
                .sound(net.minecraft.world.level.block.SoundType.METAL)
                .lightLevel(state -> 7) // Glowing block
                .noOcclusion() // Allow light to pass through
        ));

}
