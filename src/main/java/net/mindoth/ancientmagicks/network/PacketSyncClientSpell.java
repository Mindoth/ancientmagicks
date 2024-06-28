package net.mindoth.ancientmagicks.network;

import net.mindoth.ancientmagicks.network.capabilities.ClientSpellData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketSyncClientSpell {

    public CompoundTag spellTag;

    public PacketSyncClientSpell(CompoundTag spellTag) {
        this.spellTag = spellTag;
    }

    public PacketSyncClientSpell(FriendlyByteBuf buf) {
        this.spellTag = buf.readNbt();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeNbt(this.spellTag);
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().enqueueWork(() -> {
            if ( this.spellTag.contains("am_spell") ) ClientSpellData.set(this.spellTag.getString("am_spell"));
            else ClientSpellData.set("minecraft:air");
        });
        contextSupplier.get().setPacketHandled(true);
    }
}
