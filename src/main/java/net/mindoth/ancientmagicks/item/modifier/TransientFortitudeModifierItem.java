package net.mindoth.ancientmagicks.item.modifier;

import com.google.common.collect.Lists;
import net.mindoth.ancientmagicks.item.ComponentItem;
import net.mindoth.ancientmagicks.item.form.entity.AbstractSpellEntity;
import net.mindoth.ancientmagicks.registries.ModItems;

import java.util.HashMap;
import java.util.List;

public class TransientFortitudeModifierItem extends SpellModifierItem {

    public TransientFortitudeModifierItem(Properties pProperties, int cost) {
        super(pProperties, cost);
    }

    @Override
    public boolean usableWithForms() {
        return false;
    }

    @Override
    public boolean usableWithEffects() {
        return true;
    }

    @Override
    public List<ComponentItem> incompatibleWith() {
        List<ComponentItem> list = Lists.newArrayList();
        list.add((ComponentItem) ModItems.TOUCH_SIGIL_ITEM.get());
        list.add((ComponentItem) ModItems.SELF_SIGIL_ITEM.get());
        return list;
    }

    @Override
    public void addEntityModifier(AbstractSpellEntity projectile, int count) {
        projectile.getEntityData().set(AbstractSpellEntity.POWER, projectile.getEntityData().get(AbstractSpellEntity.POWER) + count);
        projectile.getEntityData().set(AbstractSpellEntity.LIFE, projectile.getEntityData().get(AbstractSpellEntity.LIFE) - count * 60);
    }

    @Override
    public void addStatsToMap(HashMap<String, Float> stats) {
        stats.merge(POWER, 3.0F, Float::sum);
        stats.merge(LIFE, -3.0F, Float::sum);
    }
}
