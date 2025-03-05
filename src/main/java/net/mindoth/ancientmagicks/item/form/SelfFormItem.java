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

import java.util.HashMap;
import java.util.List;

public class SelfFormItem extends SpellFormItem {

    public SelfFormItem(Properties pProperties, int manaCost, int cooldown) {
        super(pProperties, manaCost, cooldown);
    }

    @Override
    public boolean formSpell(LivingEntity owner, Entity caster, List<ComponentItem> spellStack) {
        Level level = caster.level();

        List<ComponentItem> newList = Lists.newArrayList();
        List<SpellModifierItem> formModifiers = Lists.newArrayList();
        boolean form = false;
        for ( ComponentItem item : spellStack ) {
            if ( !form ) {
                if ( item instanceof SpellFormItem ) form = true;
                if ( item instanceof SpellModifierItem modifier ) formModifiers.add(modifier);
            }
            else newList.add(item);
        }
        HashMap<String, Float> formStats = SpellEffectItem.createSpellStats(formModifiers);
        float aoe = formStats.get(AOE);

        List<SpellModifierItem> modifiers = Lists.newArrayList();
        HashMap<String, Float> stats = SpellEffectItem.createDefaultStats();
        List<Boolean> boolist = Lists.newArrayList();
        for ( ComponentItem item : newList ) {
            if ( item instanceof SpellModifierItem modifier ) modifiers.add(modifier);
            if ( item instanceof SpellEffectItem effect ) {
                HitResult hitResult = new EntityHitResult(caster, caster.position());
                for ( SpellModifierItem modifier : modifiers ) modifier.addStatsToMap(stats);
                boolist.add(effect.castSpell(level, owner, caster, hitResult, stats, aoe));
                modifiers = Lists.newArrayList();
                stats = SpellEffectItem.createDefaultStats();
            }
        }
        for ( boolean bool : boolist ) if ( bool ) return true;
        return false;
    }
}
