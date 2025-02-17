package net.mindoth.ancientmagicks.item.temp.frostarmor;

import net.mindoth.ancientmagicks.client.particle.ember.ParticleColor;
import net.mindoth.ancientmagicks.item.temp.abstractspell.AbstractArmorSpell;
import net.mindoth.ancientmagicks.registries.AncientMagicksEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class FrostArmorItem extends AbstractArmorSpell {

    public FrostArmorItem(Properties pProperties, int spellTier, int manaCost, int cooldown, SpellType spellType) {
        super(pProperties, spellTier, manaCost, cooldown, spellType);
    }

    @Override
    public ParticleColor.IntWrapper getParticleColor() {
        return ColorCode.AQUA.getParticleColor();
    }

    @Override
    protected void applyEffect(Level level, LivingEntity owner, Entity caster, Entity target) {
        if ( caster instanceof LivingEntity living ) {
            living.addEffect(new MobEffectInstance(AncientMagicksEffects.FROST_ARMOR.get(), getLife(), 0, false, false));
        }
    }
}
