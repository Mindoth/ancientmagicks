package net.mindoth.ancientmagicks.revamp.item;

import net.mindoth.ancientmagicks.revamp.SpellData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class AttackRuneItem extends RuneItem {
    public AttackRuneItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public SpellData resolve(Entity caster, SpellData spellData) {
        if ( !spellData.getEntities().isEmpty() ) {
            Entity target = spellData.getEntities().get(spellData.getEntities().size() - 1);
            spellData.purgeEntities(1);
            if ( target != null ) target.hurt(target.damageSources().indirectMagic(caster, caster), 4);
        }
        else spellData.setValid(false);
        return spellData;
    }
}
