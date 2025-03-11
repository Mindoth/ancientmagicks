package net.mindoth.ancientmagicks.item.effect;

import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.HashMap;
import java.util.List;

public class PotionEffectItem extends EntityTargetEffect {

    public PotionEffectItem(Properties pProperties, int manaCost, int cooldown) {
        super(pProperties, manaCost, cooldown);
    }

    @Override
    protected boolean canApply(Level level, LivingEntity owner, Entity caster, HitResult result) {
        return result instanceof EntityHitResult entityHitResult && entityHitResult.getEntity() instanceof LivingEntity
                && allyFilter(owner, entityHitResult.getEntity(), isHarmful()) && mobTypeFilter(entityHitResult.getEntity());
    }

    protected MobEffect getEffect() {
        return null;
    }

    @Override
    protected boolean doSpell(Level level, LivingEntity owner, Entity caster, HitResult result, HashMap<String, Float> stats, String data) {
        LivingEntity target = (LivingEntity)((EntityHitResult)result).getEntity();
        addEnchantParticles(target, getParticleColor().r, getParticleColor().g, getParticleColor().b, 0.15F, 8);
        int amp = Mth.floor(Math.max(0, (stats.get(POWER) - 1) / 4));
        int life = 600 * Mth.floor(stats.get(LIFE));
        if ( getEffect().isInstantenous() ) life = 1;
        target.addEffect(new MobEffectInstance(getEffect(), life, amp, false, isHarmful()));
        return true;
    }
}
