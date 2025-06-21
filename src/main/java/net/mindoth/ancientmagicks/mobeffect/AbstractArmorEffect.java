package net.mindoth.ancientmagicks.mobeffect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public abstract class AbstractArmorEffect extends MobEffect {

    protected AbstractArmorEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public boolean isBeneficial() {
        return true;
    }
}
