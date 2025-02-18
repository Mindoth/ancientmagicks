package net.mindoth.ancientmagicks.item.spell;

import net.mindoth.ancientmagicks.item.SpellItem;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.HashMap;

public class EffectSpell extends EntityTargetSpell {

    public EffectSpell(Properties pProperties, int manaCost, int cooldown) {
        super(pProperties, manaCost, cooldown);
    }

    protected MobEffect getEffect() {
        return null;
    }

    @Override
    protected boolean canApply(Level level, LivingEntity owner, Entity caster, HitResult result) {
        return result instanceof EntityHitResult entityHitResult && entityHitResult.getEntity() instanceof LivingEntity
                && allyFilter(owner, entityHitResult.getEntity()) && mobTypeFilter(entityHitResult.getEntity());
    }

    protected boolean mobTypeFilter(Entity target) {
        return true;
    }

    @Override
    protected boolean doSpell(Level level, LivingEntity owner, Entity caster, HitResult result, HashMap<String, Float> stats) {
        LivingEntity target = (LivingEntity)((EntityHitResult)result).getEntity();
        addEnchantParticles(target, getParticleColor().r, getParticleColor().g, getParticleColor().b, 0.15F, 8);
        int amp = Math.max(0, (Mth.floor(stats.get(SpellItem.POWER)) - 1) / 10);
        target.addEffect(new MobEffectInstance(getEffect(), Mth.floor(stats.get(SpellItem.LIFE)), amp, false, isHarmful()));
        return true;
    }
}
