package net.mindoth.ancientmagicks.network;

import com.google.common.collect.Lists;
import net.mindoth.ancientmagicks.client.gui.inventory.WandData;
import net.mindoth.ancientmagicks.item.castingitem.CastingItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.List;
import java.util.function.Supplier;

public class PacketSetStaffSlot {
    public CompoundTag selectedItem;

    public PacketSetStaffSlot(CompoundTag itemStack) {
        this.selectedItem = itemStack;
    }

    public PacketSetStaffSlot(FriendlyByteBuf buf) {
        this.selectedItem = buf.readNbt();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeNbt(this.selectedItem);
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            if ( context.get().getSender() != null ) {
                ServerPlayer player = context.get().getSender();
                if ( CastingItem.getHeldCastingItem(player).getItem() instanceof CastingItem ) {
                    ItemStack staff = CastingItem.getHeldCastingItem(player);
                    if ( CastingItem.isValidCastingItem(staff) ) {
                        WandData data = CastingItem.getData(staff);
                        List<CompoundTag> list = Lists.newArrayList();
                        for ( int i = 0; i < data.getHandler().getSlots(); i++ ) list.add(data.getHandler().getStackInSlot(i).getOrCreateTag());
                        staff.getTag().putInt("staffslot", list.indexOf(this.selectedItem));
                    }
                }
            }
        });
    }
}
