package net.mindoth.ancientmagicks.revamp.item.rune;

import net.mindoth.ancientmagicks.revamp.MultiEntityHitResult;
import net.mindoth.ancientmagicks.revamp.SpellData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.EntityHitResult;

public class AttackRuneItem extends EntityTargetTemplate {
    public AttackRuneItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public SpellData resolve(Entity caster, SpellData spellData) {
        if ( !spellData.getEntities().isEmpty() ) {
            EntityHitResult result = spellData.getLatestEntity();
            spellData.purgeEntities(1);
            int power;
            if ( spellData.getIntegers().isEmpty() || spellData.getLatestInteger() == null ) power = 4;
            else power = spellData.getLatestInteger();
            spellData.purgeIntegers(1);
            if ( result != null ) handleAttack(caster, result, power);
        }
        else spellData.setValid(false);
        return spellData;
    }

    private void handleAttack(Entity caster, EntityHitResult result, int power) {
        if ( result instanceof MultiEntityHitResult mResult ) {
            for ( Entity target : mResult.getEntities() ) {
                if ( target instanceof LivingEntity && target.isAttackable() && target.isAlive() ) {
                    attackEntity(caster, caster, target, power);
                    addEnchantParticles(target, 0.15F, 8, defaultStats());
                }
            }
        }
        else {
            Entity target = result.getEntity();
            if ( target instanceof LivingEntity && target.isAttackable() && target.isAlive() ) {
                attackEntity(caster, caster, target, power);
                addEnchantParticles(target, 0.15F, 8, defaultStats());
            }
        }
    }
}
