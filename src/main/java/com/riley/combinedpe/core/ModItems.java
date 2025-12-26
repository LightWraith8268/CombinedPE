package com.riley.combinedpe.core;

import com.riley.combinedpe.CombinedPE;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Registry for CombinedPE items
 */
public class ModItems {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(Registries.ITEM, CombinedPE.MOD_ID);

    // Items will be registered here as they are added
    // Builder's Bag items will go here

}
