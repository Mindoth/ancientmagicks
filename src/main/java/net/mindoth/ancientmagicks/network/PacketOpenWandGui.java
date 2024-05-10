package net.mindoth.ancientmagicks.network;

import net.mindoth.ancientmagicks.client.gui.inventory.WandContainer;
import net.mindoth.ancientmagicks.client.gui.inventory.WandData;
import net.mindoth.ancientmagicks.item.weapon.CastingItem;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.UUID;
import java.util.function.Supplier;

public class PacketOpenWandGui {

    public PacketOpenWandGui() {
    }

    public PacketOpenWandGui(PacketBuffer buf) {
    }

    public void encode(PacketBuffer buf) {
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            if ( context.get().getSender() != null ) {
                ServerPlayerEntity player = context.get().getSender();
                if ( CastingItem.getHeldCastingItem(player).getItem() instanceof CastingItem ) {
                    ItemStack wand = CastingItem.getHeldCastingItem(player);
                    WandData data = CastingItem.getData(wand);
                    if ( CastingItem.isValidCastingItem(wand) ) {
                        player.stopUsingItem();
                        UUID uuid = data.getUuid();
                        data.updateAccessRecords(player.getName().getString(), System.currentTimeMillis());
                        NetworkHooks.openGui(player, new SimpleNamedContainerProvider(
                                (windowId, playerInventory, playerEntity) -> new WandContainer(windowId, playerInventory, uuid, data.getTier(), data.getHandler()),
                                wand.getHoverName()), (buffer -> buffer.writeUUID(uuid).writeInt(data.getTier().ordinal())));
                    }
                }
            }
        });
    }
}
