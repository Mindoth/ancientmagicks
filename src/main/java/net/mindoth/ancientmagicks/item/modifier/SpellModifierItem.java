package net.mindoth.ancientmagicks.item.modifier;

import com.google.common.collect.Lists;
import net.mindoth.ancientmagicks.item.SpellComponentItem;
import net.mindoth.ancientmagicks.item.effect.SpellEffectItem;
import net.mindoth.ancientmagicks.item.form.SpellFormItem;
import net.mindoth.ancientmagicks.item.form.entity.AbstractSpellEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.List;

public class SpellModifierItem extends SpellComponentItem {

    public SpellModifierItem(Properties pProperties, int cost) {
        super(pProperties, cost);
    }

    public boolean usableWithForms() {
        return false;
    }

    public boolean usableWithEffects() {
        return false;
    }

    public List<SpellComponentItem> exclusiveWith() {
        return Lists.newArrayList();
    }

    public List<SpellComponentItem> incompatibleWith() {
        return Lists.newArrayList();
    }

    //Wood brain mode
    public static boolean canAddModifier(SpellModifierItem modifier, SpellComponentItem component) {
        return ((modifier.usableWithForms() && component instanceof SpellFormItem) || (modifier.usableWithEffects() && component instanceof SpellEffectItem))
                && (modifier.exclusiveWith().isEmpty() || modifier.exclusiveWith().contains(component))
                && (modifier.incompatibleWith().isEmpty() || !modifier.incompatibleWith().contains(component));
    }

    public void addEntityModifier(AbstractSpellEntity projectile, int count) {
    }

    public void addStatsToMap(HashMap<String, Float> stats) {
    }

    public EncodeableData addDataFromEncodeable(String data, Level level, Vec3 posVec) {
        return new EncodeableData(level, posVec);
    }

    public static class EncodeableData {
        public Level level;
        public Vec3 posVec;
        public EncodeableData(Level level, Vec3 posVec) {
            this.level = level;
            this.posVec = posVec;
        }
    }
}
