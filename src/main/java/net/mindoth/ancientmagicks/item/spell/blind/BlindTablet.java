package net.mindoth.ancientmagicks.item.spell.blind;

import net.mindoth.ancientmagicks.item.castingitem.TabletItem;
import net.mindoth.shadowizardlib.event.ShadowEvents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class BlindTablet extends TabletItem {

    public BlindTablet(Properties pProperties, int tier, boolean isChannel, int cooldown) {
        super(pProperties, tier, isChannel, cooldown);
    }

    @Override
    public boolean castMagic(Player owner, Entity caster, Vec3 center, float xRot, float yRot, int useTime) {
        boolean state = false;
        Level level = caster.level();

        int life = 200;
        float range = 14.0F;
        float size = range * 0.5F;

        LivingEntity target;
        if ( caster == owner ) target = (LivingEntity) ShadowEvents.getPointedEntity(level, caster, range, 0.25F, caster == owner, true);
        else target = (LivingEntity)ShadowEvents.getNearestEntity(caster, level, size, null);

        if ( !isAlly(owner, target)) {
            target.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, life));
            summonParticleLine(caster, target);
            state = true;
        }

        if ( state ) playMagicSound(level, center);

        return state;
    }

    private void summonParticleLine(Entity caster, Entity target) {
        double playerX = ShadowEvents.getEntityCenter(caster).x;
        double playerY = ShadowEvents.getEntityCenter(caster).y;
        double playerZ = ShadowEvents.getEntityCenter(caster).z;
        double listedEntityX = target.getEyePosition().x;
        double listedEntityY = target.getEyePosition().y;
        double listedEntityZ = target.getEyePosition().z;
        int particleInterval = (int)Math.round(caster.distanceToSqr(target.position()));
        ServerLevel level = (ServerLevel)caster.level();
        for ( int k = 1; k < (1 + particleInterval); k++ ) {
            double lineX = playerX * (1 - ((double) k / particleInterval)) + listedEntityX * ((double) k / particleInterval);
            double lineY = playerY * (1 - ((double) k / particleInterval)) + listedEntityY * ((double) k / particleInterval);
            double lineZ = playerZ * (1 - ((double) k / particleInterval)) + listedEntityZ * ((double) k / particleInterval);
            level.sendParticles(ParticleTypes.SMOKE, lineX, lineY, lineZ, 0, 0, 0, 0, 0);
        }
    }
}
