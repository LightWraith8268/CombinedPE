package com.riley.combinedpe.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.riley.combinedpe.CombinedPE;
import com.riley.combinedpe.emc.DynamicEMCMapper;
import com.riley.combinedpe.emc.EMCCache;
import com.riley.combinedpe.integration.projecte.ProjectECompat;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.item.ItemArgument;
import net.minecraft.commands.arguments.item.ItemInput;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/**
 * Commands for CombinedPE mod
 *
 * Commands:
 * - /combinedpe rescan - Force a complete EMC re-scan
 * - /combinedpe setemc <item> <value> - Manually set EMC for an item
 * - /combinedpe getemc [item] - Get EMC value for an item (or held item)
 * - /combinedpe clearcache - Clear the EMC cache
 */
public class CombinedPECommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
            Commands.literal("combinedpe")
                .requires(source -> source.hasPermission(2)) // Requires OP level 2
                .then(Commands.literal("rescan")
                    .executes(CombinedPECommand::rescan)
                )
                .then(Commands.literal("setemc")
                    .then(Commands.argument("item", ItemArgument.item(null))
                        .then(Commands.argument("value", IntegerArgumentType.integer(0))
                            .executes(CombinedPECommand::setEMC)
                        )
                    )
                )
                .then(Commands.literal("getemc")
                    .executes(CombinedPECommand::getEMCHeld)
                    .then(Commands.argument("item", ItemArgument.item(null))
                        .executes(CombinedPECommand::getEMCItem)
                    )
                )
                .then(Commands.literal("clearcache")
                    .executes(CombinedPECommand::clearCache)
                )
        );
    }

    /**
     * Force a complete EMC re-scan
     */
    private static int rescan(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();

        if (!ProjectECompat.isProjectELoaded()) {
            source.sendFailure(Component.literal("ProjectE is not loaded!"));
            return 0;
        }

        source.sendSuccess(() -> Component.literal("Invalidating EMC cache and triggering re-scan..."), true);

        // Invalidate cache
        EMCCache.invalidateCache();

        // Clear current EMC values
        DynamicEMCMapper.clearCache();

        source.sendSuccess(() -> Component.literal(
            "EMC cache cleared. Re-scan will occur automatically on next world reload or use /reload"
        ), true);

        CombinedPE.LOGGER.info("{} triggered EMC re-scan", source.getTextName());

        return 1;
    }

    /**
     * Manually set EMC for an item
     */
    private static int setEMC(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();

        try {
            ItemInput itemInput = ItemArgument.getItem(context, "item");
            int emcValue = IntegerArgumentType.getInteger(context, "value");

            Item item = itemInput.getItem();
            String itemId = BuiltInRegistries.ITEM.getKey(item).toString();

            // TODO: Store custom EMC value (needs runtime config modification)
            // For now, just inform user to add to config file
            source.sendSuccess(() -> Component.literal(
                "To set EMC for " + itemId + " to " + emcValue + ", add this to your config:"
            ), false);
            source.sendSuccess(() -> Component.literal(
                "  [emc_overrides]"
            ), false);
            source.sendSuccess(() -> Component.literal(
                "  overrides = [\"" + itemId + "=" + emcValue + "\"]"
            ), false);
            source.sendSuccess(() -> Component.literal(
                "Then run /combinedpe rescan"
            ), false);

            return 1;

        } catch (Exception e) {
            source.sendFailure(Component.literal("Failed to set EMC: " + e.getMessage()));
            return 0;
        }
    }

    /**
     * Get EMC value for held item
     */
    private static int getEMCHeld(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();

        if (source.getEntity() == null) {
            source.sendFailure(Component.literal("This command must be run by a player"));
            return 0;
        }

        if (!(source.getEntity() instanceof net.minecraft.world.entity.player.Player player)) {
            source.sendFailure(Component.literal("This command must be run by a player"));
            return 0;
        }

        ItemStack heldItem = player.getMainHandItem();
        if (heldItem.isEmpty()) {
            source.sendFailure(Component.literal("You must be holding an item"));
            return 0;
        }

        return getEMCForItem(source, heldItem);
    }

    /**
     * Get EMC value for specified item
     */
    private static int getEMCItem(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();

        try {
            ItemInput itemInput = ItemArgument.getItem(context, "item");
            ItemStack stack = new ItemStack(itemInput.getItem());

            return getEMCForItem(source, stack);

        } catch (Exception e) {
            source.sendFailure(Component.literal("Failed to get EMC: " + e.getMessage()));
            return 0;
        }
    }

    /**
     * Helper to get and display EMC for an item
     */
    private static int getEMCForItem(CommandSourceStack source, ItemStack stack) {
        String itemName = stack.getHoverName().getString();
        String itemId = BuiltInRegistries.ITEM.getKey(stack.getItem()).toString();

        if (ProjectECompat.hasEMC(stack)) {
            long emc = ProjectECompat.getEMCValue(stack);
            source.sendSuccess(() -> Component.literal(
                itemName + " (" + itemId + ") has EMC: " + emc
            ), false);
            return 1;
        } else {
            source.sendSuccess(() -> Component.literal(
                itemName + " (" + itemId + ") has no EMC value"
            ), false);
            return 0;
        }
    }

    /**
     * Clear the EMC cache
     */
    private static int clearCache(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();

        if (EMCCache.invalidateCache()) {
            source.sendSuccess(() -> Component.literal(
                "EMC cache cleared. Re-scan will occur on next world reload."
            ), true);
            CombinedPE.LOGGER.info("{} cleared EMC cache", source.getTextName());
            return 1;
        } else {
            source.sendSuccess(() -> Component.literal(
                "No cache file found (already clear or never scanned)"
            ), false);
            return 0;
        }
    }
}
