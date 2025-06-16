package net.mindoth.ancientmagicks.item.modifier;

import com.google.common.collect.Lists;
import net.mindoth.ancientmagicks.item.ComponentItem;
import net.mindoth.ancientmagicks.item.form.entity.AbstractSpellEntity;
import net.mindoth.ancientmagicks.registries.ModItems;

import java.util.HashMap;
import java.util.List;

public class SlackenModifierItem extends SpellModifierItem {

    public SlackenModifierItem(Properties pProperties, int manaCost, int cooldown) {
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
        list.add((ComponentItem) ModItems.PROJECTILE_SIGIL_ITEM.get());
        return list;
    }

    @Override
    public void addEntityModifier(AbstractSpellEntity projectile, int count) {
        projectile.getEntityData().set(AbstractSpellEntity.SPEED, Math.max(0, projectile.getEntityData().get(AbstractSpellEntity.SPEED) - count * 0.2F));
    }

    @Override
    public void addStatsToMap(HashMap<String, Float> stats) {
        stats.merge(SPEED, -0.2F, Float::sum);
        stats.merge(SPEED, 0.0F, Float::max);
    }
}
