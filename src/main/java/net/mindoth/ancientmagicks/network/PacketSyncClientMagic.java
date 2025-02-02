package net.mindoth.ancientmagicks.network;

import net.mindoth.ancientmagicks.capabilities.playermagic.ClientMagicData;
import net.mindoth.ancientmagicks.capabilities.playermagic.PlayerMagic;
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
            if ( this.spellTag.contains(PlayerMagic.AM_SPELL) ) ClientMagicData.setCurrentSpell(this.spellTag.getString(PlayerMagic.AM_SPELL));
            else ClientMagicData.setCurrentSpell("minecraft:air");
            if ( this.spellTag.contains(PlayerMagic.AM_KNOWN_SPELLS) ) ClientMagicData.setKnownSpells(this.spellTag.getString(PlayerMagic.AM_KNOWN_SPELLS));
            else ClientMagicData.setKnownSpells("");
        });
        contextSupplier.get().setPacketHandled(true);
    }
}
