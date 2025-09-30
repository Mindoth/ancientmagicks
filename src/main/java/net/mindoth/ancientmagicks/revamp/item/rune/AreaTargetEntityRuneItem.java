package net.mindoth.ancientmagicks.revamp.item.rune;

import net.mindoth.ancientmagicks.revamp.MultiEntityHitResult;
import net.mindoth.ancientmagicks.revamp.SpellData;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class AreaTargetEntityRuneItem extends RuneItem {
    public AreaTargetEntityRuneItem(Properties pProperties) {
        super(pProperties);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(Component.translatable("tooltip.ancientmagicks.vector").append(Component.literal(" | "))
                .append(Component.translatable("tooltip.ancientmagicks.vector")).append(Component.literal(" | "))
                .append(Component.literal("(")).append(Component.translatable("tooltip.ancientmagicks.dimension")).append(Component.literal(")")).append(Component.literal(" | "))
                .append(Component.literal("(")).append(Component.translatable("tooltip.ancientmagicks.integer")).append(Component.literal(")"))
                .append(Component.literal(" -> ")).append(Component.translatable("tooltip.ancientmagicks.entity")).withStyle(ChatFormatting.GRAY));
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
            Vec3 pos = getPoint(position, direction, caster, level, 4.5F, 0.25F, false, true, true, false);

            Vec3 start = new Vec3(pos.x + range, pos.y + range, pos.z + range);
            Vec3 end = new Vec3(pos.x - range, pos.y - range, pos.z - range);
            AABB box = new AABB(start, end);
            List<Entity> entities = level.getEntitiesOfClass(Entity.class, box);

            if ( entities.isEmpty() ) spellData.addObject(null);
            else spellData.addObject(new MultiEntityHitResult(caster, pos, entities));
            aoeEntitySpellParticles(level, box, range, defaultStats());
        }
        else spellData.setValid(false);
        return spellData;
    }
}
