package net.mindoth.ancientmagicks.capabilities;

import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.event.MagickEvents;
import net.mindoth.ancientmagicks.network.AncientMagicksNetwork;
import net.mindoth.ancientmagicks.network.PacketSyncClientMagic;
import net.mindoth.ancientmagicks.capabilities.playermagic.PlayerMagic;
import net.mindoth.ancientmagicks.capabilities.playermagic.PlayerMagicProvider;
import net.mindoth.ancientmagicks.registries.attribute.AncientMagicksAttributes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AncientMagicks.MOD_ID)
public class AncientMagicksCapabilities {

    @SubscribeEvent
    public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
        if ( event.getObject() instanceof Player player ) {
            if ( !player.getCapability(PlayerMagicProvider.PLAYER_MAGIC).isPresent() ) {
                event.addCapability(new ResourceLocation(AncientMagicks.MOD_ID, "am_magic"), new PlayerMagicProvider());
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerCreated(PlayerEvent.Clone event) {
        if ( event.isWasDeath() && event.getEntity() instanceof ServerPlayer serverPlayer ) {
            event.getOriginal().reviveCaps();
            event.getOriginal().getCapability(PlayerMagicProvider.PLAYER_MAGIC).ifPresent(oldStore -> {
                event.getEntity().getCapability(PlayerMagicProvider.PLAYER_MAGIC).ifPresent(newStore -> {
                    newStore.copyFrom(oldStore);
                    CompoundTag tag = new CompoundTag();
                    tag.putString("am_spell", oldStore.getCurrentSpell());
                    tag.putString("am_known_spells", oldStore.getKnownSpells());
                    AncientMagicksNetwork.sendToPlayer(new PacketSyncClientMagic(tag), serverPlayer);
                    MagickEvents.changeMana(serverPlayer, serverPlayer.getAttributeValue(AncientMagicksAttributes.MP_MAX.get()));
                });
            });
            event.getOriginal().invalidateCaps();
        }
    }

    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.register(PlayerMagic.class);
    }
}
