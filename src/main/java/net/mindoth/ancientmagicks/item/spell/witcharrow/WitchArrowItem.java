package net.mindoth.ancientmagicks.item.spell.witcharrow;

import net.mindoth.ancientmagicks.item.spell.abstractspell.AbstractSpellEntity;
import net.mindoth.ancientmagicks.item.spell.abstractspell.AbstractSpellShoot;
import net.mindoth.ancientmagicks.item.spell.abstractspell.SpellSchool;
import net.mindoth.shadowizardlib.event.ShadowEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class WitchArrowItem extends AbstractSpellShoot {

    public WitchArrowItem(Properties pProperties, int spellTier, int manaCost, int cooldown, SpellSchool spellSchool) {
        super(pProperties, spellTier, manaCost, cooldown, spellSchool);
    }

    @Override
    protected AbstractSpellEntity getProjectile(Level level, Player owner, Entity caster) {
        AbstractSpellEntity projectile = new WitchArrowEntity(level, owner, caster, this);
        float range = 64.0F;
        if ( owner != caster ) range = 0.0F;
        Entity target = ShadowEvents.getPointedEntity(level, caster, range, 0.5F, caster == owner, true);
        if ( target != null && target != caster && (target instanceof LivingEntity living && !isAlly(owner, living)) ) projectile.target = target;
        return projectile;
    }
}
