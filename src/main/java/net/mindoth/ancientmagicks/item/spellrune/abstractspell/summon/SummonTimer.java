package net.mindoth.ancientmagicks.item.spellrune.abstractspell.summon;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierManager;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;

public class SummonTimer extends Effect {
    public SummonTimer(EffectType pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public void removeAttributeModifiers(LivingEntity pLivingEntity, AttributeModifierManager pAttributeMap, int pAmplifier) {
        if ( pLivingEntity instanceof SummonedMinion ) ((SummonedMinion)pLivingEntity).onUnSummon();
    }
}
