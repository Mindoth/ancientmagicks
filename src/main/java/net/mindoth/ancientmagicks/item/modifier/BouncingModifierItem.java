package net.mindoth.ancientmagicks.item.modifier;

import com.google.common.collect.Lists;
import net.mindoth.ancientmagicks.item.ComponentItem;
import net.mindoth.ancientmagicks.item.form.entity.AbstractSpellEntity;
import net.mindoth.ancientmagicks.registries.AncientMagicksItems;

import java.util.List;

public class BouncingModifierItem extends SpellModifierItem {

    public BouncingModifierItem(Properties pProperties, int manaCost, int cooldown) {
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
        projectile.getEntityData().set(AbstractSpellEntity.BLOCK_BOUNCE, projectile.getEntityData().get(AbstractSpellEntity.BLOCK_BOUNCE) + count);
    }
}
