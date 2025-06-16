package net.mindoth.ancientmagicks.item.form;

import net.mindoth.ancientmagicks.item.ComponentItem;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

public class SpellFormItem extends ComponentItem {

    public SpellFormItem(Properties pProperties, int cost) {
        super(pProperties, cost);
    }

    public boolean formSpell(LivingEntity owner, Entity caster, List<ComponentItem> spellStack, List<String> data) {
        return false;
    }
}
