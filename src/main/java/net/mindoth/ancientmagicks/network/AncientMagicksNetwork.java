package net.mindoth.ancientmagicks.network;

import net.mindoth.ancientmagicks.AncientMagicks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;

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
                .consumer(PacketOpenWandGui::handle)
                .add();

        net.messageBuilder(PacketSetStaffSlot.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(PacketSetStaffSlot::new)
                .encoder(PacketSetStaffSlot::encode)
                .consumer(PacketSetStaffSlot::handle)
                .add();

        net.messageBuilder(PacketSendStaffData.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(PacketSendStaffData::new)
                .encoder(PacketSendStaffData::encode)
                .consumer(PacketSendStaffData::handle)
                .add();

        net.messageBuilder(PacketReceiveStaffData.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(PacketReceiveStaffData::new)
                .encoder(PacketReceiveStaffData::encode)
                .consumer(PacketReceiveStaffData::handle)
                .add();

        net.messageBuilder(PacketSendCustomParticles.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(PacketSendCustomParticles::new)
                .encoder(PacketSendCustomParticles::encode)
                .consumer(PacketSendCustomParticles::handle)
                .add();
    }

    public static <MSG> void sendToServer(MSG message) {
        CHANNEL.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayerEntity player) {
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), message);
    }

    public static void sendToNearby(World world, BlockPos pos, Object msg){
        if ( world instanceof ServerWorld ) {
            ServerWorld serverWorld = (ServerWorld)world;
            serverWorld.getChunkSource().chunkMap.getPlayers(new ChunkPos(pos), false)
                    .filter(p -> p.distanceToSqr(pos.getX(), pos.getY(), pos.getZ()) < 64 * 64)
                    .forEach(p -> CHANNEL.send(PacketDistributor.PLAYER.with(() -> p), msg));
        }
    }

    public static void sendToNearby(World world, Entity caster, Object msg) {
        sendToNearby(world, caster.blockPosition(), msg);
    }
}
