package net.mindoth.ancientmagicks.item;

import com.google.common.collect.Lists;
import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.capabilities.playermagic.ClientMagicData;
import net.mindoth.ancientmagicks.item.castingitem.CastingItem;
import net.mindoth.ancientmagicks.network.AncientMagicksNetwork;
import net.mindoth.ancientmagicks.network.PacketOpenSpellBook;
import net.mindoth.ancientmagicks.registries.AncientMagicksItems;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class SpellBookItem extends Item implements DyeableMagicItem {

    public static final String NBT_KEY_OWNER = "am_book_owner";
    public static final String NBT_KEY_BOOK_SPELLS = "am_book_spells";

    public SpellBookItem(Properties pProperties) {
        super(pProperties);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flagIn) {
        if ( stack.getOrCreateTag().contains(NBT_KEY_OWNER) ) {
            String owner = stack.getOrCreateTag().getString(NBT_KEY_OWNER);
            Component component = Component.translatable("tooltip.ancientmagicks.book_owner").withStyle(ChatFormatting.GRAY)
                    .append(Component.literal(owner)).withStyle(ChatFormatting.GRAY);
            tooltip.add(component);
        }

        super.appendHoverText(stack, world, tooltip, flagIn);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, @Nonnull InteractionHand handIn) {
        InteractionResultHolder<ItemStack> result = InteractionResultHolder.fail(player.getItemInHand(handIn));
        if ( !level.isClientSide && player instanceof ServerPlayer serverPlayer ) {
            ItemStack stack = player.getItemInHand(handIn);
            if ( stack.getItem() == AncientMagicksItems.SPELL_BOOK.get()
                    && (CastingItem.getHeldStaff(player) == ItemStack.EMPTY || player.isCrouching()) ) {
                CompoundTag tag = stack.getOrCreateTag();
                if ( !tag.contains(NBT_KEY_OWNER) ) tag.putString(NBT_KEY_OWNER, player.getScoreboardName());
                List<SpellItem> spellList;
                if ( tag.contains(NBT_KEY_BOOK_SPELLS) ) spellList = ClientMagicData.stringListToSpellList(tag.getString(NBT_KEY_BOOK_SPELLS));
                else spellList = Lists.newArrayList();
                List<ItemStack> stackList = Lists.newArrayList();
                for ( int i = 1; i <= 3; i++ ) {
                    for ( SpellItem spell : AncientMagicks.SPELL_LIST ) if ( spell.getSpellTier() == i && AncientMagicks.isSpellEnabled(spell) ) {
                        if ( player.isCreative() || spellList.contains(spell) ) {
                            stackList.add(AncientMagicks.createSpellScroll(new ItemStack(AncientMagicksItems.SPELL_SCROLL.get()), spell));
                        }
                    }
                }
                AncientMagicksNetwork.sendToPlayer(new PacketOpenSpellBook(tag, stackList), serverPlayer);
            }
        }
        return result;
    }

    public static @Nonnull ItemStack getHeldSpellBook(Player playerEntity) {
        ItemStack book = playerEntity.getMainHandItem().getItem() instanceof SpellBookItem ? playerEntity.getMainHandItem() : ItemStack.EMPTY;
        if ( book == ItemStack.EMPTY ) book = playerEntity.getOffhandItem().getItem() instanceof SpellBookItem ? playerEntity.getOffhandItem() : ItemStack.EMPTY;
        return book;
    }

    public static ItemStack getBookSlot(Player player) {
        for ( int i = 0; i < player.getInventory().getContainerSize(); i++ ) {
            if ( player.getInventory().getItem(i).getItem() instanceof SpellBookItem ) return player.getInventory().getItem(i);
        }
        return ItemStack.EMPTY;
    }
}
