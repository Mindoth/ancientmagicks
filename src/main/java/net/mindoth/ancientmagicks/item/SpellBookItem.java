package net.mindoth.ancientmagicks.item;

import com.google.common.collect.Lists;
import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.item.castingitem.CastingItem;
import net.mindoth.ancientmagicks.network.AncientMagicksNetwork;
import net.mindoth.ancientmagicks.network.PacketOpenSpellBook;
import net.mindoth.ancientmagicks.registries.AncientMagicksItems;
import net.mindoth.ancientmagicks.registries.recipe.SpellBookAddRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.List;

public class SpellBookItem extends Item implements DyeableMagicItem {

    public static final String NBT_KEY_SPELLS = "am_book_spells";

    public SpellBookItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, @Nonnull InteractionHand handIn) {
        InteractionResultHolder<ItemStack> result = InteractionResultHolder.fail(player.getItemInHand(handIn));
        if ( !level.isClientSide && player instanceof ServerPlayer serverPlayer ) {
            ItemStack stack = player.getItemInHand(handIn);
            if ( stack.getItem() == AncientMagicksItems.SPELL_BOOK.get()
                    && (CastingItem.getHeldStaff(player) == ItemStack.EMPTY || player.isCrouching()) ) {
                List<ItemStack> stackList = Lists.newArrayList();
                if ( stack.getTag() != null && stack.getTag().contains(NBT_KEY_SPELLS) ) {
                    stackList = stringListToStackList(stack.getTag().getString(NBT_KEY_SPELLS));
                }
                AncientMagicksNetwork.sendToPlayer(new PacketOpenSpellBook(stackList), serverPlayer);
            }
        }
        return result;
    }

    public static List<ItemStack> stringListToStackList(String list) {
        List<ItemStack> spellList = Lists.newArrayList();
        for ( String spellString : List.of(list.split(";")) ) {
            ItemStack stack = new ItemStack(AncientMagicksItems.PARCHMENT.get());
            stack.getOrCreateTag().putString(ParchmentItem.NBT_KEY_PAPER_SPELL, spellString);
            spellList.add(stack);
        }
        return spellList;
    }

    public static @Nonnull ItemStack getHeldSpellBook(Player playerEntity) {
        ItemStack book = playerEntity.getMainHandItem().getItem() instanceof SpellBookItem ? playerEntity.getMainHandItem() : ItemStack.EMPTY;
        if ( book == ItemStack.EMPTY ) book = playerEntity.getOffhandItem().getItem() instanceof SpellBookItem ? playerEntity.getOffhandItem() : ItemStack.EMPTY;
        return book;
    }
}
