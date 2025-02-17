package net.mindoth.ancientmagicks.item.spell.harm;

import net.mindoth.ancientmagicks.item.SpellItem;
import net.mindoth.ancientmagicks.item.spell.EventSpell;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.HashMap;

public class HarmItem extends EventSpell {

    public HarmItem(Properties pProperties, int manaCost, int cooldown) {
        super(pProperties, manaCost, cooldown);
    }

    @Override
    protected boolean canApply(Level level, LivingEntity owner, Entity caster, Entity target) {
        return target instanceof LivingEntity && filter(owner, target);
    }

    @Override
    protected void doEvent(Level level, LivingEntity owner, Entity caster, Entity target, HashMap<String, Float> stats) {
        attackEntityWithoutKnockback(owner, caster, target, stats.get(SpellItem.POWER));
    }
}
