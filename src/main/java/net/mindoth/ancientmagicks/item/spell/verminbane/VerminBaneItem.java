package net.mindoth.ancientmagicks.item.spell.verminbane;

import net.mindoth.ancientmagicks.item.SpellItem;
import net.mindoth.shadowizardlib.event.ShadowEvents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.stream.Stream;

public class VerminBaneItem extends SpellItem {

    public VerminBaneItem(Properties pProperties, int spellTier, int manaCost, int cooldown) {
        super(pProperties, spellTier, manaCost, cooldown);
    }

    @Override
    public boolean isChannel() {
        return true;
    }

    @Override
    public boolean castMagic(Player owner, Entity caster, Vec3 center, float xRot, float yRot, int useTime) {
        boolean state = false;
        Level level = caster.level();
        int adjuster = 1;
        float down = -0.2F;
        if ( caster != owner ) {
            adjuster = -1;
            down = 0.0F;
        }

        float range = 2.5F;
        if ( owner != caster ) range = 0.0F;
        float size = 1.5F;
        float power = 20.0F;

        Vec3 point = ShadowEvents.getPoint(level, caster, range, 0, caster == owner, false, false, true, false);
        List<Entity> targets = level.getEntities(caster, new AABB(new Vec3(point.x + size, point.y + size, point.z + size),
                new Vec3(point.x - size, point.y - size, point.z - size)));
        targets.removeIf(target -> !(target instanceof LivingEntity));

        Vec3 point2 = ShadowEvents.getPoint(level, caster, range * 2, 0, caster == owner, false, false, true, false);
        List<Entity> targets2 = level.getEntities(caster, new AABB(new Vec3(point2.x + size, point2.y + size, point2.z + size),
                new Vec3(point2.x - size, point2.y - size, point2.z - size)));
        targets2.removeIf(target -> !(target instanceof LivingEntity) || targets.contains(target));

        List<Entity> doubleList = Stream.concat(targets.stream(), targets2.stream()).toList();

        for ( Entity target : doubleList ) {
            if ( target != caster && target instanceof Mob mob && !isAlly(owner, mob) && hasLineOfSight(caster, target) && mob.getMobType() == MobType.ARTHROPOD ) {
                attackEntityWithoutKnockback(owner, caster, target, getPowerInRange(10.0F, power));
            }
        }

        spawnParticles(level, caster, adjuster, down);
        state = true;

        if ( state ) {
            if ( useTime % 3 == 0 ) playWindSound(level, center);
        }

        return state;
    }

    private void spawnParticles(Level level, Entity caster, int adjuster, float down) {
        if ( !(level instanceof ServerLevel world) ) return;
        Vec3 center = caster.getEyePosition(1.0F).add(0, down, 0);
        Vec3 lookVec = caster.getLookAngle();
        double arc = 25;
        for ( int i = 0; i < 8; i++ ) {
            world.sendParticles(ParticleTypes.SNEEZE, center.x, center.y + down, center.z, 0,
                    lookVec.x + level.random.triangle(0.0D, 0.0172275D * arc),
                    lookVec.y + level.random.triangle(0.0D, 0.0172275D * arc),
                    lookVec.z + level.random.triangle(0.0D, 0.0172275D * arc),
                    1.0D);
        }
    }
}