package net.mindoth.ancientmagicks.network;

import com.google.common.collect.Lists;
import net.mindoth.ancientmagicks.client.gui.GuiSpellWheel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.List;
import java.util.function.Supplier;

public class PacketReceiveWandData {

    public int size;
    public List<ItemStack> oldList = Lists.newArrayList();
    public List<ItemStack> itemList = Lists.newArrayList();
    public CompoundTag wandNbt;

    public PacketReceiveWandData(List<ItemStack> oldList, CompoundTag wandNbt) {
        this.size = oldList.size();
        this.oldList = oldList;
        this.wandNbt = wandNbt;
    }

    public PacketReceiveWandData(FriendlyByteBuf buf) {
        int size = buf.readVarInt();
        for ( int i = 0; i < size; i++ ) {
            this.itemList.add(buf.readItem());
        }
        this.wandNbt = buf.readNbt();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeVarInt(this.size);
        for ( ItemStack itemStack : this.oldList ) {
            buf.writeItem(itemStack);
        }
        buf.writeNbt(this.wandNbt);
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().enqueueWork(() -> GuiSpellWheel.open(this.itemList, this.wandNbt));
        contextSupplier.get().setPacketHandled(true);
    }
}
