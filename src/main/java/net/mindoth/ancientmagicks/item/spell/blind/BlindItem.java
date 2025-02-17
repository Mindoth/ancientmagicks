package net.mindoth.ancientmagicks.item.spell.blind;

import net.mindoth.ancientmagicks.client.particle.ember.ParticleColor;
import net.mindoth.ancientmagicks.item.temp.EffectSpell;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;

public class BlindItem extends EffectSpell {

    public BlindItem(Properties pProperties, int manaCost, int cooldown) {
        super(pProperties, manaCost, cooldown);
    }

    @Override
    public ParticleColor.IntWrapper getParticleColor() {
        return ColorCode.BLACK.getParticleColor();
    }

    @Override
    protected int getLife() {
        return 200;
    }

    @Override
    protected MobEffect getEffect() {
        return MobEffects.BLINDNESS;
    }
}
