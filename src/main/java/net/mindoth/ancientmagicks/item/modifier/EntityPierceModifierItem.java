package net.mindoth.ancientmagicks.item.modifier;

import net.mindoth.ancientmagicks.item.form.entity.AbstractSpellEntity;
import net.mindoth.ancientmagicks.item.form.entity.ProjectileSpellEntity;

public class EntityPierceModifierItem extends SpellModifierItem {

    public EntityPierceModifierItem(Properties pProperties, int manaCost, int cooldown) {
        super(pProperties, manaCost, cooldown);
    }

    @Override
    public void addEntityModifier(AbstractSpellEntity projectile, int count) {
        projectile.getEntityData().set(AbstractSpellEntity.ENTITY_PIERCE, Integer.MAX_VALUE);
    }
}
