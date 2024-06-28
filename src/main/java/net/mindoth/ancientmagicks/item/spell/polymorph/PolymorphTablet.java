package net.mindoth.ancientmagicks.item.spell.polymorph;

import net.mindoth.ancientmagicks.item.castingitem.TabletItem;
import net.mindoth.shadowizardlib.event.ShadowEvents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class PolymorphTablet extends TabletItem {

    public PolymorphTablet(Properties pProperties, int tier) {
        super(pProperties, tier);
    }

    @Override
    public boolean castMagic(Player owner, Entity caster, Vec3 center, float xRot, float yRot, int useTime) {
        boolean state = false;
        Level level = caster.level();

        float range = 14.0F;
        float size = 1.0F;

        LivingEntity target;
        if ( caster == owner ) target = (LivingEntity)ShadowEvents.getPointedEntity(level, caster, range, 0.25F, caster == owner, true);
        else target = (LivingEntity)ShadowEvents.getNearestEntity(caster, level, size, null);

        if ( level instanceof ServerLevel serverLevel ) {
            if ( target instanceof Mob && !(target instanceof Sheep) ) {
                Sheep sheep = ((Mob)target).convertTo(EntityType.SHEEP, false);
                sheep.finalizeSpawn(serverLevel, serverLevel.getCurrentDifficultyAt(sheep.blockPosition()), MobSpawnType.CONVERSION, null, null);
                state = true;
                summonParticleLine(caster, sheep);

                /*Mob sheep = new Sheep(EntityType.SHEEP, serverLevel);
                ForgeEventFactory.onFinalizeSpawn(sheep, serverLevel, serverLevel.getCurrentDifficultyAt(sheep.blockPosition()), MobSpawnType.CONVERSION, null, null);
                serverLevel.tryAddFreshEntityWithPassengers(sheep);
                if ( sheep.isAddedToWorld() ) {
                    sheep.moveTo(target.position());
                    sheep.spawnAnim();
                    target.discard();
                }*/
            }
        }

        if ( state ) playMagicSound(level, center);

        return state;
    }

    private void summonParticleLine(Entity caster, Entity target) {
        double playerX = ShadowEvents.getEntityCenter(caster).x;
        double playerY = ShadowEvents.getEntityCenter(caster).y;
        double playerZ = ShadowEvents.getEntityCenter(caster).z;
        double listedEntityX = ShadowEvents.getEntityCenter(target).x;
        double listedEntityY = ShadowEvents.getEntityCenter(target).y;
        double listedEntityZ = ShadowEvents.getEntityCenter(target).z;
        int particleInterval = (int)Math.round(caster.distanceToSqr(target.position()));
        ServerLevel level = (ServerLevel)caster.level();
        for ( int k = 1; k < (1 + particleInterval); k++ ) {
            double lineX = playerX * (1 - ((double) k / particleInterval)) + listedEntityX * ((double) k / particleInterval);
            double lineY = playerY * (1 - ((double) k / particleInterval)) + listedEntityY * ((double) k / particleInterval);
            double lineZ = playerZ * (1 - ((double) k / particleInterval)) + listedEntityZ * ((double) k / particleInterval);
            level.sendParticles(ParticleTypes.EFFECT, lineX, lineY, lineZ, 0, 0, 0, 0, 0);
        }
    }
}
