package net.mindoth.ancientmagicks.item.spell.mindcontrol;

import net.mindoth.ancientmagicks.client.particle.ember.ParticleColor;
import net.mindoth.ancientmagicks.item.spell.EffectSpell;
import net.mindoth.ancientmagicks.registries.AncientMagicksEffects;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.HashMap;

public class MindControlSpellItem extends EffectSpell {

    public MindControlSpellItem(Properties pProperties, int manaCost, int cooldown) {
        super(pProperties, manaCost, cooldown);
    }

    @Override
    public ParticleColor.IntWrapper getParticleColor() {
        return ColorCode.BLACK.getParticleColor();
    }

    @Override
    protected int getRenderType() {
        return 3;
    }

    @Override
    public boolean mobTypeFilter(Entity target) {
        return target instanceof Mob;
    }

    @Override
    protected MobEffect getEffect() {
        return AncientMagicksEffects.MIND_CONTROL.get();
    }

    @Override
    protected boolean doSpell(Level level, LivingEntity owner, Entity caster, HitResult result, HashMap<String, Float> stats) {
        Mob mob = (Mob)((EntityHitResult)result).getEntity();
        addEnchantParticles(mob, getParticleColor().r, getParticleColor().g, getParticleColor().b, 0.15F, 8);
        int amp = Math.max(0, (Mth.floor(stats.get(POWER)) - 1) / 10);
        int life = Mth.floor(stats.get(LIFE) - 100) * 30 + 600;
        mob.getPersistentData().putUUID(MindControlEffect.NBT_KEY_CONTROL, owner.getUUID());
        mob.addEffect(new MobEffectInstance(getEffect(), life, amp, false, isHarmful()));
        //if ( mob instanceof PathfinderMob pthMob ) mob.goalSelector.addGoal(0, new MeleeAttackGoal(pthMob, 1.0F, true));
        mob.setTarget(MindControlEffect.findMindControlTarget(mob, owner, mob.level()));
        return true;
    }
}
