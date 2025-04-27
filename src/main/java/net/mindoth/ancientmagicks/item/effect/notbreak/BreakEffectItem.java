package net.mindoth.ancientmagicks.item.effect.notbreak;

import com.mojang.authlib.GameProfile;
import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.item.effect.BlockTargetEffect;
import net.mindoth.ancientmagicks.item.effect.SpellEffectItem;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = AncientMagicks.MOD_ID)
public class BreakEffectItem extends BlockTargetEffect {

    private static final GameProfile FAKE_PROFILE = new GameProfile(UUID.fromString("fdc17a6f-5d46-484e-9343-820f43c7b101"), "am_fake_player_profile");

    public BreakEffectItem(Properties pProperties, int manaCost, int cooldown) {
        super(pProperties, manaCost, cooldown);
    }

    @Override
    protected boolean doSpell(Level level, LivingEntity owner, Entity caster, HitResult result, HashMap<String, Float> stats, String data) {
        BlockPos pos = ((BlockHitResult)result).getBlockPos();
        if ( level.getBlockState(pos).isAir() ) return false;
        if ( !(level instanceof ServerLevel serverLevel) ) return false;
        FakePlayer player = FakePlayerFactory.get(serverLevel, FAKE_PROFILE);
        int power = Mth.floor(stats.get(SpellEffectItem.POWER));
        player.setItemSlot(EquipmentSlot.MAINHAND, getToolFromStrength(power));
        BlockState blockState = level.getBlockState(pos);
        Block block = blockState.getBlock();
        if ( !block.canHarvestBlock(blockState, level, pos, player) || block.defaultDestroyTime() < 0 || blockState.isAir() ) return false;
        /*for ( BlockToBreak blockToBreak : BLOCKS_TO_BREAK_QUEUE ) {
            if ( blockToBreak.pos == pos && blockToBreak.caster == player ) return false;
        }
        BLOCKS_TO_BREAK_QUEUE.add(new BlockToBreak(pos, player));*/
        //level.destroyBlock(pos, true);
        List<ItemStack> list = Block.getDrops(blockState, serverLevel, pos, level.getBlockEntity(pos), player, player.getItemBySlot(EquipmentSlot.MAINHAND));
        level.removeBlock(pos, false);
        if ( !list.isEmpty() ) for ( ItemStack stack : list ) level.addFreshEntity(new ItemEntity(level, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, stack));
        level.playSound(null, pos, blockState.getSoundType().getBreakSound(), SoundSource.PLAYERS, 0.3F, 1);
        return true;
    }

    private ItemStack getToolFromStrength(int power) {
        if ( power < 1 ) return ItemStack.EMPTY;
        else if ( power == 1 ) return new ItemStack(Items.WOODEN_PICKAXE);
        else if ( power == 2 ) return new ItemStack(Items.STONE_PICKAXE);
        else if ( power == 3 ) return new ItemStack(Items.IRON_PICKAXE);
        else return new ItemStack(Items.DIAMOND_PICKAXE);
    }

    /*private static final class BlockToBreak {

        private final BlockPos pos;
        private final FakePlayer caster;

        public BlockToBreak(final BlockPos pos, final FakePlayer caster) {
            this.pos = pos;
            this.caster = caster;
        }
    }

    private static final LinkedList<BlockToBreak> BLOCKS_TO_BREAK_QUEUE = new LinkedList<>();

    @SubscribeEvent
    public static void onBreakTick(final TickEvent.ServerTickEvent event) {
        if ( BLOCKS_TO_BREAK_QUEUE.isEmpty() ) return;
        final BlockToBreak blockToBreak = BLOCKS_TO_BREAK_QUEUE.removeFirst();
        final FakePlayer player = blockToBreak.caster;
        final BlockPos pos = blockToBreak.pos;
        final Level level = player.level();
        final BlockState blockState = level.getBlockState(pos);
        if ( blockState.getBlock().canHarvestBlock(blockState, level, pos, player) && blockState.getBlock().defaultDestroyTime() < 0 || blockState.isAir() ) return;
        level.destroyBlock(pos, true);
        System.out.println(BLOCKS_TO_BREAK_QUEUE.size());
    }*/
}
