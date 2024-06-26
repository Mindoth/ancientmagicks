package net.mindoth.ancientmagicks.item.spellrune.collapse;

import net.mindoth.ancientmagicks.item.spellrune.SpellRuneItem;
import net.mindoth.shadowizardlib.event.ShadowEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class CollapseSpell extends SpellRuneItem {

    public CollapseSpell(Properties pProperties, int cooldown) {
        super(pProperties, cooldown);
    }

    @Override
    public boolean castMagic(Player owner, Entity caster, Vec3 center, float xRot, float yRot, int useTime) {
        boolean state = false;
        Level level = caster.level();
        Vec3 casterPos = caster.getEyePosition(1.0F);
        playMagicSummonSound(level, casterPos);

        int size = 3;
        float range = 14.0F;

        Vec3 point = getBlockPoint(caster, range, 0, caster == owner);
        BlockPos pos = new BlockPos((int)point.x, (int)point.y, (int)point.z);
        for ( int xPos = pos.getX() - size; xPos <= pos.getX() + size; xPos++ ) {
            for ( int yPos = pos.getY() - size; yPos <= pos.getY() + size; yPos++ ) {
                for ( int zPos = pos.getZ() - size; zPos <= pos.getZ() + size; zPos++ ) {
                    BlockPos blockPos = new BlockPos(xPos, yPos, zPos);
                    BlockState blockState = level.getBlockState(blockPos);
                    if ( blockState.getBlock() != Blocks.BEDROCK && blockState.isSolid()
                            && (level.isEmptyBlock(blockPos.below()) || FallingBlock.isFree(level.getBlockState(blockPos.below()))) ) {
                        FallingBlockEntity.fall(level, blockPos, blockState);
                        state = true;
                    }
                }
            }
        }
        return state;
    }

    private static Vec3 getBlockPoint(Entity caster, float range, float error, boolean isPlayer) {
        int adjuster = 1;
        if ( !isPlayer ) adjuster = -1;

        Vec3 direction = ShadowEvents.calculateViewVector(caster.getXRot() * (float)adjuster, caster.getYRot() * (float)adjuster).normalize();
        direction = direction.multiply(range, range, range);
        Vec3 center = caster.getEyePosition(1.0F).add(direction);
        Vec3 returnPoint = center;
        double playerX = ShadowEvents.getEntityCenter(caster).x;
        double playerY = caster.getEyePosition(1.0F).y;
        double playerZ = ShadowEvents.getEntityCenter(caster).z;
        double listedEntityX = center.x();
        double listedEntityY = center.y();
        double listedEntityZ = center.z();
        int particleInterval = (int)Math.round(caster.distanceToSqr(center));

        for ( int k = 1; k < 1 + particleInterval; ++k ) {
            double lineX = playerX * (1.0 - (double)k / (double)particleInterval) + listedEntityX * ((double)k / (double)particleInterval);
            double lineY = playerY * (1.0 - (double)k / (double)particleInterval) + listedEntityY * ((double)k / (double)particleInterval);
            double lineZ = playerZ * (1.0 - (double)k / (double)particleInterval) + listedEntityZ * ((double)k / (double)particleInterval);

            returnPoint = new Vec3(lineX - (double)error, lineY - (double)error, lineZ - (double)error);
            if ( caster.level().getBlockState(new BlockPos((int)lineX, (int)lineY, (int)lineZ)).isSolid() ) break;
        }
        return returnPoint;
    }
}