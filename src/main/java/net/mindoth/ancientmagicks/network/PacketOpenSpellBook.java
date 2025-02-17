package net.mindoth.ancientmagicks.network;

import com.google.common.collect.Lists;
import net.mindoth.ancientmagicks.client.screen.AncientTabletScreen;
import net.mindoth.ancientmagicks.client.screen.SpellBookScreen;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.List;
import java.util.function.Supplier;

public class PacketOpenSpellBook {

    public int size;
    public List<ItemStack> oldList = Lists.newArrayList();
    public List<ItemStack> itemList = Lists.newArrayList();

    public PacketOpenSpellBook(List<ItemStack> oldList) {
        this.size = oldList.size();
        this.oldList = oldList;
    }

    public PacketOpenSpellBook(FriendlyByteBuf buf) {
        int size = buf.readVarInt();
        for ( int i = 0; i < size; i++ ) this.itemList.add(buf.readItem());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeVarInt(this.size);
        for ( ItemStack itemStack : this.oldList ) buf.writeItem(itemStack);
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().enqueueWork(() -> SpellBookScreen.open(this.itemList, 0));
        contextSupplier.get().setPacketHandled(true);
    }
}
