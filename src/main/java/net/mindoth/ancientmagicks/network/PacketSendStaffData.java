package net.mindoth.ancientmagicks.network;

import net.mindoth.ancientmagicks.client.gui.inventory.WandData;
import net.mindoth.ancientmagicks.item.weapon.CastingItem;
import net.mindoth.ancientmagicks.item.weapon.WandItem;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketSendStaffData {

    public PacketSendStaffData() {
    }

    public PacketSendStaffData(PacketBuffer buf) {
    }

    public void encode(PacketBuffer buf) {
    }

    public boolean handle(Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().getSender().refreshContainer(contextSupplier.get().getSender().containerMenu);
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            if ( context.getSender() != null ) {
                ServerPlayerEntity player = context.getSender();
                ItemStack staff = WandItem.getHeldCastingItem(player);
                WandData data = CastingItem.getData(staff);
                if ( data.getUuid() != null ) {
                    player.stopUsingItem();
                    AncientMagicksNetwork.sendToPlayer(new PacketReceiveStaffData(CastingItem.getStaffList(staff), staff.getTag()), player);
                }
            }
        });
        return true;
    }
}
