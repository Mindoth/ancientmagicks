package net.mindoth.ancientmagicks.item.spell.harm;

import net.mindoth.ancientmagicks.item.SpellItem;
import net.mindoth.ancientmagicks.item.spell.EntityTargetSpell;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.HashMap;

public class HarmItem extends EntityTargetSpell {

    public HarmItem(Properties pProperties, int manaCost, int cooldown) {
        super(pProperties, manaCost, cooldown);
    }

    @Override
    protected boolean doSpell(Level level, LivingEntity owner, Entity caster, HitResult result, HashMap<String, Float> stats) {
        Entity target = ((EntityHitResult)result).getEntity();
        attackEntityWithoutKnockback(owner, caster, target, stats.get(SpellItem.POWER));
        return true;
    }
}
