package net.mindoth.ancientmagicks.revamp.item.rune;

import net.mindoth.ancientmagicks.revamp.MultiEntityHitResult;
import net.mindoth.ancientmagicks.revamp.SpellData;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class AreaTargetEntityRuneItem extends RuneItem {
    public AreaTargetEntityRuneItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public SpellData resolve(Entity caster, SpellData spellData) {
        if ( !spellData.getVectors().isEmpty() && spellData.getVectors().size() >= 2 ) {
            Vec3 position = spellData.getLatestVector();
            spellData.purgeVectors(1);
            Vec3 direction = spellData.getLatestVector();
            spellData.purgeVectors(1);
            Level level;
            if ( spellData.getDimensions().isEmpty() || spellData.getLatestDimension() == null ) level = caster.level();
            else level = spellData.getLatestDimension();
            spellData.purgeDimensions(1);
            int range;
            if ( spellData.getIntegers().isEmpty() || spellData.getLatestInteger() == null ) range = 1;
            else range = spellData.getLatestInteger();
            spellData.purgeIntegers(1);
            Vec3 pos = getPoint(position, direction, caster, level, 4.5F, 0, false, true, true, false);

            Vec3 start = new Vec3(pos.x + range, pos.y + range, pos.z + range);
            Vec3 end = new Vec3(pos.x - range, pos.y - range, pos.z - range);
            AABB box = new AABB(start, end);
            List<Entity> entities = level.getEntitiesOfClass(Entity.class, box);

            if ( entities.isEmpty() ) spellData.addEntity(null);
            else spellData.addEntity(new MultiEntityHitResult(caster, pos, entities));
            aoeEntitySpellParticles(level, box, range, defaultStats());
        }
        else spellData.setValid(false);
        return spellData;
    }

    public static Vec3 getPoint(Vec3 position, Vec3 direction, Entity caster, Level level, float range, float error, boolean centerBlock, boolean stopsAtEntity, boolean stopsAtSolid, boolean stopsAtLiquid) {
        direction = direction.multiply(range, range, range);
        Vec3 center = position.add(direction);
        Vec3 returnPoint = center;
        double playerX = position.x();
        double playerY = position.y();
        double playerZ = position.z();
        double listedEntityX = center.x();
        double listedEntityY = center.y();
        double listedEntityZ = center.z();
        int particleInterval = (int)Math.round(position.distanceToSqr(center));
        for ( int k = 1; k < 1 + particleInterval; ++k ) {
            double lineX = playerX * (1.0D - (double)k / (double)particleInterval) + listedEntityX * ((double)k / (double)particleInterval);
            double lineY = playerY * (1.0D - (double)k / (double)particleInterval) + listedEntityY * ((double)k / (double)particleInterval);
            double lineZ = playerZ * (1.0D - (double)k / (double)particleInterval) + listedEntityZ * ((double)k / (double)particleInterval);
            //((ServerLevel)level).sendParticles(ParticleTypes.FLAME, lineX, lineY, lineZ, 0, 0, 0, 0, 0);
            Vec3 start = new Vec3(lineX + error, lineY + error, lineZ + error);
            Vec3 end = new Vec3(lineX - error, lineY - error, lineZ - error);
            AABB area = new AABB(start, end);
            List<Entity> targets = level.getEntities(caster, area);
            Entity target = null;
            double lowestSoFar = Double.MAX_VALUE;
            for ( Entity closestSoFar : targets ) {
                if ( closestSoFar instanceof LivingEntity ) {
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
            if ( stopsAtLiquid && level.getBlockState(new BlockPos(Mth.floor(lineX), Mth.floor(lineY), Mth.floor(lineZ))).getBlock() instanceof LiquidBlock ) {
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
