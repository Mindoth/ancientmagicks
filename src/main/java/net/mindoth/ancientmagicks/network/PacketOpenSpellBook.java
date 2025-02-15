package net.mindoth.ancientmagicks.network;

import com.google.common.collect.Lists;
import net.mindoth.ancientmagicks.client.screen.AncientTabletScreen;
import net.mindoth.ancientmagicks.client.screen.SpellBookScreen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.List;
import java.util.function.Supplier;

public class PacketOpenSpellBook {

    public CompoundTag tag;
    public int size;
    public List<ItemStack> oldList = Lists.newArrayList();
    public List<ItemStack> itemList = Lists.newArrayList();

    public PacketOpenSpellBook(CompoundTag tag, List<ItemStack> oldList) {
        this.tag = tag;
        this.size = oldList.size();
        this.oldList = oldList;
    }

    public PacketOpenSpellBook(FriendlyByteBuf buf) {
        this.tag = buf.readNbt();
        int size = buf.readVarInt();
        for ( int i = 0; i < size; i++ ) this.itemList.add(buf.readItem());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeNbt(this.tag);
        buf.writeVarInt(this.size);
        for ( ItemStack itemStack : this.oldList ) buf.writeItem(itemStack);
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().enqueueWork(() -> SpellBookScreen.open(this.tag, this.itemList, 0));
        contextSupplier.get().setPacketHandled(true);
    }
}
