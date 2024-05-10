package net.mindoth.ancientmagicks.network;

import com.google.common.collect.Lists;
import net.mindoth.ancientmagicks.client.gui.GuiSpellWheel;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.List;
import java.util.function.Supplier;

public class PacketReceiveStaffData {

    public int size;
    public List<ItemStack> oldList = Lists.newArrayList();
    public List<ItemStack> itemList = Lists.newArrayList();
    public CompoundNBT nbt;

    public PacketReceiveStaffData(List<ItemStack> oldList, CompoundNBT nbt) {
        this.size = oldList.size();
        this.oldList = oldList;
        this.nbt = nbt;
    }

    public PacketReceiveStaffData(PacketBuffer buf) {
        int size = buf.readVarInt();
        for ( int i = 0; i < size; i++ ) {
            this.itemList.add(buf.readItem());
        }
        this.nbt = buf.readNbt();
    }

    public void encode(PacketBuffer buf) {
        buf.writeVarInt(this.size);
        for ( ItemStack itemStack : this.oldList ) {
            buf.writeItem(itemStack);
        }
        buf.writeNbt(this.nbt);
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().enqueueWork(() -> GuiSpellWheel.open(this.itemList, this.nbt));
        contextSupplier.get().setPacketHandled(true);
    }
}
