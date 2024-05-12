package net.mindoth.ancientmagicks.item.modifierrune;

import net.mindoth.ancientmagicks.item.spellrune.abstractspell.AbstractSpellEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.HashMap;

public class CompactRune extends ModifierRuneItem {
    public CompactRune(Properties pProperties, int cooldown) {
        super(pProperties, cooldown);
    }

    @Override
    public HashMap<String, Float> addModifiersToValues(HashMap<String, Float> map) {
        map.put("size", map.getOrDefault("size", 1.0F) - 1);
        return map;
    }

    @Override
    public AbstractSpellEntity addModifiersToSpellEntity(AbstractSpellEntity spell) {
        spell.size--;
        return spell;
    }

    @Override
    public Mob addModifiersToMinionEntity(Mob minion) {
        if ( minion.getAttributes().hasAttribute(Attributes.FOLLOW_RANGE) ) {
            minion.getAttributes().getInstance(Attributes.FOLLOW_RANGE)
                    .setBaseValue(minion.getAttributes().getBaseValue(Attributes.FOLLOW_RANGE) - 6);
        }
        return minion;
    }
}
