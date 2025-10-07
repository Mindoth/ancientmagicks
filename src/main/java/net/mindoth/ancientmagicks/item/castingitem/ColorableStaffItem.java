package net.mindoth.ancientmagicks.item.castingitem;

import net.mindoth.ancientmagicks.item.DyeableMagickItem;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;

import java.util.Map;

public class ColorableStaffItem extends StaffItem implements DyeableMagickItem {
    public ColorableStaffItem(Properties pProperties, Item repairItem, Map<Attribute, AttributeModifier> additionalAttributes) {
        super(pProperties, repairItem, additionalAttributes);
    }
}
