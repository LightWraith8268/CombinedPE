package com.riley.combinedpe.core;

import com.riley.combinedpe.CombinedPE;
import com.riley.combinedpe.bag.BagMenu;
import com.riley.combinedpe.workbench.EnhancedWorkbenchMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Registry for CombinedPE menu types (containers)
 */
public class ModMenuTypes {

    public static final DeferredRegister<MenuType<?>> MENU_TYPES =
            DeferredRegister.create(Registries.MENU, CombinedPE.MOD_ID);

    /**
     * Menu type for Builder's Bag GUI
     */
    public static final DeferredHolder<MenuType<?>, MenuType<BagMenu>> BAG_MENU =
            MENU_TYPES.register("bag_menu",
                    () -> IMenuTypeExtension.create((containerId, playerInventory, data) -> {
                        // Read bag stack from additional data (sent from server)
                        var bagStack = net.minecraft.world.item.ItemStack.STREAM_CODEC.decode(data);
                        return new BagMenu(containerId, playerInventory, bagStack);
                    }));

    /**
     * Menu type for Enhanced Workbench GUI
     */
    public static final DeferredHolder<MenuType<?>, MenuType<EnhancedWorkbenchMenu>> ENHANCED_WORKBENCH =
            MENU_TYPES.register("enhanced_workbench",
                    () -> IMenuTypeExtension.create(EnhancedWorkbenchMenu::new));
}
