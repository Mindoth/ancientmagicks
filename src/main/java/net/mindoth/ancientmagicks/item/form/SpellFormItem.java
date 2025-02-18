package net.mindoth.ancientmagicks.item.form;

import net.mindoth.ancientmagicks.item.SpellItem;
import net.mindoth.ancientmagicks.item.modifier.SpellModifierItem;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;

import java.util.List;

public class SpellFormItem extends Item {

    public SpellFormItem(Properties pProperties, int manaCost, int cooldown) {
        super(pProperties);
    }

    public boolean formSpell(SpellItem spell, LivingEntity owner, Entity caster, List<SpellModifierItem> modifiers) {
        return false;
    }
}
