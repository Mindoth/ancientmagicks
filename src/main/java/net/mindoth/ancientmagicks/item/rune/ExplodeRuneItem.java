package net.mindoth.ancientmagicks.item.rune;

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

public class ExplodeRuneItem extends UseOnPositionTemplate {
    public ExplodeRuneItem(Properties pProperties) {
        super(pProperties);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(Component.translatable("tooltip.ancientmagicks.position").append(Component.literal(", "))
                .append(Component.translatable("tooltip.ancientmagicks.integer")).append(Component.literal(" ->")).withStyle(ChatFormatting.GRAY));
        super.appendHoverText(stack, world, tooltip, flagIn);
    }

    @Override
    public SpellData result(Entity caster, SpellData spellData, Vec3 position, Level level) {
        if ( !spellData.getIntegers().isEmpty() ) {
            if ( spellData.getLatestInteger() != null ) {
                int power = spellData.getLatestInteger();
                spellData.purgeIntegers(1);
                level.explode(null, position.x, position.y, position.z, power, Level.ExplosionInteraction.MOB);
                return spellData;
            }
            else spellData.purgeIntegers(1);
        }
        else spellData.setValid(false);
        return spellData;
    }
}
