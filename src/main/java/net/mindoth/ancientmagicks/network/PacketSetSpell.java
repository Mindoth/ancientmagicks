package net.mindoth.ancientmagicks.network;

import net.mindoth.ancientmagicks.event.SpellCasting;
import net.mindoth.ancientmagicks.item.castingitem.CastingItem;
import net.mindoth.ancientmagicks.item.castingitem.SpellTabletItem;
import net.mindoth.ancientmagicks.network.capabilities.playerspell.ClientSpellData;
import net.mindoth.ancientmagicks.network.capabilities.playerspell.PlayerSpellProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.function.Supplier;

public class PacketSetSpell {
    public CompoundTag selectedItem;

    public PacketSetSpell(CompoundTag spellRune) {
        this.selectedItem = spellRune;
    }

    public PacketSetSpell(FriendlyByteBuf buf) {
        this.selectedItem = buf.readNbt();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeNbt(this.selectedItem);
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            if ( context.get().getSender() != null ) {
                ServerPlayer player = context.get().getSender();
                if ( SpellCasting.getHeldCastingItem(player).getItem() instanceof CastingItem ) {
                    ItemStack wand = SpellCasting.getHeldCastingItem(player);
                    if ( CastingItem.isValidCastingItem(wand) ) {
                        String spellString = this.selectedItem.getString("am_spell");
                        player.getCapability(PlayerSpellProvider.PLAYER_SPELL).ifPresent(spell -> {
                            List<SpellTabletItem> knownSpells = ClientSpellData.stringListToSpellList(spell.getKnownSpells());
                            SpellTabletItem spellTabletItem = (SpellTabletItem)ForgeRegistries.ITEMS.getValue(new ResourceLocation(spellString));
                            if ( knownSpells.contains(spellTabletItem) || player.isCreative() ) spell.setCurrentSpell(spellString);
                        });
                    }
                }
            }
        });
    }
}
