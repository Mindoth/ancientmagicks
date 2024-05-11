package net.mindoth.ancientmagicks.network;

import com.google.common.collect.Lists;
import net.mindoth.ancientmagicks.client.gui.inventory.WandData;
import net.mindoth.ancientmagicks.item.castingitem.CastingItem;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.List;
import java.util.function.Supplier;

public class PacketSetStaffSlot {
    public CompoundNBT selectedItem;

    public PacketSetStaffSlot(CompoundNBT itemStack) {
        this.selectedItem = itemStack;
    }

    public PacketSetStaffSlot(PacketBuffer buf) {
        this.selectedItem = buf.readNbt();
    }

    public void encode(PacketBuffer buf) {
        buf.writeNbt(this.selectedItem);
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            if ( context.get().getSender() != null ) {
                ServerPlayerEntity player = context.get().getSender();
                if ( CastingItem.getHeldCastingItem(player).getItem() instanceof CastingItem ) {
                    ItemStack staff = CastingItem.getHeldCastingItem(player);
                    if ( CastingItem.isValidCastingItem(staff) ) {
                        WandData data = CastingItem.getData(staff);
                        List<CompoundNBT> list = Lists.newArrayList();
                        for ( int i = 0; i < data.getHandler().getSlots(); i++ ) list.add(data.getHandler().getStackInSlot(i).getOrCreateTag());
                        staff.getTag().putInt("staffslot", list.indexOf(this.selectedItem));
                    }
                }
            }
        });
    }
}
