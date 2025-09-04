package net.mindoth.ancientmagicks.revamp.item.rune;

import net.mindoth.ancientmagicks.revamp.MultiEntityHitResult;
import net.mindoth.ancientmagicks.revamp.SpellData;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.function.BiPredicate;

public class TargetEntityRuneItem extends UseOnEntityTemplate {
    public TargetEntityRuneItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public SpellData result(Entity caster, SpellData spellData, Entity entity) {
        if ( !spellData.getVectors().isEmpty() ) {
            Vec3 direction = spellData.getLatestVector();
            spellData.purgeVectors(1);
            Entity target = getPointedEntity(direction, entity.level(), entity, 4.5F, 0, true, null);
            if ( target != null ) spellData.addEntity(new MultiEntityHitResult(caster, target.position(), Collections.singletonList(target)));
            else spellData.addEntity(null);
        }
        else spellData.setValid(false);
        return spellData;
    }

    private Entity getPointedEntity(Vec3 direction, Level level, Entity caster, float range, float error, boolean stopsAtSolid, @Nullable BiPredicate<Entity, Entity> filter) {
        direction = direction.multiply(range, range, range);
        Vec3 center = caster.getEyePosition().add(direction);
        Entity returnEntity = null;
        double playerX = caster.getEyePosition().x;
        double playerY = caster.getEyePosition().y;
        double playerZ = caster.getEyePosition().z;
        double listedEntityX = center.x();
        double listedEntityY = center.y();
        double listedEntityZ = center.z();
        int particleInterval = (int)Math.round(caster.distanceToSqr(center));
        for ( int k = 1; k < (1 + particleInterval); k++ ) {
            double lineX = playerX * (1 - ((double) k / particleInterval)) + listedEntityX * ((double) k / particleInterval);
            double lineY = playerY * (1 - ((double) k / particleInterval)) + listedEntityY * ((double) k / particleInterval);
            double lineZ = playerZ * (1 - ((double) k / particleInterval)) + listedEntityZ * ((double) k / particleInterval);
            //((ServerLevel)level).sendParticles(ParticleTypes.FLAME, lineX, lineY, lineZ, 0, 0, 0, 0, 0);
            Vec3 start = new Vec3(lineX + error, lineY + error, lineZ + error);
            Vec3 end = new Vec3(lineX - error, lineY - error, lineZ - error);
            AABB area = new AABB(start, end);
            List<Entity> targets = level.getEntities(caster, area);
            Entity target = null;
            double lowestSoFar = Double.MAX_VALUE;
            for ( Entity closestSoFar : targets ) {
                if ( filter == null || filter.test(caster, closestSoFar) ) {
                    double testDistance = closestSoFar.distanceToSqr(center);
                    if ( testDistance < lowestSoFar ) target = closestSoFar;
                }
            }
            if ( target != null ) {
                returnEntity = target;
                break;
            }
            if ( stopsAtSolid && caster.level().getBlockState(new BlockPos(Mth.floor(lineX), Mth.floor(lineY), Mth.floor(lineZ))).isSolid() ) break;
        }
        return returnEntity;
    }
}
