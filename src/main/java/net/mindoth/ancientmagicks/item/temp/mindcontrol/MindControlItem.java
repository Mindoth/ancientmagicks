package net.mindoth.ancientmagicks.item.temp.mindcontrol;

import net.mindoth.ancientmagicks.client.particle.ember.ParticleColor;
import net.mindoth.ancientmagicks.item.temp.abstractspell.AbstractSpellRayCast;
import net.mindoth.ancientmagicks.registries.AncientMagicksEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;

public class MindControlItem extends AbstractSpellRayCast {

    public MindControlItem(Properties pProperties, int spellTier, int manaCost, int cooldown, SpellType spellType) {
        super(pProperties, spellTier, manaCost, cooldown, spellType);
    }

    @Override
    public ParticleColor.IntWrapper getParticleColor() {
        return ColorCode.BLACK.getParticleColor();
    }

    @Override
    protected int getLife() {
        return 600;
    }

    @Override
    protected boolean canApply(Level level, LivingEntity owner, Entity caster, Entity target) {
        return target instanceof Mob mob && !isAlly(owner, mob) && !mob.hasEffect(AncientMagicksEffects.MIND_CONTROL.get());
    }

    @Override
    protected void applyEffect(Level level, LivingEntity owner, Entity caster, Entity target) {
        Mob mob = (Mob)target;
        mob.getPersistentData().putUUID(MindControlEffect.NBT_KEY_CONTROL, owner.getUUID());
        mob.addEffect(new MobEffectInstance(AncientMagicksEffects.MIND_CONTROL.get(), getLife()));
        //if ( mob instanceof PathfinderMob pthMob ) mob.goalSelector.addGoal(0, new MeleeAttackGoal(pthMob, 1.0F, true));
        mob.setTarget(MindControlEffect.findMindControlTarget(mob, owner, mob.level()));
    }

    @Override
    protected int getRenderType() {
        return 3;
    }
}
