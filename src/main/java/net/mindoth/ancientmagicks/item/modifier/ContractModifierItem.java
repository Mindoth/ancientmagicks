package net.mindoth.ancientmagicks.item.modifier;

import net.mindoth.ancientmagicks.item.form.entity.AbstractSpellEntity;

import java.util.HashMap;

public class ContractModifierItem extends SpellModifierItem {

    public ContractModifierItem(Properties pProperties, int manaCost, int cooldown) {
        super(pProperties, manaCost, cooldown);
    }

    @Override
    public void addEntityModifier(AbstractSpellEntity projectile, int count) {
        projectile.getEntityData().set(AbstractSpellEntity.REACH, Math.max(0, projectile.getEntityData().get(AbstractSpellEntity.REACH) - (float)count));
    }

    @Override
    public void addStatsToMap(HashMap<String, Float> stats) {
        stats.merge(REACH, -1.0F, Float::sum);
        stats.merge(REACH, 0.0F, Float::max);
    }
}