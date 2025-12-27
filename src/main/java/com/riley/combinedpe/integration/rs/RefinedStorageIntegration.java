package com.riley.combinedpe.integration.rs;

import com.riley.combinedpe.CombinedPE;
import net.neoforged.fml.ModList;

/**
 * Integration with Refined Storage 2.0.
 *
 * Provides EMC External Storage capability - allows RS External Storage blocks
 * to extract items using nearest player's ProjectE EMC balance.
 *
 * HOW TO ENABLE:
 * 1. Add Refined Storage 2.0 jar to libs/ directory
 * 2. Add to build.gradle.kts dependencies:
 *    compileOnly(files("libs/refinedstorage-neoforge-2.0.0-beta.X.jar"))
 * 3. Uncomment the registration code in init()
 * 4. Build and test
 *
 * USAGE:
 * - Place RS External Storage next to any block
 * - Player must be within 16 blocks
 * - RS can extract items player has learned in ProjectE
 * - Quantity limited by player's EMC balance
 * - EMC automatically deducted on extraction
 */
public class RefinedStorageIntegration {

    private static final String RS_MOD_ID = "refinedstorage";

    /**
     * Check if Refined Storage is loaded.
     */
    public static boolean isLoaded() {
        return ModList.get().isLoaded(RS_MOD_ID);
    }

    /**
     * Initialize RS integration.
     * Called during mod setup if RS is detected.
     */
    public static void init() {
        if (!isLoaded()) {
            CombinedPE.LOGGER.info("Refined Storage not detected, skipping integration");
            return;
        }

        CombinedPE.LOGGER.info("Refined Storage detected, initializing EMC External Storage integration");

        try {
            com.refinedmods.refinedstorage.common.api.RefinedStorageApi api =
                com.refinedmods.refinedstorage.common.api.RefinedStorageApi.INSTANCE;

            // Register EMC External Storage provider
            api.addExternalStorageProviderFactory((level, pos, direction) -> {
                return new EMCExternalStorageProvider(level, pos);
            });

            // Register Builder's Bag External Storage provider
            api.addExternalStorageProviderFactory((level, pos, direction) -> {
                return new BagExternalStorageProvider(level, pos);
            });

            CombinedPE.LOGGER.info("RS EMC External Storage integration registered successfully");
            CombinedPE.LOGGER.info("RS Builder's Bag External Storage integration registered successfully");
            CombinedPE.LOGGER.info("Place RS External Storage near you to access EMC transmutation and bag inventory");
        } catch (Exception e) {
            CombinedPE.LOGGER.error("Failed to register RS integration", e);
        }
    }
}
