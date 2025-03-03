package net.mindoth.ancientmagicks.item.effect.spook;

import net.mindoth.ancientmagicks.client.particle.ember.ParticleColor;
import net.mindoth.ancientmagicks.item.effect.PotionEffectItem;
import net.mindoth.ancientmagicks.registries.AncientMagicksEffects;
import net.minecraft.world.effect.MobEffect;

public class SpookEffectItem extends PotionEffectItem {

    public SpookEffectItem(Properties pProperties, int manaCost, int cooldown) {
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
        return AncientMagicksEffects.SPOOK.get();
    }
}
