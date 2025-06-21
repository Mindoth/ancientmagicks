package net.mindoth.ancientmagicks.item.modifier;

import com.google.common.collect.Lists;
import net.mindoth.ancientmagicks.item.SpellComponentItem;
import net.mindoth.ancientmagicks.item.form.entity.AbstractSpellEntity;
import net.mindoth.ancientmagicks.registries.ModItems;

import java.util.HashMap;
import java.util.List;

public class ContractModifierItem extends SpellModifierItem {

    public ContractModifierItem(Properties pProperties, int cost) {
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
        list.add((SpellComponentItem) ModItems.TOUCH_SIGIL.get());
        return list;
    }

    @Override
    public void addEntityModifier(AbstractSpellEntity projectile, int count) {
        projectile.getEntityData().set(AbstractSpellEntity.REACH, Math.max(0, projectile.getEntityData().get(AbstractSpellEntity.REACH) - count));
    }

    @Override
    public void addStatsToMap(HashMap<String, Float> stats) {
        stats.merge(REACH, -1.0F, Float::sum);
        stats.merge(REACH, 0.0F, Float::max);
    }
}