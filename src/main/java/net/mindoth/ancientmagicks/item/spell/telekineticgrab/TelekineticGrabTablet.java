package net.mindoth.ancientmagicks.item.spell.telekineticgrab;

import net.mindoth.ancientmagicks.item.castingitem.SpellTabletItem;
import net.mindoth.shadowizardlib.event.ShadowEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class TelekineticGrabTablet extends SpellTabletItem {

    public TelekineticGrabTablet(Properties pProperties, int tier, boolean isChannel, int cooldown) {
        super(pProperties, tier, isChannel, cooldown);
    }

    @Override
    public boolean castMagic(Player owner, Entity caster, Vec3 center, float xRot, float yRot, int useTime) {
        boolean state = false;
        Level level = caster.level();
        Vec3 casterPos = caster.getEyePosition(1.0F);

        float range = 14.0F;
        float size = 0.5F;

        Entity target = getPointedItemEntity(level, caster, range, size, caster == owner);
        if ( target instanceof ItemEntity) {
            ArrayList<ItemEntity> itemPile = getItemEntitiesAround(target, level, 1.0F, null);
            for ( ItemEntity itemEntity : itemPile ) {
                itemEntity.push((casterPos.x - itemEntity.getX()) / 6, (casterPos.y - itemEntity.getY()) / 6, (casterPos.z - itemEntity.getZ()) / 6);
                itemEntity.setNoPickUpDelay();
                state = true;
            }
        }

        if ( state ) playMagicShootSound(level, center);

        return state;
    }

    private static ArrayList<ItemEntity> getItemEntitiesAround(Entity caster, Level pLevel, double size, @Nullable List<ItemEntity> exceptions) {
        ArrayList<ItemEntity> targets = (ArrayList<ItemEntity>) pLevel.getEntitiesOfClass(ItemEntity.class, caster.getBoundingBox().inflate(size));
        if ( exceptions != null && !exceptions.isEmpty() ) targets.removeIf(exceptions::contains);
        return targets;
    }

    private static Entity getPointedItemEntity(Level level, Entity caster, float range, float error, boolean isPlayer) {
        int adjuster = 1;
        if ( !isPlayer ) adjuster = -1;
        Vec3 direction = ShadowEvents.calculateViewVector(caster.getXRot() * adjuster, caster.getYRot() * adjuster).normalize();
        direction = direction.multiply(range, range, range);
        Vec3 center = caster.getEyePosition(0).add(direction);
        Entity returnEntity = caster;
        double playerX = ShadowEvents.getEntityCenter(caster).x;
        double playerY = caster.getEyePosition(1.0F).y;
        double playerZ = ShadowEvents.getEntityCenter(caster).z;
        double listedEntityX = center.x();
        double listedEntityY = center.y();
        double listedEntityZ = center.z();
        int particleInterval = (int)Math.round(caster.distanceToSqr(center));
        for ( int k = 1; k < (1 + particleInterval); k++ ) {
            double lineX = playerX * (1 - ((double) k / particleInterval)) + listedEntityX * ((double) k / particleInterval);
            double lineY = playerY * (1 - ((double) k / particleInterval)) + listedEntityY * ((double) k / particleInterval);
            double lineZ = playerZ * (1 - ((double) k / particleInterval)) + listedEntityZ * ((double) k / particleInterval);
            //float error = 0.25F;
            Vec3 start = new Vec3(lineX + error, lineY + error, lineZ + error);
            Vec3 end = new Vec3(lineX - error, lineY - error, lineZ - error);
            AABB area = new AABB(start, end);
            List<Entity> targets = level.getEntities(caster, area);
            Entity target = null;
            double lowestSoFar = Double.MAX_VALUE;
            for ( Entity closestSoFar : targets ) {
                if ( closestSoFar instanceof ItemEntity ) {
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
        }
        return returnEntity;
    }
}
