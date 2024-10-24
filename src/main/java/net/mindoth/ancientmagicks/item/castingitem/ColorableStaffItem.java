package net.mindoth.ancientmagicks.item.castingitem;

import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.DyeableLeatherItem;

import java.util.Map;

public class ColorableStaffItem extends StaffItem implements DyeableLeatherItem {
    public ColorableStaffItem(Properties pProperties, double attackDamage, double attackSpeed, Map<Attribute, AttributeModifier> additionalAttributes) {
        super(pProperties, attackDamage, attackSpeed, additionalAttributes);
    }
}
