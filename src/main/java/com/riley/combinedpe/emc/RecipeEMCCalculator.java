package com.riley.combinedpe.emc;

import com.riley.combinedpe.CombinedPE;
import com.riley.combinedpe.Config;
import com.riley.combinedpe.integration.projecte.ProjectECompat;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import java.util.*;

/**
 * Calculates EMC values for items based on their recipes
 *
 * Supports:
 * - Crafting recipes (shaped, shapeless)
 * - Smelting recipes (furnace, blast furnace, smoker)
 * - Smithing recipes
 *
 * Algorithm:
 * 1. Sum EMC of all recipe ingredients
 * 2. Apply multiplier based on recipe type
 * 3. Divide by output count
 * 4. Return calculated EMC per item
 */
public class RecipeEMCCalculator {

    private final Level level;
    private final RegistryAccess registryAccess;
    private final RecipeManager recipeManager;

    // Cache calculated values to avoid recalculation
    private final Map<ItemStack, Long> calculatedEMC = new HashMap<>();

    // Track items currently being calculated (prevents infinite recursion)
    private final Set<ItemStack> calculationStack = new HashSet<>();

    public RecipeEMCCalculator(Level level) {
        this.level = level;
        this.registryAccess = level.registryAccess();
        this.recipeManager = level.getRecipeManager();
    }

    /**
     * Calculate EMC for an item based on all available recipes
     * @param output The item to calculate EMC for
     * @return Calculated EMC value, or 0 if no valid recipe found
     */
    public long calculateEMC(ItemStack output) {
        // Check cache first
        if (calculatedEMC.containsKey(output)) {
            return calculatedEMC.get(output);
        }

        // Check if already being calculated (prevent infinite recursion)
        if (calculationStack.contains(output)) {
            CombinedPE.LOGGER.warn("Circular recipe dependency detected for {}", output);
            return 0;
        }

        // If item already has EMC from ProjectE, use that
        if (ProjectECompat.hasEMC(output)) {
            long existingEMC = ProjectECompat.getEMCValue(output);
            calculatedEMC.put(output, existingEMC);
            return existingEMC;
        }

        calculationStack.add(output);

        try {
            // Try different recipe types in order of preference
            long emcValue = 0;

            // 1. Try crafting recipes first (most common)
            emcValue = calculateFromCraftingRecipes(output);

            // 2. If no crafting recipe, try smelting
            if (emcValue == 0) {
                emcValue = calculateFromSmeltingRecipes(output);
            }

            // 3. If no smelting recipe, try smithing
            if (emcValue == 0) {
                emcValue = calculateFromSmithingRecipes(output);
            }

            // Cache the result
            calculatedEMC.put(output, emcValue);

            if (emcValue > 0) {
                CombinedPE.LOGGER.debug("Calculated EMC for {}: {}", output, emcValue);
            }

            return emcValue;

        } finally {
            calculationStack.remove(output);
        }
    }

    /**
     * Calculate EMC from crafting recipes (shaped and shapeless)
     */
    private long calculateFromCraftingRecipes(ItemStack output) {
        List<RecipeHolder<CraftingRecipe>> recipes = recipeManager
            .getAllRecipesFor(RecipeType.CRAFTING);

        for (RecipeHolder<CraftingRecipe> recipeHolder : recipes) {
            CraftingRecipe recipe = recipeHolder.value();
            ItemStack result = recipe.getResultItem(registryAccess);

            // Check if this recipe produces our target item
            if (ItemStack.isSameItemSameComponents(result, output)) {
                long ingredientEMC = calculateIngredientEMC(recipe.getIngredients());

                if (ingredientEMC > 0) {
                    int outputCount = result.getCount();
                    double multiplier = Config.CRAFTING_MULTIPLIER.get();

                    long emcPerItem = (long) ((ingredientEMC * multiplier) / outputCount);
                    return emcPerItem;
                }
            }
        }

        return 0;
    }

