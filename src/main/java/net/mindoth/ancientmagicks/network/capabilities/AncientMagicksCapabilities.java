package net.mindoth.ancientmagicks.network.capabilities;

import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.network.AncientMagicksNetwork;
import net.mindoth.ancientmagicks.network.PacketSyncClientSpell;
import net.mindoth.ancientmagicks.network.capabilities.playerspell.PlayerSpell;
import net.mindoth.ancientmagicks.network.capabilities.playerspell.PlayerSpellProvider;
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
            if ( !player.getCapability(PlayerSpellProvider.PLAYER_SPELL).isPresent() ) {
                event.addCapability(new ResourceLocation(AncientMagicks.MOD_ID, "am_spell"), new PlayerSpellProvider());
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerCreated(PlayerEvent.Clone event) {
        if ( event.isWasDeath() && event.getEntity() instanceof ServerPlayer serverPlayer ) {
            event.getOriginal().reviveCaps();
            event.getOriginal().getCapability(PlayerSpellProvider.PLAYER_SPELL).ifPresent(oldStore -> {
                event.getEntity().getCapability(PlayerSpellProvider.PLAYER_SPELL).ifPresent(newStore -> {
                    newStore.copyFrom(oldStore);
                    CompoundTag tag = new CompoundTag();
                    tag.putString("am_spell", oldStore.getCurrentSpell());
                    tag.putString("am_known_spells", oldStore.getKnownSpells());
                    tag.putBoolean("am_blue_rune", oldStore.getBlue());
                    tag.putBoolean("am_purple_rune", oldStore.getPurple());
                    tag.putBoolean("am_yellow_rune", oldStore.getYellow());
                    tag.putBoolean("am_green_rune", oldStore.getGreen());
                    tag.putBoolean("am_black_rune", oldStore.getBlack());
                    tag.putBoolean("am_white_rune", oldStore.getWhite());
                    tag.putBoolean("am_brown_rune", oldStore.getBrown());
                    tag.putBoolean("am_red_rune", oldStore.getRed());
                    AncientMagicksNetwork.sendToPlayer(new PacketSyncClientSpell(tag), serverPlayer);
                });
            });
            event.getOriginal().invalidateCaps();
        }
    }

    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.register(PlayerSpell.class);
    }
}
