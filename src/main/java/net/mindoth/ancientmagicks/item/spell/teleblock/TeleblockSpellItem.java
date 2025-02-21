package net.mindoth.ancientmagicks.item.spell.teleblock;

import net.mindoth.ancientmagicks.item.spell.EffectSpell;
import net.mindoth.ancientmagicks.registries.AncientMagicksEffects;
import net.minecraft.world.effect.MobEffect;

public class TeleblockSpellItem extends EffectSpell {

    public TeleblockSpellItem(Properties pProperties, int manaCost, int cooldown) {
        super(pProperties, manaCost, cooldown);
    }

    @Override
    protected MobEffect getEffect() {
        return AncientMagicksEffects.TELEBLOCK.get();
    }
}
