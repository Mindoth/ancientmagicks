package net.mindoth.ancientmagicks.event;

import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.capability.playermagic.PlayerMagickProvider;
import net.mindoth.ancientmagicks.item.castingitem.CastingItem;
import net.mindoth.ancientmagicks.network.ModNetwork;
import net.mindoth.ancientmagicks.network.PacketSyncClientMana;
import net.mindoth.ancientmagicks.registries.attribute.ModAttributes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AncientMagicks.MOD_ID)
public class MagickEvents {

    @SubscribeEvent
    public static void baseRegen(final TickEvent.LevelTickEvent event) {
        if ( event.phase != TickEvent.Phase.END || event.level.isClientSide ) return;
        event.level.players().stream().toList().forEach(player -> {
            if ( !(player instanceof ServerPlayer serverPlayer ) || player.isDeadOrDying() || player.isRemoved() ) return;
            serverPlayer.getCapability(PlayerMagickProvider.PLAYER_MAGICK).ifPresent(magic -> {
                final double maxMagick = serverPlayer.getAttributeValue(ModAttributes.MAGICK.get());
                final double currentMagick = magic.getCurrentMana();
                final double magickRegen = 6;
                if ( player.tickCount % 120 == 0 ) changeMagick(player, magickRegen);
                if ( currentMagick > maxMagick ) changeMagick(player, maxMagick - currentMagick);
            });
        });
    }

    //ANY CHANGES IN A PLAYER'S RESOURCE SHOULD BE DONE HERE
    public static void changeMagick(Entity entity, double addition) {
        if ( !(entity instanceof ServerPlayer serverPlayer) || serverPlayer.isRemoved() || (serverPlayer.isCreative() && addition < 0) ) return;
        serverPlayer.getCapability(PlayerMagickProvider.PLAYER_MAGICK).ifPresent(magic -> {
            final double maxMagick = serverPlayer.getAttributeValue(ModAttributes.MAGICK.get());
            final double currentMagick = magic.getCurrentMana();
            final double newMagick = Math.max(0.0D, Math.min(maxMagick, currentMagick + addition));
            magic.setCurrentMana(newMagick);
            ModNetwork.sendToPlayer(new PacketSyncClientMana(newMagick), serverPlayer);
        });
    }

    @SubscribeEvent
    public static void disableInteraction(final PlayerInteractEvent.EntityInteract event) {
        Player player = event.getEntity();
        if ( !CastingItem.getHeldCastingItem(player).isEmpty() ) event.setCanceled(true);
    }
}
