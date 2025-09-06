package net.mindoth.ancientmagicks.revamp.item.rune;

import net.mindoth.ancientmagicks.revamp.MultiBlockHitResult;
import net.mindoth.ancientmagicks.revamp.SpellData;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Collections;
import java.util.List;

public class TargetBlockRuneItem extends RuneItem {
    public TargetBlockRuneItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public SpellData resolve(Entity caster, SpellData spellData) {
        if ( !spellData.getVectors().isEmpty() && spellData.getVectors().size() >= 2 ) {
            Vec3 position = spellData.getLatestVector();
            spellData.purgeVectors(1);
            Vec3 direction = spellData.getLatestVector();
            spellData.purgeVectors(1);
            Level level;
            if ( spellData.getDimensions().isEmpty() || spellData.getLatestDimension() == null ) level = caster.level();
            else level = spellData.getLatestDimension();
            spellData.purgeDimensions(1);
            MultiBlockHitResult mResult = getPOVHitResult(position, direction, caster, level, ClipContext.Fluid.SOURCE_ONLY, 4.5F);
            spellData.addBlock(mResult);
            aoeBlockSpellParticles(level, Collections.singletonList(mResult.getBlockPos()), defaultStats());
        }
        else spellData.setValid(false);
        return spellData;
    }
}
