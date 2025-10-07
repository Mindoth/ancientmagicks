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

public class VectorRuneItem extends UseOnPositionTemplate {
    public VectorRuneItem(Properties pProperties) {
        super(pProperties);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(Component.translatable("tooltip.ancientmagicks.position").append(Component.literal(", "))
                .append(Component.translatable("tooltip.ancientmagicks.position"))
                .append(Component.literal(" -> ")).append(Component.translatable("tooltip.ancientmagicks.vector")).withStyle(ChatFormatting.GRAY));
        super.appendHoverText(stack, world, tooltip, flagIn);
    }

    protected SpellData result(Entity caster, SpellData spellData, Vec3 pos, Level level) {
        if ( !spellData.getPositions().isEmpty() ) {
            if ( spellData.getLatestPosition() != null ) {
                Vec3 position = spellData.getLatestPosition().getPos();
                spellData.purgePositions(1);
                Vec3 vector = pos.vectorTo(position);
                spellData.addObject(vector.normalize());
                return spellData;
            }
            else spellData.purgePositions(1);
        }
        else spellData.setValid(false);
        return spellData;
    }
}
