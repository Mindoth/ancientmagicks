package net.mindoth.ancientmagicks.item.effect.teleblock;

import net.mindoth.ancientmagicks.item.effect.PotionEffectItem;
import net.mindoth.ancientmagicks.registries.AncientMagicksEffects;
import net.minecraft.world.effect.MobEffect;

public class TeleblockEffectItem extends PotionEffectItem {

    public TeleblockEffectItem(Properties pProperties, int manaCost, int cooldown) {
        super(pProperties, manaCost, cooldown);
    }

    @Override
    protected MobEffect getEffect() {
        return AncientMagicksEffects.TELEBLOCK.get();
    }
}
