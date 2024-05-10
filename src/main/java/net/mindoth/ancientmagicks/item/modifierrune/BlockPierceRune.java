package net.mindoth.ancientmagicks.item.modifierrune;

import net.mindoth.ancientmagicks.item.spellrune.abstractspell.AbstractSpellEntity;

import java.util.HashMap;

public class BlockPierceRune extends ModifierRuneItem {

    public BlockPierceRune(Properties pProperties, int cooldown) {
        super(pProperties, cooldown);
    }

    @Override
    public HashMap<String, Float> addModifiersToValues(HashMap<String, Float> map) {
        map.put("blockPierce", 0.0F + Integer.MAX_VALUE);
        return map;
    }

    @Override
    public AbstractSpellEntity addModifiersToSpellEntity(AbstractSpellEntity spell) {
        spell.blockPierce = Integer.MAX_VALUE;
        return spell;
    }
}
