package net.mindoth.ancientmagicks.item.modifier;

import net.mindoth.ancientmagicks.item.form.entity.AbstractSpellEntity;

import java.util.HashMap;

public class DiminishModifierItem extends SpellModifierItem {

    public DiminishModifierItem(Properties pProperties, int manaCost, int cooldown) {
        super(pProperties, manaCost, cooldown);
    }

    @Override
    public void addEntityModifier(AbstractSpellEntity projectile, int count) {
        projectile.getEntityData().set(AbstractSpellEntity.LIFE, Math.max(0, projectile.getEntityData().get(AbstractSpellEntity.LIFE) - count * 20));
    }

    @Override
    public void addStatsToMap(HashMap<String, Float> stats) {
        stats.merge(LIFE, -1.0F, Float::sum);
        stats.merge(LIFE, 0.0F, Float::max);
    }
}
