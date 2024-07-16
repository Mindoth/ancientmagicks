package net.mindoth.ancientmagicks.network;

import net.mindoth.ancientmagicks.item.ColorRuneItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketSyncSpellCombos {

    public CompoundTag comboTag;

    public PacketSyncSpellCombos(CompoundTag comboTag) {
        this.comboTag = comboTag;
    }

    public PacketSyncSpellCombos(FriendlyByteBuf buf) {
        this.comboTag = buf.readNbt();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeNbt(this.comboTag);
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().enqueueWork(() -> {
            ColorRuneItem.CURRENT_COMBO_MAP = ColorRuneItem.buildComboMap(this.comboTag.getString("am_combostring"));
        });
        contextSupplier.get().setPacketHandled(true);
    }
}
