package net.mindoth.ancientmagicks.item.modifier;

import net.mindoth.ancientmagicks.item.form.entity.AbstractSpellEntity;

public class GravityModifierItem extends SpellModifierItem {

    public GravityModifierItem(Properties pProperties, int manaCost, int cooldown) {
        super(pProperties, manaCost, cooldown);
    }

    @Override
    public void addEntityModifier(AbstractSpellEntity projectile, int count) {
        if ( projectile.isNoGravity() ) projectile.setNoGravity(false);
    }
}
