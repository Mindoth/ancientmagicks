package net.mindoth.ancientmagicks.item.spellrune.flight;

import net.mindoth.ancientmagicks.item.spellrune.SpellRuneItem;
import net.mindoth.ancientmagicks.registries.AncientMagicksEffects;
import net.mindoth.shadowizardlib.event.ShadowEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class FlightRune extends SpellRuneItem {

    public FlightRune(Properties pProperties, int cooldown) {
        super(pProperties, cooldown);
    }

    @Override
    public void castMagic(Player owner, Entity caster, Vec3 center, float xRot, float yRot, int useTime) {
        Level level = caster.level();
        Vec3 casterPos = caster.getEyePosition(1.0F);
        playMagicSound(level, casterPos);

        int life = 200;
        float size = 1.0F;

        LivingEntity target;
        if ( caster == owner ) {
            if ( (float)casterPos.distanceTo(center) != 0 ) {
                target = (LivingEntity) ShadowEvents.getPointedEntity(level, caster, (float)casterPos.distanceTo(center), 0.25F, caster == owner, true);
            }
            else target = owner;
        }
        else target = (LivingEntity)ShadowEvents.getNearestEntity(caster, level, size, null);

        if ( target instanceof Player && isAlly(owner, target)) {
            target.addEffect(new MobEffectInstance(AncientMagicksEffects.FLIGHT.get(), life, 0, false, false));
        }
        else {
            target.addEffect(new MobEffectInstance(MobEffects.LEVITATION, life, 0, false, false));
        }
    }
}
