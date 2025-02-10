package net.mindoth.ancientmagicks.item.castingitem;

import net.mindoth.ancientmagicks.item.DyeableMagicItem;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;

import java.util.Map;

public class ColorableStaffItem extends StaffItem implements DyeableMagicItem {
    public ColorableStaffItem(Properties pProperties, Item repairItem, Map<Attribute, AttributeModifier> additionalAttributes) {
        super(pProperties, repairItem, additionalAttributes);
    }
}
