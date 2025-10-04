package net.mindoth.ancientmagicks.item.rune.shelf;

import net.mindoth.ancientmagicks.event.DimVec3;
import net.mindoth.ancientmagicks.event.MultiBlockHitResult;
import net.mindoth.ancientmagicks.event.SpellData;
import net.mindoth.ancientmagicks.item.rune.UseOnPositionTemplate;
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

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class TargetFaceRuneItem extends UseOnPositionTemplate {
    public TargetFaceRuneItem(Item.Properties pProperties) {
        super(pProperties);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(Component.translatable("tooltip.ancientmagicks.vector").append(Component.literal(", "))
                .append(Component.translatable("tooltip.ancientmagicks.vector"))
                .append(Component.literal(" -> ")).append(Component.translatable("tooltip.ancientmagicks.block")).withStyle(ChatFormatting.GRAY));
        super.appendHoverText(stack, world, tooltip, flagIn);
    }

    @Override
    public SpellData result(Entity caster, SpellData spellData, Vec3 position, Level level) {
        if ( !spellData.getVectors().isEmpty() ) {
            Vec3 direction = spellData.getLatestVector();
            spellData.purgeVectors(1);
            MultiBlockHitResult mResult = getPOVHitResult(position, direction, caster, level, ClipContext.Fluid.SOURCE_ONLY, 4.5F);
            BlockPos blockPos = getPosOfFace(mResult.getBlockPos(), mResult.getDirection());
            Vec3 pos = new Vec3(blockPos.getX(), blockPos.getY(), blockPos.getZ());
            spellData.addObject(new MultiBlockHitResult(pos, mResult.getDirection(), blockPos, mResult.isInside(), Collections.singletonList(blockPos),
                    new DimVec3(new Vec3(blockPos.getX(), blockPos.getY(), blockPos.getZ()), level)));

            //aoeBlockSpellParticles(level, Collections.singletonList(blockPos), defaultStats());
            Vec3 start = position.add(direction.multiply(1.0D, 1.0D, 1.0D));
            Vec3 end = blockPos.getCenter();
            summonParticleLine(start, end, (int)position.distanceTo(end) * 4, start, level, 0.15F, 8, defaultStats());
        }
        else spellData.setValid(false);
        return spellData;
    }
}
