package net.mindoth.ancientmagicks.revamp.item.rune;

import net.mindoth.ancientmagicks.revamp.SpellData;
import net.minecraft.world.entity.Entity;

public class IntegerThreeRuneItem extends RuneItem {
    public IntegerThreeRuneItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public SpellData resolve(Entity caster, SpellData spellData) {
        spellData.addInteger(3);
        return spellData;
    }
}
