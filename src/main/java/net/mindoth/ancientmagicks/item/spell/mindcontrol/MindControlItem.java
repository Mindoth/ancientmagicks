package net.mindoth.ancientmagicks.item.spell.mindcontrol;

import net.mindoth.ancientmagicks.item.SpellItem;
import net.mindoth.ancientmagicks.registries.AncientMagicksEffects;
import net.mindoth.shadowizardlib.event.ShadowEvents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class MindControlItem extends SpellItem {

    public MindControlItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public boolean isAncient() {
        return true;
    }

    @Override
    public int getCooldown() {
        return 1200;
    }

    @Override
    public boolean castMagic(Player owner, Entity caster, Vec3 center, float xRot, float yRot, int useTime) {
        boolean state = false;
        Level level = caster.level();

        int life = 600;
        float range = 14.0F;
        float size = range * 0.5F;

        LivingEntity target;
        if ( caster == owner ) target = (LivingEntity) ShadowEvents.getPointedEntity(level, caster, range, 0.25F, caster == owner, true);
        else target = (LivingEntity)ShadowEvents.getNearestEntity(caster, level, size, null);

        if ( target instanceof Mob mob && !isAlly(owner, mob) ) {
            mob.getPersistentData().putUUID(MindControlEffect.NBT_KEY, owner.getUUID());
            mob.addEffect(new MobEffectInstance(AncientMagicksEffects.MIND_CONTROL.get(), life));
            //if ( mob instanceof PathfinderMob pthMob ) mob.goalSelector.addGoal(0, new MeleeAttackGoal(pthMob, 1.0F, true));
            ShadowEvents.summonParticleLine(ParticleTypes.ENTITY_EFFECT, caster, ShadowEvents.getEntityCenter(caster), ShadowEvents.getEntityCenter(mob),
                    0, 0, 0, 0, 1);
            mob.setTarget(MindControlEffect.findMindControlTarget(mob, owner, mob.level()));
            state = true;
        }

        if ( state ) playMagicSound(level, center);

        return state;
    }
}
