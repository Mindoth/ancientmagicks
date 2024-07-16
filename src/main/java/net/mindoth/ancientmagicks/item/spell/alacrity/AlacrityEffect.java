package net.mindoth.ancientmagicks.item.spell.alacrity;

import com.google.common.collect.Maps;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.Map;

public class AlacrityEffect extends MobEffect {
    private final Map<Attribute, AttributeModifier> attributeModifiers = Maps.newHashMap();

    public AlacrityEffect(MobEffectCategory pCategory, int pColor) {
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
}
