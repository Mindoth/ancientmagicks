package net.mindoth.ancientmagicks.item.temp.fly;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.player.Player;

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
}
