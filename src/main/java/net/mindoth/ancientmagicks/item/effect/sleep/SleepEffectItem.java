package net.mindoth.ancientmagicks.item.effect.sleep;

import net.mindoth.ancientmagicks.client.particle.ember.ParticleColor;
import net.mindoth.ancientmagicks.item.effect.PotionEffectItem;
import net.mindoth.ancientmagicks.registries.AncientMagicksEffects;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;

public class SleepEffectItem extends PotionEffectItem {

    public SleepEffectItem(Properties pProperties, int manaCost, int cooldown) {
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
    public boolean mobTypeFilter(Entity target) {
        return target instanceof Mob;
    }

    @Override
    protected MobEffect getEffect() {
        return AncientMagicksEffects.SLEEP.get();
    }
}
