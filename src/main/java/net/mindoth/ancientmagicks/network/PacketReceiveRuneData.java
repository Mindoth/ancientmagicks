package net.mindoth.ancientmagicks.network;

import net.mindoth.ancientmagicks.client.screen.GuiSpellWheel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketReceiveRuneData {

    public ItemStack book;

    public PacketReceiveRuneData(ItemStack book) {
        this.book = book;
    }

    public PacketReceiveRuneData(FriendlyByteBuf buf) {
        this.book = buf.readItem();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeItem(this.book);
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().enqueueWork(() -> GuiSpellWheel.open(this.book));
        contextSupplier.get().setPacketHandled(true);
    }
}
