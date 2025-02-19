package net.mindoth.ancientmagicks.item;

import com.google.common.collect.Lists;
import net.mindoth.ancientmagicks.item.castingitem.CastingItem;
import net.mindoth.ancientmagicks.network.AncientMagicksNetwork;
import net.mindoth.ancientmagicks.network.PacketOpenSpellBook;
import net.mindoth.ancientmagicks.registries.AncientMagicksItems;
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
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class SpellBookItem extends Item implements DyeableMagicItem {

    public static final String NBT_KEY_SPELLS = "am_book_spells";
    public static final String NBT_KEY_CODES = "am_book_codes";
    public static final String NBT_KEY_OWNER_NAME = "am_book_owner_name";
    public static final String NBT_KEY_OWNER_UUID = "am_book_owner_uuid";

    public SpellBookItem(Properties pProperties) {
        super(pProperties);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flagIn) {
        if ( stack.hasTag() && stack.getTag().contains(NBT_KEY_OWNER_NAME) ) {
            String name = stack.getTag().getString(NBT_KEY_OWNER_NAME);
            tooltip.add(Component.translatable("tooltip.ancientmagicks.owner").withStyle(ChatFormatting.GRAY)
                    .append(Component.literal(name).withStyle(ChatFormatting.GRAY)));
        }
        super.appendHoverText(stack, world, tooltip, flagIn);
    }

    private static @NotNull CompoundTag handleSignature(ServerPlayer serverPlayer, ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        if ( !tag.contains(NBT_KEY_OWNER_UUID) ){
            tag.putUUID(NBT_KEY_OWNER_UUID, serverPlayer.getUUID());
            tag.putString(NBT_KEY_OWNER_NAME, serverPlayer.getDisplayName().getString());
        }
        if ( tag.contains(NBT_KEY_OWNER_UUID) && tag.contains(NBT_KEY_OWNER_NAME) ) {
            if ( tag.getUUID(NBT_KEY_OWNER_UUID) == serverPlayer.getUUID() && !tag.getString(NBT_KEY_OWNER_NAME).equals(serverPlayer.getDisplayName().getString())) {
                tag.putString(NBT_KEY_OWNER_NAME, serverPlayer.getDisplayName().getString());
            }
        }
        return tag;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, @Nonnull InteractionHand handIn) {
        InteractionResultHolder<ItemStack> result = InteractionResultHolder.fail(player.getItemInHand(handIn));
        if ( !level.isClientSide && player instanceof ServerPlayer serverPlayer ) {
            ItemStack stack = player.getItemInHand(handIn);
            if ( stack.getItem() == AncientMagicksItems.SPELL_BOOK.get() && (CastingItem.getHeldStaff(player) == ItemStack.EMPTY || player.isCrouching()) ) {
                handleSignature(serverPlayer, stack);
                AncientMagicksNetwork.sendToPlayer(new PacketOpenSpellBook(stack, 0), serverPlayer);
            }
        }
        return result;
    }

    public static ItemStack constructSpellScroll(String string, String name, Item item, String code) {
        ItemStack stack = new ItemStack(item);
        stack.setHoverName(Component.literal(name));
        CompoundTag tag = stack.getOrCreateTag();
        tag.putString(ParchmentItem.NBT_KEY_SPELL_STRING, string);
        tag.putString(ParchmentItem.NBT_KEY_CODE_STRING, code);
        return stack;
    }

    public static List<ItemStack> getScrollListFromBook(CompoundTag tag) {
        List<ItemStack> scrollList = Lists.newArrayList();

        String spell = tag.getString(NBT_KEY_SPELLS);
        List<String> stringList = List.of(spell.split(";"));

        String code = tag.getString(NBT_KEY_CODES);
        List<String> codeList = List.of(code.split(";"));

        String name = tag.getString(ParchmentItem.NBT_KEY_SPELL_NAME);
        List<String> nameList = List.of(name.split(";"));

        String item = tag.getString(ParchmentItem.NBT_KEY_PAPER_TIER);
        List<String> itemList = List.of(item.split(";"));

        for ( int i = 0; i < stringList.size(); i++ ) {
            ItemStack stack = constructSpellScroll(stringList.get(i), nameList.get(i), ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemList.get(i))), codeList.get(i));
            scrollList.add(stack);
        }

        return scrollList;
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
        for ( int i = 0; i <= player.getInventory().getContainerSize(); i++ ) {
            ItemStack slot = player.getInventory().getItem(i);
            if ( slot.getItem() instanceof SpellBookItem && slot.hasTag() && slot.getTag().contains(NBT_KEY_SPELLS) ) return slot;
        }
        return ItemStack.EMPTY;
    }

    public static @Nonnull ItemStack getHeldSpellBook(Player playerEntity) {
        ItemStack book = playerEntity.getMainHandItem().getItem() instanceof SpellBookItem ? playerEntity.getMainHandItem() : ItemStack.EMPTY;
        if ( book == ItemStack.EMPTY ) book = playerEntity.getOffhandItem().getItem() instanceof SpellBookItem ? playerEntity.getOffhandItem() : ItemStack.EMPTY;
        return book;
    }
}
