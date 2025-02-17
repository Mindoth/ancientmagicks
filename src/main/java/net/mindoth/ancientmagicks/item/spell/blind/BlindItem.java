package net.mindoth.ancientmagicks.item.spell.blind;

import net.mindoth.ancientmagicks.item.spell.EffectSpell;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;

public class BlindItem extends EffectSpell {

    public BlindItem(Properties pProperties, int manaCost, int cooldown) {
        super(pProperties, manaCost, cooldown);
    }

    @Override
    protected MobEffect getEffect() {
        return MobEffects.BLINDNESS;
    }
}
