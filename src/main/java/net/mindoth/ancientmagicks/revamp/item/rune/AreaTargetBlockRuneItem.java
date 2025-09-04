package net.mindoth.ancientmagicks.revamp.item.rune;

import net.mindoth.ancientmagicks.revamp.MultiBlockHitResult;
import net.mindoth.ancientmagicks.revamp.SpellData;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.compress.utils.Lists;

import java.util.List;

public class AreaTargetBlockRuneItem extends BlockTargetTemplate {
    public AreaTargetBlockRuneItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public SpellData resolve(Entity caster, SpellData spellData) {
        if ( !spellData.getEntities().isEmpty() && !spellData.getVectors().isEmpty() ) {
            Entity entity = spellData.getLatestEntity().getEntity();
            spellData.purgeEntities(1);
            Vec3 direction = spellData.getLatestVector();
            spellData.purgeVectors(1);
            int range;
            if ( spellData.getIntegers().isEmpty() || spellData.getLatestInteger() == null ) range = 1;
            else range = spellData.getLatestInteger();
            spellData.purgeIntegers(1);

            BlockHitResult result = getPOVHitResult(direction, entity.level(), entity, ClipContext.Fluid.SOURCE_ONLY, 4.5F);
            BlockPos blockPos = result.getBlockPos();
            List<BlockPos> blocks = Lists.newArrayList();
            if ( range == 0 ) blocks.add(blockPos);
            else blocks = getBlockList(this, result, blockPos, range);

            MultiBlockHitResult mResult = new MultiBlockHitResult(result.getLocation(), result.getDirection(), result.getBlockPos(), result.isInside(), blocks);
            spellData.addBlock(mResult);
            spellData.addDimension(entity.level());
            aoeBlockSpellParticles(entity.level(), blocks, defaultStats());
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
