package net.mindoth.ancientmagicks.item;

import com.google.common.collect.Lists;
import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.item.castingitem.CastingItem;
import net.mindoth.ancientmagicks.network.ModNetwork;
import net.mindoth.ancientmagicks.network.PacketOpenSpellBook;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

public class SpellBookItem extends Item implements DyeableMagicItem {

    public static final String NBT_KEY_SPELLS = "am_book_spells";
    public static final String NBT_KEY_DATA = "am_data";
    public static final String NBT_KEY_CODES = "am_book_codes";
    public static final String NBT_KEY_OWNER_NAME = "am_book_owner_name";
    public static final String NBT_KEY_OWNER_UUID = "am_book_owner_uuid";
    public static final String NBT_KEY_BOOK_SLOT = "am_book_slot";
    public static final String NBT_KEY_NULL_NAME = "am_spell_has_null_name";

    public SpellBookItem(Properties pProperties) {
        super(pProperties);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flagIn) {
        if ( stack.hasTag() && stack.getTag().contains(NBT_KEY_OWNER_NAME) ) {
            String name = stack.getTag().getString(NBT_KEY_OWNER_NAME);
            tooltip.add(Component.translatable("tooltip.ancientmagicks.book_owner").withStyle(ChatFormatting.GRAY)
                    .append(Component.literal(name).withStyle(ChatFormatting.GRAY)));
        }
        super.appendHoverText(stack, world, tooltip, flagIn);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, @Nonnull InteractionHand handIn) {
        InteractionResultHolder<ItemStack> result = InteractionResultHolder.fail(player.getItemInHand(handIn));
        if ( !level.isClientSide && player instanceof ServerPlayer serverPlayer ) {
            ItemStack stack = player.getItemInHand(handIn);
            if ( CastingItem.getHeldStaff(player) == ItemStack.EMPTY || player.isCrouching() ) {
                handleSignature(serverPlayer, stack);
                ModNetwork.sendToPlayer(new PacketOpenSpellBook(stack, 0), serverPlayer);
            }
        }
        return result;
    }

    public static void handleSignature(ServerPlayer serverPlayer, ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        if ( !tag.contains(NBT_KEY_BOOK_SLOT) ) tag.putString(NBT_KEY_BOOK_SLOT, "");
        if ( !tag.contains(NBT_KEY_OWNER_UUID) ){
            tag.putUUID(NBT_KEY_OWNER_UUID, serverPlayer.getUUID());
            tag.putString(NBT_KEY_OWNER_NAME, serverPlayer.getDisplayName().getString());
        }
        if ( tag.contains(NBT_KEY_OWNER_UUID) && tag.contains(NBT_KEY_OWNER_NAME) ) {
            if ( tag.getUUID(NBT_KEY_OWNER_UUID) == serverPlayer.getUUID() && !tag.getString(NBT_KEY_OWNER_NAME).equals(serverPlayer.getDisplayName().getString())) {
                tag.putString(NBT_KEY_OWNER_NAME, serverPlayer.getDisplayName().getString());
            }
        }
    }

    public static ItemStack getActiveScrollFromBook(ItemStack book) {
        ItemStack state = null;
        CompoundTag tag = book.getTag();
        List<ItemStack> spellList = SpellBookItem.getScrollListFromBook(tag);

        List<Item> codeList = Lists.newArrayList();
        for ( String string : List.of(tag.getString(NBT_KEY_BOOK_SLOT).split(",")) ) {
            Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(string));
            if ( item instanceof ColorRuneItem colorModifierItem ) codeList.add(colorModifierItem);
        }

