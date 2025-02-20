package net.mindoth.ancientmagicks.item.modifier;

import net.mindoth.ancientmagicks.item.spell.SpellItem;
import net.mindoth.ancientmagicks.item.form.AbstractSpellEntity;
import net.mindoth.ancientmagicks.item.form.ProjectileSpellEntity;

import java.util.HashMap;

public class ExtendModifierItem extends SpellModifierItem {

    public ExtendModifierItem(Properties pProperties, int manaCost, int cooldown) {
        super(pProperties, manaCost, cooldown);
    }

    @Override
    public void addModifierToEntity(ProjectileSpellEntity projectile, int count) {
        if ( projectile != null ) {
            projectile.getEntityData().set(AbstractSpellEntity.LIFE, projectile.getEntityData().get(AbstractSpellEntity.LIFE) + count * 20);
        }
    }

    @Override
    public void addStatsToMap(HashMap<String, Float> stats) {
        stats.merge(SpellItem.LIFE, 20.0F, Float::sum);
    }
}
