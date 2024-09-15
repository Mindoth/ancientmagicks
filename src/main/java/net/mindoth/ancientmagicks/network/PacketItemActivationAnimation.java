package net.mindoth.ancientmagicks.network;

import net.mindoth.ancientmagicks.item.castingitem.SpellTabletItem;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketItemActivationAnimation {

    public ItemStack itemStack;
    public Entity entity;

    public PacketItemActivationAnimation(ItemStack animatedItem, Entity entity) {
        this.itemStack = animatedItem;
        this.entity = entity;
    }

    public PacketItemActivationAnimation(FriendlyByteBuf buf) {
        Minecraft mc = Minecraft.getInstance();
        this.itemStack = buf.readItem();
        assert mc.level != null;
        this.entity = mc.level.getEntity(buf.readInt());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeItem(this.itemStack);
        buf.writeInt(this.entity.getId());
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> SpellTabletItem.playItemActivationAnimation(this.itemStack, this.entity));
        });
        contextSupplier.get().setPacketHandled(true);
    }
}
