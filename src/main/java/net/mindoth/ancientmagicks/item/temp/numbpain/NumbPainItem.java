package net.mindoth.ancientmagicks.item.temp.numbpain;

import net.mindoth.ancientmagicks.client.particle.ember.ParticleColor;
import net.mindoth.ancientmagicks.item.temp.abstractspell.AbstractSpellRayCast;
import net.mindoth.ancientmagicks.registries.AncientMagicksEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class NumbPainItem extends AbstractSpellRayCast {

    public NumbPainItem(Properties pProperties, int spellTier, int manaCost, int cooldown, SpellType spellType) {
        super(pProperties, spellTier, manaCost, cooldown, spellType);
    }

    @Override
    public ParticleColor.IntWrapper getParticleColor() {
        return ColorCode.BLACK.getParticleColor();
    }

    @Override
    protected int getLife() {
        return 200;
    }

    @Override
    protected int getRenderType() {
        return 3;
    }

    @Override
    protected boolean canApply(Level level, LivingEntity owner, Entity caster, Entity target) {
        return target instanceof LivingEntity;
    }

    @Override
    protected void applyEffect(Level level, LivingEntity owner, Entity caster, Entity target) {
        ((LivingEntity)target).addEffect(new MobEffectInstance(AncientMagicksEffects.NUMBNESS.get(), getLife(), 0));
    }
}
