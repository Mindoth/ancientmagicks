package net.mindoth.ancientmagicks.revamp.item;

import net.mindoth.ancientmagicks.revamp.SpellData;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class TargetBlockRuneItem extends RuneItem {
    public TargetBlockRuneItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public SpellData resolve(Entity caster, SpellData spellData) {
        if ( !spellData.getEntities().isEmpty() ) {
            Entity entity = spellData.getEntities().get(spellData.getEntities().size() - 1);
            spellData.purgeEntities(1);
            if ( entity != null ) {
                BlockHitResult blockHitResult = getPOVHitResult(entity.level(), entity, ClipContext.Fluid.SOURCE_ONLY, 4.5F);
                spellData.addBlock(blockHitResult);
                spellData.addDimension(entity.level());
            }
            else spellData.addBlock(null);
        }
        else spellData.setValid(false);
        return spellData;
    }

    public static BlockHitResult getPOVHitResult(Level pLevel, Entity entity, ClipContext.Fluid pFluidMode, float range) {
        float f = entity.getXRot();
        float f1 = entity.getYRot();
        Vec3 vec3 = entity.getEyePosition();
        float f2 = Mth.cos(-f1 * ((float)Math.PI / 180F) - (float)Math.PI);
        float f3 = Mth.sin(-f1 * ((float)Math.PI / 180F) - (float)Math.PI);
        float f4 = -Mth.cos(-f * ((float)Math.PI / 180F));
        float f5 = Mth.sin(-f * ((float)Math.PI / 180F));
        float f6 = f3 * f4;
        float f7 = f2 * f4;
        double d0 = range;
        Vec3 vec31 = vec3.add((double)f6 * d0, (double)f5 * d0, (double)f7 * d0);
        return pLevel.clip(new ClipContext(vec3, vec31, ClipContext.Block.OUTLINE, pFluidMode, entity));
    }
}
