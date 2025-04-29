package net.mindoth.ancientmagicks.item.form;

import com.google.common.collect.Lists;
import net.mindoth.ancientmagicks.item.ComponentItem;
import net.mindoth.ancientmagicks.item.effect.SpellEffectItem;
import net.mindoth.ancientmagicks.item.modifier.SpellModifierItem;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.List;

public class SelfFormItem extends SpellFormItem {

    public SelfFormItem(Properties pProperties, int manaCost, int cooldown) {
        super(pProperties, manaCost, cooldown);
    }

    @Override
    public boolean formSpell(LivingEntity owner, Entity caster, List<ComponentItem> spellStack, List<String> data) {
        List<ComponentItem> newList = Lists.newArrayList();
        List<String> newData = Lists.newArrayList();
        List<SpellModifierItem> formModifiers = Lists.newArrayList();
        boolean form = false;
        for ( int i = 0; i < spellStack.size(); i++ ) {
            ComponentItem item = spellStack.get(i);
            if ( !form ) {
                if ( item instanceof SpellFormItem ) form = true;
                else if ( item instanceof SpellModifierItem modifier ) formModifiers.add(modifier);
            }
            else {
                newList.add(item);
                newData.add(data.get(i));
            }
        }
        HashMap<String, Float> formStats = ComponentItem.createDefaultStats();
        Level defLevel = caster.level();
        Vec3 defPosVec = caster.position();
        for ( int i = 0; i < formModifiers.size(); i++ ) {
            SpellModifierItem modifier = formModifiers.get(i);
            modifier.addStatsToMap(formStats);
            SpellModifierItem.EncodeableData ed = modifier.addDataFromEncodeable(data.get(i), defLevel, defPosVec);
            defLevel = ed.level;
            defPosVec = ed.posVec;
        }
        HashMap<String, Float> stats = ComponentItem.createDefaultStats();
        List<SpellModifierItem> modifiers = Lists.newArrayList();
        List<Boolean> boolist = Lists.newArrayList();
        for ( int i = 0; i < newList.size(); i++ ) {
            ComponentItem item = newList.get(i);
            if ( item instanceof SpellModifierItem modifier ) modifiers.add(modifier);
            if ( item instanceof SpellEffectItem effect ) {
                Level level = defLevel;
                Vec3 posVec = defPosVec;
                for ( int j = 0; j < modifiers.size(); j++ ) {
                    modifiers.get(j).addStatsToMap(stats);
                    SpellModifierItem.EncodeableData ed = modifiers.get(j).addDataFromEncodeable(newData.get(j), level, posVec);
                    level = ed.level;
                    posVec = ed.posVec;
                }
                HitResult hitResult = new EntityHitResult(caster, posVec);

                boolist.add(effect.castSpell(level, owner, caster, hitResult, formStats.get(AOE), stats, newData.get(i)));
                modifiers = Lists.newArrayList();
                stats = ComponentItem.createDefaultStats();
            }
        }
        for ( boolean bool : boolist ) if ( bool ) return true;
        return false;
    }
}
