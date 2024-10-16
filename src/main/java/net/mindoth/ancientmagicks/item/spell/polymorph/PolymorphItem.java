package net.mindoth.ancientmagicks.item.spell.polymorph;

import net.mindoth.ancientmagicks.item.SpellItem;
import net.mindoth.shadowizardlib.event.ShadowEvents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class PolymorphItem extends SpellItem {

    public PolymorphItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public boolean isAncient() {
        return true;
    }

    @Override
    public int getCooldown() {
        return 200;
    }

    @Override
    public boolean castMagic(Player owner, Entity caster, Vec3 center, float xRot, float yRot, int useTime) {
        boolean state = false;
        Level level = caster.level();

        float range = 14.0F;
        float size = 7.0F;

        LivingEntity target;
        if ( caster == owner ) target = (LivingEntity)ShadowEvents.getPointedEntity(level, caster, range, 0.25F, caster == owner, true);
        else target = (LivingEntity)ShadowEvents.getNearestEntity(caster, level, size, null);

        if ( level instanceof ServerLevel serverLevel ) {
            if ( target instanceof Mob && !(target instanceof Sheep) ) {
                Sheep sheep = ((Mob)target).convertTo(EntityType.SHEEP, false);
                sheep.finalizeSpawn(serverLevel, serverLevel.getCurrentDifficultyAt(sheep.blockPosition()), MobSpawnType.CONVERSION, null, null);
                ShadowEvents.summonParticleLine(ParticleTypes.ENTITY_EFFECT, caster, ShadowEvents.getEntityCenter(caster), ShadowEvents.getEntityCenter(sheep),
                        0, 1, 85F / 255F, 1, 1);
                state = true;
            }
        }

        if ( state ) playMagicSound(level, center);

        return state;
    }
}
