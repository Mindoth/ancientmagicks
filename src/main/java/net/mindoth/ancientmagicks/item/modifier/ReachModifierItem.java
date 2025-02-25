package net.mindoth.ancientmagicks.item.modifier;

import net.mindoth.ancientmagicks.item.form.entity.AbstractSpellEntity;

import java.util.HashMap;

public class ReachModifierItem extends SpellModifierItem {

    public ReachModifierItem(Properties pProperties, int manaCost, int cooldown) {
        super(pProperties, manaCost, cooldown);
    }

    @Override
    public void addModifierOnEntityCreation(AbstractSpellEntity projectile, int count) {
        projectile.getEntityData().set(AbstractSpellEntity.REACH, projectile.getEntityData().get(AbstractSpellEntity.REACH) + (float)count);
    }

    @Override
    public void addStatsToMap(HashMap<String, Float> stats) {
        stats.merge(REACH, 1.0F, Float::sum);
    }
}
