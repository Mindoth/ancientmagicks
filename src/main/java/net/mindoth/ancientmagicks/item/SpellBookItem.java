package net.mindoth.ancientmagicks.item;

import com.google.common.collect.Lists;
import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.item.castingitem.CastingItem;
import net.mindoth.ancientmagicks.network.AncientMagicksNetwork;
import net.mindoth.ancientmagicks.network.PacketOpenSpellBook;
import net.mindoth.ancientmagicks.registries.AncientMagicksItems;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;
import java.util.List;

public class SpellBookItem extends Item implements DyeableMagicItem {

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
                for ( int i = 0; i <= 5; i++ ) {
                    for ( SpellItem spell : AncientMagicks.SPELL_LIST ) if ( AncientMagicks.isSpellEnabled(spell) && spell.getSpellTier() == i ) {
                        stackList.add(AncientMagicks.createSpellScroll(new ItemStack(AncientMagicksItems.SPELL_SCROLL.get()), spell));
                    }
                }
                AncientMagicksNetwork.sendToPlayer(new PacketOpenSpellBook(stackList), serverPlayer);
            }
        }
        return result;
    }

    public static @Nonnull ItemStack getHeldSpellBook(Player playerEntity) {
        ItemStack book = playerEntity.getMainHandItem().getItem() instanceof SpellBookItem ? playerEntity.getMainHandItem() : ItemStack.EMPTY;
        if ( book == ItemStack.EMPTY ) book = playerEntity.getOffhandItem().getItem() instanceof SpellBookItem ? playerEntity.getOffhandItem() : ItemStack.EMPTY;
        return book;
    }
}
