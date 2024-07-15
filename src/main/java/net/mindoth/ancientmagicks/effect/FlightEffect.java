package net.mindoth.ancientmagicks.effect;

import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.registries.AncientMagicksEffects;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AncientMagicks.MOD_ID)
public class FlightEffect extends MobEffect {

    public FlightEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        if ( pLivingEntity.level().isClientSide ) return;
        if ( !(pLivingEntity instanceof Player player) ) return;
        if ( player.isCreative() || player.getAbilities().mayfly ) return;
        player.getAbilities().mayfly = true;
        player.onUpdateAbilities();
    }

    @Override
    public void removeAttributeModifiers(LivingEntity living, AttributeMap map, int pAmplifier) {
        if ( living.level().isClientSide ) return;
        if ( !(living instanceof Player player) ) return;
        if ( player.isCreative() || !player.getAbilities().mayfly ) return;
        player.getAbilities().mayfly = false;
        player.getAbilities().flying = false;
        player.onUpdateAbilities();
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onFlightDamage(final LivingDamageEvent event) {
        LivingEntity living = event.getEntity();
        if ( living.level().isClientSide ) return;
        if ( event.getAmount() > 0 && living.hasEffect(AncientMagicksEffects.FLIGHT.get()) ) {
            living.removeEffect(AncientMagicksEffects.FLIGHT.get());
        }
    }
}
