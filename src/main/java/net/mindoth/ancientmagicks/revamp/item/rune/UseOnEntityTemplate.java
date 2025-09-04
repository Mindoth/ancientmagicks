package net.mindoth.ancientmagicks.revamp.item.rune;

import net.mindoth.ancientmagicks.revamp.MultiBlockHitResult;
import net.mindoth.ancientmagicks.revamp.MultiEntityHitResult;
import net.mindoth.ancientmagicks.revamp.SpellData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Collections;

public class UseOnEntityTemplate extends RuneItem {
    public UseOnEntityTemplate(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public SpellData resolve(Entity caster, SpellData spellData) {
        if ( !spellData.getEntities().isEmpty() ) {
            if ( spellData.getLatestEntity() != null ) {
                MultiEntityHitResult result = spellData.getLatestEntity();
                spellData.purgeEntities(1);

                final SpellData copyData = SpellData.clone(spellData);
                spellData = result(caster, spellData, result.getEntities().get(0));
                boolean state = spellData.isValid();
                for ( int i = 1; i < result.getEntities().size(); i++ ) {
                    final SpellData tempData = SpellData.clone(copyData);
                    Entity entity = result.getEntities().get(i);
                    result(caster, tempData, entity);
                    if ( !state && tempData.isValid() ) state = true;
                }
                spellData.setValid(state);
            }
        }
        else spellData.setValid(false);
        return spellData;
    }

    protected SpellData result(Entity caster, SpellData spellData, Entity entity) {
        return spellData;
    }

    public static void attackEntity(Entity owner, Entity caster, Entity target, float amount) {
        target.hurt(target.damageSources().indirectMagic(caster, owner), amount);
    }

    public static void attackEntityWithoutKnockback(Entity owner, Entity caster, Entity target, float amount) {
        final double vx = target.getDeltaMovement().x;
        final double vy = target.getDeltaMovement().y;
        final double vz = target.getDeltaMovement().z;
        attackEntity(owner, target, caster, amount);
        target.setDeltaMovement(vx, vy, vz);
        target.hurtMarked = true;
    }

    public static boolean isPushable(Entity entity) {
        return ( entity instanceof LivingEntity || entity instanceof ItemEntity || entity instanceof PrimedTnt || entity instanceof FallingBlockEntity);
    }

    public static MultiBlockHitResult getPOVHitResult(Vec3 direction, Level pLevel, Entity entity, ClipContext.Fluid pFluidMode, float range) {
        direction = direction.multiply(range, range, range);
        Vec3 vec3 = entity.getEyePosition();
        Vec3 vec31 = vec3.add(direction);
        BlockHitResult result = pLevel.clip(new ClipContext(vec3, vec31, ClipContext.Block.OUTLINE, pFluidMode, entity));
        return new MultiBlockHitResult(result.getLocation(), result.getDirection(), result.getBlockPos(), result.isInside(), Collections.singletonList(result.getBlockPos()), pLevel);
    }
}
