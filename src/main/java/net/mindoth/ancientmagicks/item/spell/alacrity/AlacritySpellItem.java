package net.mindoth.ancientmagicks.item.spell.alacrity;

import net.mindoth.ancientmagicks.item.spell.EffectSpell;
import net.mindoth.ancientmagicks.registries.AncientMagicksEffects;
import net.minecraft.world.effect.MobEffect;

public class AlacritySpellItem extends EffectSpell {

    public AlacritySpellItem(Properties pProperties, int manaCost, int cooldown) {
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
