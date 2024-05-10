package net.mindoth.ancientmagicks.item.spellrune.flight;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;

public class FlightEffect extends Effect {

    public FlightEffect(EffectType pCategory, int pColor) {
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

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        if ( pLivingEntity.level.isClientSide ) return;
        if ( !(pLivingEntity instanceof PlayerEntity) ) return;
        PlayerEntity player = (PlayerEntity)pLivingEntity;
        if ( player.isCreative() || player.abilities.mayfly ) return;
        player.abilities.mayfly = true;
        player.onUpdateAbilities();
    }

    @Override
    public void removeAttributeModifiers(LivingEntity pLivingEntity, AttributeModifierManager pAttributeMap, int pAmplifier) {
        if ( pLivingEntity.level.isClientSide ) return;
        if ( !(pLivingEntity instanceof PlayerEntity) ) return;
        PlayerEntity player = (PlayerEntity)pLivingEntity;
        if ( player.isCreative() || !player.abilities.mayfly ) return;
        player.abilities.mayfly = false;
        player.abilities.flying = false;
        player.onUpdateAbilities();
    }
}
