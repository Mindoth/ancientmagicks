package net.mindoth.ancientmagicks.item.rune;

import net.mindoth.ancientmagicks.event.DimVec3;
import net.mindoth.ancientmagicks.event.SpellData;
import net.minecraft.ChatFormatting;
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

public class EntityPositionRuneItem extends UseOnEntityTemplate {
    public EntityPositionRuneItem(Properties pProperties) {
        super(pProperties);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(Component.translatable("tooltip.ancientmagicks.entity").append(Component.literal(" -> "))
                .append(Component.translatable("tooltip.ancientmagicks.position")).withStyle(ChatFormatting.GRAY));
        super.appendHoverText(stack, world, tooltip, flagIn);
    }

    @Override
    protected SpellData result(Entity caster, SpellData spellData, Entity entity) {
        spellData.addObject(new DimVec3(entity.position(), entity.level()));
        return spellData;
    }
}
