package net.mindoth.ancientmagicks.item.spell.summonskeletoncaster;

import net.mindoth.ancientmagicks.item.SpellItem;
import net.mindoth.ancientmagicks.item.castingitem.CastingItem;
import net.mindoth.ancientmagicks.item.castingitem.StaffItem;
import net.mindoth.ancientmagicks.item.spell.mindcontrol.MindControlEffect;
import net.mindoth.ancientmagicks.registries.AncientMagicksEffects;
import net.mindoth.shadowizardlib.event.ShadowEvents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;

import java.util.EnumSet;

public class RangedMagickAttackGoal<T extends net.minecraft.world.entity.Mob & RangedAttackMob> extends Goal {
    private final T mob;
    private final double speedModifier;
    private int attackIntervalMin;
    private final float attackRadiusSqr;
    private final SpellItem spell;
    private int attackTime = -1;
    private int seeTime;
    private boolean strafingClockwise;
    private boolean strafingBackwards;
    private int strafingTime = -1;

    public RangedMagickAttackGoal(T pMob, double pSpeedModifier, int pAttackIntervalMin, float pAttackRadius, SpellItem spell) {
        this.mob = pMob;
        this.speedModifier = pSpeedModifier;
        this.attackIntervalMin = pAttackIntervalMin;
        this.attackRadiusSqr = pAttackRadius * pAttackRadius;
        this.spell = spell;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    public void setMinAttackInterval(int pAttackCooldown) {
        this.attackIntervalMin = pAttackCooldown;
    }

    @Override
    public boolean canUse() {
        return this.mob.getTarget() == null ? false : this.isHoldingStaff();
    }

    protected boolean isHoldingStaff() {
        return this.mob.isHolding(is -> is.getItem() instanceof StaffItem);
    }

    @Override
    public boolean canContinueToUse() {
        return (this.canUse() || !this.mob.getNavigation().isDone()) && this.isHoldingStaff();
    }

    @Override
    public void start() {
        super.start();
        this.mob.setAggressive(true);
    }

    @Override
    public void stop() {
        super.stop();
        this.mob.setAggressive(false);
        this.seeTime = 0;
        this.attackTime = -1;
        this.mob.stopUsingItem();
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        LivingEntity livingentity = this.mob.getTarget();
        if ( livingentity != null ) {
            double d0 = this.mob.distanceToSqr(livingentity.getX(), livingentity.getY(), livingentity.getZ());
            boolean flag = this.mob.getSensing().hasLineOfSight(livingentity);
            boolean flag1 = this.seeTime > 0;

            if ( flag != flag1 ) this.seeTime = 0;
            if ( flag ) ++this.seeTime;
            else --this.seeTime;

            if ( !(d0 > (double)this.attackRadiusSqr) && this.seeTime >= 20 ) {
                this.mob.getNavigation().stop();
                ++this.strafingTime;
            }
            else {
                this.mob.getNavigation().moveTo(livingentity, this.speedModifier);
                this.strafingTime = -1;
            }

            if ( this.strafingTime >= 20 ) {
                if ( (double)this.mob.getRandom().nextFloat() < 0.3D ) {
                    this.strafingClockwise = !this.strafingClockwise;
                }
                if ( (double)this.mob.getRandom().nextFloat() < 0.3D ) {
                    this.strafingBackwards = !this.strafingBackwards;
                }
                this.strafingTime = 0;
            }

            if ( this.strafingTime > -1 ) {
                if ( d0 > (double)(this.attackRadiusSqr * 0.75F) ) this.strafingBackwards = false;
                else if ( d0 < (double)(this.attackRadiusSqr * 0.25F) ) this.strafingBackwards = true;
                this.mob.getMoveControl().strafe(this.strafingBackwards ? -0.5F : 0.5F, this.strafingClockwise ? 0.5F : -0.5F);
                Entity entity = this.mob.getControlledVehicle();
                if ( entity instanceof Mob ) {
                    Mob mob = (Mob)entity;
                    mob.lookAt(livingentity, 30.0F, 30.0F);
                }
                this.mob.lookAt(livingentity, 30.0F, 30.0F);
            }
            else this.mob.getLookControl().setLookAt(livingentity, 30.0F, 30.0F);

            InteractionHand hand = ProjectileUtil.getWeaponHoldingHand(this.mob, item -> item instanceof StaffItem);
            ItemStack stack = this.mob.getItemInHand(hand);
            if ( this.mob.isUsingItem() ) {
                if ( !flag && this.seeTime < -60 ) this.mob.stopUsingItem();
                else if ( flag ) {
                    int i = this.mob.getTicksUsingItem();
                    if ( i >= 20 ) {
                        /*this.mob.stopUsingItem();
                        this.mob.performRangedAttack(livingentity, BowItem.getPowerForTime(i));*/

                        LivingEntity controller = this.mob;
                        CompoundTag tag = this.mob.getPersistentData();
                        if ( this.mob.hasEffect(AncientMagicksEffects.MIND_CONTROL.get()) && tag.contains(MindControlEffect.NBT_KEY_CONTROL) ) {
                            Entity owner = ShadowEvents.getEntityByUUID(this.mob.level(), tag.getUUID(MindControlEffect.NBT_KEY_CONTROL));
                            if ( owner instanceof LivingEntity living ) controller = living;
                        }
                        CastingItem.doSpell(controller, this.mob, stack, this.spell, i);
                        this.attackTime = this.attackIntervalMin;
                    }
                }
            }
            else if ( --this.attackTime <= 0 && this.seeTime >= -60 ) this.mob.startUsingItem(hand);
        }
    }
}
