package net.mindoth.ancientmagicks.item.form;

import net.mindoth.ancientmagicks.item.ComponentItem;
import net.mindoth.ancientmagicks.item.spell.SpellItem;
import net.mindoth.ancientmagicks.item.modifier.SpellModifierItem;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

public class SpellFormItem extends ComponentItem {

    public SpellFormItem(Properties pProperties, int manaCost, int cooldown) {
        super(pProperties, manaCost, cooldown);
    }

    public boolean formSpell(LivingEntity owner, Entity caster, List<List<ComponentItem>> spellRecipe) {
        return false;
    }
}
