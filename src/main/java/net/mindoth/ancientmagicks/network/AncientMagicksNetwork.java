package net.mindoth.ancientmagicks.network;

import net.mindoth.ancientmagicks.AncientMagicks;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
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

        net.messageBuilder(PacketOpenWandGui.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(PacketOpenWandGui::new)
                .encoder(PacketOpenWandGui::encode)
                .consumerMainThread(PacketOpenWandGui::handle)
                .add();

        net.messageBuilder(PacketSetSpellRune.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(PacketSetSpellRune::new)
                .encoder(PacketSetSpellRune::encode)
                .consumerMainThread(PacketSetSpellRune::handle)
                .add();

        net.messageBuilder(PacketSendWandData.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(PacketSendWandData::new)
                .encoder(PacketSendWandData::encode)
                .consumerMainThread(PacketSendWandData::handle)
                .add();

        net.messageBuilder(PacketReceiveWandData.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(PacketReceiveWandData::new)
                .encoder(PacketReceiveWandData::encode)
                .consumerMainThread(PacketReceiveWandData::handle)
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

        net.messageBuilder(PacketSyncSpellCombos.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(PacketSyncSpellCombos::new)
                .encoder(PacketSyncSpellCombos::encode)
                .consumerMainThread(PacketSyncSpellCombos::handle)
                .add();

        net.messageBuilder(PacketSyncClientSpell.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(PacketSyncClientSpell::new)
                .encoder(PacketSyncClientSpell::encode)
                .consumerMainThread(PacketSyncClientSpell::handle)
                .add();
    }

    public static <MSG> void sendToServer(MSG message) {
        CHANNEL.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), message);
    }

    public static void sendToNearby(Level world, BlockPos pos, Object msg){
        if ( world instanceof ServerLevel ) {
            ServerLevel serverWorld = (ServerLevel)world;
            serverWorld.getChunkSource().chunkMap.getPlayers(new ChunkPos(pos), false).stream()
                    .filter(p -> p.distanceToSqr(pos.getX(), pos.getY(), pos.getZ()) < 64 * 64)
                    .forEach(p -> CHANNEL.send(PacketDistributor.PLAYER.with(() -> p), msg));
        }
    }

    public static void sendToNearby(Level world, Entity caster, Object msg) {
        sendToNearby(world, caster.blockPosition(), msg);
    }
}
