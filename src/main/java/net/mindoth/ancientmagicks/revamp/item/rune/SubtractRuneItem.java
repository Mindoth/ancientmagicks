package net.mindoth.ancientmagicks.revamp.item.rune;

import net.mindoth.ancientmagicks.revamp.SpellData;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class SubtractRuneItem extends RuneItem {
    public SubtractRuneItem(Properties pProperties) {
        super(pProperties);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(Component.translatable("tooltip.ancientmagicks.integer").append(Component.literal(" | "))
                .append(Component.translatable("tooltip.ancientmagicks.integer"))
                .append(Component.literal(" -> ")).append(Component.translatable("tooltip.ancientmagicks.integer")).withStyle(ChatFormatting.GRAY));
        super.appendHoverText(stack, world, tooltip, flagIn);
    }

    @Override
    public SpellData resolve(Entity caster, SpellData spellData) {
        if ( !spellData.getIntegers().isEmpty() && spellData.getIntegers().size() >= 2 ) {
            int first = spellData.getLatestInteger();
            spellData.purgeIntegers(1);
            int second = spellData.getLatestInteger();
            spellData.purgeIntegers(1);
            spellData.addObject(first - second);
        }
        else spellData.setValid(false);
        return spellData;
    }
}
