package com.riley.combinedpe.integration.projecte;

import com.riley.combinedpe.CombinedPE;
import moze_intel.projecte.api.ProjectEAPI;
import moze_intel.projecte.api.proxy.IEMCProxy;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/**
 * ProjectE compatibility and API access
 *
 * This class uses ProjectE's modern API (1.21.1+) to:
 * - Check if items have EMC values
 * - Query EMC values for calculations
 * - Register custom EMC mappers for dynamic EMC registration
 *
 * Note: ProjectE 1.21.1 uses service-based API with IEMCProxy singleton
 * instead of the old static EMCHelper class
 */
public class ProjectECompat {

    /**
     * Check if ProjectE is loaded
     */
    public static boolean isProjectELoaded() {
        try {
            Class.forName("moze_intel.projecte.api.ProjectEAPI");
            // Verify the EMC proxy instance is accessible
            IEMCProxy.INSTANCE.getValue(ItemStack.EMPTY);
            CombinedPE.LOGGER.info("ProjectE detected! EMC Proxy accessible.");
            return true;
        } catch (ClassNotFoundException e) {
            CombinedPE.LOGGER.warn("ProjectE not found!");
            return false;
        } catch (Exception e) {
            CombinedPE.LOGGER.error("Failed to access ProjectE EMC proxy", e);
            return false;
        }
    }

    /**
     * Get EMC value for an item
     * @param stack The item stack to check
     * @return EMC value, or 0 if no value exists
     */
    public static long getEMCValue(ItemStack stack) {
        return IEMCProxy.INSTANCE.getValue(stack);
    }

    /**
     * Get EMC value for an item (convenience method)
     * @param item The item to check
     * @return EMC value, or 0 if no value exists
     */
    public static long getEMCValue(Item item) {
        return IEMCProxy.INSTANCE.getValue(item);
    }

    /**
     * Check if an item has an EMC value
     * @param stack The item stack to check
     * @return true if the item has EMC, false otherwise
     */
    public static boolean hasEMC(ItemStack stack) {
        return IEMCProxy.INSTANCE.hasValue(stack);
    }

    /**
     * Check if an item has an EMC value (convenience method)
     * @param item The item to check
     * @return true if the item has EMC, false otherwise
     */
    public static boolean hasEMC(Item item) {
        return IEMCProxy.INSTANCE.hasValue(item);
    }

    /**
     * Note: Custom EMC registration in ProjectE 1.21.1+ is done through
     * EMC Mappers (IEMCMapper) rather than direct registration.
     * We will implement this in Phase 2 when building the dynamic EMC system.
     */
}
