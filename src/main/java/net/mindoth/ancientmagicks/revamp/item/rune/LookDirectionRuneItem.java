package net.mindoth.ancientmagicks.revamp.item.rune;

import net.mindoth.ancientmagicks.revamp.SpellData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;

public class LookDirectionRuneItem extends UseOnEntityTemplate {
    public LookDirectionRuneItem(Item.Properties pProperties) {
        super(pProperties);
    }

    @Override
    public SpellData result(Entity caster, SpellData spellData, Entity entity) {
        spellData.addVector(entity.getLookAngle());
        return spellData;
    }
}
