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

    // Cache calculated values to avoid recalculation (using double for fractional precision)
    private final Map<ItemStack, Double> calculatedEMC = new HashMap<>();

    // Track items currently being calculated (prevents infinite recursion)
    private final Set<ItemStack> calculationStack = new HashSet<>();

    // Minimum EMC value for any calculated item
    // Any item with calculated EMC < 1.0 gets rounded up to 1
    private static final double MIN_EMC_VALUE = 1.0;

    public RecipeEMCCalculator(Level level) {
        this.level = level;
        this.registryAccess = level.registryAccess();
        this.recipeManager = level.getRecipeManager();
    }

    /**
     * Calculate EMC for an item based on all available recipes
     * @param output The item to calculate EMC for
     * @return Calculated EMC value (double for fractional precision), or 0 if no valid recipe found
     */
    public double calculateEMC(ItemStack output) {
        // Check cache first
        if (calculatedEMC.containsKey(output)) {
            return calculatedEMC.get(output);
        }

        // Check if already being calculated (prevent infinite recursion)
        if (calculationStack.contains(output)) {
            CombinedPE.LOGGER.warn("Circular recipe dependency detected for {}", output);
            return 0.0;
        }

        // If item already has EMC from ProjectE, use that
        if (ProjectECompat.hasEMC(output)) {
            double existingEMC = ProjectECompat.getEMCValue(output);
            calculatedEMC.put(output, existingEMC);
            return existingEMC;
        }

        calculationStack.add(output);

        try {
            // Try different recipe types in order of preference
            double emcValue = 0.0;

            // 1. Try crafting recipes first (most common)
            emcValue = calculateFromCraftingRecipes(output);

            // 2. If no crafting recipe, try smelting
            if (emcValue == 0.0) {
                emcValue = calculateFromSmeltingRecipes(output);
            }

            // 3. If no smelting recipe, try smithing
            if (emcValue == 0.0) {
                emcValue = calculateFromSmithingRecipes(output);
            }

            // Apply minimum EMC value - anything below 1.0 gets rounded to 1.0
            if (emcValue > 0.0 && emcValue < MIN_EMC_VALUE) {
                CombinedPE.LOGGER.debug("EMC for {} below minimum ({} < {}), rounding to {}",
                    output, emcValue, MIN_EMC_VALUE, MIN_EMC_VALUE);
                emcValue = MIN_EMC_VALUE;
            }

            // Cache the result
            calculatedEMC.put(output, emcValue);

            if (emcValue > 0.0) {
                CombinedPE.LOGGER.debug("Calculated EMC for {}: {}", output, emcValue);
            }

            return emcValue;

        } finally {
            calculationStack.remove(output);
        }
    }

    /**
     * Get EMC value as long (for ProjectE API compatibility)
     * Rounds the double value to nearest long
     */
    public long calculateEMCAsLong(ItemStack output) {
        return Math.round(calculateEMC(output));
    }

    /**
     * Calculate EMC from crafting recipes (shaped and shapeless)
     */
    private double calculateFromCraftingRecipes(ItemStack output) {
        List<RecipeHolder<CraftingRecipe>> recipes = recipeManager
            .getAllRecipesFor(RecipeType.CRAFTING);

        for (RecipeHolder<CraftingRecipe> recipeHolder : recipes) {
            CraftingRecipe recipe = recipeHolder.value();
            ItemStack result = recipe.getResultItem(registryAccess);

            // Check if this recipe produces our target item
            if (ItemStack.isSameItemSameComponents(result, output)) {
                double ingredientEMC = calculateIngredientEMC(recipe.getIngredients());

                if (ingredientEMC > 0.0) {
                    int outputCount = result.getCount();
                    double multiplier = Config.CRAFTING_MULTIPLIER.get();

                    double emcPerItem = (ingredientEMC * multiplier) / outputCount;
                    return emcPerItem;
                }
            }
        }

        return 0.0;
    }

    /**
     * Calculate EMC from smelting recipes (furnace, blast furnace, smoker)
     * Uses generic Recipe handling to support modded recipe types
     */
    private double calculateFromSmeltingRecipes(ItemStack output) {
        // Check all smelting recipe types
        RecipeType<?>[] smeltingTypes = {
            RecipeType.SMELTING,
            RecipeType.BLASTING,
            RecipeType.SMOKING
        };

        for (RecipeType<?> recipeType : smeltingTypes) {
            double emcValue = calculateFromSmeltingType(output, recipeType);
            if (emcValue > 0.0) {
                return emcValue;
            }
        }

        return 0.0;
    }

    /**
     * Calculate EMC from a specific smelting recipe type
     * Uses generic Recipe handling to support modded recipe types (like Malum's MetalNodeBlastingRecipe)
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private double calculateFromSmeltingType(ItemStack output, RecipeType<?> recipeType) {
        // Get all recipes for this type (use raw type to handle wildcard properly)
        List<RecipeHolder> recipes = recipeManager.getAllRecipesFor((RecipeType) recipeType);

        for (RecipeHolder recipeHolder : recipes) {
            try {
                Recipe<?> recipe = recipeHolder.value();
                ItemStack result = recipe.getResultItem(registryAccess);

                if (ItemStack.isSameItemSameComponents(result, output)) {
                    // Check if recipe has ingredients (skip if not)
                    List<Ingredient> ingredients = recipe.getIngredients();
                    if (ingredients == null || ingredients.isEmpty()) {
                        continue;
                    }

                    double ingredientEMC = calculateIngredientEMC(ingredients);

                    if (ingredientEMC > 0.0) {
                        int outputCount = result.getCount();
                        double multiplier = Config.SMELTING_MULTIPLIER.get();

                        double emcPerItem = (ingredientEMC * multiplier) / outputCount;
                        return emcPerItem;
                    }
                }
            } catch (Exception e) {
                // Log and skip recipes that cause exceptions (e.g., incompatible modded recipes)
                CombinedPE.LOGGER.warn("Skipping recipe due to error: {}", e.getMessage());
                continue;
            }
        }

        return 0.0;
    }

    /**
     * Calculate EMC from smithing recipes
     */
    private double calculateFromSmithingRecipes(ItemStack output) {
        List<RecipeHolder<SmithingRecipe>> recipes = recipeManager
            .getAllRecipesFor(RecipeType.SMITHING);

        for (RecipeHolder<SmithingRecipe> recipeHolder : recipes) {
            SmithingRecipe recipe = recipeHolder.value();
            ItemStack result = recipe.getResultItem(registryAccess);

            if (ItemStack.isSameItemSameComponents(result, output)) {
                double ingredientEMC = calculateIngredientEMC(recipe.getIngredients());

                if (ingredientEMC > 0.0) {
                    int outputCount = result.getCount();
                    // Use crafting multiplier for smithing (no separate config yet)
                    double multiplier = Config.CRAFTING_MULTIPLIER.get();

                    double emcPerItem = (ingredientEMC * multiplier) / outputCount;
                    return emcPerItem;
                }
            }
        }

        return 0.0;
    }

    /**
     * Calculate total EMC of recipe ingredients
     */
    private double calculateIngredientEMC(List<Ingredient> ingredients) {
        double totalEMC = 0.0;

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
            double ingredientEMC = 0.0;

            if (ProjectECompat.hasEMC(ingredientStack)) {
                // Item already has EMC from ProjectE
                ingredientEMC = ProjectECompat.getEMCValue(ingredientStack);
            } else {
                // Recursively calculate EMC
                ingredientEMC = calculateEMC(ingredientStack);
            }

            // If any ingredient has no EMC, recipe is invalid
            if (ingredientEMC == 0.0) {
                return 0.0;
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
     * Get all calculated EMC values (as doubles for precision)
     */
    public Map<ItemStack, Double> getCalculatedEMC() {
        return new HashMap<>(calculatedEMC);
    }
}
