package net.mindoth.ancientmagicks.item.spell.sleep;

import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.registries.AncientMagicksEffects;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AncientMagicks.MOD_ID)
public class SleepEffect extends MobEffect {
    public SleepEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public void addAttributeModifiers(LivingEntity living, AttributeMap map, int pAmplifier) {
        if ( !living.isSleeping() && living instanceof Mob ) living.startSleeping(living.blockPosition());
    }

    @SubscribeEvent
    public static void wakeUpWhenAttacked(final LivingHurtEvent event) {
        LivingEntity living = event.getEntity();
        if ( living.hasEffect(AncientMagicksEffects.SLEEP.get()) ) living.removeEffect(AncientMagicksEffects.SLEEP.get());
    }

    @Override
    public void removeAttributeModifiers(LivingEntity living, AttributeMap map, int pAmplifier) {
        if ( living.isSleeping() ) living.stopSleeping();
    }

    @SubscribeEvent
    public static void onEntitySleep(final MobEffectEvent.Applicable event) {
        if ( event.getEffectInstance().getEffect() == AncientMagicksEffects.SLEEP.get() ) {
            if ( event.getEntity() instanceof Player ) event.setResult(Event.Result.DENY);
            else event.setResult(Event.Result.DEFAULT);
        }
    }
}
