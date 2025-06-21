package net.mindoth.ancientmagicks.item.modifier;

import com.google.common.collect.Lists;
import net.mindoth.ancientmagicks.item.SpellComponentItem;
import net.mindoth.ancientmagicks.item.form.entity.AbstractSpellEntity;
import net.mindoth.ancientmagicks.registries.ModItems;

import java.util.HashMap;
import java.util.List;

public class QuickenModifierItem extends SpellModifierItem {

    public QuickenModifierItem(Properties pProperties, int cost) {
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
        projectile.getEntityData().set(AbstractSpellEntity.SPEED, projectile.getEntityData().get(AbstractSpellEntity.SPEED) + count * 0.2F);
    }

    @Override
    public void addStatsToMap(HashMap<String, Float> stats) {
        stats.merge(SPEED, 0.2F, Float::sum);
    }
}
