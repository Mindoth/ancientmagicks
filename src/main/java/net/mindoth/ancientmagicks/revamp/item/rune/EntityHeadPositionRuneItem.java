package net.mindoth.ancientmagicks.revamp.item.rune;

import net.mindoth.ancientmagicks.revamp.SpellData;
import net.minecraft.world.entity.Entity;

public class EntityHeadPositionRuneItem extends UseOnEntityTemplate {
    public EntityHeadPositionRuneItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    protected SpellData result(Entity caster, SpellData spellData, Entity entity) {
        spellData.addVector(entity.getEyePosition());
        return spellData;
    }
}
