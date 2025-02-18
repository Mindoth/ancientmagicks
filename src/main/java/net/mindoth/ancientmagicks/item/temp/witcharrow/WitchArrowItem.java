package net.mindoth.ancientmagicks.item.temp.witcharrow;

import net.mindoth.ancientmagicks.item.form.AbstractSpellEntity;
import net.mindoth.ancientmagicks.item.temp.abstractspell.AbstractSpellShoot;
import net.mindoth.shadowizardlib.event.ShadowEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class WitchArrowItem extends AbstractSpellShoot {

    public WitchArrowItem(Properties pProperties, int spellTier, int manaCost, int cooldown, SpellType spellType) {
        super(pProperties, spellTier, manaCost, cooldown, spellType);
    }

    @Override
    protected AbstractSpellEntity getProjectile(Level level, LivingEntity owner, Entity caster) {
        AbstractSpellEntity projectile = new WitchArrowEntity(level, owner, caster, this);
        float range = 64.0F;
        if ( owner == caster ) {
            Entity target = ShadowEvents.getPointedEntity(level, caster, range, 0.5F, true, this::allyFilter);
            if ( target != null && allyFilter(owner, target) ) projectile.target = target;
        }
        return projectile;
    }
}
