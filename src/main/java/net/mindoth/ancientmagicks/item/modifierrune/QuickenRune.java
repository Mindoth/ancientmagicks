package net.mindoth.ancientmagicks.item.modifierrune;

import net.mindoth.ancientmagicks.item.spellrune.abstractspell.AbstractSpellEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.HashMap;

public class QuickenRune extends ModifierRuneItem {

    public QuickenRune(Properties pProperties, int cooldown) {
        super(pProperties, cooldown);
    }

    @Override
    public HashMap<String, Float> addModifiersToValues(HashMap<String, Float> map) {
        map.put("speed", map.getOrDefault("speed", 0.0F) + 1);
        return map;
    }

    @Override
    public AbstractSpellEntity addModifiersToSpellEntity(AbstractSpellEntity spell) {
        spell.speed += 0.4F;
        return spell;
    }

    @Override
    public Mob addModifiersToMinionEntity(Mob minion) {
        if ( minion.getAttributes().hasAttribute(Attributes.MOVEMENT_SPEED) ) {
            minion.getAttributes().getInstance(Attributes.MOVEMENT_SPEED)
                    .setBaseValue(minion.getAttributes().getBaseValue(Attributes.MOVEMENT_SPEED) + 0.1F);
        }
        return minion;
    }
}
