package net.mindoth.ancientmagicks.item.spell.windburst;

import net.mindoth.ancientmagicks.item.spell.abstractspell.SpellItem;
import net.mindoth.ancientmagicks.item.spell.abstractspell.SpellSchool;
import net.mindoth.shadowizardlib.event.ShadowEvents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class WindBurstItem extends SpellItem {

    public WindBurstItem(Properties pProperties, int spellTier, int manaCost, int cooldown, SpellSchool spellSchool) {
        super(pProperties, spellTier, manaCost, cooldown, spellSchool);
    }

    @Override
    public boolean castMagic(Player owner, Entity caster, Vec3 center, float xRot, float yRot, int useTime) {
        boolean state = false;
        Level level = caster.level();
        Vec3 casterPos = caster.getEyePosition(1.0F);

        float range = 2.0F;
        float size = 2.0F;
        float power = 2.0F;

        Vec3 point = ShadowEvents.getPoint(level, caster, range, 0, caster == owner, false, true, true, false);
        List<Entity> targets = level.getEntities(caster, new AABB(new Vec3(point.x + size, point.y + size, point.z + size),
                new Vec3(point.x - size, point.y - size, point.z - size)));
        for ( Entity target : targets ) {
            Vec3 targetPoint = ShadowEvents.getPoint(level, caster, 1, 0.25F, caster == owner, false, true, true, false);
            if ( target != caster && isPushable(target) && hasLineOfSight(caster, target) ) {
                target.push((targetPoint.x - casterPos.x) * power, (targetPoint.y - casterPos.y + 0.5F) * power, (targetPoint.z - casterPos.z) * power);
                target.hurtMarked = true;
                if ( !state ) state = true;
            }
        }

        if ( state ) {
            Vec3 particlePoint = ShadowEvents.getPoint(level, caster, range, 0, caster == owner, false, true, true, false);
            addParticles(level, casterPos, particlePoint);
            playWindSound(level, center);
        }

        return state;
    }

    private static void addParticles(Level world, Vec3 casterPos, Vec3 center) {
        ServerLevel level = (ServerLevel)world;
        float size = 0.1F;
        for ( int i = 0; i < 4; i++ ) {
            float randX = (float) ((Math.random() * (size - (-size))) + (-size));
            float randY = (float) ((Math.random() * (size - (-size))) + (-size));
            float randZ = (float) ((Math.random() * (size - (-size))) + (-size));
            level.sendParticles(ParticleTypes.POOF, casterPos.x + randX, casterPos.y + randY, casterPos.z + randZ,
                    0, (center.x - casterPos.x) / 3, (center.y - casterPos.y) / 3, (center.z - casterPos.z) / 3, 0.5F);
        }
    }
}
