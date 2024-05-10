package net.mindoth.ancientmagicks.item.modifierrune;

import net.mindoth.ancientmagicks.item.spellrune.abstractspell.AbstractSpellEntity;

import java.util.HashMap;

public class ExtendRune extends ModifierRuneItem {

    public ExtendRune(Properties pProperties, int cooldown) {
        super(pProperties, cooldown);
    }

    @Override
    public HashMap<String, Float> addModifiersToValues(HashMap<String, Float> map) {
        map.put("life", map.getOrDefault("life", 0.0F) + 1);
        return map;
    }

    @Override
    public AbstractSpellEntity addModifiersToSpellEntity(AbstractSpellEntity spell) {
        spell.life += spell.getDefaultLife();
        return spell;
    }
}
