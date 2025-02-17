package net.mindoth.ancientmagicks.item.temp;

import net.mindoth.ancientmagicks.item.SpellItem;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class EffectSpell extends SpellItem {

    public EffectSpell(Properties pProperties, int manaCost, int cooldown) {
        super(pProperties, manaCost, cooldown);
    }

    protected int getLife() {
        return 600;
    }

    protected boolean canApply(Level level, LivingEntity owner, Entity caster, Entity target) {
        return filter(owner, target);
    }

    protected MobEffect getEffect() {
        return null;
    }

    @Override
    public boolean doSpell(Level level, LivingEntity owner, Entity caster, Entity target) {
        if ( target instanceof LivingEntity living && canApply(level, owner, caster, target) ) {
            living.addEffect(new MobEffectInstance(getEffect(), getLife(), 0, false, isHarmful()));
            return true;
        }
        return false;
    }
}
