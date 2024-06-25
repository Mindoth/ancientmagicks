package net.mindoth.ancientmagicks.item.spellrune.ward;

import net.mindoth.ancientmagicks.item.spellrune.SpellRuneItem;
import net.mindoth.shadowizardlib.event.ShadowEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class WardSpell extends SpellRuneItem {

    public WardSpell(Properties pProperties, int tier) {
        super(pProperties, tier);
    }

    @Override
    public void castMagic(Player owner, Entity caster, Vec3 center, float xRot, float yRot, int useTime) {
        Level level = caster.level();
        Vec3 casterPos = caster.getEyePosition(1.0F);
        playMagicSound(level, casterPos);

        int life = 1200;
        float size = 1.0F;

        LivingEntity target;
        if ( caster == owner ) {
            if ( (float)casterPos.distanceTo(center) != 0 ) {
                target = (LivingEntity) ShadowEvents.getPointedEntity(level, caster, (float)casterPos.distanceTo(center), 0.25F, caster == owner, true);
            }
            else target = owner;
        }
        else target = (LivingEntity)ShadowEvents.getNearestEntity(caster, level, size, null);

        if ( isAlly(owner, target)) {
            if ( target.hasEffect(MobEffects.DAMAGE_RESISTANCE) ) {
                target.removeEffect(MobEffects.DAMAGE_RESISTANCE);
                target.heal(4);
            }
            else target.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, life, 0, false, false));
        }
    }
}
