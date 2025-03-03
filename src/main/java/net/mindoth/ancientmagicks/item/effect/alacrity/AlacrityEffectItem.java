package net.mindoth.ancientmagicks.item.effect.alacrity;

import net.mindoth.ancientmagicks.item.effect.PotionEffectItem;
import net.mindoth.ancientmagicks.registries.AncientMagicksEffects;
import net.minecraft.world.effect.MobEffect;

public class AlacrityEffectItem extends PotionEffectItem {

    public AlacrityEffectItem(Properties pProperties, int manaCost, int cooldown) {
        super(pProperties, manaCost, cooldown);
    }

    @Override
    public boolean isHarmful() {
        return false;
    }

    @Override
    protected MobEffect getEffect() {
        return AncientMagicksEffects.ALACRITY.get();
    }
}
