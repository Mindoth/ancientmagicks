package net.mindoth.ancientmagicks.item.effect;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class BlockTargetEffect extends SpellEffectItem {

    public BlockTargetEffect(Properties pProperties, int cost) {
        super(pProperties, cost);
    }

    @Override
    protected boolean canApply(Level level, LivingEntity owner, Entity caster, HitResult result, String data) {
        return result instanceof BlockHitResult;
    }

    protected boolean isInside() {
        return true;
    }
}
