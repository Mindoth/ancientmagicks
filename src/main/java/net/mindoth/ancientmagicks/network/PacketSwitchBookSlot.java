package net.mindoth.ancientmagicks.network;

import net.mindoth.ancientmagicks.item.SpellBookItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketSwitchBookSlot {

    public ItemStack book;
    public int slot;

    public PacketSwitchBookSlot(ItemStack book, int slot) {
        this.book = book;
        this.slot = slot;
    }

    public PacketSwitchBookSlot(FriendlyByteBuf buf) {
        this.book = buf.readItem();
        this.slot = buf.readInt();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeItem(this.book);
        buf.writeInt(this.slot);
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        contextSupplier.get().enqueueWork(() -> {
            if ( context.getSender() != null ) {
                ServerPlayer player = context.getSender();
                if ( player.getInventory().contains(this.book) ) {
                    ItemStack book = player.getInventory().getItem(player.getInventory().findSlotMatchingItem(this.book));
                    SpellBookItem.handleSignature(player, book);
                    book.getTag().putInt(SpellBookItem.NBT_KEY_BOOK_SLOT, this.slot);
                }
            }
        });
        contextSupplier.get().setPacketHandled(true);
    }
}
