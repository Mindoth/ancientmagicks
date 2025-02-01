package net.mindoth.ancientmagicks.client.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class AncientMagicksScreen extends Screen {

    protected AncientMagicksScreen(Component pTitle) {
        super(pTitle);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    public static void drawTexture(ResourceLocation resourceLocation, int x, int y, int u, int v, int w, int h, int fileWidth, int fileHeight, GuiGraphics graphics) {
        graphics.blit(resourceLocation, x, y, u, v, w, h, fileWidth, fileHeight);
    }

    protected void renderItem(GuiGraphics graphics, @Nullable ItemStack spell, ItemStack stack, int xPos, int yPos) {
        if ( spell != null ) graphics.renderItem(spell, xPos, yPos);
        else graphics.renderItem(stack, xPos, yPos);
        graphics.renderItemDecorations(this.font, stack, xPos, yPos);
    }
}
