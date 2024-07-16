package net.mindoth.ancientmagicks.network;

import com.google.common.collect.Lists;
import net.mindoth.ancientmagicks.client.gui.GuiSpellWheel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.List;
import java.util.function.Supplier;

public class PacketReceiveRuneData {

    public int size;
    public List<ItemStack> oldList = Lists.newArrayList();
    public List<ItemStack> itemList = Lists.newArrayList();
    public CompoundTag tag;
    public boolean isOffHand;

    public PacketReceiveRuneData(List<ItemStack> oldList, CompoundTag tag, boolean isOffHand) {
        this.size = oldList.size();
        this.oldList = oldList;
        this.tag = tag;
        this.isOffHand = isOffHand;
    }

    public PacketReceiveRuneData(FriendlyByteBuf buf) {
        int size = buf.readVarInt();
        for ( int i = 0; i < size; i++ ) {
            this.itemList.add(buf.readItem());
        }
        this.tag = buf.readNbt();
        this.isOffHand = buf.readBoolean();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeVarInt(this.size);
        for ( ItemStack itemStack : this.oldList ) {
            buf.writeItem(itemStack);
        }
        buf.writeNbt(this.tag);
        buf.writeBoolean(this.isOffHand);
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().enqueueWork(() -> {
            GuiSpellWheel.open(this.itemList, this.tag, this.isOffHand);
        });
        contextSupplier.get().setPacketHandled(true);
    }
}
