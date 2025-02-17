package net.mindoth.ancientmagicks.item.modifier;

import net.mindoth.ancientmagicks.item.SpellItem;
import net.mindoth.ancientmagicks.item.form.AbstractSpellEntity;
import net.mindoth.ancientmagicks.item.form.SpellProjectileEntity;

import javax.annotation.Nullable;
import java.util.HashMap;

public class DampenModifierItem extends SpellModifierItem {

    public DampenModifierItem(Properties pProperties, int manaCost, int cooldown) {
        super(pProperties, manaCost, cooldown);
    }

    @Override
    public void addModifierToEntity(SpellProjectileEntity projectile, int count) {
        if ( projectile != null ) {
            projectile.getEntityData().set(AbstractSpellEntity.POWER, -count);
        }
    }

    @Override
    public void addStatsToMap(HashMap<String, Float> stats) {
        stats.merge(SpellItem.POWER, -1.0F, Float::sum);
    }
}
