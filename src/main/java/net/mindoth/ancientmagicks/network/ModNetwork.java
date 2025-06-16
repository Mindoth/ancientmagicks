package net.mindoth.ancientmagicks.network;

import net.mindoth.ancientmagicks.AncientMagicks;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModNetwork {
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

        net.messageBuilder(PacketEditColorCode.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(PacketEditColorCode::new)
                .encoder(PacketEditColorCode::encode)
                .consumerMainThread(PacketEditColorCode::handle)
                .add();

        net.messageBuilder(PacketDumpSpell.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(PacketDumpSpell::new)
                .encoder(PacketDumpSpell::encode)
                .consumerMainThread(PacketDumpSpell::handle)
                .add();

        net.messageBuilder(PacketCraftSpell.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(PacketCraftSpell::new)
                .encoder(PacketCraftSpell::encode)
                .consumerMainThread(PacketCraftSpell::handle)
                .add();

        net.messageBuilder(PacketAskToOpenSpellWheel.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(PacketAskToOpenSpellWheel::new)
                .encoder(PacketAskToOpenSpellWheel::encode)
                .consumerMainThread(PacketAskToOpenSpellWheel::handle)
                .add();

        net.messageBuilder(PacketOpenSpellWheel.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(PacketOpenSpellWheel::new)
                .encoder(PacketOpenSpellWheel::encode)
                .consumerMainThread(PacketOpenSpellWheel::handle)
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

        net.messageBuilder(PacketSyncClientMana.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(PacketSyncClientMana::new)
                .encoder(PacketSyncClientMana::encode)
                .consumerMainThread(PacketSyncClientMana::handle)
                .add();

        net.messageBuilder(PacketRemoveSpellFromBook.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(PacketRemoveSpellFromBook::new)
                .encoder(PacketRemoveSpellFromBook::encode)
                .consumerMainThread(PacketRemoveSpellFromBook::handle)
                .add();

        net.messageBuilder(PacketUpdateBookData.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(PacketUpdateBookData::new)
                .encoder(PacketUpdateBookData::encode)
                .consumerMainThread(PacketUpdateBookData::handle)
                .add();

        net.messageBuilder(PacketSwitchBookSlot.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(PacketSwitchBookSlot::new)
                .encoder(PacketSwitchBookSlot::encode)
                .consumerMainThread(PacketSwitchBookSlot::handle)
                .add();
    }

    public static <MSG> void sendToServer(MSG message) {
        CHANNEL.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        if ( player != null ) CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), message);
    }

    public static <MSG> void sendToPlayersTrackingEntity(MSG message, Entity entity) {
        if ( entity != null ) sendToPlayersTrackingEntity(message, entity, false);
    }

    public static <MSG> void sendToPlayersTrackingEntity(MSG message, Entity entity, boolean sendToSource) {
        if ( entity != null ) {
            CHANNEL.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), message);
            if ( sendToSource && entity instanceof ServerPlayer serverPlayer ) sendToPlayer(message, serverPlayer);
        }
    }

    public static <MSG> void sendToNearby(MSG message, Level world, Entity caster) {
        sendToNearby(message, world, caster.blockPosition());
    }

    public static <MSG> void sendToNearby(MSG message, Level level, Vec3 center) {
        if ( level instanceof ServerLevel serverLevel ) {
            BlockPos pos = new BlockPos(Mth.floor(center.x), Mth.floor(center.y), Mth.floor(center.z));
            serverLevel.getChunkSource().chunkMap.getPlayers(new ChunkPos(pos), false).stream()
                    .filter(p -> p.distanceToSqr(pos.getX(), pos.getY(), pos.getZ()) < 64 * 64)
                    .forEach(p -> sendToPlayer(message, p));
        }
    }

    public static <MSG> void sendToNearby(MSG message, Level level, BlockPos pos) {
        if ( level instanceof ServerLevel serverLevel ) {
            serverLevel.getChunkSource().chunkMap.getPlayers(new ChunkPos(pos), false).stream()
                    .filter(p -> p.distanceToSqr(pos.getX(), pos.getY(), pos.getZ()) < 64 * 64)
                    .forEach(p -> sendToPlayer(message, p));
        }
    }
}
