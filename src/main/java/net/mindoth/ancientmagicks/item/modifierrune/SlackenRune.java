package net.mindoth.ancientmagicks.item.modifierrune;

import net.mindoth.ancientmagicks.item.spellrune.abstractspell.AbstractSpellEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.Attributes;

import java.util.HashMap;

public class SlackenRune extends ModifierRuneItem {

    public SlackenRune(Properties pProperties, int cooldown) {
        super(pProperties, cooldown);
    }

    @Override
    public HashMap<String, Float> addModifiersToValues(HashMap<String, Float> map) {
        map.put("speed", map.getOrDefault("speed", 0.0F) - 1);
        return map;
    }

    @Override
    public AbstractSpellEntity addModifiersToSpellEntity(AbstractSpellEntity spell) {
        spell.speed *= 0.5;
        return spell;
    }

    @Override
    public MobEntity addModifiersToMinionEntity(MobEntity minion) {
        if ( minion.getAttributes().hasAttribute(Attributes.MOVEMENT_SPEED) ) {
            minion.getAttributes().getInstance(Attributes.MOVEMENT_SPEED)
                    .setBaseValue(minion.getAttributes().getBaseValue(Attributes.MOVEMENT_SPEED) - 0.1F);
        }
        return minion;
    }
}
