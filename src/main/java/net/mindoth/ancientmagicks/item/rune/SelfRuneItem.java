package net.mindoth.ancientmagicks.item.rune;

import net.mindoth.ancientmagicks.event.MultiEntityHitResult;
import net.mindoth.ancientmagicks.event.SpellData;
import net.mindoth.ancientmagicks.item.RuneItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class SelfRuneItem extends RuneItem {
    public SelfRuneItem(Properties pProperties) {
        super(pProperties);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(Component.literal("-> ").append(Component.translatable("tooltip.ancientmagicks.entity")).withStyle(ChatFormatting.GRAY));
        super.appendHoverText(stack, world, tooltip, flagIn);
    }

    @Override
    public SpellData resolve(Entity caster, SpellData spellData) {
        spellData.addObject(new MultiEntityHitResult(caster, caster.position(), Collections.singletonList(caster)));
        return spellData;
    }
}
