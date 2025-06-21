package net.mindoth.ancientmagicks.item.effect;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class EntityTargetEffect extends SpellEffectItem {

    public EntityTargetEffect(Properties pProperties, int cost) {
        super(pProperties, cost);
    }

    @Override
    protected boolean canApply(Level level, LivingEntity owner, Entity caster, HitResult result, String data) {
        return result instanceof EntityHitResult entityHitResult && allyFilter(owner, entityHitResult.getEntity(), isHarmful(data));
    }
}
