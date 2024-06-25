package net.mindoth.ancientmagicks.network;

import net.mindoth.ancientmagicks.item.ColorRuneItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketSyncSpellCombos {

    public CompoundTag tag;

    public PacketSyncSpellCombos(CompoundTag tag) {
        this.tag = tag;
    }

    public PacketSyncSpellCombos(FriendlyByteBuf buf) {
        this.tag = buf.readNbt();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeNbt(this.tag);
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().enqueueWork(() -> ColorRuneItem.buildClientComboMap(this.tag.getString("am_combostring")));
        contextSupplier.get().setPacketHandled(true);
    }
}
