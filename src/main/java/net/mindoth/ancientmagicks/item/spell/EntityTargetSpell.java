package net.mindoth.ancientmagicks.item.spell;

import net.mindoth.ancientmagicks.item.SpellItem;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class EntityTargetSpell extends SpellItem {

    public EntityTargetSpell(Properties pProperties, int manaCost, int cooldown) {
        super(pProperties, manaCost, cooldown);
    }

    @Override
    protected boolean canApply(Level level, LivingEntity owner, Entity caster, HitResult result) {
        return result instanceof EntityHitResult entityHitResult && allyFilter(owner, entityHitResult.getEntity());
    }
}
