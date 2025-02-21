package net.mindoth.ancientmagicks.item.modifier;

import net.mindoth.ancientmagicks.item.spell.SpellItem;
import net.mindoth.ancientmagicks.item.form.entity.AbstractSpellEntity;
import net.mindoth.ancientmagicks.item.form.entity.ProjectileSpellEntity;

import java.util.HashMap;

public class ContractModifierItem extends SpellModifierItem {

    public ContractModifierItem(Properties pProperties, int manaCost, int cooldown) {
        super(pProperties, manaCost, cooldown);
    }

    @Override
    public void addModifierToEntity(ProjectileSpellEntity projectile, int count) {
        if ( projectile != null ) {
            projectile.getEntityData().set(AbstractSpellEntity.REACH, Math.max(0, projectile.getEntityData().get(AbstractSpellEntity.REACH) - (float)count));
        }
    }

    @Override
    public void addStatsToMap(HashMap<String, Float> stats) {
        stats.merge(SpellItem.REACH, -1.0F, Float::sum);
        stats.merge(SpellItem.REACH, 0.0F, Float::max);
    }
}