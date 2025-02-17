package net.mindoth.ancientmagicks.item.spell.witcharmor;

import net.mindoth.ancientmagicks.item.spell.abstractspell.AbstractArmorSpell;
import net.mindoth.ancientmagicks.registries.AncientMagicksEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class WitchArmorItem extends AbstractArmorSpell {

    public WitchArmorItem(Properties pProperties, int spellTier, int manaCost, int cooldown, SpellType spellType) {
        super(pProperties, spellTier, manaCost, cooldown, spellType);
    }

    @Override
    protected void applyEffect(Level level, LivingEntity owner, Entity caster, Entity target) {
        if ( caster instanceof LivingEntity living ) {
            living.addEffect(new MobEffectInstance(AncientMagicksEffects.WITCH_ARMOR.get(), getLife(), 0, false, false));
        }
    }
}
