package net.mindoth.ancientmagicks.item.rune;

import net.mindoth.ancientmagicks.event.DimVec3;
import net.mindoth.ancientmagicks.event.MultiBlockHitResult;
import net.mindoth.ancientmagicks.event.SpellData;
import net.mindoth.ancientmagicks.item.RuneItem;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class BlockFaceRuneItem extends RuneItem {
    public BlockFaceRuneItem(Properties pProperties) {
        super(pProperties);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(Component.translatable("tooltip.ancientmagicks.block").append(Component.literal(" -> "))
                .append(Component.translatable("tooltip.ancientmagicks.block")).withStyle(ChatFormatting.GRAY));
        super.appendHoverText(stack, world, tooltip, flagIn);
    }

    @Override
    public SpellData resolve(Entity caster, SpellData spellData) {
        if ( !spellData.getBlocks().isEmpty() ) {
            if ( spellData.getLatestBlock() != null ) {
                MultiBlockHitResult result = spellData.getLatestBlock();
                spellData.purgeBlocks(1);
                BlockPos blockPos = getPosOfFace(result.getBlocks().get(0), result.getDirection());
                Vec3 vec3 = new Vec3(blockPos.getX(), blockPos.getY(), blockPos.getZ());
                MultiBlockHitResult mBlockHitResult = new MultiBlockHitResult(vec3, result.getDirection(), blockPos, result.isInside(),
                        List.of(blockPos), new DimVec3(vec3, result.getPos().getLevel()));
                spellData.addObject(mBlockHitResult);
            }
            else spellData.purgeBlocks(1);
        }
        else spellData.setValid(false);
        return spellData;
    }
}
