package net.mindoth.ancientmagicks.mobeffect;

import net.mindoth.ancientmagicks.AncientMagicks;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AncientMagicks.MOD_ID)
public class PerfectInvisibilityEffect extends MobEffect {

    public PerfectInvisibilityEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    /*@Override
    public void addAttributeModifiers(LivingEntity living, AttributeMap map, int amp) {
        super.addAttributeModifiers(living, map, amp);

        var targetingCondition = TargetingConditions.forCombat().ignoreLineOfSight().selector(entity -> (((Mob)entity).getTarget() == living));

        living.level().getNearbyEntities(Mob.class, targetingCondition, living, living.getBoundingBox().inflate(64.0D))
                .forEach(target -> {
                    target.setTarget(null);
                    target.targetSelector.getAvailableGoals().forEach(WrappedGoal::stop);
                    target.getBrain().eraseMemory(MemoryModuleType.ATTACK_TARGET);
                });
        AttributeInstance nameTagDistance = living.getAttribute(ForgeMod.NAMETAG_DISTANCE.get());
        if ( nameTagDistance != null && !nameTagDistance.hasModifier(GreaterInvisibilityEffect.DECREASED_NAME_TAG_DISTANCE) ) {
            nameTagDistance.addPermanentModifier(GreaterInvisibilityEffect.DECREASED_NAME_TAG_DISTANCE);
        }
    }

    @Override
    public void removeAttributeModifiers(LivingEntity living, AttributeMap map, int pAmplifier) {
        super.removeAttributeModifiers(living, map, pAmplifier);
        AttributeInstance nameTagDistance = living.getAttribute(ForgeMod.NAMETAG_DISTANCE.get());
        if ( nameTagDistance != null && nameTagDistance.hasModifier(GreaterInvisibilityEffect.DECREASED_NAME_TAG_DISTANCE)
                && !living.hasEffect(AncientMagicksEffects.GREATER_INVISIBILITY.get()) ) {
            nameTagDistance.removeModifier(GreaterInvisibilityEffect.DECREASED_NAME_TAG_DISTANCE);
        }
    }

    @SubscribeEvent
    public static void onPreventTargetingPerfectInvis(final LivingChangeTargetEvent event) {
        if ( event.getEntity().level().isClientSide ) return;
        if ( event.getNewTarget() == null ) return;
        if ( event.getNewTarget().hasEffect(AncientMagicksEffects.PERFECT_INVISIBILITY.get()) ) event.setCanceled(true);
    }*/
}
