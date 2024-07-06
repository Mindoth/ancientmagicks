package net.mindoth.ancientmagicks.item;

import com.google.common.collect.Lists;
import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.item.castingitem.SpellTabletItem;
import net.mindoth.ancientmagicks.network.AncientMagicksNetwork;
import net.mindoth.ancientmagicks.network.PacketReceiveRuneData;
import net.mindoth.ancientmagicks.network.capabilities.playerspell.PlayerSpellProvider;
import net.mindoth.ancientmagicks.registries.AncientMagicksItems;
import net.minecraft.nbt.CompoundTag;
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
import java.util.Random;

public class AncientTabletItem extends Item {
    public AncientTabletItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    @Nonnull
    public InteractionResultHolder<ItemStack> use(Level level, Player player, @Nonnull InteractionHand handIn) {
        InteractionResultHolder<ItemStack> result = InteractionResultHolder.fail(player.getItemInHand(handIn));
        if ( !level.isClientSide && player instanceof ServerPlayer serverPlayer ) {
            ItemStack stack = serverPlayer.getItemInHand(handIn);
            CompoundTag tag = stack.getOrCreateTag();
            CompoundTag finalTag;
            if ( !tag.contains("am_secretspell") ) finalTag = createSpellToDiscover(tag);
            else finalTag = tag;
            if ( finalTag.contains("am_secretspell") ) {
                serverPlayer.getCapability(PlayerSpellProvider.PLAYER_SPELL).ifPresent(spell -> {
                    List<ItemStack> runeList = Lists.newArrayList();
                    if ( spell.getBlue() || player.isCreative() ) runeList.add(new ItemStack(AncientMagicksItems.BLUE_RUNE.get()));
                    if ( spell.getPurple() || player.isCreative() ) runeList.add(new ItemStack(AncientMagicksItems.PURPLE_RUNE.get()));
                    if ( spell.getYellow() || player.isCreative() ) runeList.add(new ItemStack(AncientMagicksItems.YELLOW_RUNE.get()));
                    if ( spell.getGreen() || player.isCreative() ) runeList.add(new ItemStack(AncientMagicksItems.GREEN_RUNE.get()));
                    if ( spell.getBlack() || player.isCreative() ) runeList.add(new ItemStack(AncientMagicksItems.BLACK_RUNE.get()));
                    if ( spell.getWhite() || player.isCreative() ) runeList.add(new ItemStack(AncientMagicksItems.WHITE_RUNE.get()));
                    AncientMagicksNetwork.sendToPlayer(new PacketReceiveRuneData(runeList, finalTag, handIn == InteractionHand.OFF_HAND), serverPlayer);
                });
            }
        }
        return result;
    }

    private static CompoundTag createSpellToDiscover(CompoundTag tag) {
        Random rand = new Random();
        List<SpellTabletItem> list = AncientMagicks.SPELL_LIST;
        int index = rand.nextInt(list.size());
        SpellTabletItem item = list.get(rand.nextInt(index));
        tag.putString("am_secretspell", ForgeRegistries.ITEMS.getKey(item).toString());

        return tag;
    }

    public static @Nonnull ItemStack getHeldAncientTabletItem(Player playerEntity) {
        ItemStack slate = playerEntity.getMainHandItem().getItem() == AncientMagicksItems.ANCIENT_TABLET.get() ? playerEntity.getMainHandItem() : null;
        return slate == null ? (playerEntity.getOffhandItem().getItem() == AncientMagicksItems.ANCIENT_TABLET.get() ? playerEntity.getOffhandItem() : ItemStack.EMPTY) : slate;
    }
}
