package net.mindoth.ancientmagicks.item.spell.mindcontrol;

import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.item.SpellItem;
import net.mindoth.ancientmagicks.item.spell.abstractspell.AbstractSpellRayCast;
import net.mindoth.ancientmagicks.registries.AncientMagicksEffects;
import net.mindoth.shadowizardlib.event.ShadowEvents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.concurrent.ThreadLocalRandom;

public class MindControlItem extends AbstractSpellRayCast {

    public MindControlItem(Properties pProperties, int spellTier, int manaCost, int cooldown) {
        super(pProperties, spellTier, manaCost, cooldown);
    }

    @Override
    protected boolean isHarmful() {
        return true;
    }

    @Override
    protected int getLife() {
        return 600;
    }

    @Override
    protected boolean canApply(Level level, Player owner, Entity caster, LivingEntity target) {
        return target instanceof Mob mob && !isAlly(owner, mob);
    }

    @Override
    protected void applyEffect(Level level, Player owner, Entity caster, LivingEntity target) {
        Mob mob = (Mob)target;
        mob.getPersistentData().putUUID(MindControlEffect.NBT_KEY, owner.getUUID());
        mob.addEffect(new MobEffectInstance(AncientMagicksEffects.MIND_CONTROL.get(), getLife()));
        //if ( mob instanceof PathfinderMob pthMob ) mob.goalSelector.addGoal(0, new MeleeAttackGoal(pthMob, 1.0F, true));
        mob.setTarget(MindControlEffect.findMindControlTarget(mob, owner, mob.level()));
    }

    @Override
    protected int getRed() {
        return 0;
    }

    @Override
    protected int getGreen() {
        return 0;
    }

    @Override
    protected int getBlue() {
        return 0;
    }

    @Override
    protected boolean hasMask() {
        return false;
    }
}
