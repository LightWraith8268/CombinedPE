package com.riley.combinedpe.compat;

import moze_intel.projecte.api.ItemInfo;
import moze_intel.projecte.api.proxy.IEMCProxy;
import net.minecraft.world.item.ItemStack;

/**
 * Compatibility layer for ProjectE API.
 *
 * Provides utility methods for EMC value queries and knowledge checks.
 */
public class ProjectECompat {

    private static final IEMCProxy EMC_PROXY = IEMCProxy.INSTANCE;

    /**
     * Check if an item has an EMC value.
     */
    public static boolean hasEMC(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }

        ItemInfo itemInfo = ItemInfo.fromStack(stack);
        return EMC_PROXY.hasValue(itemInfo);
    }

    /**
     * Get the EMC value of an item.
     * Returns 0 if item has no EMC value.
     */
    public static long getEMCValue(ItemStack stack) {
        if (stack.isEmpty()) {
            return 0;
        }

        ItemInfo itemInfo = ItemInfo.fromStack(stack);
        if (!EMC_PROXY.hasValue(itemInfo)) {
            return 0;
        }

        return EMC_PROXY.getValue(itemInfo);
    }
}
