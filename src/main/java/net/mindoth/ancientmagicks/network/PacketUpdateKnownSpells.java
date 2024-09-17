package net.mindoth.ancientmagicks.network;

import net.mindoth.ancientmagicks.item.SpellTabletItem;
import net.mindoth.ancientmagicks.network.capabilities.playerspell.ClientSpellData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;
import java.util.function.Supplier;

public class PacketUpdateKnownSpells {

    public CompoundTag spellTag;

    public PacketUpdateKnownSpells(CompoundTag spellTag) {
        this.spellTag = spellTag;
    }

    public PacketUpdateKnownSpells(FriendlyByteBuf buf) {
        this.spellTag = buf.readNbt();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeNbt(this.spellTag);
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().enqueueWork(() -> {
            if ( ClientSpellData.getKnownSpells() != null && this.spellTag.contains("am_secretspell") ) {
                final String spellString = this.spellTag.getString("am_secretspell");
                final SpellTabletItem item = (SpellTabletItem) ForgeRegistries.ITEMS.getValue(new ResourceLocation(spellString));
                if ( Objects.equals(ClientSpellData.getKnownSpells(), "") ) {
                    ClientSpellData.setKnownSpells(spellString);
                }
                else if ( !ClientSpellData.stringListToSpellList(ClientSpellData.getKnownSpells()).contains(item) ) {
                    ClientSpellData.setKnownSpells(ClientSpellData.getKnownSpells() + "," + spellString);
                }
            }
        });
        contextSupplier.get().setPacketHandled(true);
    }
}
