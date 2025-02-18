package net.mindoth.ancientmagicks.network;

import net.mindoth.ancientmagicks.item.SpellBookItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

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
                ItemStack book = SpellBookItem.getSpellBookSlot(player);
                AncientMagicksNetwork.sendToPlayer(new PacketReceiveRuneData(book), player);
            }
        });
    }
}
