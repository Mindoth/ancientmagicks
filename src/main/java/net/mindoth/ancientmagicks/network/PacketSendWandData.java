package net.mindoth.ancientmagicks.network;

import net.mindoth.ancientmagicks.registries.AncientMagicksItems;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class PacketSendWandData {

    public PacketSendWandData() {
    }

    public PacketSendWandData(FriendlyByteBuf buf) {
    }

    public void encode(FriendlyByteBuf buf) {
    }

    List<ItemStack> runeList = Arrays.asList(
            new ItemStack(AncientMagicksItems.BLUE_RUNE.get()),
            new ItemStack(AncientMagicksItems.PURPLE_RUNE.get()),
            new ItemStack(AncientMagicksItems.YELLOW_RUNE.get()),
            new ItemStack(AncientMagicksItems.GREEN_RUNE.get()),
            new ItemStack(AncientMagicksItems.WHITE_RUNE.get()),
            new ItemStack(AncientMagicksItems.BLACK_RUNE.get())
    );

    public boolean handle(Supplier<NetworkEvent.Context> contextSupplier) {
        //contextSupplier.get().getSender().refreshContainer(contextSupplier.get().getSender().containerMenu);
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            if ( context.getSender() != null ) {
                ServerPlayer player = context.getSender();
                player.stopUsingItem();
                AncientMagicksNetwork.sendToPlayer(new PacketReceiveWandData(runeList), player);
            }
        });
        return true;
    }
}
