package net.mindoth.ancientmagicks.item.spell.witcharmor;

import net.mindoth.ancientmagicks.item.spell.abstractspell.AbstractSpellArmor;
import net.mindoth.ancientmagicks.registries.AncientMagicksEffects;
import net.minecraft.world.effect.MobEffect;

public class WitchArmorItem extends AbstractSpellArmor {

    public WitchArmorItem(Properties pProperties, int spellTier, int manaCost, int cooldown, SpellType spellType) {
        super(pProperties, spellTier, manaCost, cooldown, spellType);
    }

    @Override
    protected MobEffect getEffect() {
        return AncientMagicksEffects.WITCH_ARMOR.get();
    }
}
