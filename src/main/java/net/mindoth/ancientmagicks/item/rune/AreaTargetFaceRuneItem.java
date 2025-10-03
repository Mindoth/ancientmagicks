package net.mindoth.ancientmagicks.item.rune;

import net.mindoth.ancientmagicks.event.MultiBlockHitResult;
import net.mindoth.ancientmagicks.event.SpellData;
import net.mindoth.ancientmagicks.item.RuneItem;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.commons.compress.utils.Lists;

import javax.annotation.Nullable;
import java.util.List;

public class AreaTargetFaceRuneItem extends RuneItem {
    public AreaTargetFaceRuneItem(Item.Properties pProperties) {
        super(pProperties);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(Component.translatable("tooltip.ancientmagicks.vector").append(Component.literal(" | "))
                .append(Component.translatable("tooltip.ancientmagicks.vector")).append(Component.literal(" | "))
                .append(Component.literal("(")).append(Component.translatable("tooltip.ancientmagicks.dimension")).append(Component.literal(")")).append(Component.literal(" | "))
                .append(Component.literal("(")).append(Component.translatable("tooltip.ancientmagicks.integer")).append(Component.literal(")"))
                .append(Component.literal(" -> ")).append(Component.translatable("tooltip.ancientmagicks.block")).withStyle(ChatFormatting.GRAY));
        super.appendHoverText(stack, world, tooltip, flagIn);
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
            BlockPos blockPos = getPosOfFace(result.getBlockPos(), result.getDirection());
            Vec3 pos = new Vec3(blockPos.getX(), blockPos.getY(), blockPos.getZ());
            List<BlockPos> blocks = Lists.newArrayList();
            if ( range == 0 ) blocks.add(blockPos);
            else blocks = getBlockList(this, result, blockPos, range);

            MultiBlockHitResult mResult = new MultiBlockHitResult(pos, result.getDirection(), blockPos, result.isInside(), blocks, result.getLevel());
            spellData.addObject(mResult);
            aoeBlockSpellParticles(level, blocks, defaultStats());
        }
        else spellData.setValid(false);
        return spellData;
    }
}
