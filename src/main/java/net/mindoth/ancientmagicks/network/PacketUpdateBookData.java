package net.mindoth.ancientmagicks.network;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketUpdateBookData {

    public ItemStack book;
    public CompoundTag spellData;
    public int page;

    public PacketUpdateBookData(ItemStack book, CompoundTag spellData, int page) {
        this.book = book;
        this.spellData = spellData;
        this.page = page;
    }

    public PacketUpdateBookData(FriendlyByteBuf buf) {
        this.book = buf.readItem();
        this.spellData = buf.readNbt();
        this.page = buf.readInt();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeItem(this.book);
        buf.writeNbt(this.spellData);
        buf.writeInt(this.page);
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        contextSupplier.get().enqueueWork(() -> {
            if ( context.getSender() != null ) {
                ServerPlayer player = context.getSender();
                if ( player.getInventory().contains(this.book) ) {
                    ItemStack book = player.getInventory().getItem(player.getInventory().findSlotMatchingItem(this.book));

                    

                    AncientMagicksNetwork.sendToPlayer(new PacketOpenSpellBook(book, this.page), player);
                }
            }
        });
        contextSupplier.get().setPacketHandled(true);
    }
}
