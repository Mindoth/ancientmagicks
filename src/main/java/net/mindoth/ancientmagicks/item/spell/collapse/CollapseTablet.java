package net.mindoth.ancientmagicks.item.spell.collapse;

import net.mindoth.ancientmagicks.item.castingitem.TabletItem;
import net.mindoth.shadowizardlib.event.ShadowEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class CollapseTablet extends TabletItem {

    public CollapseTablet(Properties pProperties, int tier, boolean isChannel, int cooldown) {
        super(pProperties, tier, isChannel, cooldown);
    }

    @Override
    public boolean castMagic(Player owner, Entity caster, Vec3 center, float xRot, float yRot, int useTime) {
        boolean state = false;
        Level level = caster.level();

        int size = 4;
        float range = 14.0F;

        Vec3 returnPoint = getBlockPoint(caster, range, caster == owner);
        BlockPos pos = new BlockPos(Mth.floor(returnPoint.x), Mth.floor(returnPoint.y), Mth.floor(returnPoint.z));
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

        if ( state ) playMagicSummonSound(level, center);

        return state;
    }

    public static Vec3 getBlockPoint(Entity caster, float range, boolean isPlayer) {
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

            returnPoint = new Vec3(lineX, lineY, lineZ);
            BlockPos blockPos = new BlockPos(Mth.floor(returnPoint.x), Mth.floor(returnPoint.y), Mth.floor(returnPoint.z));
            if ( caster.level().getBlockState(blockPos).isSolid() ) break;
        }
        return returnPoint;
    }
}