package net.mindoth.ancientmagicks.revamp.item.rune;

import net.mindoth.ancientmagicks.revamp.SpellData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class HarmRuneItem extends UseOnEntityTemplate {
    public HarmRuneItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    protected SpellData result(Entity caster, SpellData spellData, Entity entity) {
        int power;
        if ( spellData.getIntegers().isEmpty() || spellData.getLatestInteger() == null ) power = 4;
        else power = 4 + spellData.getLatestInteger();
        spellData.purgeIntegers(1);
        if ( entity instanceof LivingEntity && entity.isAttackable() && entity.isAlive() ) {
            attackEntity(caster, caster, entity, power);
        }
        return spellData;
    }
}
