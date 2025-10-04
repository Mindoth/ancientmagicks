package net.mindoth.ancientmagicks.item.rune;

import net.mindoth.ancientmagicks.event.SpellData;
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

public class AttackRuneItem extends UseOnEntityTemplate {
    public AttackRuneItem(Properties pProperties) {
        super(pProperties);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(Component.translatable("tooltip.ancientmagicks.entity").append(Component.literal(", "))
                .append(Component.translatable("tooltip.ancientmagicks.integer")).append(Component.literal(" ->")).withStyle(ChatFormatting.GRAY));
        super.appendHoverText(stack, world, tooltip, flagIn);
    }

    @Override
    protected SpellData result(Entity caster, SpellData spellData, Entity entity) {
        if ( !spellData.getIntegers().isEmpty() ) {
            if ( spellData.getLatestInteger() != null ) {
                int power = spellData.getLatestInteger();
                spellData.purgeIntegers(1);
                if ( entity instanceof LivingEntity && entity.isAttackable() && entity.isAlive() ) {
                    attackEntity(caster, caster, entity, power);
                }
                return spellData;
            }
            else spellData.purgeIntegers(1);
        }
        else spellData.setValid(false);
        return spellData;
    }
}
