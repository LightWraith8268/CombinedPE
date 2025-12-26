package com.riley.combinedpe.core;

import com.riley.combinedpe.CombinedPE;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Registry for CombinedPE blocks
 */
public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(Registries.BLOCK, CombinedPE.MOD_ID);

    // Blocks will be registered here as they are added

}