        for ( int i = 0; i < spellList.size(); i++ ) {
            ItemStack scroll = spellList.get(i);
            if ( AncientMagicks.listsMatch(codeList, ParchmentItem.getScrollComboList(scroll)) ) state = SpellBookItem.getScrollListFromBook(tag).get(i);
        }
        return state;
    }

    public static ItemStack constructSpellScroll(String string, String data, String name, Item item, String code) {
        ItemStack stack = new ItemStack(item);
        if ( !Objects.equals(name, NBT_KEY_NULL_NAME) ) stack.setHoverName(Component.literal(name));
        CompoundTag tag = stack.getOrCreateTag();
        tag.putString(ParchmentItem.NBT_KEY_SPELL_STRING, string);
        tag.putString(ParchmentItem.NBT_KEY_DATA_STRING, data);
        tag.putString(ParchmentItem.NBT_KEY_CODE_STRING, code);
        return stack;
    }

    public static List<ItemStack> getScrollListFromBook(CompoundTag tag) {
        List<ItemStack> scrollList = Lists.newArrayList();

        String spell = tag.getString(NBT_KEY_SPELLS);
        List<String> stringList = List.of(spell.split(";"));

        String data = tag.getString(NBT_KEY_DATA);
        List<String> dataList = List.of(data.split(";"));

        String code = tag.getString(NBT_KEY_CODES);
        List<String> codeList = List.of(code.split(";"));

        String name = tag.getString(ParchmentItem.NBT_KEY_SPELL_NAME);
        List<String> nameList = List.of(name.split(";"));

        String item = tag.getString(ParchmentItem.NBT_KEY_PAPER_TIER);
        List<String> itemList = List.of(item.split(";"));

        for ( int i = 0; i < stringList.size(); i++ ) {
            ItemStack stack = constructSpellScroll(stringList.get(i), dataList.get(i), nameList.get(i), ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemList.get(i))), codeList.get(i));
            scrollList.add(stack);
        }

        return scrollList;
    }

    public static ItemStack constructBook(ItemStack ogBook, List<ItemStack> scrolls) {
        ItemStack book = ogBook.copy();
        if ( book.hasTag() ) {
            if ( book.getTag().contains(NBT_KEY_SPELLS) ) book.getTag().remove(NBT_KEY_SPELLS);
            if ( book.getTag().contains(NBT_KEY_DATA) ) book.getTag().remove(NBT_KEY_DATA);
            if ( book.getTag().contains(NBT_KEY_CODES) ) book.getTag().remove(NBT_KEY_CODES);
            if ( book.getTag().contains(ParchmentItem.NBT_KEY_SPELL_NAME) ) book.getTag().remove(ParchmentItem.NBT_KEY_SPELL_NAME);
            if ( book.getTag().contains(ParchmentItem.NBT_KEY_PAPER_TIER) ) book.getTag().remove(ParchmentItem.NBT_KEY_PAPER_TIER);
        }
        for ( ItemStack scroll : scrolls ) addSpellToBook(book, scroll);
        return book;
    }

    public static void addSpellToBook(ItemStack book, ItemStack scroll) {
        CompoundTag bookTag = book.getOrCreateTag();

        String spellString = scroll.getTag().getString(ParchmentItem.NBT_KEY_SPELL_STRING);
        addSpellTagsToBook(bookTag, spellString, NBT_KEY_SPELLS);

        String dataString = scroll.getTag().getString(ParchmentItem.NBT_KEY_DATA_STRING);
        addSpellTagsToBook(bookTag, dataString, NBT_KEY_DATA);

        String code = scroll.getTag().getString(ParchmentItem.NBT_KEY_CODE_STRING);
        addSpellTagsToBook(bookTag, code, NBT_KEY_CODES);

        String name;
        if ( scroll.hasCustomHoverName() ) name = scroll.getHoverName().getString();
        else name = NBT_KEY_NULL_NAME;
        addSpellTagsToBook(bookTag, name, ParchmentItem.NBT_KEY_SPELL_NAME);

        String item = ForgeRegistries.ITEMS.getKey(scroll.getItem()).toString();
        addSpellTagsToBook(bookTag, item, ParchmentItem.NBT_KEY_PAPER_TIER);
    }

    public static void addSpellTagsToBook(CompoundTag bookTag, String string, String key) {
        if ( !bookTag.contains(key) ) bookTag.putString(key, string);
        else {
            String spellList = bookTag.getString(key) + ";" + string;
            bookTag.remove(key);
            bookTag.putString(key, spellList);
        }
    }

    public static ItemStack getSpellBookSlot(Player player) {
        ItemStack offHand = player.getOffhandItem();
        if ( offHand.getItem() instanceof SpellBookItem && offHand.hasTag() && offHand.getTag().contains(NBT_KEY_SPELLS)
                && !offHand.getTag().getString(NBT_KEY_SPELLS).isEmpty() ) return offHand;
        for ( int i = 0; i <= player.getInventory().getContainerSize(); i++ ) {
            ItemStack slot = player.getInventory().getItem(i);
            if ( slot.getItem() instanceof SpellBookItem && slot.hasTag() && slot.getTag().contains(NBT_KEY_SPELLS)
                    && !slot.getTag().getString(NBT_KEY_SPELLS).isEmpty() ) return slot;
        }
        return ItemStack.EMPTY;
    }

    public static @Nonnull ItemStack getHeldSpellBook(Player playerEntity) {
        ItemStack book = playerEntity.getMainHandItem().getItem() instanceof SpellBookItem ? playerEntity.getMainHandItem() : ItemStack.EMPTY;
        if ( book == ItemStack.EMPTY ) book = playerEntity.getOffhandItem().getItem() instanceof SpellBookItem ? playerEntity.getOffhandItem() : ItemStack.EMPTY;
        return book;
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return slotChanged;
    }
}