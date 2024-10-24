package net.mindoth.ancientmagicks.network;

import net.mindoth.ancientmagicks.capabilities.playermagic.ClientMagicData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketSyncClientMana {

    public double mana;

    public PacketSyncClientMana(double mana) {
        this.mana = mana;
    }

    public PacketSyncClientMana(FriendlyByteBuf buf) {
        this.mana = buf.readDouble();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeDouble(this.mana);
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().enqueueWork(() -> ClientMagicData.setCurrentMana(this.mana));
        contextSupplier.get().setPacketHandled(true);
    }
}
