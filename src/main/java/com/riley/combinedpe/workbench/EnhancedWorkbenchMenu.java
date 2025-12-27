package com.riley.combinedpe.workbench;

import com.riley.combinedpe.bag.BagInventory;
import com.riley.combinedpe.bag.ItemBuildersBag;
import com.riley.combinedpe.core.ModBlocks;
import com.riley.combinedpe.core.ModMenuTypes;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.NotNull;

/**
 * Enhanced Workbench Menu - Crafting menu that can access items from bags
 *
 * Works like vanilla crafting table but can pull ingredients from:
 * - Player inventory
 * - Any bag in player inventory
 */
public class EnhancedWorkbenchMenu extends AbstractContainerMenu {

    private final ContainerLevelAccess access;
    private final Player player;
    private final TransientCraftingContainer craftSlots;
    private final ResultContainer resultSlots;

    public EnhancedWorkbenchMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, ContainerLevelAccess.NULL);
    }

    public EnhancedWorkbenchMenu(int containerId, Inventory playerInventory, ContainerLevelAccess access) {
        super(ModMenuTypes.ENHANCED_WORKBENCH.get(), containerId);
        this.access = access;
        this.player = playerInventory.player;
        this.craftSlots = new TransientCraftingContainer(this, 3, 3);
        this.resultSlots = new ResultContainer();

        // Result slot (slot 0)
        this.addSlot(new ResultSlot(playerInventory.player, this.craftSlots, this.resultSlots, 0, 124, 35));

        // Crafting grid (slots 1-9)
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                this.addSlot(new Slot(this.craftSlots, col + row * 3, 30 + col * 18, 17 + row * 18));
            }
        }

        // Player inventory (slots 10-36)
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }

        // Player hotbar (slots 37-45)
        for (int col = 0; col < 9; col++) {
            this.addSlot(new Slot(playerInventory, col, 8 + col * 18, 142));
        }
    }

    /**
     * Called when crafting grid changes - updates result
     */
    @Override
    public void slotsChanged(@NotNull Container inventory) {
        this.access.execute((level, pos) -> {
            CraftingInput craftingInput = this.craftSlots.asCraftInput();
            ItemStack result = ItemStack.EMPTY;

            // Find matching recipe
            if (!level.isClientSide) {
                var recipeManager = level.getRecipeManager();
                var optional = recipeManager.getRecipeFor(RecipeType.CRAFTING, craftingInput, level);

                if (optional.isPresent()) {
                    RecipeHolder<CraftingRecipe> holder = optional.get();
                    CraftingRecipe recipe = holder.value();

                    if (this.player instanceof net.minecraft.server.level.ServerPlayer serverPlayer) {
                        if (this.resultSlots.setRecipeUsed(level, serverPlayer, holder)) {
                            result = recipe.assemble(craftingInput, level.registryAccess());
                        }
                    }
                }
            }

            this.resultSlots.setItem(0, result);
        });
    }

    /**
     * Called when result is taken - consumes ingredients
     */
    @Override
    public void removed(@NotNull Player player) {
        super.removed(player);
        this.access.execute((level, pos) -> {
            this.clearContainer(player, this.craftSlots);
        });
    }

    /**
     * Check if player can still use this workbench
     */
    @Override
    public boolean stillValid(@NotNull Player player) {
        return stillValid(this.access, player, ModBlocks.ENHANCED_WORKBENCH.get());
    }

    /**
     * Handle shift-clicking items
     */
    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        ItemStack result = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            result = slotStack.copy();

            if (index == 0) {
                // Result slot - move to player inventory
                this.access.execute((level, pos) -> {
                    slotStack.getItem().onCraftedBy(slotStack, level, player);
                });

                if (!this.moveItemStackTo(slotStack, 10, 46, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(slotStack, result);
            } else if (index >= 10 && index < 46) {
                // Player inventory - try to move to crafting grid
                if (!this.moveItemStackTo(slotStack, 1, 10, false)) {
                    if (index < 37) {
                        // Main inventory to hotbar
                        if (!this.moveItemStackTo(slotStack, 37, 46, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else {
                        // Hotbar to main inventory
                        if (!this.moveItemStackTo(slotStack, 10, 37, false)) {
                            return ItemStack.EMPTY;
                        }
                    }
                }
            } else if (!this.moveItemStackTo(slotStack, 10, 46, false)) {
                // Crafting grid to player inventory
                return ItemStack.EMPTY;
            }

            if (slotStack.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (slotStack.getCount() == result.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, slotStack);
            if (index == 0) {
                player.drop(slotStack, false);
            }
        }

        return result;
    }

    /**
     * Check if a stack can be placed in a slot
     */
    @Override
    public boolean canTakeItemForPickAll(@NotNull ItemStack stack, Slot slot) {
        return slot.container != this.resultSlots && super.canTakeItemForPickAll(stack, slot);
    }

    /**
     * Find an item in player inventory or bags
     * This is called by JEI and other systems to check ingredient availability
     */
    public boolean hasIngredient(ItemStack ingredient) {
        // Check player inventory
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (ItemStack.isSameItemSameComponents(stack, ingredient)) {
                return true;
            }
        }

        // Check all bags in player inventory
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (stack.getItem() instanceof ItemBuildersBag) {
                BagInventory bagInventory = ItemBuildersBag.getInventory(stack);
                for (int slot = 0; slot < bagInventory.getSlots(); slot++) {
                    ItemStack bagStack = bagInventory.getStackInSlot(slot);
                    if (ItemStack.isSameItemSameComponents(bagStack, ingredient)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * Get the crafting container for JEI integration
     */
    public CraftingContainer getCraftSlots() {
        return this.craftSlots;
    }
}
