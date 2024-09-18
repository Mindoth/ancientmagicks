package net.mindoth.ancientmagicks.item.spell.ghostwalk;

import net.mindoth.ancientmagicks.registries.AncientMagicksEffects;
import net.minecraft.client.model.EntityModel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

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

    int LASTHURTTIME;

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
        this.LASTHURTTIME = living.getLastHurtMobTimestamp();
    }

    @Override
    public void applyEffectTick(LivingEntity living, int amp) {
        if ( !living.level().isClientSide && this.LASTHURTTIME != living.getLastHurtMobTimestamp() ) {
            living.removeEffect(this);
        }
    }

    public static final AttributeModifier DECREASED_NAMETAG_DISTANCE = new AttributeModifier(UUID.fromString("3a531960-6840-410e-a694-a006b8e8548a"),
            "Ghostwalk Invisible Equipment", 0.0D, AttributeModifier.Operation.ADDITION);

    @SubscribeEvent
    public static void onLivingNameTagEvent(TickEvent.PlayerTickEvent event) {
        if ( event.phase == TickEvent.Phase.END ) {
            Player player = event.player;
            if ( !player.level().isClientSide ) {
                AttributeInstance nameTagDistance = player.getAttribute(ForgeMod.NAMETAG_DISTANCE.get());
                if ( nameTagDistance != null ) {
                    if ( player.hasEffect(AncientMagicksEffects.GHOSTWALK.get()) ) {
                        if ( !nameTagDistance.hasModifier(DECREASED_NAMETAG_DISTANCE) ) {
                            nameTagDistance.addPermanentModifier(DECREASED_NAMETAG_DISTANCE);
                        }
                    }
                    else {
                        if ( nameTagDistance.hasModifier(DECREASED_NAMETAG_DISTANCE) ) {
                            nameTagDistance.removeModifier(DECREASED_NAMETAG_DISTANCE);
                        }
                    }
                }
            }
        }
    }

    //TODO: Sync visual effect to clients for Ghostwalk
    @SubscribeEvent
    public static void interruptRender(RenderLivingEvent.Pre<? extends LivingEntity, ? extends EntityModel<? extends LivingEntity>> event) {
        LivingEntity living = event.getEntity();
        AttributeInstance nameTagDistance = living.getAttribute(ForgeMod.NAMETAG_DISTANCE.get());
        if ( nameTagDistance != null && nameTagDistance.hasModifier(DECREASED_NAMETAG_DISTANCE) ) event.setCanceled(true);
    }
}
