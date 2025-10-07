package net.mindoth.ancientmagicks.item.rune;

import net.mindoth.ancientmagicks.event.MultiEntityHitResult;
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

public class ForceRuneItem extends UseOnEntityTemplate {
    public ForceRuneItem(Properties pProperties) {
        super(pProperties);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(Component.translatable("tooltip.ancientmagicks.entity").append(Component.literal(", "))
                .append(Component.translatable("tooltip.ancientmagicks.vector")).append(Component.literal(", "))
                .append(Component.translatable("tooltip.ancientmagicks.integer")).append(Component.literal(" ->")).withStyle(ChatFormatting.GRAY));
        super.appendHoverText(stack, world, tooltip, flagIn);
    }

    @Override
    protected SpellData result(Entity caster, SpellData spellData, MultiEntityHitResult result, Entity entity) {
        if ( !spellData.getVectors().isEmpty() && !spellData.getIntegers().isEmpty() ) {
            if ( spellData.getLatestVector() != null && spellData.getLatestInteger() != null ) {
                Vec3 direction = spellData.getLatestVector();
                spellData.purgeVectors(1);
                int power = spellData.getLatestInteger();
                spellData.purgeIntegers(1);
                Vec3 towards = entity.position().add(direction);
                entity.push((towards.x - entity.position().x) * power, (towards.y - entity.position().y) * power, (towards.z - entity.position().z) * power);
                entity.hurtMarked = true;
            }
            else {
                spellData.purgeVectors(1);
                spellData.purgeIntegers(1);
            }
        }
        else spellData.setValid(false);
        return spellData;
    }
}
