package net.mindoth.ancientmagicks.item.effect.heal;

import net.mindoth.ancientmagicks.client.particle.ember.ParticleColor;
import net.mindoth.ancientmagicks.item.effect.PotionEffectItem;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;

public class HealEffectItem extends PotionEffectItem {

    public HealEffectItem(Properties pProperties, int manaCost, int cooldown) {
        super(pProperties, manaCost, cooldown);
    }

    @Override
    public boolean isHarmful() {
        return false;
    }

    @Override
    public ParticleColor.IntWrapper getParticleColor() {
        return ColorCode.GREEN.getParticleColor();
    }

    @Override
    public MobEffect getEffect() {
        return MobEffects.HEAL;
    }
}
