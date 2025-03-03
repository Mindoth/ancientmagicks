package net.mindoth.ancientmagicks.item.modifier;

import net.mindoth.ancientmagicks.item.form.entity.AbstractSpellEntity;

import java.util.HashMap;

public class SlackenModifierItem extends SpellModifierItem {

    public SlackenModifierItem(Properties pProperties, int manaCost, int cooldown) {
        super(pProperties, manaCost, cooldown);
    }

    @Override
    public void addModifierOnEntityCreation(AbstractSpellEntity projectile, int count) {
        projectile.getEntityData().set(AbstractSpellEntity.SPEED, Math.max(0, projectile.getEntityData().get(AbstractSpellEntity.SPEED) - count * 0.2F));
    }

    @Override
    public void addStatsToMap(HashMap<String, Float> stats) {
        stats.merge(SPEED, -0.2F, Float::sum);
        stats.merge(SPEED, 0.0F, Float::max);
    }
}
