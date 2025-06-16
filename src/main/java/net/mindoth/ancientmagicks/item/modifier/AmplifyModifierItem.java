package net.mindoth.ancientmagicks.item.modifier;

import net.mindoth.ancientmagicks.item.form.entity.AbstractSpellEntity;

import java.util.HashMap;

public class AmplifyModifierItem extends SpellModifierItem {

    public AmplifyModifierItem(Properties pProperties, int cost) {
        super(pProperties, cost);
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
        projectile.getEntityData().set(AbstractSpellEntity.POWER, projectile.getEntityData().get(AbstractSpellEntity.POWER) + count);
    }

    @Override
    public void addStatsToMap(HashMap<String, Float> stats) {
        stats.merge(POWER, 1.0F, Float::sum);
    }
}
