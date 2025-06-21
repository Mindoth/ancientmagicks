package net.mindoth.ancientmagicks.mobeffect;

import net.mindoth.ancientmagicks.AncientMagicks;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AncientMagicks.MOD_ID)
public class GreaterInvisibilityEffect extends MobEffect {

    public GreaterInvisibilityEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    /*public static final AttributeModifier DECREASED_NAME_TAG_DISTANCE = new AttributeModifier(UUID.fromString("3a531960-6840-410e-a694-a006b8e8548a"),
            "Greater Invisibility Invisible Equipment", 0.0D, AttributeModifier.Operation.ADDITION);

    @Override
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
        if ( nameTagDistance != null && !nameTagDistance.hasModifier(DECREASED_NAME_TAG_DISTANCE) ) nameTagDistance.addPermanentModifier(DECREASED_NAME_TAG_DISTANCE);
    }

    @SubscribeEvent
    public static void loseInvisibilityOnAttack(final LivingAttackEvent event) {
        if ( !(event.getSource().getEntity() instanceof LivingEntity source) || event.getEntity().level().isClientSide
                || !(source.hasEffect(AncientMagicksEffects.GREATER_INVISIBILITY.get())) ) return;

        AttributeInstance nameTagDistance = source.getAttribute(ForgeMod.NAMETAG_DISTANCE.get());
        if ( nameTagDistance != null && nameTagDistance.hasModifier(DECREASED_NAME_TAG_DISTANCE) ) source.removeEffect(AncientMagicksEffects.GREATER_INVISIBILITY.get());
    }

    @Override
    public void removeAttributeModifiers(LivingEntity living, AttributeMap map, int pAmplifier) {
        super.removeAttributeModifiers(living, map, pAmplifier);
        AttributeInstance nameTagDistance = living.getAttribute(ForgeMod.NAMETAG_DISTANCE.get());
        if ( nameTagDistance != null && nameTagDistance.hasModifier(DECREASED_NAME_TAG_DISTANCE) && !living.hasEffect(AncientMagicksEffects.PERFECT_INVISIBILITY.get()) ) {
            nameTagDistance.removeModifier(DECREASED_NAME_TAG_DISTANCE);
        }
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void interruptRender(RenderLivingEvent.Pre<? extends LivingEntity, ? extends EntityModel<? extends LivingEntity>> event) {
        LivingEntity living = event.getEntity();
        AttributeInstance nameTagDistance = living.getAttribute(ForgeMod.NAMETAG_DISTANCE.get());
        if ( nameTagDistance != null && nameTagDistance.hasModifier(DECREASED_NAME_TAG_DISTANCE) ) event.setCanceled(true);
    }*/
}
