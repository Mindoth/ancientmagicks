package net.mindoth.ancientmagicks.client.keybinds;

import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.client.gui.GuiSpellWheel;
import net.mindoth.ancientmagicks.item.weapon.CastingItem;
import net.mindoth.ancientmagicks.item.weapon.StaffItem;
import net.mindoth.ancientmagicks.item.weapon.WandItem;
import net.mindoth.ancientmagicks.network.PacketOpenWandGui;
import net.mindoth.ancientmagicks.network.PacketSendStaffData;
import net.mindoth.ancientmagicks.network.AncientMagicksNetwork;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = AncientMagicks.MOD_ID)
public class KeyHandler {

    private static final Minecraft MINECRAFT = Minecraft.getInstance();

    @SubscribeEvent
    public static void keyEvent(final InputEvent.KeyInputEvent event) {
        if ( MINECRAFT.player == null ) return;
        checkKeysPressed(event.getKey(), event.getAction());
    }

    public static void checkKeysPressed(int key, int keyAction) {
        ItemStack wand = WandItem.getHeldCastingItem(MINECRAFT.player);
        if ( key == AncientMagicksKeyBinds.spellSelector.getKey().getValue() && wand.getItem() instanceof CastingItem ) {
            if ( keyAction == 1 && MINECRAFT.screen == null ) {
                if ( wand.getItem() instanceof WandItem ) AncientMagicksNetwork.sendToServer(new PacketOpenWandGui());
                if ( wand.getItem() instanceof StaffItem ) AncientMagicksNetwork.sendToServer(new PacketSendStaffData());
            }
            else if ( keyAction == 0 && MINECRAFT.screen instanceof GuiSpellWheel ) MINECRAFT.player.closeContainer();
        }
    }
}
