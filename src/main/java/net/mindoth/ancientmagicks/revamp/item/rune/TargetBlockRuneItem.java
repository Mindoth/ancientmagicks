package net.mindoth.ancientmagicks.revamp.item.rune;

import net.mindoth.ancientmagicks.revamp.MultiBlockHitResult;
import net.mindoth.ancientmagicks.revamp.SpellData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Collections;

public class TargetBlockRuneItem extends UseOnEntityTemplate {
    public TargetBlockRuneItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public SpellData result(Entity caster, SpellData spellData, Entity entity) {
        if ( !spellData.getVectors().isEmpty() ) {
            Vec3 direction = spellData.getLatestVector();
            spellData.purgeVectors(1);
            MultiBlockHitResult result = getPOVHitResult(direction, entity.level(), entity, ClipContext.Fluid.SOURCE_ONLY, 4.5F);
            spellData.addBlock(result);
        }
        else spellData.setValid(false);
        return spellData;
    }
}
