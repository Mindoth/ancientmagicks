package net.mindoth.ancientmagicks.item.modifier;

import com.google.common.collect.Lists;
import net.mindoth.ancientmagicks.item.SpellComponentItem;
import net.mindoth.ancientmagicks.item.form.entity.AbstractSpellEntity;
import net.mindoth.ancientmagicks.registries.ModItems;

import java.util.HashMap;
import java.util.List;

public class NarrowMindedModifierItem extends SpellModifierItem {

    public NarrowMindedModifierItem(Properties pProperties, int cost) {
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
        list.add((SpellComponentItem) ModItems.SELF_SIGIL.get());
        return list;
    }

    @Override
    public void addEntityModifier(AbstractSpellEntity projectile, int count) {
        projectile.getEntityData().set(AbstractSpellEntity.LIFE, projectile.getEntityData().get(AbstractSpellEntity.LIFE) + count * 60);
        projectile.getEntityData().set(AbstractSpellEntity.REACH, projectile.getEntityData().get(AbstractSpellEntity.REACH) - count * 3);
    }

    @Override
    public void addStatsToMap(HashMap<String, Float> stats) {
        stats.merge(LIFE, 3.0F, Float::sum);
        stats.merge(REACH, -3.0F, Float::sum);
    }
}
