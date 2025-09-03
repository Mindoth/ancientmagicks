package net.mindoth.ancientmagicks.revamp.item.rune;

import net.mindoth.ancientmagicks.revamp.SpellData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class TargetFaceRuneItem extends RuneItem {
    public TargetFaceRuneItem(Item.Properties pProperties) {
        super(pProperties);
    }

    @Override
    public SpellData resolve(Entity caster, SpellData spellData) {
        if ( !spellData.getEntities().isEmpty() && !spellData.getVectors().isEmpty() ) {
            Entity entity = spellData.getEntities().get(spellData.getEntities().size() - 1);
            spellData.purgeEntities(1);
            Vec3 direction = spellData.getVectors().get(spellData.getVectors().size() - 1);
            spellData.purgeVectors(1);
            if ( entity != null ) {
                BlockHitResult blockHitResult = getPOVHitResult(direction, entity.level(), entity, ClipContext.Fluid.SOURCE_ONLY, 4.5F);
                BlockPos blockPos = getPosOfFace(blockHitResult.getBlockPos(), blockHitResult.getDirection());
                Vec3 pos = new Vec3(blockPos.getX() + 0.5D, blockPos.getY(), blockPos.getZ() + 0.5D);
                spellData.addVector(pos);
                spellData.addDimension(entity.level());
            }
            else {
                spellData.addVector(null);
                spellData.addDimension(null);
            }
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

    private static BlockPos getPosOfFace(BlockPos blockPos, Direction face) {
        return switch (face) {
            case UP -> blockPos.above();
            case EAST -> blockPos.east();
            case WEST -> blockPos.west();
            case SOUTH -> blockPos.south();
            case NORTH -> blockPos.north();
            case DOWN -> blockPos.below();
        };
    }
}
