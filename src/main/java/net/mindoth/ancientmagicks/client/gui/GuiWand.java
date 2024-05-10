package net.mindoth.ancientmagicks.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.mindoth.ancientmagicks.client.gui.inventory.WandContainer;
import net.mindoth.ancientmagicks.item.weapon.WandType;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nonnull;

public class GuiWand extends ContainerScreen<WandContainer> {
    public GuiWand(WandContainer container, PlayerInventory playerInventory, ITextComponent name) {
        super(container, playerInventory, name);

        WandType tier = container.getTier();
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
    protected void renderBg(@Nonnull MatrixStack matrixStack, float partialTicks, int x, int y) {
        RenderSystem.color4f(1.0f, 1.0f, 1.0f ,1.0f);
        this.getMinecraft().textureManager.bind(GUI);
        blit(matrixStack, leftPos, topPos, 0,0, imageWidth, imageHeight, imageWidth, imageHeight);
    }

    @Override
    protected void renderLabels(@Nonnull MatrixStack matrixStack, int x, int y) {
        this.font.draw(matrixStack, this.title.getString(), 7,6,0x404040);
    }

    @Override
    public void render(@Nonnull MatrixStack matrixStack, int p_render_1_, int p_render_2_, float p_render_3_) {
        this.renderBackground(matrixStack);
        super.render(matrixStack,p_render_1_, p_render_2_, p_render_3_);
        this.renderTooltip(matrixStack, p_render_1_, p_render_2_);
    }
}
