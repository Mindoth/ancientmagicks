package net.mindoth.ancientmagicks.item.modifier;

import com.google.common.collect.Lists;
import net.mindoth.ancientmagicks.item.ComponentItem;
import net.mindoth.ancientmagicks.item.form.entity.AbstractSpellEntity;
import net.mindoth.ancientmagicks.registries.AncientMagicksItems;

import java.util.HashMap;
import java.util.List;

public class ExtendModifierItem extends SpellModifierItem {

    public ExtendModifierItem(Properties pProperties, int manaCost, int cooldown) {
        super(pProperties, manaCost, cooldown);
    }

    @Override
    public boolean usableWithForms() {
        return true;
    }

    @Override
    public boolean usableWithEffects() {
        return true;
    }

    @Override
    public List<ComponentItem> incompatibleWith() {
        List<ComponentItem> list = Lists.newArrayList();
        list.add((ComponentItem)AncientMagicksItems.TOUCH_FORM_ITEM.get());
        list.add((ComponentItem)AncientMagicksItems.SELF_FORM_ITEM.get());
        list.add((ComponentItem)AncientMagicksItems.LOCATION_FORM_ITEM.get());
        return list;
    }

    @Override
    public void addEntityModifier(AbstractSpellEntity projectile, int count) {
        projectile.getEntityData().set(AbstractSpellEntity.LIFE, projectile.getEntityData().get(AbstractSpellEntity.LIFE) + count * 20);
    }

    @Override
    public void addStatsToMap(HashMap<String, Float> stats) {
        stats.merge(LIFE, 1.0F, Float::sum);
    }
}
