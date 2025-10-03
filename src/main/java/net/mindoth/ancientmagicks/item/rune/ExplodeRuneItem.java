package net.mindoth.ancientmagicks.item.rune;

import net.mindoth.ancientmagicks.event.SpellData;
import net.mindoth.ancientmagicks.item.RuneItem;
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

public class ExplodeRuneItem extends RuneItem {
    public ExplodeRuneItem(Properties pProperties) {
        super(pProperties);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(Component.translatable("tooltip.ancientmagicks.position").append(Component.literal(" | "))
                .append(Component.literal("(")).append(Component.translatable("tooltip.ancientmagicks.integer")).append(Component.literal(")"))
                .append(Component.literal(" -> ")).append(Component.translatable("tooltip.ancientmagicks.explode")).withStyle(ChatFormatting.GRAY));
        super.appendHoverText(stack, world, tooltip, flagIn);
    }

    @Override
    public SpellData resolve(Entity caster, SpellData spellData) {
        if ( !spellData.getPositions().isEmpty() ) {
            Vec3 position = spellData.getLatestPosition().getPos();
            Level level = spellData.getLatestPosition().getLevel();
            spellData.purgePositions(1);
            float power;
            if ( spellData.getIntegers().isEmpty() || spellData.getLatestInteger() == null ) power = 1.0F;
            else power = 1 + spellData.getLatestInteger() * 0.5F;
            spellData.purgeIntegers(1);

            level.explode(null, position.x, position.y, position.z, power, Level.ExplosionInteraction.MOB);
        }
        else spellData.setValid(false);
        return spellData;
    }
}
