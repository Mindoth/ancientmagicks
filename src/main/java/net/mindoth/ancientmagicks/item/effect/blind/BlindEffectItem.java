package net.mindoth.ancientmagicks.item.effect.blind;

import net.mindoth.ancientmagicks.client.particle.ember.ParticleColor;
import net.mindoth.ancientmagicks.item.effect.PotionEffectItem;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;

public class BlindEffectItem extends PotionEffectItem {

    public BlindEffectItem(Properties pProperties, int manaCost, int cooldown) {
        super(pProperties, manaCost, cooldown);
    }

    @Override
    public ParticleColor.IntWrapper getParticleColor() {
        return ColorCode.BLACK.getParticleColor();
    }

    @Override
    protected int getRenderType() {
        return 3;
    }

    @Override
    protected MobEffect getEffect() {
        return MobEffects.BLINDNESS;
    }
}
