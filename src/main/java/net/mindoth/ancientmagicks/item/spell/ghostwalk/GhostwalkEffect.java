package net.mindoth.ancientmagicks.item.spell.ghostwalk;

import net.mindoth.ancientmagicks.registries.AncientMagicksEffects;
import net.minecraft.client.model.EntityModel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

@Mod.EventBusSubscriber
public class GhostwalkEffect extends MobEffect {

    public GhostwalkEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    public static final AttributeModifier DECREASED_NAME_TAG_DISTANCE = new AttributeModifier(UUID.fromString("3a531960-6840-410e-a694-a006b8e8548a"),
            "Ghostwalk Invisible Equipment", 0.0D, AttributeModifier.Operation.ADDITION);

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
    public static void loseGhostwalkOnAttack(final LivingAttackEvent event) {
        if ( !(event.getSource().getEntity() instanceof LivingEntity source) || event.getEntity().level().isClientSide || !source.hasEffect(AncientMagicksEffects.GHOSTWALK.get()) ) return;
        AttributeInstance nameTagDistance = source.getAttribute(ForgeMod.NAMETAG_DISTANCE.get());
        if ( nameTagDistance != null && nameTagDistance.hasModifier(DECREASED_NAME_TAG_DISTANCE) ) source.removeEffect(AncientMagicksEffects.GHOSTWALK.get());
    }

    @Override
    public void removeAttributeModifiers(LivingEntity living, AttributeMap map, int pAmplifier) {
        super.removeAttributeModifiers(living, map, pAmplifier);
        AttributeInstance nameTagDistance = living.getAttribute(ForgeMod.NAMETAG_DISTANCE.get());
        if ( nameTagDistance != null && nameTagDistance.hasModifier(DECREASED_NAME_TAG_DISTANCE) ) nameTagDistance.removeModifier(DECREASED_NAME_TAG_DISTANCE);
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void interruptRender(RenderLivingEvent.Pre<? extends LivingEntity, ? extends EntityModel<? extends LivingEntity>> event) {
        LivingEntity living = event.getEntity();
        AttributeInstance nameTagDistance = living.getAttribute(ForgeMod.NAMETAG_DISTANCE.get());
        if ( nameTagDistance != null && nameTagDistance.hasModifier(DECREASED_NAME_TAG_DISTANCE) ) event.setCanceled(true);
    }
}
