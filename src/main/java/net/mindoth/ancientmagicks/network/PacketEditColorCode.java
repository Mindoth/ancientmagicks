package net.mindoth.ancientmagicks.network;

import net.mindoth.ancientmagicks.client.gui.menu.SpellCraftingMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketEditColorCode {

    public int index;
    public Item rune;

    public PacketEditColorCode(int index, Item rune) {
        this.index = index;
        this.rune = rune;
    }

    public PacketEditColorCode(FriendlyByteBuf buf) {
        this.index = buf.readInt();
        this.rune = buf.readItem().getItem();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(this.index);
        buf.writeItem(new ItemStack(this.rune));
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            if ( context.getSender() != null ) {
                ServerPlayer player = context.getSender();
                if ( player.containerMenu instanceof SpellCraftingMenu menu ) menu.processColorCodeEditing(this.index, this.rune);
            }
        });
    }
}
