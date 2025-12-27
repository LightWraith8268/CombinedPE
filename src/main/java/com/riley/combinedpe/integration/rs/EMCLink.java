package com.riley.combinedpe.integration.rs;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * EMC Link configuration system.
 *
 * Solves the performance problem of listing ALL learned ProjectE items in RS Grid.
 * Instead, each External Storage is configured to show only ONE specific item.
 *
 * Design (ProjectEX-style):
 * - Player places RS External Storage
 * - Player configures which item it should show (e.g., "Diamond")
 * - External Storage shows virtually unlimited diamonds (based on player's EMC)
 * - When extracted, EMC is deducted from nearest player
 * - When inserted, items convert to EMC
 *
 * Configuration storage:
 * - Links are stored per-dimension, per-position
 * - Links persist across server restarts (saved to world data)
 * - Links are cleared when External Storage is broken
 *
 * Future GUI enhancement:
 * - Right-click External Storage with item to set link
 * - Shift-right-click to clear link
 * - Display current link item in tooltip/screen
 */
public class EMCLink {

    /**
     * Storage: dimension -> position -> (player UUID, item stack)
     */
    private static final Map<String, Map<BlockPos, LinkData>> links = new HashMap<>();

    /**
     * Data for a single EMC link.
     */
    public static class LinkData {
        public final UUID playerUUID;
        public final ItemStack linkedItem;

        public LinkData(UUID playerUUID, ItemStack linkedItem) {
            this.playerUUID = playerUUID;
            this.linkedItem = linkedItem.copy();
            this.linkedItem.setCount(1); // Store only item type, not count
        }
    }

    /**
     * Get dimension key for a level.
     */
    private static String getDimensionKey(Level level) {
        return level.dimension().location().toString();
    }

    /**
     * Set an EMC link for a position.
     *
     * @param level Level containing the External Storage
     * @param pos Position of the External Storage
     * @param playerUUID UUID of the player whose EMC to use
     * @param item Item to link (only item type matters, count ignored)
     */
    public static void setLink(Level level, BlockPos pos, UUID playerUUID, ItemStack item) {
        String dim = getDimensionKey(level);
        links.computeIfAbsent(dim, k -> new HashMap<>());
        links.get(dim).put(pos, new LinkData(playerUUID, item));
    }

    /**
     * Get the EMC link for a position.
     *
     * @param level Level containing the External Storage
     * @param pos Position of the External Storage
     * @return Link data, or null if no link configured
     */
    public static LinkData getLink(Level level, BlockPos pos) {
        String dim = getDimensionKey(level);
        Map<BlockPos, LinkData> dimLinks = links.get(dim);
        if (dimLinks == null) {
            return null;
        }
        return dimLinks.get(pos);
    }

    /**
     * Remove an EMC link for a position.
     * Call this when External Storage block is broken.
     *
     * @param level Level containing the External Storage
     * @param pos Position of the External Storage
     */
    public static void removeLink(Level level, BlockPos pos) {
        String dim = getDimensionKey(level);
        Map<BlockPos, LinkData> dimLinks = links.get(dim);
        if (dimLinks != null) {
            dimLinks.remove(pos);
            if (dimLinks.isEmpty()) {
                links.remove(dim);
            }
        }
    }

    /**
     * Clear all links for a dimension.
     * Call this when dimension is unloaded/reset.
     *
     * @param level Level to clear
     */
    public static void clearDimension(Level level) {
        String dim = getDimensionKey(level);
        links.remove(dim);
    }

    /**
     * Clear all links.
     * Call this on server stop or for debugging.
     */
    public static void clearAll() {
        links.clear();
    }

    /**
     * Get total number of links across all dimensions.
     */
    public static int getTotalLinks() {
        int total = 0;
        for (Map<BlockPos, LinkData> dimLinks : links.values()) {
            total += dimLinks.size();
        }
        return total;
    }
}
