package net.mindoth.ancientmagicks.network;

import net.mindoth.ancientmagicks.client.gui.inventory.WandData;
import net.mindoth.ancientmagicks.item.castingitem.CastingItem;
import net.mindoth.ancientmagicks.network.capabilities.PlayerSpellProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketSendWandData {

    public PacketSendWandData() {
    }

    public PacketSendWandData(FriendlyByteBuf buf) {
    }

    public void encode(FriendlyByteBuf buf) {
    }

    public boolean handle(Supplier<NetworkEvent.Context> contextSupplier) {
        //contextSupplier.get().getSender().refreshContainer(contextSupplier.get().getSender().containerMenu);
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            if ( context.getSender() != null ) {
                ServerPlayer player = context.getSender();
                ItemStack wand = CastingItem.getHeldCastingItem(player);
                WandData data = CastingItem.getData(wand);
                CompoundTag tag = contextSupplier.get().getSender().getPersistentData().getCompound(Player.PERSISTED_NBT_TAG);
                if ( data.getUuid() != null ) {
                    player.stopUsingItem();
                    AncientMagicksNetwork.sendToPlayer(new PacketReceiveWandData(CastingItem.getWandList(wand), wand.getTag()), player);
                }
            }
        });
        return true;
    }
}
