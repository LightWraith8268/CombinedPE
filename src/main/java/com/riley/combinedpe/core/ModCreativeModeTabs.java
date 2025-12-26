package com.riley.combinedpe.core;

import com.riley.combinedpe.CombinedPE;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Creative mode tabs for CombinedPE
 */
public class ModCreativeModeTabs {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CombinedPE.MOD_ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> COMBINEDPE_TAB =
            CREATIVE_MODE_TABS.register("combinedpe", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.combinedpe"))
                    .icon(() -> new ItemStack(ModItems.ULTIMATE_BAG.get()))
                    .displayItems((parameters, output) -> {
                        // Builder's Bags (all tiers)
                        output.accept(ModItems.BASIC_BAG.get());
                        output.accept(ModItems.ADVANCED_BAG.get());
                        output.accept(ModItems.SUPERIOR_BAG.get());
                        output.accept(ModItems.MASTERFUL_BAG.get());
                        output.accept(ModItems.ULTIMATE_BAG.get());
                    })
                    .build());
}
