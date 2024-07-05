package net.mindoth.ancientmagicks.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

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

    public boolean handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            if ( context.getSender() != null ) {
                ServerPlayer player = context.getSender();
                player.setItemSlot(this.isOffHand ? EquipmentSlot.OFFHAND : EquipmentSlot.MAINHAND, this.resulStack);
            }
        });
        return true;
    }
}
