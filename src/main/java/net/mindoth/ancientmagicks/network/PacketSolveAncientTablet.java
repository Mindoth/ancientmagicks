package net.mindoth.ancientmagicks.network;

import net.mindoth.ancientmagicks.item.AncientTabletItem;
import net.mindoth.ancientmagicks.item.castingitem.SpellTabletItem;
import net.mindoth.ancientmagicks.network.capabilities.playerspell.ClientSpellData;
import net.mindoth.ancientmagicks.network.capabilities.playerspell.PlayerSpellProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;
import java.util.function.Supplier;

public class PacketSolveAncientTablet {

    public ItemStack resulStack;
    public boolean isOffHand;

    public PacketSolveAncientTablet(ItemStack resulStack, boolean isOffHand) {
        this.resulStack = resulStack;
        this.isOffHand = isOffHand;
    }

    public PacketSolveAncientTablet(FriendlyByteBuf buf) {
        this.resulStack = buf.readItem();
        this.isOffHand = buf.readBoolean();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeItem(this.resulStack);
        buf.writeBoolean(this.isOffHand);
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            if ( context.getSender() != null ) {
                ServerPlayer player = context.getSender();
                EquipmentSlot hand = this.isOffHand ? EquipmentSlot.OFFHAND : EquipmentSlot.MAINHAND;
                ItemStack handTablet = player.getItemBySlot(hand);
                if ( handTablet.getItem() instanceof AncientTabletItem && handTablet.hasTag() ) {
                    CompoundTag tag = handTablet.getTag();
                    if ( tag != null && tag.contains("am_secretspell") ) {
                        final String spellString = tag.getString("am_secretspell");
                        final SpellTabletItem item = (SpellTabletItem)ForgeRegistries.ITEMS.getValue(new ResourceLocation(spellString));
                        if ( this.resulStack.getItem() == item ) player.setItemSlot(hand, this.resulStack);
                        player.getCapability(PlayerSpellProvider.PLAYER_SPELL).ifPresent(spell -> {
                            if ( Objects.equals(spell.getKnownSpells(), "") ) {
                                spell.setKnownSpells(spellString);
                                AncientMagicksNetwork.sendToPlayer(new PacketUpdateKnownSpells(tag), player);
                            }
                            else if ( !ClientSpellData.stringListToSpellList(spell.getKnownSpells()).contains(item) ) {
                                spell.setKnownSpells(spell.getKnownSpells() + "," + spellString);
                                AncientMagicksNetwork.sendToPlayer(new PacketUpdateKnownSpells(tag), player);
                            }
                        });
                    }
                }
            }
        });
    }
}
