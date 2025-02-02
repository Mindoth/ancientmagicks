package net.mindoth.ancientmagicks.network;

import net.mindoth.ancientmagicks.item.SpellStorageItem;
import net.mindoth.ancientmagicks.item.spell.abstractspell.SpellItem;
import net.mindoth.ancientmagicks.capabilities.playermagic.ClientMagicData;
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
            if ( ClientMagicData.getKnownSpells() != null && this.spellTag.contains(SpellStorageItem.AM_NEW_SPELL) ) {
                final String spellString = this.spellTag.getString(SpellStorageItem.AM_NEW_SPELL);
                final SpellItem item = (SpellItem) ForgeRegistries.ITEMS.getValue(new ResourceLocation(spellString));
                if ( Objects.equals(ClientMagicData.getKnownSpells(), "") ) {
                    ClientMagicData.setKnownSpells(spellString);
                }
                else if ( !ClientMagicData.stringListToSpellList(ClientMagicData.getKnownSpells()).contains(item) ) {
                    ClientMagicData.setKnownSpells(ClientMagicData.getKnownSpells() + "," + spellString);
                }
            }
        });
        contextSupplier.get().setPacketHandled(true);
    }
}
