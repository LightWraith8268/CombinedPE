package com.riley.combinedpe.bag;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

/**
 * GUI screen for Builder's Bag with scrollable inventory
 * Supports massive inventories (up to 1,728 slots for Ultimate tier)
 */
public class BagScreen extends AbstractContainerScreen<BagMenu> {

    private static final int SLOTS_PER_ROW = 18;
    private static final int VISIBLE_ROWS = 6; // Configurable later
    private static final int SLOT_SIZE = 18;
    private static final int SCROLLBAR_WIDTH = 12;
    private static final int PADDING = 8;

    // Scrolling state
    private float scrollOffset = 0.0F;
    private boolean isScrolling = false;
    private boolean needsScrollbar = false;

    public BagScreen(BagMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void init() {
        super.init();

        int totalRows = getTotalRows();
        this.needsScrollbar = totalRows > VISIBLE_ROWS;

        // Calculate GUI dimensions
        this.imageWidth = (SLOTS_PER_ROW * SLOT_SIZE) + (PADDING * 2) + (needsScrollbar ? SCROLLBAR_WIDTH : 0);
        this.imageHeight = calculateHeight();

        // Center on screen
        this.leftPos = (this.width - this.imageWidth) / 2;
        this.topPos = (this.height - this.imageHeight) / 2;
    }

    private int getTotalRows() {
        return (int) Math.ceil(menu.getTotalSlots() / (double) SLOTS_PER_ROW);
    }

    private int calculateHeight() {
        // Header + visible bag slots + padding + player inventory + hotbar + padding
        int headerHeight = 18;
        int bagSlotsHeight = VISIBLE_ROWS * SLOT_SIZE;
        int playerInventoryHeight = (3 * SLOT_SIZE) + SLOT_SIZE + 4; // 3 rows + hotbar + gap
        int bottomPadding = 8;

        return headerHeight + bagSlotsHeight + 14 + playerInventoryHeight + bottomPadding;
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        // Render background
        this.renderBackground(graphics, mouseX, mouseY, partialTick);

        // Render container
        super.render(graphics, mouseX, mouseY, partialTick);

        // Render tooltips
        this.renderTooltip(graphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        // Draw main background
        graphics.fill(leftPos, topPos, leftPos + imageWidth, topPos + imageHeight, 0xFFC6C6C6);

        // Draw header
        renderHeader(graphics);

        // Draw bag inventory area
        renderBagInventory(graphics, mouseX, mouseY);

        // Draw scrollbar if needed
        if (needsScrollbar) {
            renderScrollbar(graphics);
        }

        // Draw player inventory background
        renderPlayerInventory(graphics);
    }

    private void renderHeader(GuiGraphics graphics) {
        int headerY = topPos + 6;

        // Draw title
        Component title = Component.literal(menu.getTier().getDisplayName() + " Builder's Bag")
                .withStyle(style -> style.withColor(menu.getTier().getColor()));

        graphics.drawString(this.font, title, leftPos + PADDING, headerY, 0x404040, false);

        // Draw capacity info on right
        long itemCount = menu.getBagInventory().getTotalItemCount();
        String formattedCount = BagInventory.formatItemCount(itemCount);
        String capacityText = menu.getTotalSlots() + " slots (" + formattedCount + " items)";
        int textWidth = this.font.width(capacityText);

        graphics.drawString(this.font, capacityText,
                leftPos + imageWidth - textWidth - PADDING - (needsScrollbar ? SCROLLBAR_WIDTH : 0),
                headerY, 0x808080, false);
    }

    private void renderBagInventory(GuiGraphics graphics, int mouseX, int mouseY) {
        int bagAreaX = leftPos + PADDING;
        int bagAreaY = topPos + 18;
        int bagAreaWidth = SLOTS_PER_ROW * SLOT_SIZE;
        int bagAreaHeight = VISIBLE_ROWS * SLOT_SIZE;

        // Draw background for bag slots
        graphics.fill(bagAreaX, bagAreaY, bagAreaX + bagAreaWidth, bagAreaY + bagAreaHeight, 0xFF8B8B8B);

        // Enable scissor for clipping
        graphics.enableScissor(bagAreaX, bagAreaY, bagAreaX + bagAreaWidth, bagAreaY + bagAreaHeight);

        // Render visible slots
        renderVisibleSlots(graphics, bagAreaX, bagAreaY);

        // Disable scissor
        graphics.disableScissor();
    }

    private void renderVisibleSlots(GuiGraphics graphics, int bagAreaX, int bagAreaY) {
        int totalRows = getTotalRows();
        int firstVisibleRow = (int) (scrollOffset * Math.max(0, totalRows - VISIBLE_ROWS));

        for (int row = 0; row < VISIBLE_ROWS; row++) {
            int actualRow = firstVisibleRow + row;
            if (actualRow >= totalRows) break;

            for (int col = 0; col < SLOTS_PER_ROW; col++) {
                int slotIndex = actualRow * SLOTS_PER_ROW + col;
                if (slotIndex >= menu.getTotalSlots()) break;

                int slotX = bagAreaX + col * SLOT_SIZE;
                int slotY = bagAreaY + row * SLOT_SIZE;

                // Draw slot background
                graphics.fill(slotX, slotY, slotX + 16, slotY + 16, 0xFF373737);
                graphics.fill(slotX, slotY, slotX + 16, slotY + 1, 0xFF8B8B8B); // Top edge
                graphics.fill(slotX, slotY, slotX + 1, slotY + 16, 0xFF8B8B8B); // Left edge
            }
        }
    }

    private void renderScrollbar(GuiGraphics graphics) {
        int scrollbarX = leftPos + imageWidth - SCROLLBAR_WIDTH - 4;
        int scrollbarY = topPos + 18;
        int scrollbarHeight = VISIBLE_ROWS * SLOT_SIZE;

        // Draw scrollbar track
        graphics.fill(scrollbarX, scrollbarY, scrollbarX + SCROLLBAR_WIDTH, scrollbarY + scrollbarHeight, 0xFF8B8B8B);

        // Draw scrollbar thumb
        int totalRows = getTotalRows();
        int thumbHeight = Math.max(20, scrollbarHeight * VISIBLE_ROWS / totalRows);
        int thumbY = scrollbarY + (int) (scrollOffset * (scrollbarHeight - thumbHeight));

        int thumbColor = isScrolling ? 0xFFFFFFFF : 0xFFC0C0C0;
        graphics.fill(scrollbarX + 1, thumbY, scrollbarX + SCROLLBAR_WIDTH - 1, thumbY + thumbHeight, thumbColor);
    }

    private void renderPlayerInventory(GuiGraphics graphics) {
        int playerInvY = topPos + 18 + (VISIBLE_ROWS * SLOT_SIZE) + 14;

        // Draw background for player inventory
        graphics.fill(leftPos + PADDING, playerInvY,
                leftPos + PADDING + (9 * SLOT_SIZE), playerInvY + (4 * SLOT_SIZE) + 4, 0xFF8B8B8B);

        // Draw "Inventory" label
        graphics.drawString(this.font, Component.literal("Inventory"),
                leftPos + PADDING, playerInvY - 10, 0x404040, false);
    }

    @Override
    protected void renderLabels(@NotNull GuiGraphics graphics, int mouseX, int mouseY) {
        // Don't draw default labels (we handle them custom)
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (!needsScrollbar) {
            return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
        }

        // Scroll bag inventory
        int totalRows = getTotalRows();
        float scrollAmount = (float) scrollY / (totalRows - VISIBLE_ROWS);

        this.scrollOffset = Mth.clamp(this.scrollOffset - scrollAmount, 0.0F, 1.0F);
        return true;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (!needsScrollbar) {
            return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
        }

        if (button == 0 && isScrolling) {
            // Update scroll based on drag
            int scrollbarY = topPos + 18;
            int scrollbarHeight = VISIBLE_ROWS * SLOT_SIZE;
            int totalRows = getTotalRows();
            int thumbHeight = Math.max(20, scrollbarHeight * VISIBLE_ROWS / totalRows);

            float scrollableHeight = scrollbarHeight - thumbHeight;
            float newOffset = (float) (mouseY - scrollbarY - (thumbHeight / 2.0)) / scrollableHeight;

            this.scrollOffset = Mth.clamp(newOffset, 0.0F, 1.0F);
            return true;
        }

        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (needsScrollbar && button == 0) {
            int scrollbarX = leftPos + imageWidth - SCROLLBAR_WIDTH - 4;
            int scrollbarY = topPos + 18;
            int scrollbarHeight = VISIBLE_ROWS * SLOT_SIZE;

            // Check if clicking on scrollbar
            if (mouseX >= scrollbarX && mouseX <= scrollbarX + SCROLLBAR_WIDTH &&
                mouseY >= scrollbarY && mouseY <= scrollbarY + scrollbarHeight) {
                this.isScrolling = true;
                return true;
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0) {
            this.isScrolling = false;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }
}
