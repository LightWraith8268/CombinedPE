package com.riley.combinedpe.core;

import com.riley.combinedpe.CombinedPE;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Registry for CombinedPE menu types (containers)
 */
public class ModMenuTypes {

    public static final DeferredRegister<MenuType<?>> MENU_TYPES =
            DeferredRegister.create(Registries.MENU, CombinedPE.MOD_ID);

    // Menu types will be registered here
    // Builder's Bag container will go here

}
