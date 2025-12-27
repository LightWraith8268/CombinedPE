package com.riley.combinedpe.workbench;

import com.riley.combinedpe.CombinedPE;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

/**
 * Enhanced Workbench GUI Screen - Client-side rendering
 *
 * Uses vanilla Minecraft crafting table texture and layout for consistency
 */
public class EnhancedWorkbenchScreen extends AbstractContainerScreen<EnhancedWorkbenchMenu> {

    /**
     * Vanilla crafting table texture
     * Using the same texture as vanilla crafting table for consistent aesthetics
     */
    private static final ResourceLocation TEXTURE = ResourceLocation.withDefaultNamespace("textures/gui/container/crafting_table.png");

    public EnhancedWorkbenchScreen(EnhancedWorkbenchMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        // Match vanilla crafting table dimensions
        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    /**
     * Render the background texture
     */
    @Override
    protected void renderBg(@NotNull GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        // Render vanilla crafting table background
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;
        graphics.blit(TEXTURE, x, y, 0, 0, this.imageWidth, this.imageHeight);
    }

    /**
     * Render the entire screen (background + foreground + tooltips)
     */
    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        // Render background (darkened screen behind GUI)
        super.render(graphics, mouseX, mouseY, partialTick);

        // Render item tooltips when hovering
        this.renderTooltip(graphics, mouseX, mouseY);
    }
}
