package net.mindoth.ancientmagicks.revamp.item.rune;

import net.mindoth.ancientmagicks.revamp.SpellData;
import net.mindoth.shadowizardlib.event.ShadowEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class TargetPositionRuneItem extends RuneItem {
    public TargetPositionRuneItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public SpellData resolve(Entity caster, SpellData spellData) {
        if ( !spellData.getEntities().isEmpty() && !spellData.getVectors().isEmpty() ) {
            Entity entity = spellData.getEntities().get(spellData.getEntities().size() - 1);
            spellData.purgeEntities(1);
            Vec3 direction = spellData.getVectors().get(spellData.getVectors().size() - 1);
            spellData.purgeVectors(1);
            if ( entity != null ) {
                Vec3 target = getPoint(direction, entity.level(), entity, 4.5F, 0, false, false, true, false);
                spellData.addVector(target);
            }
            else spellData.addVector(null);
        }
        else spellData.setValid(false);
        return spellData;
    }

    public static Vec3 getPoint(Vec3 direction, Level level, Entity caster, float range, float error, boolean centerBlock, boolean stopsAtEntity, boolean stopsAtSolid, boolean stopsAtLiquid) {
        direction = direction.multiply((double)range, (double)range, (double)range);
        Vec3 center = caster.getEyePosition().add(direction);
        Vec3 returnPoint = center;
        double playerX = caster.getEyePosition().x;
        double playerY = caster.getEyePosition().y;
        double playerZ = caster.getEyePosition().z;
        double listedEntityX = center.x();
        double listedEntityY = center.y();
        double listedEntityZ = center.z();
        int particleInterval = (int)Math.round(caster.distanceToSqr(center));
        for ( int k = 1; k < 1 + particleInterval; ++k ) {
            double lineX = playerX * (1.0D - (double)k / (double)particleInterval) + listedEntityX * ((double)k / (double)particleInterval);
            double lineY = playerY * (1.0D - (double)k / (double)particleInterval) + listedEntityY * ((double)k / (double)particleInterval);
            double lineZ = playerZ * (1.0D - (double)k / (double)particleInterval) + listedEntityZ * ((double)k / (double)particleInterval);
            Vec3 start = new Vec3(lineX + error, lineY + error, lineZ + error);
            Vec3 end = new Vec3(lineX - error, lineY - error, lineZ - error);
            AABB area = new AABB(start, end);
            List<Entity> targets = level.getEntities(caster, area);
            Entity target = null;
            double lowestSoFar = Double.MAX_VALUE;
            for ( Entity closestSoFar : targets ) {
                if ( closestSoFar instanceof LivingEntity) {
                    double testDistance = closestSoFar.distanceToSqr(center);
                    if ( testDistance < lowestSoFar ) target = closestSoFar;
                }
            }
            if ( stopsAtEntity && target != null ) {
                if ( centerBlock ) {
                    BlockPos pos = target.blockPosition();
                    returnPoint = pos.getCenter();
                }
                break;
            }
            if ( stopsAtLiquid && level.getBlockState(new BlockPos(Mth.floor(lineX), Mth.floor(lineY), Mth.floor(lineZ))).getBlock() instanceof LiquidBlock) {
                if ( centerBlock ) {
                    BlockPos pos = new BlockPos(Mth.floor(returnPoint.x), Mth.floor(returnPoint.y), Mth.floor(returnPoint.z));
                    returnPoint = pos.getCenter();
                }
                break;
            }
            if ( stopsAtSolid && level.getBlockState(new BlockPos(Mth.floor(lineX), Mth.floor(lineY), Mth.floor(lineZ))).isSolid() ) {
                if ( centerBlock ) {
                    BlockPos pos = new BlockPos(Mth.floor(returnPoint.x), Mth.floor(returnPoint.y), Mth.floor(returnPoint.z));
                    returnPoint = pos.getCenter();
                }
                break;
            }
            returnPoint = new Vec3(lineX, lineY, lineZ);
        }
        return returnPoint;
    }
}
