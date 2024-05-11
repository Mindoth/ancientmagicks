package net.mindoth.ancientmagicks.item.spellrune.telekineticgrab;

import net.mindoth.ancientmagicks.item.modifierrune.ModifierRuneItem;
import net.mindoth.ancientmagicks.item.spellrune.SpellRuneItem;
import net.mindoth.shadowizardlib.event.ShadowEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TelekineticGrabRune extends SpellRuneItem {

    public TelekineticGrabRune(Properties pProperties, int cooldown) {
        super(pProperties, cooldown);
    }

    @Override
    public void shootMagic(PlayerEntity owner, Entity caster, Vector3d center, float xRot, float yRot, int useTime, List<ModifierRuneItem> modifierList) {
        World level = caster.level;
        Vector3d casterPos = caster.getEyePosition(1.0F);
        playMagicShootSound(level, casterPos);
        HashMap<String, Float> valueMap = new HashMap<>();

        valueMap.put("size", 1.0F);
        float range = 3.5F + (float)casterPos.distanceTo(center);
        for ( ModifierRuneItem rune : modifierList ) rune.addModifiersToValues(valueMap);
        if ( valueMap.get("size") <= 0 ) valueMap.put("size", 1.0F);

        Entity target = getPointedItemEntity(level, caster, range, valueMap.get("size") * 0.5F, caster == owner);
        if ( target instanceof ItemEntity ) {
            ArrayList<ItemEntity> itemPile = getItemEntitiesAround(target, level, valueMap.get("size"), null);
            for ( ItemEntity itemEntity : itemPile ) {
                itemEntity.push((casterPos.x - itemEntity.getX()) / 6, (casterPos.y - itemEntity.getY() + 1) / 6, (casterPos.z - itemEntity.getZ()) / 6);
                itemEntity.setNoPickUpDelay();
            }
        }
    }

    private static ArrayList<ItemEntity> getItemEntitiesAround(Entity caster, World pLevel, double size, @Nullable List<ItemEntity> exceptions) {
        ArrayList<ItemEntity> targets = (ArrayList<ItemEntity>) pLevel.getEntitiesOfClass(ItemEntity.class, caster.getBoundingBox().inflate(size));
        if ( exceptions != null && !exceptions.isEmpty() ) targets.removeIf(exceptions::contains);
        return targets;
    }

    private static Entity getPointedItemEntity(World level, Entity caster, float range, float error, boolean isPlayer) {
        int adjuster = 1;
        if ( !isPlayer ) adjuster = -1;
        Vector3d direction = ShadowEvents.calculateViewVector(caster.xRot * adjuster, caster.yRot * adjuster).normalize();
        direction = direction.multiply(range, range, range);
        Vector3d center = caster.getEyePosition(0).add(direction);
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
            Vector3d start = new Vector3d(lineX + error, lineY + error, lineZ + error);
            Vector3d end = new Vector3d(lineX - error, lineY - error, lineZ - error);
            AxisAlignedBB area = new AxisAlignedBB(start, end);
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
