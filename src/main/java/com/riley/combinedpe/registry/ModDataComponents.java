package com.riley.combinedpe.registry;

import com.riley.combinedpe.CombinedPE;
import com.riley.combinedpe.bag.BagInventory;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Registration for custom data components
 */
public class ModDataComponents {

    public static final DeferredRegister.DataComponents REGISTRAR =
        DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, CombinedPE.MOD_ID);

    /**
     * Data component for Builder's Bag inventory storage
     */
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<BagInventory>> BAG_INVENTORY =
        REGISTRAR.registerComponentType(
            "bag_inventory",
            builder -> builder
                .persistent(BagInventory.CODEC)
                .networkSynchronized(BagInventory.STREAM_CODEC)
        );
}
