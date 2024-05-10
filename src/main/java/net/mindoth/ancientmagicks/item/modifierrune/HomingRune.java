package net.mindoth.ancientmagicks.item.modifierrune;

import net.mindoth.ancientmagicks.item.spellrune.abstractspell.AbstractSpellEntity;

import java.util.HashMap;

public class HomingRune extends ModifierRuneItem {

    public HomingRune(Properties pProperties, int cooldown) {
        super(pProperties, cooldown);
    }

    @Override
    public HashMap<String, Float> addModifiersToValues(HashMap<String, Float> map) {
        map.put("homing", 1.0F);
        return map;
    }

    @Override
    public AbstractSpellEntity addModifiersToSpellEntity(AbstractSpellEntity spell) {
        spell.homing = true;
        return spell;
    }
}
