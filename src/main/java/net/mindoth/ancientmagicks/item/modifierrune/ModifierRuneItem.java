package net.mindoth.ancientmagicks.item.modifierrune;

import net.mindoth.ancientmagicks.item.RuneItem;
import net.mindoth.ancientmagicks.item.spellrune.abstractspell.AbstractSpellEntity;
import net.minecraft.entity.MobEntity;

import java.util.HashMap;

public class ModifierRuneItem extends RuneItem {

    public ModifierRuneItem(Properties pProperties, int cooldown) {
        super(pProperties, cooldown);
    }

    public HashMap<String, Float> addModifiersToValues(HashMap<String, Float> map) {
        return map;
    }

    public AbstractSpellEntity addModifiersToSpellEntity(AbstractSpellEntity spell) {
        return spell;
    }

    public MobEntity addModifiersToMinionEntity(MobEntity minion) {
        return minion;
    }
}
