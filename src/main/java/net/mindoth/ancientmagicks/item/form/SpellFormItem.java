package net.mindoth.ancientmagicks.item.form;

import net.mindoth.ancientmagicks.item.SpellItem;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;

public class SpellFormItem extends Item {
    public SpellFormItem(Properties pProperties) {
        super(pProperties);
    }

    public boolean castSpell(SpellItem spell, LivingEntity owner, Entity caster, int useTime) {
        return false;
    }
}
