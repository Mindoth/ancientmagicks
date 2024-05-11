package net.mindoth.ancientmagicks.item.spellrune.windburst;

import net.mindoth.ancientmagicks.item.modifierrune.ModifierRuneItem;
import net.mindoth.ancientmagicks.item.spellrune.SpellRuneItem;
import net.mindoth.shadowizardlib.event.ShadowEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.HashMap;
import java.util.List;

public class WindBurstRune extends SpellRuneItem {

    public WindBurstRune(Properties pProperties, int cooldown) {
        super(pProperties, cooldown);
    }

    @Override
    public void shootMagic(PlayerEntity owner, Entity caster, Vector3d center, float xRot, float yRot, int useTime, List<ModifierRuneItem> modifierList) {
        World level = caster.level;
        Vector3d casterPos = caster.getEyePosition(1.0F);
        playWindSound(level, center);
        HashMap<String, Float> valueMap = new HashMap<>();

        valueMap.put("size", 1.0F);
        valueMap.put("power", 1.0F);
        valueMap.put("blockPierce", 0.0F);
        float range = 3.5F + (float)casterPos.distanceTo(center);
        for ( ModifierRuneItem rune : modifierList ) rune.addModifiersToValues(valueMap);
        if ( valueMap.get("size") <= 0 ) valueMap.put("size", 1.0F);
        float size = 0.5F * valueMap.get("size");

        Entity target = getPointedPushableEntity(level, caster, range, 0.25F, caster == owner, valueMap.get("blockPierce") == 0);
        Vector3d targetPoint = ShadowEvents.getPoint(level, caster, 1, 0.25F, caster == owner, false, true, valueMap.get("blockPierce") == 0);
        if ( target != caster && isPushable(target) ) {
            List<Entity> pushEntity = ShadowEvents.getEntitiesAround(target, size * 0.25F, size * 0.25F, size * 0.25F);
            pushEntity.add(target);
            pushEntity.remove(caster);
            for ( Entity listEntity : pushEntity ) {
                if ( SpellRuneItem.isPushable(listEntity) ) {
                    float power = valueMap.get("power");
                    listEntity.push((targetPoint.x - casterPos.x) * power, (targetPoint.y - casterPos.y + 0.5F) * power, (targetPoint.z - casterPos.z) * power);
                }
            }
        }
        Vector3d particlePoint = ShadowEvents.getPoint(level, caster, range, 0.25F, caster == owner, false, true, valueMap.get("blockPierce") == 0);
        addParticles(level, casterPos, particlePoint);
    }

    private static void addParticles(World level, Vector3d casterPos, Vector3d center) {
        float size = 0.1F;
        for ( int i = 0; i < 4; i++ ) {
            float randX = (float) ((Math.random() * (size - (-size))) + (-size));
            float randY = (float) ((Math.random() * (size - (-size))) + (-size));
            float randZ = (float) ((Math.random() * (size - (-size))) + (-size));
            ((ServerWorld)level).sendParticles(ParticleTypes.POOF, casterPos.x + randX, casterPos.y + randY, casterPos.z + randZ,
                    0, (center.x - casterPos.x) / 3, (center.y - casterPos.y) / 3, (center.z - casterPos.z) / 3, 0.5F);
        }
    }

    private static Entity getPointedPushableEntity(World level, Entity caster, float range, float error, boolean isPlayer, boolean stopsAtSolid) {
        int adjuster = 1;
        if ( !isPlayer ) adjuster = -1;
        Vector3d direction = ShadowEvents.calculateViewVector(caster.xRot * adjuster, caster.yRot * adjuster).normalize();
        direction = direction.multiply(range, range, range);
        Vector3d center = caster.getEyePosition(0).add(direction);
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
            Vector3d start = new Vector3d(lineX + error, lineY + error, lineZ + error);
            Vector3d end = new Vector3d(lineX - error, lineY - error, lineZ - error);
            AxisAlignedBB area = new AxisAlignedBB(start, end);
            List<Entity> targets = level.getEntities(caster, area);
            Entity target = null;
            double lowestSoFar = Double.MAX_VALUE;
            for ( Entity closestSoFar : targets ) {
                if ( SpellRuneItem.isPushable(closestSoFar) ) {
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
            if ( stopsAtSolid && caster.level.getBlockState(new BlockPos(lineX, lineY, lineZ)).getMaterial().isSolid() ) break;
        }
        return returnEntity;
    }
}
