package net.mindoth.ancientmagicks.item.modifier;

import net.mindoth.ancientmagicks.item.form.AbstractSpellEntity;
import net.mindoth.ancientmagicks.item.form.SpellProjectileEntity;

import javax.annotation.Nullable;

public class PiercingModifierItem extends SpellModifierItem {

    public PiercingModifierItem(Properties pProperties, int manaCost, int cooldown) {
        super(pProperties, manaCost, cooldown);
    }

    @Override
    public void addModifierToEntity(SpellProjectileEntity projectile, int count) {
        if ( projectile != null ) {
            projectile.getEntityData().set(AbstractSpellEntity.PIERCING, Integer.MAX_VALUE);
        }
    }
}
