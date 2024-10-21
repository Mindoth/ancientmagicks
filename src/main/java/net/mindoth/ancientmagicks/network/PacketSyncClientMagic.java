package net.mindoth.ancientmagicks.network;

import net.mindoth.ancientmagicks.network.capabilities.playermagic.ClientMagicData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketSyncClientMagic {

    public CompoundTag spellTag;

    public PacketSyncClientMagic(CompoundTag spellTag) {
        this.spellTag = spellTag;
    }

    public PacketSyncClientMagic(FriendlyByteBuf buf) {
        this.spellTag = buf.readNbt();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeNbt(this.spellTag);
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().enqueueWork(() -> {
            if ( this.spellTag.contains("am_spell") ) ClientMagicData.setCurrentSpell(this.spellTag.getString("am_spell"));
            else ClientMagicData.setCurrentSpell("minecraft:air");
            if ( this.spellTag.contains("am_known_spells") ) ClientMagicData.setKnownSpells(this.spellTag.getString("am_known_spells"));
            else ClientMagicData.setKnownSpells("");
        });
        contextSupplier.get().setPacketHandled(true);
    }
}
