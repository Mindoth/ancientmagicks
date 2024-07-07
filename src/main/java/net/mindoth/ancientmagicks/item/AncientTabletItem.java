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
import net.minecraftforge.common.extensions.IForgeItem;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class AncientTabletItem extends Item implements IForgeItem {
    public AncientTabletItem(Properties pProperties) {
        super(pProperties.stacksTo(1));
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
                    List<ItemStack> runeList = ColorRuneItem.addColorRunesToList(player, Lists.newArrayList(), spell);
                    AncientMagicksNetwork.sendToPlayer(new PacketReceiveRuneData(runeList, finalTag, handIn == InteractionHand.OFF_HAND), serverPlayer);
                });
            }
        }
        return result;
    }

    private static CompoundTag createSpellToDiscover(CompoundTag tag) {
        List<SpellTabletItem> list = AncientMagicks.SPELL_LIST;
        int index = ThreadLocalRandom.current().nextInt(0, list.size());
        SpellTabletItem item = list.get(index);
        tag.putString("am_secretspell", ForgeRegistries.ITEMS.getKey(item).toString());

        return tag;
    }

    /*@Override
    public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entity) {
        Level level = entity.level();
        BlockPos pos = entity.getOnPos();
        if ( level.getBlockState(pos).getBlock() == Blocks.WATER_CAULDRON ) {
            entity.discard();
            flair(level, entity, pos);
        }
        return false;
    }

    private static void flair(Level level, ItemEntity entity, BlockPos pos) {
        if ( level instanceof ServerLevel world ) {
            world.playSound(null, pos, SoundEvents.PLAYER_SPLASH_HIGH_SPEED, SoundSource.BLOCKS, 0.5F, 1.0F);
            world.sendParticles(ParticleTypes.BUBBLE_POP, entity.getX(), entity.getY(), entity.getZ(),
                    0, 0, 0.5D, 0, 0.5D);
        }
    }*/
}
