package net.mindoth.ancientmagicks.item.temp.greaterinvisibility;

import net.mindoth.ancientmagicks.item.temp.abstractspell.AbstractSpellRayCast;
import net.mindoth.ancientmagicks.registries.AncientMagicksEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class GreaterInvisibilityItem extends AbstractSpellRayCast {

    public GreaterInvisibilityItem(Properties pProperties, int spellTier, int manaCost, int cooldown, SpellType spellType) {
        super(pProperties, spellTier, manaCost, cooldown, spellType);
    }

    @Override
    protected void applyEffect(Level level, LivingEntity owner, Entity caster, Entity target) {
        ((LivingEntity)target).addEffect(new MobEffectInstance(AncientMagicksEffects.GREATER_INVISIBILITY.get(), getLife(), 0, false, isHarmful()));
    }
}
