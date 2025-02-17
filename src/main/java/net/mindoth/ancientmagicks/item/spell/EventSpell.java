package net.mindoth.ancientmagicks.item.spell;

import net.mindoth.ancientmagicks.item.SpellItem;
import net.mindoth.ancientmagicks.item.modifier.SpellModifierItem;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;

public class EventSpell extends SpellItem {

    public EventSpell(Properties pProperties, int manaCost, int cooldown) {
        super(pProperties, manaCost, cooldown);
    }

    protected boolean canApply(Level level, LivingEntity owner, Entity caster, Entity target) {
        return filter(owner, target);
    }

    @Override
    public boolean castSpell(Level level, LivingEntity owner, Entity caster, Entity target, HashMap<String, Float> stats) {
        if ( canApply(level, owner, caster, target) ) {
            doEvent(level, owner, caster, target, stats);
            return true;
        }
        return false;
    }

    protected void doEvent(Level level, LivingEntity owner, Entity caster, Entity target, HashMap<String, Float> stats) {
    }
}
