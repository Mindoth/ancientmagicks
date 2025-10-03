package net.mindoth.ancientmagicks.item.rune;

import net.mindoth.ancientmagicks.event.MultiEntityHitResult;
import net.mindoth.ancientmagicks.event.SpellData;
import net.mindoth.ancientmagicks.item.RuneItem;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.item.PrimedTnt;

public class UseOnEntityTemplate extends RuneItem {
    public UseOnEntityTemplate(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public SpellData resolve(Entity caster, SpellData spellData) {
        if ( !spellData.getEntities().isEmpty() ) {
            if ( spellData.getLatestEntity() != null ) {
                MultiEntityHitResult result = spellData.getLatestEntity();
                spellData.purgeEntities(1);
                final SpellData copyData = SpellData.clone(spellData);
                spellData = result(caster, spellData, result.getEntities().get(0));
                boolean state = spellData.isValid();
                for ( int i = 1; i < result.getEntities().size(); i++ ) {
                    final SpellData tempData = SpellData.clone(copyData);
                    Entity entity = result.getEntities().get(i);
                    result(caster, tempData, entity);
                    if ( !state && tempData.isValid() ) state = true;
                }
                spellData.setValid(state);
            }
        }
        else spellData.setValid(false);
        return spellData;
    }

    protected SpellData result(Entity caster, SpellData spellData, Entity entity) {
        return spellData;
    }

    public static void attackEntity(Entity owner, Entity caster, Entity target, float amount) {
        target.hurt(target.damageSources().indirectMagic(caster, owner), amount);
    }

    public static void attackEntityWithoutKnockback(Entity owner, Entity caster, Entity target, float amount) {
        final double vx = target.getDeltaMovement().x;
        final double vy = target.getDeltaMovement().y;
        final double vz = target.getDeltaMovement().z;
        attackEntity(owner, target, caster, amount);
        target.setDeltaMovement(vx, vy, vz);
        target.hurtMarked = true;
    }

    public static boolean isPushable(Entity entity) {
        return ( entity instanceof LivingEntity || entity instanceof ItemEntity || entity instanceof PrimedTnt || entity instanceof FallingBlockEntity);
    }
}
