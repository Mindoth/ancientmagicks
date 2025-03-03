package net.mindoth.ancientmagicks.item.modifier;

import net.mindoth.ancientmagicks.item.form.entity.AbstractSpellEntity;

import java.util.HashMap;

public class ShrinkModifierItem extends SpellModifierItem {

    public ShrinkModifierItem(Properties pProperties, int manaCost, int cooldown) {
        super(pProperties, manaCost, cooldown);
    }

    @Override
    public void addEntityModifier(AbstractSpellEntity projectile, int count) {
        projectile.getEntityData().set(AbstractSpellEntity.AOE, Math.max(0, projectile.getEntityData().get(AbstractSpellEntity.AOE) - (float)count));
    }

    @Override
    public void addStatsToMap(HashMap<String, Float> stats) {
        stats.merge(AOE, -1.0F, Float::sum);
        stats.merge(AOE, 0.0F, Float::max);
    }
}
