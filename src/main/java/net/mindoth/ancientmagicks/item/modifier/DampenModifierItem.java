package net.mindoth.ancientmagicks.item.modifier;

import net.mindoth.ancientmagicks.item.form.entity.AbstractSpellEntity;

import java.util.HashMap;

public class DampenModifierItem extends SpellModifierItem {

    public DampenModifierItem(Properties pProperties, int manaCost, int cooldown) {
        super(pProperties, manaCost, cooldown);
    }

    @Override
    public boolean usableWithForms() {
        return false;
    }

    @Override
    public boolean usableWithEffects() {
        return true;
    }

    @Override
    public void addEntityModifier(AbstractSpellEntity projectile, int count) {
        projectile.getEntityData().set(AbstractSpellEntity.POWER, Math.max(0, projectile.getEntityData().get(AbstractSpellEntity.POWER) - count));
    }

    @Override
    public void addStatsToMap(HashMap<String, Float> stats) {
        stats.merge(POWER, -1.0F, Float::sum);
        stats.merge(POWER, 0.0F, Float::max);
    }
}
