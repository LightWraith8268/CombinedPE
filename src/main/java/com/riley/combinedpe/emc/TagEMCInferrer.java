package com.riley.combinedpe.emc;

import com.riley.combinedpe.CombinedPE;
import com.riley.combinedpe.integration.projecte.ProjectECompat;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.*;

/**
 * Infers EMC values for items based on tags
 *
 * For items without recipes, we can infer EMC from similar items with the same tags:
 * - Items with forge:ingots/copper tag → average EMC of other copper ingots
 * - Items with forge:gems/diamond tag → average EMC of other diamond gems
 * - Items with c:dusts tag → average EMC of other dusts
 *
 * This helps modded items get reasonable EMC values even without explicit recipes
 */
public class TagEMCInferrer {

    /**
     * Try to infer EMC for an item based on its tags
     * @param item The item to infer EMC for
     * @return Inferred EMC value, or 0.0 if no inference possible
     */
    public static double inferEMCFromTags(Item item) {
        ItemStack stack = new ItemStack(item);

        // Get all tags this item belongs to
        Set<TagKey<Item>> itemTags = getItemTags(item);

        if (itemTags.isEmpty()) {
            return 0.0;
        }

        // Try each tag and find the best EMC inference
        List<Double> tagEMCValues = new ArrayList<>();

        for (TagKey<Item> tag : itemTags) {
            double tagEMC = getAverageEMCForTag(tag, item);
            if (tagEMC > 0.0) {
                tagEMCValues.add(tagEMC);
                CombinedPE.LOGGER.debug("Tag {} suggests EMC {} for {}",
                    tag.location(), tagEMC, BuiltInRegistries.ITEM.getKey(item));
            }
        }

        if (tagEMCValues.isEmpty()) {
            return 0.0;
        }

        // Use the median value to avoid outliers
        // (average can be skewed by one very high/low value)
        Collections.sort(tagEMCValues);
        double inferredEMC;

        if (tagEMCValues.size() == 1) {
            inferredEMC = tagEMCValues.get(0);
        } else {
            int middleIndex = tagEMCValues.size() / 2;
            inferredEMC = tagEMCValues.get(middleIndex);
        }

        CombinedPE.LOGGER.info("Inferred EMC for {} from tags: {} (from {} tag suggestions)",
            BuiltInRegistries.ITEM.getKey(item), inferredEMC, tagEMCValues.size());

        return inferredEMC;
    }

    /**
     * Get average EMC value for all items in a tag (excluding the target item)
     */
    private static double getAverageEMCForTag(TagKey<Item> tag, Item excludeItem) {
        List<Double> emcValues = new ArrayList<>();

        // Iterate through all items in this tag
        for (Holder<Item> itemHolder : BuiltInRegistries.ITEM.getTagOrEmpty(tag)) {
            Item taggedItem = itemHolder.value();

            // Skip the item we're trying to infer for
            if (taggedItem == excludeItem) {
                continue;
            }

            // Check if this item has EMC
            if (ProjectECompat.hasEMC(taggedItem)) {
                long itemEMC = ProjectECompat.getEMCValue(taggedItem);
                emcValues.add((double) itemEMC);
            }
        }

        if (emcValues.isEmpty()) {
            return 0.0;
        }

        // Calculate average
        double sum = 0.0;
        for (double emc : emcValues) {
            sum += emc;
        }

        return sum / emcValues.size();
    }

    /**
     * Get all tags an item belongs to
     */
    private static Set<TagKey<Item>> getItemTags(Item item) {
        Set<TagKey<Item>> tags = new HashSet<>();

        // Check all registered tags
        for (TagKey<Item> tag : BuiltInRegistries.ITEM.getTagNames().toList()) {
            // Iterate through tag items to check if our item is present
            for (Holder<Item> holder : BuiltInRegistries.ITEM.getTagOrEmpty(tag)) {
                if (holder.value() == item) {
                    tags.add(tag);
                    break;
                }
            }
        }

        return tags;
    }

    /**
     * Check if a tag is suitable for EMC inference
     * Some tags are too broad (like "minecraft:planks") or not material-based
     */
    public static boolean isTagSuitableForInference(TagKey<Item> tag) {
        String tagPath = tag.location().getPath();

        // Good tags for inference (material-based)
        if (tagPath.contains("ingots")) return true;
        if (tagPath.contains("gems")) return true;
        if (tagPath.contains("dusts")) return true;
        if (tagPath.contains("nuggets")) return true;
        if (tagPath.contains("ores")) return true;
        if (tagPath.contains("raw_materials")) return true;
        if (tagPath.contains("storage_blocks")) return true;

        // Bad tags for inference (too broad or functional)
        if (tagPath.equals("planks")) return false;
        if (tagPath.equals("logs")) return false;
        if (tagPath.contains("tools")) return false;
        if (tagPath.contains("weapons")) return false;
        if (tagPath.contains("armor")) return false;

        // Default: allow for custom mod tags
        return true;
    }
}
