package net.mindoth.ancientmagicks.item.modifier;

import com.google.common.collect.Lists;
import net.mindoth.ancientmagicks.item.ComponentItem;
import net.mindoth.ancientmagicks.item.form.entity.AbstractSpellEntity;
import net.mindoth.ancientmagicks.registries.ModItems;

import java.util.HashMap;
import java.util.List;

public class ReachModifierItem extends SpellModifierItem {

    public ReachModifierItem(Properties pProperties, int cost) {
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
    public List<ComponentItem> exclusiveWith() {
        List<ComponentItem> list = Lists.newArrayList();
        list.add((ComponentItem) ModItems.TOUCH_SIGIL_ITEM.get());
        return list;
    }

    @Override
    public void addEntityModifier(AbstractSpellEntity projectile, int count) {
        projectile.getEntityData().set(AbstractSpellEntity.REACH, projectile.getEntityData().get(AbstractSpellEntity.REACH) + count);
    }

    @Override
    public void addStatsToMap(HashMap<String, Float> stats) {
        stats.merge(REACH, 1.0F, Float::sum);
    }
}
