package net.mindoth.ancientmagicks.client.screen;

import net.mindoth.ancientmagicks.capabilities.playermagic.ClientMagicData;
import net.mindoth.ancientmagicks.config.ModClientConfig;
import net.mindoth.ancientmagicks.item.armor.ColorableMagickArmorItem;
import net.mindoth.ancientmagicks.item.castingitem.CastingItem;
import net.mindoth.ancientmagicks.registries.attribute.ModAttributes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class HudMana implements IGuiOverlay {

    public static final HudMana OVERLAY = new HudMana();
    private static final Minecraft MINECRAFT = Minecraft.getInstance();

    @Override
    public void render(ForgeGui gui, GuiGraphics graphics, float pt, int width, int height) {
        Player player = MINECRAFT.player;
        if ( player == null ) return;
        if ( !shouldDisplayMana() ) return;
        double maxMagick = player.getAttributeValue(ModAttributes.MAGICK.get());
        double currentMagick = ClientMagicData.getCurrentMana();
        String mana = (int)currentMagick + "/" + (int)maxMagick;
        int posX = (MINECRAFT.getWindow().getGuiScaledWidth() / 2) + 30 + ModClientConfig.MAGICK_BAR_X_OFFSET.get();
        int posY = MINECRAFT.getWindow().getGuiScaledHeight() - 48 + ModClientConfig.MAGICK_BAR_Y_OFFSET.get();
        if ( player.getAirSupply() != player.getMaxAirSupply()
                && ModClientConfig.MAGICK_BAR_X_OFFSET.get() == 0 && ModClientConfig.MAGICK_BAR_Y_OFFSET.get() == 0 ) posY -= 10;
        graphics.drawString(gui.getFont(), mana, posX, posY, 2744299);
    }

    private static boolean shouldDisplayMana() {
        Player player = MINECRAFT.player;
        ItemStack main = player.getMainHandItem();
        ItemStack off = player.getOffhandItem();
        return !(MINECRAFT.screen instanceof GuiSpellWheel || player.isSpectator() || player.isCreative())
                && (ClientMagicData.getCurrentMana() < player.getAttributeValue(ModAttributes.MAGICK.get())
                || CastingItem.isValidCastingItem(main) || CastingItem.isValidCastingItem(off) || isWearingMagicArmor(player));
    }

    private static boolean isWearingMagicArmor(Player player) {
        for ( ItemStack slot : player.getArmorSlots() ) if ( slot.getItem() instanceof ColorableMagickArmorItem) return true;
        return false;
    }
}
