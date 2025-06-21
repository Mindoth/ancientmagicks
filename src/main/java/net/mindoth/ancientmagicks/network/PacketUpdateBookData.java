package net.mindoth.ancientmagicks.network;

import com.google.common.collect.Lists;
import net.mindoth.ancientmagicks.item.SpellBookItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.List;
import java.util.function.Supplier;

public class PacketUpdateBookData {

    public ItemStack book;
    public int size;
    public List<ItemStack> scrollList = Lists.newArrayList();

    public PacketUpdateBookData(ItemStack book, List<ItemStack> scrollList) {
        this.book = book;
        this.size = scrollList.size();
        this.scrollList = scrollList;
    }

    public PacketUpdateBookData(FriendlyByteBuf buf) {
        this.book = buf.readItem();
        int size = buf.readVarInt();
        for ( int i = 0; i < size; i++ ) this.scrollList.add(buf.readItem());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeItem(this.book);
        buf.writeVarInt(this.size);
        for ( ItemStack stack : this.scrollList ) buf.writeItem(stack);
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        contextSupplier.get().enqueueWork(() -> {
            if ( context.getSender() != null ) {
                ServerPlayer player = context.getSender();
                if ( player.getInventory().contains(this.book) ) {
                    ItemStack book;
                    if ( ItemStack.isSameItemSameTags(player.getOffhandItem(), this.book) && !(player.getMainHandItem().getItem() instanceof SpellBookItem) ) book = player.getOffhandItem();
                    else book = player.getInventory().getItem(player.getInventory().findSlotMatchingItem(this.book));
                    book.setTag(SpellBookItem.constructBook(this.book, this.scrollList).getTag());
                }
            }
        });
        contextSupplier.get().setPacketHandled(true);
    }
}
