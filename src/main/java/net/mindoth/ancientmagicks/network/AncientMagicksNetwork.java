package net.mindoth.ancientmagicks.network;

import net.mindoth.ancientmagicks.AncientMagicks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class AncientMagicksNetwork {
    private static SimpleChannel CHANNEL;

    private static int packetId = 0;
    private static int id() {
        return packetId++;
    }

    public static void init() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(AncientMagicks.MOD_ID, "network"))
                .networkProtocolVersion(() -> "2.0.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        CHANNEL = net;

        net.messageBuilder(PacketSetSpell.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(PacketSetSpell::new)
                .encoder(PacketSetSpell::encode)
                .consumerMainThread(PacketSetSpell::handle)
                .add();

        net.messageBuilder(PacketSendRuneData.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(PacketSendRuneData::new)
                .encoder(PacketSendRuneData::encode)
                .consumerMainThread(PacketSendRuneData::handle)
                .add();

        net.messageBuilder(PacketReceiveRuneData.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(PacketReceiveRuneData::new)
                .encoder(PacketReceiveRuneData::encode)
                .consumerMainThread(PacketReceiveRuneData::handle)
                .add();

        net.messageBuilder(PacketOpenAncientTablet.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(PacketOpenAncientTablet::new)
                .encoder(PacketOpenAncientTablet::encode)
                .consumerMainThread(PacketOpenAncientTablet::handle)
                .add();

        net.messageBuilder(PacketOpenSpellBook.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(PacketOpenSpellBook::new)
                .encoder(PacketOpenSpellBook::encode)
                .consumerMainThread(PacketOpenSpellBook::handle)
                .add();

        net.messageBuilder(PacketSendCustomParticles.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(PacketSendCustomParticles::new)
                .encoder(PacketSendCustomParticles::encode)
                .consumerMainThread(PacketSendCustomParticles::handle)
                .add();

        net.messageBuilder(PacketSyncSpellCombos.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(PacketSyncSpellCombos::new)
                .encoder(PacketSyncSpellCombos::encode)
                .consumerMainThread(PacketSyncSpellCombos::handle)
                .add();

        net.messageBuilder(PacketSyncClientMagic.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(PacketSyncClientMagic::new)
                .encoder(PacketSyncClientMagic::encode)
                .consumerMainThread(PacketSyncClientMagic::handle)
                .add();

        net.messageBuilder(PacketSyncClientMana.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(PacketSyncClientMana::new)
                .encoder(PacketSyncClientMana::encode)
                .consumerMainThread(PacketSyncClientMana::handle)
                .add();

        net.messageBuilder(PacketUpdateKnownSpells.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(PacketUpdateKnownSpells::new)
                .encoder(PacketUpdateKnownSpells::encode)
                .consumerMainThread(PacketUpdateKnownSpells::handle)
                .add();

        net.messageBuilder(PacketItemActivationAnimation.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(PacketItemActivationAnimation::new)
                .encoder(PacketItemActivationAnimation::encode)
                .consumerMainThread(PacketItemActivationAnimation::handle)
                .add();

        net.messageBuilder(PacketRemoveSpellFromBook.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(PacketRemoveSpellFromBook::new)
                .encoder(PacketRemoveSpellFromBook::encode)
                .consumerMainThread(PacketRemoveSpellFromBook::handle)
                .add();
    }

    public static <MSG> void sendToServer(MSG message) {
        CHANNEL.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), message);
    }

    public static <MSG> void sendToPlayersTrackingEntity(MSG message, Entity entity) {
        sendToPlayersTrackingEntity(message, entity, false);
    }

    public static <MSG> void sendToPlayersTrackingEntity(MSG message, Entity entity, boolean sendToSource) {
        CHANNEL.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), message);
        if ( sendToSource && entity instanceof ServerPlayer serverPlayer ) sendToPlayer(message, serverPlayer);
    }
}
