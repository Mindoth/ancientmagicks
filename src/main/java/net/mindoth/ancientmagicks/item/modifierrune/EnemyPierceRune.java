package net.mindoth.ancientmagicks.item.modifierrune;

import net.mindoth.ancientmagicks.item.spellrune.abstractspell.AbstractSpellEntity;

public class EnemyPierceRune extends ModifierRuneItem {

    public EnemyPierceRune(Properties pProperties, int cooldown) {
        super(pProperties, cooldown);
    }

    @Override
    public AbstractSpellEntity addModifiersToSpellEntity(AbstractSpellEntity spell) {
        spell.enemyPierce = Integer.MAX_VALUE;
        return spell;
    }
}
