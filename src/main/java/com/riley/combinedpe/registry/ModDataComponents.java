package com.riley.combinedpe.registry;

import com.riley.combinedpe.CombinedPE;
import com.riley.combinedpe.bag.BagInventory;
import com.riley.combinedpe.bag.VirtualStackData;
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

    /**
     * Data component for virtual stack counts (mega-stacks)
     */
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<VirtualStackData>> VIRTUAL_STACKS =
        REGISTRAR.registerComponentType(
            "virtual_stacks",
            builder -> builder
                .persistent(VirtualStackData.CODEC)
                .networkSynchronized(VirtualStackData.STREAM_CODEC)
        );

    /**
     * Data component for stack upgrade tier
     */
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<com.riley.combinedpe.bag.StackUpgradeTier>> STACK_UPGRADE =
        REGISTRAR.registerComponentType(
            "stack_upgrade",
            builder -> builder
                .persistent(net.minecraft.util.StringRepresentable.fromEnum(com.riley.combinedpe.bag.StackUpgradeTier::values))
                .networkSynchronized(net.minecraft.network.codec.ByteBufCodecs.fromCodec(
                    net.minecraft.util.StringRepresentable.fromEnum(com.riley.combinedpe.bag.StackUpgradeTier::values)
                ))
        );
}
