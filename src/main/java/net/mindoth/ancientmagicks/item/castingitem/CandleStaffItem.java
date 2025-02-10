package net.mindoth.ancientmagicks.item.castingitem;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

public class CandleStaffItem extends StaffItem {

    public CandleStaffItem(Properties pProperties, Item repairItem, Map<Attribute, AttributeModifier> additionalAttributes) {
        super(pProperties, repairItem, additionalAttributes);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flagIn) {
        Component component = Component.translatable("tooltip.ancientmagicks.candle_staff").withStyle(ChatFormatting.GOLD);
        tooltip.add(component);

        super.appendHoverText(stack, world, tooltip, flagIn);
    }
}
