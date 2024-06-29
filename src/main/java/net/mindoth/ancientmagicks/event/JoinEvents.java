package net.mindoth.ancientmagicks.event;

import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.item.ColorRuneItem;
import net.mindoth.ancientmagicks.network.AncientMagicksNetwork;
import net.mindoth.ancientmagicks.network.PacketSyncClientSpell;
import net.mindoth.ancientmagicks.network.PacketSyncSpellCombos;
import net.mindoth.ancientmagicks.network.capabilities.PlayerSpellProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AncientMagicks.MOD_ID)
public class JoinEvents {

    @SubscribeEvent
    public static void onPlayerJoin(final PlayerEvent.PlayerLoggedInEvent event) {
        if ( event.getEntity() instanceof ServerPlayer player ) {
            AncientMagicksNetwork.sendToPlayer(new PacketSyncSpellCombos(ColorRuneItem.CURRENT_COMBO_TAG), player);
            player.getCapability(PlayerSpellProvider.PLAYER_SPELL).ifPresent(spell -> {
                CompoundTag tag = new CompoundTag();
                if ( spell.getSpell() != null ) tag.putString("am_spell", spell.getSpell());
                else spell.setSpell("minecraft:air");
                AncientMagicksNetwork.sendToPlayer(new PacketSyncClientSpell(tag), player);
            });
        }
    }
}
