package net.mindoth.ancientmagicks.revamp.item.rune;

import net.mindoth.ancientmagicks.revamp.MultiBlockHitResult;
import net.mindoth.ancientmagicks.revamp.SpellData;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.compress.utils.Lists;

import java.util.List;

public class AreaTargetBlockRuneItem extends RuneItem {
    public AreaTargetBlockRuneItem(Properties pProperties) {
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
            int range;
            if ( spellData.getIntegers().isEmpty() || spellData.getLatestInteger() == null ) range = 1;
            else range = spellData.getLatestInteger();
            spellData.purgeIntegers(1);

            MultiBlockHitResult result = getPOVHitResult(position, direction, caster, level, ClipContext.Fluid.SOURCE_ONLY, 4.5F);
            BlockPos blockPos = result.getBlockPos();
            List<BlockPos> blocks = Lists.newArrayList();
            if ( range == 0 ) blocks.add(blockPos);
            else blocks = getBlockList(this, result, blockPos, range);

            MultiBlockHitResult mResult = new MultiBlockHitResult(result.getLocation(), result.getDirection(), result.getBlockPos(), result.isInside(), blocks, result.getLevel());
            spellData.addBlock(mResult);
            aoeBlockSpellParticles(level, blocks, defaultStats());
        }
        else spellData.setValid(false);
        return spellData;
    }
}
