package net.mindoth.ancientmagicks.revamp.item;

import net.mindoth.ancientmagicks.revamp.SpellData;
import net.minecraft.world.entity.Entity;

public class SelfRuneItem extends RuneItem {
    public SelfRuneItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public SpellData resolve(Entity caster, SpellData spellData) {
        spellData.addEntity(caster);
        return spellData;
    }
}
