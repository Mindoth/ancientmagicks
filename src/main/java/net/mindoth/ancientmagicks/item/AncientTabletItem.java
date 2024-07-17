package net.mindoth.ancientmagicks.item;

import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.item.castingitem.SpellTabletItem;
import net.mindoth.ancientmagicks.network.AncientMagicksNetwork;
import net.mindoth.ancientmagicks.network.PacketReceiveRuneData;
import net.mindoth.ancientmagicks.network.capabilities.playerspell.PlayerSpellProvider;
import net.mindoth.ancientmagicks.registries.AncientMagicksItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Mod.EventBusSubscriber(modid = AncientMagicks.MOD_ID)
public class AncientTabletItem extends Item {
    public AncientTabletItem(Properties pProperties) {
        super(pProperties.stacksTo(1));
    }

    @Override
    @Nonnull
    public InteractionResultHolder<ItemStack> use(Level level, Player player, @Nonnull InteractionHand handIn) {
        InteractionResultHolder<ItemStack> result = InteractionResultHolder.fail(player.getItemInHand(handIn));
        if ( !level.isClientSide && player instanceof ServerPlayer serverPlayer ) {
            ItemStack stack = serverPlayer.getItemInHand(handIn);
            final CompoundTag finalTag = stack.getTag();
            if ( finalTag != null && finalTag.contains("am_secretspell") ) {
                serverPlayer.getCapability(PlayerSpellProvider.PLAYER_SPELL).ifPresent(spell -> {
                    List<ItemStack> runeList = ColorRuneItem.getColorRuneList(player, spell);
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

    @Override
    public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entity) {
        Level level = entity.level();
        if ( !level.isClientSide ) {
            BlockPos blockPos = entity.blockPosition();
            if ( level.getBlockState(blockPos).getBlock() == Blocks.WATER_CAULDRON ) {
                CompoundTag tag = stack.getOrCreateTag();
                final CompoundTag finalTag;
                if ( !tag.contains("am_secretspell") ) finalTag = createSpellToDiscover(tag);
                else finalTag = tag;
                if ( finalTag.contains("am_secretspell") ) {
                    Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(finalTag.getString("am_secretspell")));
                    if ( item instanceof SpellTabletItem spell ) {
                        List<ColorRuneItem> runeList = ColorRuneItem.CURRENT_COMBO_MAP.get(spell);
                        ColorRuneItem randomRune = runeList.get(ThreadLocalRandom.current().nextInt(0, runeList.size()));
                        double randX = ThreadLocalRandom.current().nextDouble(blockPos.getX() + 0.1D, blockPos.getX() + 0.9D);
                        double randZ = ThreadLocalRandom.current().nextDouble(blockPos.getZ() + 0.1D, blockPos.getZ() + 0.9D);
                        Vec3 particlePos = new Vec3(randX, blockPos.getY() + 0.9D, randZ);
                        if ( level instanceof ServerLevel serverLevel ) playParticles(ParticleTypes.ENTITY_EFFECT, serverLevel, particlePos, randomRune);
                    }
                }
            }
        }
        return false;
    }

    private static void playParticles(SimpleParticleType type, ServerLevel serverLevel, Vec3 pos, ColorRuneItem rune) {
        if ( rune == AncientMagicksItems.BLUE_RUNE.get() ) serverLevel.sendParticles(type, pos.x, pos.y, pos.z, 0, 0, 1, 1, 1);
        if ( rune == AncientMagicksItems.PURPLE_RUNE.get() ) serverLevel.sendParticles(type, pos.x, pos.y, pos.z, 0, 1, 85F / 255F, 1, 1);
        if ( rune == AncientMagicksItems.YELLOW_RUNE.get() ) serverLevel.sendParticles(type, pos.x, pos.y, pos.z, 0, 1, 1, 0, 1);
        if ( rune == AncientMagicksItems.GREEN_RUNE.get() ) serverLevel.sendParticles(type, pos.x, pos.y, pos.z, 0, 0, 1, 0, 1);
        if ( rune == AncientMagicksItems.BLACK_RUNE.get() ) serverLevel.sendParticles(type, pos.x, pos.y, pos.z, 0, 0, 0, 0, 1);
        if ( rune == AncientMagicksItems.WHITE_RUNE.get() ) serverLevel.sendParticles(type, pos.x, pos.y, pos.z, 0, 1, 1, 0, 1);
        if ( rune == AncientMagicksItems.BROWN_RUNE.get() ) serverLevel.sendParticles(type, pos.x, pos.y, pos.z, 0, 122F / 255F, 70F / 255F, 33F / 255F, 1);
        if ( rune == AncientMagicksItems.RED_RUNE.get() ) serverLevel.sendParticles(type, pos.x, pos.y, pos.z, 0, 1, 0, 0, 1);
    }
}
