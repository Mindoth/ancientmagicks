package net.mindoth.ancientmagicks.item.modifier;

import com.google.common.collect.Lists;
import net.mindoth.ancientmagicks.item.SpellComponentItem;
import net.mindoth.ancientmagicks.item.form.entity.AbstractSpellEntity;
import net.mindoth.ancientmagicks.registries.ModItems;

import java.util.List;

public class GravityModifierItem extends SpellModifierItem {

    public GravityModifierItem(Properties pProperties, int cost) {
        super(pProperties, cost);
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
    public List<SpellComponentItem> exclusiveWith() {
        List<SpellComponentItem> list = Lists.newArrayList();
        list.add((SpellComponentItem) ModItems.PROJECTILE_SIGIL.get());
        return list;
    }

    @Override
    public void addEntityModifier(AbstractSpellEntity projectile, int count) {
        if ( projectile.isNoGravity() ) projectile.setNoGravity(false);
    }
}
