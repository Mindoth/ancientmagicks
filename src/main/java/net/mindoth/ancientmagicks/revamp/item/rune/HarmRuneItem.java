package net.mindoth.ancientmagicks.revamp.item.rune;

import net.mindoth.ancientmagicks.revamp.SpellData;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class HarmRuneItem extends UseOnEntityTemplate {
    public HarmRuneItem(Properties pProperties) {
        super(pProperties);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(Component.translatable("tooltip.ancientmagicks.entity").append(Component.literal(" -> "))
                .append(Component.translatable("tooltip.ancientmagicks.harm")).withStyle(ChatFormatting.GRAY));
        super.appendHoverText(stack, world, tooltip, flagIn);
    }

    @Override
    protected SpellData result(Entity caster, SpellData spellData, Entity entity) {
        int power;
        if ( spellData.getIntegers().isEmpty() || spellData.getLatestInteger() == null ) power = 4;
        else power = 4 + spellData.getLatestInteger();
        spellData.purgeIntegers(1);
        if ( entity instanceof LivingEntity && entity.isAttackable() && entity.isAlive() ) {
            attackEntity(caster, caster, entity, power);
        }
        return spellData;
    }
}
