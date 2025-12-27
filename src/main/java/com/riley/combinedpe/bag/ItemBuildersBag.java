package com.riley.combinedpe.bag;

import com.riley.combinedpe.registry.ModDataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

/**
 * Builder's Bag item - provides intelligent block storage and supply for building
 *
 * Features (tier-dependent):
 * - Supplier Module: Automatically provides blocks when building
 * - Container Module: Pulls blocks from external storage
 * - Crafting Module: Auto-crafts items when needed
 * - EMC Provider Module: Uses ProjectE EMC for block provision
 */
public class ItemBuildersBag extends Item {

    private final BagTier tier;

    public ItemBuildersBag(BagTier tier, Properties properties) {
        super(properties
            .stacksTo(1) // Bags don't stack
            .component(ModDataComponents.BAG_INVENTORY.get(), BagInventory.empty(tier.getCapacity()))
            .component(ModDataComponents.VIRTUAL_STACKS.get(), VirtualStackData.empty())
            .component(ModDataComponents.STACK_UPGRADE.get(), StackUpgradeTier.NONE)
        );
        this.tier = tier;
    }

    /**
     * Get the tier of this bag
     */
    public BagTier getTier() {
        return tier;
    }

    /**
     * Get the bag's inventory from the stack
     */
    public static BagInventory getInventory(ItemStack stack) {
        BagInventory inventory = stack.get(ModDataComponents.BAG_INVENTORY.get());
        if (inventory == null) {
            // Initialize empty inventory if missing
            ItemBuildersBag item = (ItemBuildersBag) stack.getItem();
            inventory = BagInventory.empty(item.tier.getCapacity());
            stack.set(ModDataComponents.BAG_INVENTORY.get(), inventory);
        }
        return inventory;
    }

    /**
     * Set the bag's inventory on the stack
     */
    public static void setInventory(ItemStack stack, BagInventory inventory) {
        stack.set(ModDataComponents.BAG_INVENTORY.get(), inventory);
    }

    /**
     * Handle right-click to open the bag
     */
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide) {
            // Open the bag GUI
            player.openMenu(new net.minecraft.world.MenuProvider() {
                @Override
                public @org.jetbrains.annotations.NotNull Component getDisplayName() {
                    return Component.literal(tier.getDisplayName() + " Builder's Bag");
                }

                @Override
                public @org.jetbrains.annotations.NotNull net.minecraft.world.inventory.AbstractContainerMenu createMenu(
                        int containerId,
                        net.minecraft.world.entity.player.Inventory playerInventory,
                        net.minecraft.world.entity.player.Player player) {
                    return new BagMenu(containerId, playerInventory, stack);
                }
            }, buf -> ItemStack.STREAM_CODEC.encode(buf, stack)); // Send bag stack to client
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    /**
     * Add tooltip information
     */
    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);

        // Tier info
        tooltipComponents.add(Component.literal("Tier: " + tier.getDisplayName())
            .withStyle(style -> style.withColor(tier.getColor())));

        // Capacity info
        BagInventory inventory = getInventory(stack);
        long itemCount = inventory.getTotalItemCount();
        String formattedCount = BagInventory.formatItemCount(itemCount);
        tooltipComponents.add(Component.literal(
            String.format("Capacity: %d slots (%s items)", tier.getCapacity(), formattedCount)
        ));

        // Stack upgrade info
        StackUpgradeTier upgrade = stack.getOrDefault(ModDataComponents.STACK_UPGRADE.get(), StackUpgradeTier.NONE);
        if (upgrade != StackUpgradeTier.NONE) {
            int maxPerSlot = tier.getMaxStackSize() * upgrade.getMultiplier();
            String maxFormatted = BagInventory.formatItemCount(maxPerSlot);
            tooltipComponents.add(Component.literal(
                String.format("Stack Upgrade: %s (%s/slot)", upgrade.getDisplayName(), maxFormatted)
            ).withStyle(style -> style.withColor(0x00FF00))); // Green
        } else {
            int maxPerSlot = tier.getMaxStackSize();
            String maxFormatted = BagInventory.formatItemCount(maxPerSlot);
            tooltipComponents.add(Component.literal(
                String.format("Max Stack: %s/slot", maxFormatted)
            ).withStyle(style -> style.withColor(0x808080))); // Gray
        }

        // Module info
        tooltipComponents.add(Component.literal(""));
        tooltipComponents.add(Component.literal("Modules:"));

        if (tier.hasSupplierModule()) {
            tooltipComponents.add(Component.literal("  ✓ Supplier"));
        }
        if (tier.hasContainerModule()) {
            tooltipComponents.add(Component.literal("  ✓ Container"));
        }
        if (tier.hasCraftingModule()) {
            tooltipComponents.add(Component.literal("  ✓ Crafting"));
        }
        if (tier.hasEMCProviderModule()) {
            tooltipComponents.add(Component.literal("  ✓ EMC Provider"));
        }
    }

    /**
     * Check if this stack is a Builder's Bag
     */
    public static boolean isBag(ItemStack stack) {
        return stack.getItem() instanceof ItemBuildersBag;
    }

    /**
     * Get the tier from a bag stack
     */
    public static BagTier getTier(ItemStack stack) {
        if (stack.getItem() instanceof ItemBuildersBag bag) {
            return bag.tier;
        }
        return BagTier.BASIC;
    }
}
