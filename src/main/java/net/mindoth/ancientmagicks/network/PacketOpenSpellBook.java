package net.mindoth.ancientmagicks.network;

import net.mindoth.ancientmagicks.client.screen.SpellBookScreen;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketOpenSpellBook {

    public ItemStack book;

    public PacketOpenSpellBook(ItemStack book) {
        this.book = book;
    }

    public PacketOpenSpellBook(FriendlyByteBuf buf) {
        this.book = buf.readItem();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeItem(this.book);
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().enqueueWork(() -> SpellBookScreen.open(this.book, 0));
        contextSupplier.get().setPacketHandled(true);
    }
}
