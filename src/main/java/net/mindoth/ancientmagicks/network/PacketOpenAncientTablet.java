package net.mindoth.ancientmagicks.network;

import com.google.common.collect.Lists;
import net.mindoth.ancientmagicks.client.screen.AncientTabletScreen;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.List;
import java.util.function.Supplier;

public class PacketOpenAncientTablet {

    public int size;
    public List<ItemStack> oldList = Lists.newArrayList();
    public List<ItemStack> itemList = Lists.newArrayList();

    public PacketOpenAncientTablet(List<ItemStack> oldList) {
        this.size = oldList.size();
        this.oldList = oldList;
    }

    public PacketOpenAncientTablet(FriendlyByteBuf buf) {
        int size = buf.readVarInt();
        for ( int i = 0; i < size; i++ ) this.itemList.add(buf.readItem());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeVarInt(this.size);
        for ( ItemStack itemStack : this.oldList ) buf.writeItem(itemStack);
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().enqueueWork(() -> AncientTabletScreen.open(this.itemList));
        contextSupplier.get().setPacketHandled(true);
    }
}
