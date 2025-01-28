package net.mindoth.ancientmagicks.network;

import net.mindoth.ancientmagicks.item.ColorRuneItem;
import net.mindoth.ancientmagicks.item.castingitem.CastingItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.List;
import java.util.function.Supplier;

public class PacketSendRuneData {

    public PacketSendRuneData() {
    }

    public PacketSendRuneData(FriendlyByteBuf buf) {
    }

    public void encode(FriendlyByteBuf buf) {
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            if ( context.getSender() != null ) {
                ServerPlayer player = context.getSender();
                player.stopUsingItem();
                boolean isOffHand = !(player.getMainHandItem().getItem() instanceof CastingItem);
                List<ItemStack> runeList = ColorRuneItem.getColorRuneList(player);
                AncientMagicksNetwork.sendToPlayer(new PacketReceiveRuneData(runeList, isOffHand), player);
            }
        });
    }
}
