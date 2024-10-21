package net.mindoth.ancientmagicks.event;

import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.network.AncientMagicksNetwork;
import net.mindoth.ancientmagicks.network.PacketSyncClientMana;
import net.mindoth.ancientmagicks.network.capabilities.playermagic.PlayerMagicProvider;
import net.mindoth.ancientmagicks.registries.attributes.AncientMagicksAttributes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AncientMagicks.MOD_ID)
public class ManaEvents {

    @SubscribeEvent
    public static void baseManaRegen(final TickEvent.PlayerTickEvent event) {
        if ( event.phase != TickEvent.Phase.END ) return;
        Player player = event.player;
        if ( player.level().isClientSide ) return;
        final double manaRegen = player.getAttributeValue(AncientMagicksAttributes.MANA_REGEN.get());
        if ( !(player instanceof ServerPlayer serverPlayer ) || player.isRemoved() ) return;
        serverPlayer.getCapability(PlayerMagicProvider.PLAYER_MAGIC).ifPresent(magic -> {
            final double maxMana = serverPlayer.getAttributeValue(AncientMagicksAttributes.MAX_MANA.get());
            final double currentMana = magic.getCurrentMana();
            if ( player.tickCount % 20 == 0 && currentMana < maxMana ) changeMana(player, manaRegen);
        });
    }

    public static void changeMana(Player player, double addition) {
        if ( !(player instanceof ServerPlayer serverPlayer ) || player.isRemoved() || (player.isCreative() && addition < 0) ) return;
        serverPlayer.getCapability(PlayerMagicProvider.PLAYER_MAGIC).ifPresent(magic -> {
            final double maxMana = serverPlayer.getAttributeValue(AncientMagicksAttributes.MAX_MANA.get());
            final double currentMana = magic.getCurrentMana();
            magic.setCurrentMana(Math.max(0.0D, Math.min(maxMana, currentMana + addition)));
            AncientMagicksNetwork.sendToPlayer(new PacketSyncClientMana(magic.getCurrentMana()), serverPlayer);
        });
    }
}
