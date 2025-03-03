package net.mindoth.ancientmagicks.item.form;

import net.mindoth.ancientmagicks.item.ComponentItem;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

public class SpellFormItem extends ComponentItem {

    public SpellFormItem(Properties pProperties, int manaCost, int cooldown) {
        super(pProperties, manaCost, cooldown);
    }

    public boolean formSpell(LivingEntity owner, Entity caster, List<ComponentItem> spellStack) {
        return false;
    }
}
