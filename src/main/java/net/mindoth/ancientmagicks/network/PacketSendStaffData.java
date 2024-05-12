package net.mindoth.ancientmagicks.network;

import net.mindoth.ancientmagicks.client.gui.inventory.WandData;
import net.mindoth.ancientmagicks.item.castingitem.CastingItem;
import net.mindoth.ancientmagicks.item.castingitem.WandItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketSendStaffData {

    public PacketSendStaffData() {
    }

    public PacketSendStaffData(FriendlyByteBuf buf) {
    }

    public void encode(FriendlyByteBuf buf) {
    }

    public boolean handle(Supplier<NetworkEvent.Context> contextSupplier) {
        //contextSupplier.get().getSender().refreshContainer(contextSupplier.get().getSender().containerMenu);
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            if ( context.getSender() != null ) {
                ServerPlayer player = context.getSender();
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