    /**
     * Calculate EMC from smelting recipes (furnace, blast furnace, smoker)
     */
    private long calculateFromSmeltingRecipes(ItemStack output) {
        // Check all smelting recipe types
        RecipeType<?>[] smeltingTypes = {
            RecipeType.SMELTING,
            RecipeType.BLASTING,
            RecipeType.SMOKING
        };

        for (RecipeType<?> recipeType : smeltingTypes) {
            long emcValue = calculateFromSmeltingType(output, (RecipeType<SmeltingRecipe>) recipeType);
            if (emcValue > 0) {
                return emcValue;
            }
        }

        return 0;
    }

    @SuppressWarnings("unchecked")
    private long calculateFromSmeltingType(ItemStack output, RecipeType<SmeltingRecipe> recipeType) {
        List<RecipeHolder<SmeltingRecipe>> recipes = (List<RecipeHolder<SmeltingRecipe>>) (List<?>)
            recipeManager.getAllRecipesFor(recipeType);

        for (RecipeHolder<SmeltingRecipe> recipeHolder : recipes) {
            SmeltingRecipe recipe = recipeHolder.value();
            ItemStack result = recipe.getResultItem(registryAccess);

            if (ItemStack.isSameItemSameComponents(result, output)) {
                long ingredientEMC = calculateIngredientEMC(recipe.getIngredients());

                if (ingredientEMC > 0) {
                    int outputCount = result.getCount();
                    double multiplier = Config.SMELTING_MULTIPLIER.get();

                    long emcPerItem = (long) ((ingredientEMC * multiplier) / outputCount);
                    return emcPerItem;
                }
            }
        }

        return 0;
    }

    /**
     * Calculate EMC from smithing recipes
     */
    private long calculateFromSmithingRecipes(ItemStack output) {
        List<RecipeHolder<SmithingRecipe>> recipes = recipeManager
            .getAllRecipesFor(RecipeType.SMITHING);

        for (RecipeHolder<SmithingRecipe> recipeHolder : recipes) {
            SmithingRecipe recipe = recipeHolder.value();
            ItemStack result = recipe.getResultItem(registryAccess);

            if (ItemStack.isSameItemSameComponents(result, output)) {
                long ingredientEMC = calculateIngredientEMC(recipe.getIngredients());

                if (ingredientEMC > 0) {
                    int outputCount = result.getCount();
                    // Use crafting multiplier for smithing (no separate config yet)
                    double multiplier = Config.CRAFTING_MULTIPLIER.get();

                    long emcPerItem = (long) ((ingredientEMC * multiplier) / outputCount);
                    return emcPerItem;
                }
            }
        }

        return 0;
    }

    /**
     * Calculate total EMC of recipe ingredients
     */
    private long calculateIngredientEMC(List<Ingredient> ingredients) {
        long totalEMC = 0;

        for (Ingredient ingredient : ingredients) {
            // Get the first matching item from the ingredient
            ItemStack[] matchingStacks = ingredient.getItems();

            if (matchingStacks.length == 0) {
                // Empty ingredient, skip
                continue;
            }

            // Use the first matching stack
            ItemStack ingredientStack = matchingStacks[0];

            // Get EMC for this ingredient (may trigger recursive calculation)
            long ingredientEMC = 0;

            if (ProjectECompat.hasEMC(ingredientStack)) {
                // Item already has EMC from ProjectE
                ingredientEMC = ProjectECompat.getEMCValue(ingredientStack);
            } else {
                // Recursively calculate EMC
                ingredientEMC = calculateEMC(ingredientStack);
            }

            // If any ingredient has no EMC, recipe is invalid
            if (ingredientEMC == 0) {
                return 0;
            }

            totalEMC += ingredientEMC * ingredientStack.getCount();
        }

        return totalEMC;
    }

    /**
     * Clear all cached calculations
     */
    public void clearCache() {
        calculatedEMC.clear();
        calculationStack.clear();
    }

    /**
     * Get all calculated EMC values
     */
    public Map<ItemStack, Long> getCalculatedEMC() {
        return new HashMap<>(calculatedEMC);
    }
}
