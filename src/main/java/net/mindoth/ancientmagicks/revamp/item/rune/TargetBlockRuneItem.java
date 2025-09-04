package net.mindoth.ancientmagicks.revamp.item.rune;

import net.mindoth.ancientmagicks.revamp.SpellData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class TargetBlockRuneItem extends BlockTargetTemplate {
    public TargetBlockRuneItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public SpellData resolve(Entity caster, SpellData spellData) {
        if ( !spellData.getEntities().isEmpty() && !spellData.getVectors().isEmpty() ) {
            Entity entity = spellData.getLatestEntity().getEntity();
            spellData.purgeEntities(1);
            Vec3 direction = spellData.getLatestVector();
            spellData.purgeVectors(1);
            BlockHitResult blockHitResult = getPOVHitResult(direction, entity.level(), entity, ClipContext.Fluid.SOURCE_ONLY, 4.5F);
            spellData.addBlock(blockHitResult);
            spellData.addDimension(entity.level());
        }
        else spellData.setValid(false);
        return spellData;
    }

    public static BlockHitResult getPOVHitResult(Vec3 direction, Level pLevel, Entity entity, ClipContext.Fluid pFluidMode, float range) {
        direction = direction.multiply(range, range, range);
        Vec3 vec3 = entity.getEyePosition();
        Vec3 vec31 = vec3.add(direction);
        return pLevel.clip(new ClipContext(vec3, vec31, ClipContext.Block.OUTLINE, pFluidMode, entity));
    }
}
