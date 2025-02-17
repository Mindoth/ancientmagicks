package net.mindoth.ancientmagicks.event;

import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.capabilities.playermagic.PlayerMagic;
import net.mindoth.ancientmagicks.capabilities.playermagic.PlayerMagicProvider;
import net.mindoth.ancientmagicks.item.ColorRuneItem;
import net.mindoth.ancientmagicks.network.AncientMagicksNetwork;
import net.mindoth.ancientmagicks.network.PacketSyncClientMagic;
import net.mindoth.ancientmagicks.network.PacketSyncClientMana;
import net.mindoth.ancientmagicks.network.PacketSyncSpellCombos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;

@Mod.EventBusSubscriber(modid = AncientMagicks.MOD_ID)
public class ServerEvents {

    public static final String TAG_NOT_FIRST_LOGIN = ("notFirstLogIn");

    @SubscribeEvent
    public static void onPlayerJoin(final PlayerEvent.PlayerLoggedInEvent event) {
        if ( event.getEntity().level().isClientSide ) return;
        Player player = event.getEntity();
        CompoundTag playerData = player.getPersistentData();
        CompoundTag data = playerData.getCompound(Player.PERSISTED_NBT_TAG);
        if ( event.getEntity() instanceof ServerPlayer serverPlayer ) {
            AncientMagicksNetwork.sendToPlayer(new PacketSyncSpellCombos(ColorRuneItem.CURRENT_COMBO_TAG), serverPlayer);
            serverPlayer.getCapability(PlayerMagicProvider.PLAYER_MAGIC).ifPresent(magic -> {
                CompoundTag tag = new CompoundTag();
                if ( magic.getCurrentSpell() != null ) tag.putString(PlayerMagic.AM_SPELL, magic.getCurrentSpell());
                else magic.setCurrentSpell("minecraft:air");
                if ( magic.getKnownSpells() != null ) tag.putString(PlayerMagic.AM_KNOWN_SPELLS, magic.getKnownSpells());
                else magic.setKnownSpells("");
                AncientMagicksNetwork.sendToPlayer(new PacketSyncClientMagic(tag), serverPlayer);
                if ( data.getBoolean(TAG_NOT_FIRST_LOGIN) ) AncientMagicksNetwork.sendToPlayer(new PacketSyncClientMana(magic.getCurrentMana()), serverPlayer);
                else MagickEvents.changeMana(serverPlayer, Integer.MIN_VALUE);
            });
        }

        //KEEP THIS LAST
        if ( !data.getBoolean(TAG_NOT_FIRST_LOGIN) ) {
            data.putBoolean(TAG_NOT_FIRST_LOGIN, true);
            playerData.put(Player.PERSISTED_NBT_TAG, data);
        }
    }

    @SubscribeEvent
    public static void onServerStart(final ServerStartingEvent event) {
        ServerLevel level = event.getServer().getLevel(ServerLevel.OVERWORLD);
        long seed = level.getSeed();
        Random seededRand = new Random(seed);
        AncientMagicks.createLists(seededRand);
    }
}
