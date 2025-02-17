package net.mindoth.ancientmagicks.item.temp.telekineticgrab;

import net.mindoth.ancientmagicks.item.SpellItem;
import net.mindoth.ancientmagicks.item.temp.abstractspell.spellpearl.SpellPearlEntity;
import net.mindoth.shadowizardlib.event.ShadowEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class TelekineticGrabItem extends SpellItem {

    public TelekineticGrabItem(Properties pProperties, int spellTier, int manaCost, int cooldown, SpellType spellType) {
        super(pProperties, spellTier, manaCost, cooldown, spellType);
    }

    @Override
    public boolean castMagic(LivingEntity owner, Entity caster, Vec3 center, int useTime) {
        boolean state = false;
        Level level = caster.level();
        Vec3 casterPos = caster.getEyePosition(1.0F);
        if ( owner != caster && owner != null && owner.level() == caster.level() ) casterPos = owner.getEyePosition(1.0F);

        float range = 14.0F;
        float size = 0.4F;
        if ( caster instanceof SpellPearlEntity) {
            range = 0.0F;
            size = 1.0F;
        }

        Vec3 soundPos = center;
        Entity target;
        if ( owner == caster ) target = getPointedItemEntity(level, caster, range, size);
        else target = getNearestItemEntity(caster, level, size);
        if ( target instanceof ItemEntity ) {
            ArrayList<ItemEntity> itemPile = getItemEntitiesAround(target, level, 1.0F, null);
            for ( ItemEntity itemEntity : itemPile ) {
                itemEntity.push((casterPos.x - itemEntity.getX()) / 6, (casterPos.y - itemEntity.getY()) / 6, (casterPos.z - itemEntity.getZ()) / 6);
                itemEntity.setNoPickUpDelay();
                state = true;
                soundPos = ShadowEvents.getEntityCenter(itemEntity);
            }
        }
        else if ( target == caster ) {
            BlockPos blockPos = getBlockPoint(caster, range, caster == owner);
            BlockState blockState = level.getBlockState(blockPos);
            Block block = blockState.getBlock();
            if ( block instanceof DoorBlock || block instanceof TrapDoorBlock || block instanceof FenceGateBlock ) {
                level.setBlock(blockPos, blockState.setValue(BlockStateProperties.OPEN, !blockState.getValue(BlockStateProperties.OPEN)), 10);
                state = true;
                soundPos = blockPos.getCenter();
            }
            else if ( block instanceof LeverBlock ) {
                level.setBlock(blockPos, blockState.setValue(BlockStateProperties.POWERED, !blockState.getValue(BlockStateProperties.POWERED)), 10);
                state = true;
                soundPos = blockPos.getCenter();
            }
            if ( block instanceof ButtonBlock button ) {
                button.press(blockState, level, blockPos);
                state = true;
                soundPos = blockPos.getCenter();
            }
        }

        if ( state ) {
            playMagicShootSound(level, soundPos);
        }

        return state;
    }

    private static ArrayList<ItemEntity> getItemEntitiesAround(Entity caster, Level pLevel, double size, @Nullable List<ItemEntity> exceptions) {
        ArrayList<ItemEntity> targets = (ArrayList<ItemEntity>) pLevel.getEntitiesOfClass(ItemEntity.class, caster.getBoundingBox().inflate(size));
        if ( exceptions != null && !exceptions.isEmpty() ) targets.removeIf(exceptions::contains);
        return targets;
    }

    private static Entity getNearestItemEntity(Entity caster, Level pLevel, double size) {
        ArrayList<ItemEntity> targets = getItemEntitiesAround(caster, pLevel, size, null);
        ItemEntity target = null;
        double lowestSoFar = Double.MAX_VALUE;
        for ( ItemEntity closestSoFar : targets ) {
            double testDistance = caster.distanceTo(closestSoFar);
            if ( testDistance < lowestSoFar ) {
                target = closestSoFar;
            }
        }
        return target;
    }

    private static Entity getPointedItemEntity(Level level, Entity caster, float range, float error) {
        Vec3 direction = ShadowEvents.calculateViewVector(caster.getXRot(), caster.getYRot()).normalize();
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

    public static BlockPos getBlockPoint(Entity caster, float range, boolean isPlayer) {
        Vec3 direction = ShadowEvents.calculateViewVector(caster.getXRot(), caster.getYRot()).normalize();
        direction = direction.multiply(range, range, range);
        Vec3 center = caster.getEyePosition().add(direction);
        double playerX = caster.getEyePosition().x;
        double playerY = caster.getEyePosition().y;
        double playerZ = caster.getEyePosition().z;
        double listedEntityX = center.x();
        double listedEntityY = center.y();
        double listedEntityZ = center.z();
        int particleInterval = (int)Math.round(caster.distanceToSqr(center));

        BlockPos blockPos = new BlockPos(Mth.floor(center.x), Mth.floor(center.y), Mth.floor(center.z));
        for ( int k = 1; k < 1 + particleInterval; ++k ) {
            double lineX = playerX * (1.0 - (double)k / (double)particleInterval) + listedEntityX * ((double)k / (double)particleInterval);
            double lineY = playerY * (1.0 - (double)k / (double)particleInterval) + listedEntityY * ((double)k / (double)particleInterval);
            double lineZ = playerZ * (1.0 - (double)k / (double)particleInterval) + listedEntityZ * ((double)k / (double)particleInterval);

            BlockPos tempPos = new BlockPos(Mth.floor(lineX), Mth.floor(lineY), Mth.floor(lineZ));
            if ( caster.level().getBlockState(tempPos).isSolid()
                    || caster.level().getBlockState(tempPos).getBlock() instanceof LeverBlock
                    || caster.level().getBlockState(tempPos).getBlock() instanceof ButtonBlock ) {
                blockPos = tempPos;
                break;
            }
        }
        return blockPos;
    }
}
