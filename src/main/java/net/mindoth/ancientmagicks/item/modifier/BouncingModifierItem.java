package net.mindoth.ancientmagicks.item.modifier;

import net.mindoth.ancientmagicks.item.form.AbstractSpellEntity;
import net.mindoth.ancientmagicks.item.form.SpellProjectileEntity;

public class BouncingModifierItem extends SpellModifierItem {

    public BouncingModifierItem(Properties pProperties, int manaCost, int cooldown) {
        super(pProperties, manaCost, cooldown);
    }

    @Override
    public void addModifierToEntity(SpellProjectileEntity projectile, int count) {
        if ( projectile != null ) {
            projectile.getEntityData().set(AbstractSpellEntity.BLOCK_BOUNCE, Integer.MAX_VALUE);
        }
    }
}
