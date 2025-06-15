package net.mindoth.ancientmagicks.item.modifier;

import com.google.common.collect.Lists;
import net.mindoth.ancientmagicks.item.ComponentItem;
import net.mindoth.ancientmagicks.item.form.entity.AbstractSpellEntity;
import net.mindoth.ancientmagicks.registries.AncientMagicksItems;

import java.util.List;

public class GravityModifierItem extends SpellModifierItem {

    public GravityModifierItem(Properties pProperties, int manaCost, int cooldown) {
        super(pProperties, manaCost, cooldown);
    }

    @Override
    public boolean usableWithForms() {
        return true;
    }

    @Override
    public boolean usableWithEffects() {
        return false;
    }

    @Override
    public List<ComponentItem> exclusiveWith() {
        List<ComponentItem> list = Lists.newArrayList();
        list.add((ComponentItem) AncientMagicksItems.PROJECTILE_SIGIL_ITEM.get());
        return list;
    }

    @Override
    public void addEntityModifier(AbstractSpellEntity projectile, int count) {
        if ( projectile.isNoGravity() ) projectile.setNoGravity(false);
    }
}
