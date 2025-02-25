package net.mindoth.ancientmagicks.item.form;

import com.google.common.collect.Lists;
import net.mindoth.ancientmagicks.item.CastingValidator;
import net.mindoth.ancientmagicks.item.ComponentItem;
import net.mindoth.ancientmagicks.item.spell.SpellItem;
import net.mindoth.ancientmagicks.item.modifier.SpellModifierItem;
import net.mindoth.shadowizardlib.event.ShadowEvents;
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
    public boolean formSpell(LivingEntity owner, Entity caster, List<List<ComponentItem>> spellStack) {
        Level level = caster.level();
        List<ComponentItem> componentList = Lists.newArrayList();
        componentList.addAll(spellStack.get(0));
        spellStack.remove(0);

        List<SpellModifierItem> modifiers = Lists.newArrayList();
        for ( ComponentItem item : componentList ) if ( item instanceof SpellModifierItem modifier ) modifiers.add(modifier);
        HashMap<String, Float> stats = SpellItem.createSpellStats(modifiers);

        for ( ComponentItem item : componentList ) {
            if ( item instanceof SpellItem spell ) {
                HitResult hitResult = new EntityHitResult(caster, caster.position());
                if ( spell.castSpell(level, owner, caster, hitResult, stats) ) {
                    //CastingValidator.castSpell(owner, caster, spellStack);
                    return true;
                }
            }
        }
        return false;
    }
}
