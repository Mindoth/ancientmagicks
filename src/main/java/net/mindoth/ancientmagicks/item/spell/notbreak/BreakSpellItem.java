package net.mindoth.ancientmagicks.item.spell.notbreak;

import net.mindoth.ancientmagicks.item.spell.BlockTargetSpell;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.HashMap;

public class BreakSpellItem extends BlockTargetSpell {

    public BreakSpellItem(Properties pProperties, int manaCost, int cooldown) {
        super(pProperties, manaCost, cooldown);
    }

    @Override
    protected boolean doSpell(Level level, LivingEntity owner, Entity caster, HitResult result, HashMap<String, Float> stats) {
        BlockPos pos = ((BlockHitResult)result).getBlockPos();
        if ( level.getBlockState(pos).isAir() ) return false;
        level.destroyBlock(pos, true, caster);
        return true;
    }
}
