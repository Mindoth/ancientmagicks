package net.mindoth.ancientmagicks.item.spell;

import net.mindoth.ancientmagicks.item.SpellItem;
import net.mindoth.ancientmagicks.item.modifier.SpellModifierItem;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;

public class EffectSpell extends SpellItem {

    public EffectSpell(Properties pProperties, int manaCost, int cooldown) {
        super(pProperties, manaCost, cooldown);
    }

    protected boolean canApply(Level level, LivingEntity owner, Entity caster, Entity target) {
        return target instanceof LivingEntity && filter(owner, target);
    }

    protected MobEffect getEffect() {
        return null;
    }

    @Override
    public boolean castSpell(Level level, LivingEntity owner, Entity caster, Entity target, HashMap<String, Float> stats) {
        if ( target instanceof LivingEntity living && canApply(level, owner, caster, target) ) {
            addEnchantParticles(target, getParticleColor().r, getParticleColor().g, getParticleColor().b, 0.15F, 8);
            int amp = Math.max(0, (Mth.floor(stats.get(SpellItem.POWER)) - 1) / 10);
            living.addEffect(new MobEffectInstance(getEffect(), Mth.floor(stats.get(SpellItem.LIFE)), amp, false, isHarmful()));

            return true;
        }
        return false;
    }
}
