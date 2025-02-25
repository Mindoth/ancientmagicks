package net.mindoth.ancientmagicks.item.modifier;

import net.mindoth.ancientmagicks.item.form.entity.AbstractSpellEntity;

import java.util.HashMap;

public class ExpandModifierItem extends SpellModifierItem {

    public ExpandModifierItem(Properties pProperties, int manaCost, int cooldown) {
        super(pProperties, manaCost, cooldown);
    }

    @Override
    public void addModifierOnEntityCreation(AbstractSpellEntity projectile, int count) {
        projectile.getEntityData().set(AbstractSpellEntity.AOE, projectile.getEntityData().get(AbstractSpellEntity.AOE) + (float)count);
    }

    @Override
    public void addStatsToMap(HashMap<String, Float> stats) {
        stats.merge(AOE, 1.0F, Float::sum);
    }
}
