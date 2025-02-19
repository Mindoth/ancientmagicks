package net.mindoth.ancientmagicks.item.form;

import net.mindoth.ancientmagicks.item.SpellItem;
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
    public boolean formSpell(SpellItem spell, LivingEntity owner, Entity caster, List<SpellModifierItem> modifiers) {
        Level level = caster.level();
        HashMap<String, Float> stats = SpellItem.createSpellStats(modifiers);

        HitResult hitResult = new EntityHitResult(caster, ShadowEvents.getEntityCenter(caster));
        return spell.castSpell(level, owner, caster, hitResult, stats);
    }
}
