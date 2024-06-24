package net.mindoth.ancientmagicks.network;

import net.mindoth.ancientmagicks.item.castingitem.CastingItem;
import net.mindoth.ancientmagicks.item.castingitem.StaffItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketSetSpellRune {
    public CompoundTag selectedItem;

    public PacketSetSpellRune(CompoundTag spellRune) {
        this.selectedItem = spellRune;
    }

    public PacketSetSpellRune(FriendlyByteBuf buf) {
        this.selectedItem = buf.readNbt();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeNbt(this.selectedItem);
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            if ( context.get().getSender() != null ) {
                ServerPlayer player = context.get().getSender();
                if ( CastingItem.getHeldCastingItem(player).getItem() instanceof StaffItem) {
                    ItemStack staff = CastingItem.getHeldCastingItem(player);
                    if ( CastingItem.isValidCastingItem(staff) ) staff.setTag(this.selectedItem);
                }
            }
        });
    }
}
