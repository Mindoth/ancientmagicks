package net.mindoth.ancientmagicks.client.gui;

import net.mindoth.ancientmagicks.client.gui.inventory.AMBagContainer;
import net.mindoth.ancientmagicks.item.castingitem.AMBagType;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import javax.annotation.Nonnull;

public class GuiAMBag extends AbstractContainerScreen<AMBagContainer> {
    public GuiAMBag(AMBagContainer container, Inventory playerInventory, Component name) {
        super(container, playerInventory, name);

        AMBagType tier = container.getTier();
        this.GUI = tier.texture;
        this.imageWidth = tier.xSize;
        this.imageHeight = tier.ySize;
    }

    private final ResourceLocation GUI;

    @Override
    protected void init() {
        super.init();
    }

    @Override
    protected void renderBg(@Nonnull GuiGraphics gg, float partialTicks, int x, int y) {
        gg.blit(GUI, this.leftPos, this.topPos, 0,0, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);
    }

    @Override
    protected void renderLabels(@Nonnull GuiGraphics gg, int x, int y) {
        gg.drawString(font, this.title.getString(), 7,6,0x404040, false);
    }

    @Override
    public void render(@Nonnull GuiGraphics gg, int pMouseX, int pMouseY, float pPartialTicks) {
        this.renderBackground(gg);
        super.render(gg,pMouseX, pMouseY, pPartialTicks);
        this.renderTooltip(gg, pMouseX, pMouseY);
    }
}
