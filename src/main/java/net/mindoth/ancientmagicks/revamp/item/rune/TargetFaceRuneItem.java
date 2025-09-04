package net.mindoth.ancientmagicks.revamp.item.rune;

import net.mindoth.ancientmagicks.revamp.MultiBlockHitResult;
import net.mindoth.ancientmagicks.revamp.SpellData;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.Vec3;

import java.util.Collections;

public class TargetFaceRuneItem extends UseOnEntityTemplate {
    public TargetFaceRuneItem(Item.Properties pProperties) {
        super(pProperties);
    }

    @Override
    public SpellData result(Entity caster, SpellData spellData, Entity entity) {
        if ( !spellData.getVectors().isEmpty() ) {
            Vec3 direction = spellData.getLatestVector();
            spellData.purgeVectors(1);
            MultiBlockHitResult result = getPOVHitResult(direction, entity.level(), entity, ClipContext.Fluid.SOURCE_ONLY, 4.5F);
            BlockPos blockPos = getPosOfFace(result.getBlockPos(), result.getDirection());
            Vec3 pos = new Vec3(blockPos.getX(), blockPos.getY(), blockPos.getZ());
            spellData.addBlock(new MultiBlockHitResult(pos, result.getDirection(), blockPos, result.isInside(), Collections.singletonList(blockPos), entity.level()));
        }
        else spellData.setValid(false);
        return spellData;
    }
}
