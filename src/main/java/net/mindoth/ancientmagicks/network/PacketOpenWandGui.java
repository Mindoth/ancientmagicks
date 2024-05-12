package net.mindoth.ancientmagicks.network;

import net.mindoth.ancientmagicks.client.gui.inventory.WandContainer;
import net.mindoth.ancientmagicks.client.gui.inventory.WandData;
import net.mindoth.ancientmagicks.item.castingitem.CastingItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkHooks;

import java.util.UUID;
import java.util.function.Supplier;

public class PacketOpenWandGui {

    public PacketOpenWandGui() {
    }

    public PacketOpenWandGui(FriendlyByteBuf buf) {
    }

    public void encode(FriendlyByteBuf buf) {
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            if ( context.get().getSender() != null ) {
                ServerPlayer player = context.get().getSender();
                if ( CastingItem.getHeldCastingItem(player).getItem() instanceof CastingItem ) {
                    ItemStack wand = CastingItem.getHeldCastingItem(player);
                    WandData data = CastingItem.getData(wand);
                    if ( CastingItem.isValidCastingItem(wand) ) {
                        player.stopUsingItem();
                        UUID uuid = data.getUuid();
                        data.updateAccessRecords(player.getName().getString(), System.currentTimeMillis());
                        NetworkHooks.openScreen(player, new SimpleMenuProvider(
                                (windowId, playerInventory, playerEntity) -> new WandContainer(windowId, playerInventory, uuid, data.getTier(), data.getHandler()),
                                wand.getHoverName()), (buffer -> buffer.writeUUID(uuid).writeInt(data.getTier().ordinal())));
                    }
                }
            }
        });
    }
}
