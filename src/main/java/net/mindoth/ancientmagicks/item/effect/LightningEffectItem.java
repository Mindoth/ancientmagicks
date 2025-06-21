package net.mindoth.ancientmagicks.item.effect;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.List;

public class LightningEffectItem extends SpellEffectItem {

    public LightningEffectItem(Properties pProperties, int cost) {
        super(pProperties, cost);
    }

    @Override
    protected boolean doSpell(Level level, LivingEntity owner, Entity caster, HitResult result, HashMap<String, Float> stats, String data) {
        boolean state = false;
        float range = stats.get(AOE);
        Vec3 center = result.getLocation();
        if ( range > 0.0F ) {
            range *= 2;
            Vec3 start = new Vec3(center.x + range, center.y + range, center.z + range);
            Vec3 end = new Vec3(center.x - range, center.y - range, center.z - range);
            AABB box = new AABB(start, end);
            List<Entity> entities = level.getEntitiesOfClass(Entity.class, box);
            if ( result instanceof EntityHitResult entityHitResult ) entities.remove(entityHitResult.getEntity());
            for ( Entity entity : entities ) {
                EntityHitResult entityHitResult = new EntityHitResult(entity);
                if ( canApply(level, owner, caster, entityHitResult, data) ) lightning(level, caster, entityHitResult, stats);
            }
            state = true;
        }
        else if ( canApply(level, owner, caster, result, data) ) state = lightning(level, caster, result, stats);
        return state;
    }

    private boolean lightning(Level level, Entity caster, HitResult result, HashMap<String, Float> stats) {
        boolean state = false;
        int power = 5 + Mth.floor(stats.get(SpellEffectItem.POWER));
        Vec3 point = result.getLocation();
        BlockPos blockPos = new BlockPos(Mth.floor(point.x), Mth.floor(point.y), Mth.floor(point.z));
        BlockState blockState = level.getBlockState(blockPos.below());
        if ( !blockState.isSolid() || !(blockState.getBlock() instanceof LiquidBlock) ) blockPos = getBlockBelow(level, blockPos);
        if ( level.canSeeSkyFromBelowWater(blockPos) ) {
            LightningBolt lightningbolt = EntityType.LIGHTNING_BOLT.create(level);
            if ( lightningbolt != null && !level.isClientSide ) {
                lightningbolt.moveTo(blockPos.getCenter().x, blockPos.getCenter().y - 0.5D, blockPos.getCenter().z);
                lightningbolt.setCause(caster instanceof ServerPlayer ? (ServerPlayer)caster : null);
                lightningbolt.setDamage(power);
                level.addFreshEntity(lightningbolt);
                state = true;
            }
        }
        return state;
    }

    private BlockPos getBlockBelow(Level level, BlockPos blockPos) {
        for ( int i = blockPos.getY(); i > level.getMinBuildHeight(); i-- ) {
            BlockPos tempPos = new BlockPos(blockPos.getX(), i, blockPos.getZ());
            if ( level.getBlockState(tempPos.below()).isSolid() || level.getBlockState(tempPos).getBlock() instanceof LiquidBlock ) {
                blockPos = tempPos;
                break;
            }
        }
        return blockPos;
    }
}
