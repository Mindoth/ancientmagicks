package net.mindoth.ancientmagicks.item.modifier;

import com.google.common.collect.Lists;
import net.mindoth.ancientmagicks.item.ComponentItem;
import net.mindoth.ancientmagicks.item.effect.SpellEffectItem;
import net.mindoth.ancientmagicks.item.form.SpellFormItem;
import net.mindoth.ancientmagicks.item.form.entity.AbstractSpellEntity;

import java.util.HashMap;
import java.util.List;

public class SpellModifierItem extends ComponentItem {

    public SpellModifierItem(Properties pProperties, int manaCost, int cooldown) {
        super(pProperties, manaCost, cooldown);
    }

    public boolean usableWithForms() {
        return false;
    }

    public boolean usableWithEffects() {
        return false;
    }

    public List<ComponentItem> exclusiveWith() {
        return Lists.newArrayList();
    }

    public List<ComponentItem> incompatibleWith() {
        return Lists.newArrayList();
    }

    //Wood brain mode
    public static boolean canAddModifier(SpellModifierItem modifier, ComponentItem component) {
        /*return ((modifier.usableWithForms() && component instanceof SpellFormItem) || (modifier.usableWithEffects() && component instanceof SpellEffectItem))
                && (modifier.exclusiveWith().isEmpty() || modifier.exclusiveWith().contains(component))
                && (modifier.incompatibleWith().isEmpty() || !modifier.incompatibleWith().contains(component));*/
        return true;
    }

    public void addEntityModifier(AbstractSpellEntity projectile, int count) {
    }

    public void addStatsToMap(HashMap<String, Float> stats) {
    }
}
