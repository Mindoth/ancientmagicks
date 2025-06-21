package net.mindoth.ancientmagicks.network;

import net.mindoth.ancientmagicks.item.SpellBookItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketSwitchBookSlot {

    public ItemStack book;
    public CompoundTag combo;

    public PacketSwitchBookSlot(ItemStack book, CompoundTag combo) {
        this.book = book;
        this.combo = combo;
    }

    public PacketSwitchBookSlot(FriendlyByteBuf buf) {
        this.book = buf.readItem();
        this.combo = buf.readNbt();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeItem(this.book);
        buf.writeNbt(this.combo);
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        contextSupplier.get().enqueueWork(() -> {
            if ( context.getSender() != null ) {
                ServerPlayer player = context.getSender();
                if ( player.getInventory().contains(this.book) ) {
                    ItemStack book;
                    if ( ItemStack.isSameItemSameTags(player.getOffhandItem(), this.book) && !(player.getMainHandItem().getItem() instanceof SpellBookItem) ) book = player.getOffhandItem();
                    else book = player.getInventory().getItem(player.getInventory().findSlotMatchingItem(this.book));
                    SpellBookItem.handleSignature(player, book);
                    book.getTag().putString(SpellBookItem.NBT_KEY_BOOK_SLOT, this.combo.getString(SpellBookItem.NBT_KEY_BOOK_SLOT));
                }
            }
        });
        contextSupplier.get().setPacketHandled(true);
    }
}
