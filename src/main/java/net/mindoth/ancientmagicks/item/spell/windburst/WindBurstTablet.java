package net.mindoth.ancientmagicks.item.spell.windburst;

import net.mindoth.ancientmagicks.item.TabletItem;
import net.mindoth.shadowizardlib.event.ShadowEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class WindBurstTablet extends TabletItem {

    public WindBurstTablet(Properties pProperties, int cooldown) {
        super(pProperties, cooldown);
    }

    @Override
    public boolean castMagic(Player owner, Entity caster, Vec3 center, float xRot, float yRot, int useTime) {
        boolean state = false;
        Level level = caster.level();
        Vec3 casterPos = caster.getEyePosition(1.0F);

        float range = 3.5F;
        float size = 0.25F;
        float power = 2;

        Entity target = getPointedPushableEntity(level, caster, range, 0.25F, caster == owner, true);
        Vec3 targetPoint = ShadowEvents.getPoint(level, caster, 1, 0.25F, caster == owner, false, true, true);
        if ( target != caster && isPushable(target) ) {
            List<Entity> pushEntity = ShadowEvents.getEntitiesAround(target, size * 0.25F, size * 0.25F, size * 0.25F);
            pushEntity.add(target);
            pushEntity.remove(caster);
            for ( Entity listEntity : pushEntity ) {
                if ( TabletItem.isPushable(listEntity) ) {
                    listEntity.push((targetPoint.x - casterPos.x) * power, (targetPoint.y - casterPos.y + 0.5F) * power, (targetPoint.z - casterPos.z) * power);
                    listEntity.hurtMarked = true;
                }
            }
        }
        Vec3 particlePoint = ShadowEvents.getPoint(level, caster, range, 0.25F, caster == owner, false, true, true);
        addParticles(level, casterPos, particlePoint);
        state = true;

        if ( state ) playWindSound(level, center);
        else playWhiffSound(level, center);

        return state;
    }

    private static void addParticles(Level level, Vec3 casterPos, Vec3 center) {
        float size = 0.1F;
        for ( int i = 0; i < 4; i++ ) {
            float randX = (float) ((Math.random() * (size - (-size))) + (-size));
            float randY = (float) ((Math.random() * (size - (-size))) + (-size));
            float randZ = (float) ((Math.random() * (size - (-size))) + (-size));
            ((ServerLevel)level).sendParticles(ParticleTypes.POOF, casterPos.x + randX, casterPos.y + randY, casterPos.z + randZ,
                    0, (center.x - casterPos.x) / 3, (center.y - casterPos.y) / 3, (center.z - casterPos.z) / 3, 0.5F);
        }
    }

    private static Entity getPointedPushableEntity(Level level, Entity caster, float range, float error, boolean isPlayer, boolean stopsAtSolid) {
        int adjuster = 1;
        if ( !isPlayer ) adjuster = -1;
        Vec3 direction = ShadowEvents.calculateViewVector(caster.getXRot() * adjuster, caster.getYRot() * adjuster).normalize();
        direction = direction.multiply(range, range, range);
        Vec3 center = caster.getEyePosition(0).add(direction);
        Entity returnEntity = caster;
        double playerX = ShadowEvents.getEntityCenter(caster).x;
        double playerY = ShadowEvents.getEntityCenter(caster).y;
        double playerZ = ShadowEvents.getEntityCenter(caster).z;
        double listedEntityX = center.x();
        double listedEntityY = center.y();
        double listedEntityZ = center.z();
        int particleInterval = (int)Math.round(caster.distanceToSqr(center));
        for ( int k = 1; k < (1 + particleInterval); k++ ) {
            double lineX = playerX * (1 - ((double) k / particleInterval)) + listedEntityX * ((double) k / particleInterval);
            double lineY = playerY * (1 - ((double) k / particleInterval)) + listedEntityY * ((double) k / particleInterval);
            double lineZ = playerZ * (1 - ((double) k / particleInterval)) + listedEntityZ * ((double) k / particleInterval);
            Vec3 start = new Vec3(lineX + error, lineY + error, lineZ + error);
            Vec3 end = new Vec3(lineX - error, lineY - error, lineZ - error);
            AABB area = new AABB(start, end);
            List<Entity> targets = level.getEntities(caster, area);
            Entity target = null;
            double lowestSoFar = Double.MAX_VALUE;
            for ( Entity closestSoFar : targets ) {
                if ( TabletItem.isPushable(closestSoFar) ) {
                    double testDistance = closestSoFar.distanceToSqr(center);
                    if ( testDistance < lowestSoFar ) {
                        target = closestSoFar;
                    }
                }
            }
            if ( target != null ) {
                returnEntity = target;
                break;
            }
            if ( stopsAtSolid && caster.level().getBlockState(new BlockPos((int)lineX, (int)lineY, (int)lineZ)).isSolid() ) break;
        }
        return returnEntity;
    }
}
