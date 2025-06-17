package net.mindoth.ancientmagicks.item.modifier;

import com.google.common.collect.Lists;
import net.mindoth.ancientmagicks.item.SpellComponentItem;
import net.mindoth.ancientmagicks.item.form.entity.AbstractSpellEntity;
import net.mindoth.ancientmagicks.registries.ModItems;

import java.util.HashMap;
import java.util.List;

public class DiminishModifierItem extends SpellModifierItem {

    public DiminishModifierItem(Properties pProperties, int cost) {
        super(pProperties, cost);
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
    public List<SpellComponentItem> incompatibleWith() {
        List<SpellComponentItem> list = Lists.newArrayList();
        list.add((SpellComponentItem) ModItems.TOUCH_SIGIL_ITEM.get());
        list.add((SpellComponentItem) ModItems.SELF_SIGIL_ITEM.get());
        return list;
    }

    @Override
    public void addEntityModifier(AbstractSpellEntity projectile, int count) {
        projectile.getEntityData().set(AbstractSpellEntity.LIFE, Math.max(0, projectile.getEntityData().get(AbstractSpellEntity.LIFE) - count * 20));
    }

    @Override
    public void addStatsToMap(HashMap<String, Float> stats) {
        stats.merge(LIFE, -1.0F, Float::sum);
        stats.merge(LIFE, 0.0F, Float::max);
    }
}
