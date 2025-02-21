package net.mindoth.ancientmagicks.item.modifier;

import net.mindoth.ancientmagicks.item.spell.SpellItem;
import net.mindoth.ancientmagicks.item.form.entity.AbstractSpellEntity;
import net.mindoth.ancientmagicks.item.form.entity.ProjectileSpellEntity;

import java.util.HashMap;

public class DiminishModifierItem extends SpellModifierItem {

    public DiminishModifierItem(Properties pProperties, int manaCost, int cooldown) {
        super(pProperties, manaCost, cooldown);
    }

    @Override
    public void addModifierToEntity(ProjectileSpellEntity projectile, int count) {
        if ( projectile != null ) {
            projectile.getEntityData().set(AbstractSpellEntity.LIFE, Math.max(0, projectile.getEntityData().get(AbstractSpellEntity.LIFE) - count * 20));
        }
    }

    @Override
    public void addStatsToMap(HashMap<String, Float> stats) {
        stats.merge(SpellItem.LIFE, -20.0F, Float::sum);
        stats.merge(SpellItem.LIFE, 0.0F, Float::max);
    }
}
