package net.mindoth.ancientmagicks.revamp.item.rune;

import net.mindoth.ancientmagicks.revamp.SpellData;
import net.minecraft.world.entity.Entity;

public class AttackRuneItem extends RuneItem {
    public AttackRuneItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public SpellData resolve(Entity caster, SpellData spellData) {
        if ( !spellData.getEntities().isEmpty() ) {
            Entity target = spellData.getEntities().get(spellData.getEntities().size() - 1);
            spellData.purgeEntities(1);
            if ( target != null ) handleAttack(caster, target);
        }
        else spellData.setValid(false);
        return spellData;
    }

    private void handleAttack(Entity caster, Entity target) {
        if ( target.isAttackable() && target.isAlive() ) {
            target.hurt(target.damageSources().indirectMagic(caster, caster), 4);
        }
    }
}
