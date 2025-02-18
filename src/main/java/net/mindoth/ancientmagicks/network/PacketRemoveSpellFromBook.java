package net.mindoth.ancientmagicks.network;

import com.google.common.collect.Lists;
import net.mindoth.ancientmagicks.item.ParchmentItem;
import net.mindoth.ancientmagicks.item.SpellBookItem;
import net.mindoth.ancientmagicks.registries.recipe.SpellBookAddRecipe;
import net.mindoth.shadowizardlib.event.ShadowEvents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.function.Supplier;

public class PacketRemoveSpellFromBook {

    public ItemStack book;
    public int slot;

    public PacketRemoveSpellFromBook(ItemStack book, int slot) {
        this.book = book;
        this.slot = slot;
    }

    public PacketRemoveSpellFromBook(FriendlyByteBuf buf) {
        this.book = buf.readItem();
        this.slot = buf.readInt();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeItem(this.book);
        buf.writeInt(this.slot);
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            if ( context.getSender() != null ) {
                ServerPlayer player = context.getSender();
                if ( player.getInventory().contains(this.book) ) {
                    ItemStack book = player.getInventory().getItem(player.getInventory().findSlotMatchingItem(this.book));
                    CompoundTag bookTag = book.getTag();
                    final List<ItemStack> spells = SpellBookItem.getScrollListFromBook(bookTag);

                    bookTag.remove(SpellBookItem.NBT_KEY_SPELLS);
                    bookTag.remove(ParchmentItem.NBT_KEY_SPELL_NAME);
                    bookTag.remove(ParchmentItem.NBT_KEY_PAPER_TIER);

                    for ( ItemStack scroll : spells ) {
                        if ( scroll != spells.get(this.slot) ) {
                            String spellString = scroll.getTag().getString(ParchmentItem.NBT_KEY_SPELL_STRING);
                            SpellBookItem.addSpellTagsToBook(bookTag, spellString, SpellBookItem.NBT_KEY_SPELLS);

                            String name = scroll.getHoverName().getString();
                            SpellBookItem.addSpellTagsToBook(bookTag, name, ParchmentItem.NBT_KEY_SPELL_NAME);

                            String item = ForgeRegistries.ITEMS.getKey(scroll.getItem()).toString();
                            SpellBookItem.addSpellTagsToBook(bookTag, item, ParchmentItem.NBT_KEY_PAPER_TIER);
                        }
                        else {
                            Vec3 center = ShadowEvents.getEntityCenter(player);
                            ItemEntity drop = new ItemEntity(player.level(), center.x, center.y, center.z, scroll);
                            drop.setDeltaMovement(0, 0, 0);
                            drop.setNoPickUpDelay();
                            player.level().addFreshEntity(drop);
                        }
                    }
                }
            }
        });
    }
}
