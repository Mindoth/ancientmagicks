package net.mindoth.ancientmagicks.item.temp.abstractspell;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class AbstractArmorEffect extends MobEffect {

    protected AbstractArmorEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public boolean isBeneficial() {
        return true;
    }
}
