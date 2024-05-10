package net.mindoth.ancientmagicks.item.modifierrune;

import net.mindoth.ancientmagicks.item.spellrune.abstractspell.AbstractSpellEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.Attributes;

import java.util.HashMap;

public class AmplifyRune extends ModifierRuneItem {

    public AmplifyRune(Properties pProperties, int cooldown) {
        super(pProperties, cooldown);
    }

    @Override
    public HashMap<String, Float> addModifiersToValues(HashMap<String, Float> map) {
        map.put("power", map.getOrDefault("power", 0.0F) + 1);
        return map;
    }

    @Override
    public AbstractSpellEntity addModifiersToSpellEntity(AbstractSpellEntity spell) {
        spell.power += spell.getDefaultPower();
        return spell;
    }

    @Override
    public MobEntity addModifiersToMinionEntity(MobEntity minion) {
        if ( minion.getAttributes().hasAttribute(Attributes.ATTACK_DAMAGE) ) {
            minion.getAttributes().getInstance(Attributes.ATTACK_DAMAGE)
                    .setBaseValue(minion.getAttributes().getBaseValue(Attributes.ATTACK_DAMAGE) + 1);
        }
        if ( minion.getAttributes().hasAttribute(Attributes.MAX_HEALTH) ) {
            minion.getAttributes().getInstance(Attributes.MAX_HEALTH)
                    .setBaseValue(minion.getAttributes().getBaseValue(Attributes.MAX_HEALTH) + 1);
        }
        return minion;
    }
}
