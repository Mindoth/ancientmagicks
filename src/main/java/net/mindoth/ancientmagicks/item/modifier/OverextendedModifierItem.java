package net.mindoth.ancientmagicks.item.modifier;

import com.google.common.collect.Lists;
import net.mindoth.ancientmagicks.item.ComponentItem;
import net.mindoth.ancientmagicks.item.form.entity.AbstractSpellEntity;
import net.mindoth.ancientmagicks.registries.ModItems;

import java.util.HashMap;
import java.util.List;

public class OverextendedModifierItem extends SpellModifierItem {

    public OverextendedModifierItem(Properties pProperties, int cost) {
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
    public List<ComponentItem> incompatibleWith() {
        List<ComponentItem> list = Lists.newArrayList();
        list.add((ComponentItem) ModItems.SELF_SIGIL_ITEM.get());
        return list;
    }

    @Override
    public void addEntityModifier(AbstractSpellEntity projectile, int count) {
        projectile.getEntityData().set(AbstractSpellEntity.REACH, projectile.getEntityData().get(AbstractSpellEntity.REACH) + count * 3);
        projectile.getEntityData().set(AbstractSpellEntity.POWER, projectile.getEntityData().get(AbstractSpellEntity.POWER) - count * 3);
    }

    @Override
    public void addStatsToMap(HashMap<String, Float> stats) {
        stats.merge(REACH, 3.0F, Float::sum);
        stats.merge(POWER, -3.0F, Float::sum);
    }
}
