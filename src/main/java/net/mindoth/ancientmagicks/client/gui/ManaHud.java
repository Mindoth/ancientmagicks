package net.mindoth.ancientmagicks.client.gui;

import net.mindoth.ancientmagicks.item.castingitem.CastingItem;
import net.mindoth.ancientmagicks.network.capabilities.playermagic.ClientMagicData;
import net.mindoth.ancientmagicks.registries.attributes.AncientMagicksAttributes;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class ManaHud implements IGuiOverlay {

    public static final ManaHud OVERLAY = new ManaHud();
    private static final Minecraft MINECRAFT = Minecraft.getInstance();

    @Override
    public void render(ForgeGui gui, GuiGraphics graphics, float pt, int width, int height) {
        Player player = MINECRAFT.player;
        if ( player == null ) return;
        if ( !shouldDisplayMana(player) ) return;
        double maxMana = player.getAttributeValue(AncientMagicksAttributes.MAX_MANA.get());
        double currentMana = ClientMagicData.getCurrentMana();
        String mana = (int)currentMana + "/" + (int)maxMana;
        int posX = (MINECRAFT.getWindow().getGuiScaledWidth() / 2) + 30;
        int posY = MINECRAFT.getWindow().getGuiScaledHeight() - 48;
        graphics.drawString(gui.getFont(), mana, posX, posY, ChatFormatting.AQUA.getColor());
    }

    private static boolean shouldDisplayMana(Player player) {
        ItemStack main = MINECRAFT.player.getMainHandItem();
        ItemStack off = MINECRAFT.player.getOffhandItem();
        return !(MINECRAFT.screen instanceof GuiSpellWheel || player.isSpectator() || player.isCreative())
                && (ClientMagicData.getCurrentMana() < player.getAttributeValue(AncientMagicksAttributes.MAX_MANA.get())
                || CastingItem.isValidCastingItem(main) || CastingItem.isValidCastingItem(off));
    }
}
