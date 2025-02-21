package net.mindoth.ancientmagicks.item.modifier;

import net.mindoth.ancientmagicks.item.spell.SpellItem;
import net.mindoth.ancientmagicks.item.form.entity.AbstractSpellEntity;
import net.mindoth.ancientmagicks.item.form.entity.ProjectileSpellEntity;

import java.util.HashMap;

public class QuickenModifierItem extends SpellModifierItem {

    public QuickenModifierItem(Properties pProperties, int manaCost, int cooldown) {
        super(pProperties, manaCost, cooldown);
    }

    @Override
    public void addModifierToEntity(ProjectileSpellEntity projectile, int count) {
        if ( projectile != null ) {
            projectile.getEntityData().set(AbstractSpellEntity.SPEED, projectile.getEntityData().get(AbstractSpellEntity.SPEED) + count * 0.2F);
        }
    }

    @Override
    public void addStatsToMap(HashMap<String, Float> stats) {
        stats.merge(SpellItem.SPEED, 0.2F, Float::sum);
    }
}
