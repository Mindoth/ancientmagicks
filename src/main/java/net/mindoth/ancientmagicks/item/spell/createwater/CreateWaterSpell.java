package net.mindoth.ancientmagicks.item.spell.createwater;

import net.mindoth.ancientmagicks.item.spell.BlockTargetSpell;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.HashMap;

public class CreateWaterSpell extends BlockTargetSpell {

    public CreateWaterSpell(Properties pProperties, int manaCost, int cooldown) {
        super(pProperties, manaCost, cooldown);
    }

    @Override
    protected boolean doSpell(Level level, LivingEntity owner, Entity caster, HitResult result, HashMap<String, Float> stats) {
        boolean state = false;
        BlockPos pos;
        if ( result instanceof BlockHitResult blockHitResult ) pos = blockHitResult.getBlockPos();
        else pos = new BlockPos(Mth.floor(result.getLocation().x), Mth.floor(result.getLocation().y), Mth.floor(result.getLocation().z));
        if ( result instanceof BlockHitResult blockHitResult ) state = createWater(level, getPosOfFace(pos, blockHitResult.getDirection()));
        else state = createWater(level, getPosOfFace(pos, Direction.UP));
        return state;
    }

    private boolean createWater(Level level, BlockPos pos) {
        boolean state = false;
        if ( level.getBlockState(pos).canBeReplaced(Fluids.WATER) ) {
            level.destroyBlock(pos, true);
            level.setBlockAndUpdate(pos, Blocks.WATER.defaultBlockState());
            state = true;
        }
        return state;
    }

    private static BlockPos getPosOfFace(BlockPos blockPos, Direction face) {
        return switch (face) {
            case UP -> blockPos.above();
            case EAST -> blockPos.east();
            case WEST -> blockPos.west();
            case SOUTH -> blockPos.south();
            case NORTH -> blockPos.north();
            case DOWN -> blockPos.below();
        };
    }
}
