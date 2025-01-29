package net.mindoth.ancientmagicks.network;

import net.mindoth.ancientmagicks.item.castingitem.CastingItem;
import net.mindoth.ancientmagicks.capabilities.playermagic.PlayerMagicProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketSetSpell {
    public CompoundTag selectedItem;

    public PacketSetSpell(CompoundTag spell) {
        this.selectedItem = spell;
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
                if ( !CastingItem.getHeldStaff(player).isEmpty() ) {
                    ItemStack castingItem = CastingItem.getHeldStaff(player);
                    if ( CastingItem.isValidCastingItem(castingItem) ) {
                        String spellString = this.selectedItem.getString("am_spell");
                        player.getCapability(PlayerMagicProvider.PLAYER_MAGIC).ifPresent(spell -> spell.setCurrentSpell(spellString));
                    }
                }
            }
        });
    }
}
