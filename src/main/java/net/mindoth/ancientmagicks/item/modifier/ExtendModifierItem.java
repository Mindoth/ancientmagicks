package net.mindoth.ancientmagicks.item.modifier;

import net.mindoth.ancientmagicks.item.form.entity.AbstractSpellEntity;

import java.util.HashMap;

public class ExtendModifierItem extends SpellModifierItem {

    public ExtendModifierItem(Properties pProperties, int manaCost, int cooldown) {
        super(pProperties, manaCost, cooldown);
    }

    @Override
    public void addModifierOnEntityCreation(AbstractSpellEntity projectile, int count) {
        projectile.getEntityData().set(AbstractSpellEntity.LIFE, projectile.getEntityData().get(AbstractSpellEntity.LIFE) + count * 20);
    }

    @Override
    public void addStatsToMap(HashMap<String, Float> stats) {
        stats.merge(LIFE, 20.0F, Float::sum);
    }
}
