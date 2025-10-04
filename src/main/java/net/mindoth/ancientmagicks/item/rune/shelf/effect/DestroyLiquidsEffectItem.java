package net.mindoth.ancientmagicks.item.rune.shelf.effect;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.HashMap;

public class DestroyLiquidsEffectItem extends BlockTargetEffect {

    public DestroyLiquidsEffectItem(Properties pProperties, int cost) {
        super(pProperties, cost);
    }

    @Override
    protected boolean doSpell(Level level, LivingEntity owner, Entity caster, HitResult result, HashMap<String, Float> stats, String data) {
        boolean state = false;
        BlockPos pos;
        if ( result instanceof BlockHitResult blockHitResult ) pos = blockHitResult.getBlockPos();
        else pos = new BlockPos(Mth.floor(result.getLocation().x), Mth.floor(result.getLocation().y), Mth.floor(result.getLocation().z));
        Block block = level.getBlockState(pos).getBlock();
        if ( block instanceof LiquidBlock ) state = destroyWater(level, pos);
        return state;
    }

    private boolean destroyWater(Level level, BlockPos pos) {
        level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
        return true;
    }
}
