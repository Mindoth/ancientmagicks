package net.mindoth.ancientmagicks.item.spell.ghostwalk;

import net.mindoth.ancientmagicks.registries.AncientMagicksEffects;
import net.minecraft.client.model.EntityModel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class GhostwalkEffect extends MobEffect {

    public GhostwalkEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public boolean isBeneficial() {
        return true;
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

    int lastHurtTimestamp;

    @Override
    public void addAttributeModifiers(LivingEntity living, AttributeMap map, int amp) {
        super.addAttributeModifiers(living, map, amp);

        var targetingCondition = TargetingConditions.forCombat().ignoreLineOfSight().selector(entity -> (((Mob)entity).getTarget() == living));

        living.level().getNearbyEntities(Mob.class, targetingCondition, living, living.getBoundingBox().inflate(64.0D))
                .forEach(target -> {
                    target.setTarget(null);
                    target.setLastHurtMob(null);
                    target.setLastHurtByMob(null);
                    target.targetSelector.getAvailableGoals().forEach(WrappedGoal::stop);
                });
        this.lastHurtTimestamp = living.getLastHurtMobTimestamp();
    }

    @Override
    public void applyEffectTick(LivingEntity livin, int amp) {
        if ( !livin.level().isClientSide && lastHurtTimestamp != livin.getLastHurtMobTimestamp() ) {
            livin.removeEffect(this);
        }
    }

    @SubscribeEvent()
    public static void interruptRender(RenderLivingEvent.Pre<? extends LivingEntity, ? extends EntityModel<? extends LivingEntity>> event) {
        if ( event.getEntity().hasEffect(AncientMagicksEffects.GHOSTWALK.get()) ) {
            event.setCanceled(true);
        }
    }
}
