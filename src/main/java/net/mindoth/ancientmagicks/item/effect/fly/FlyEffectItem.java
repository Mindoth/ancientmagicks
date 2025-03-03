package net.mindoth.ancientmagicks.item.effect.fly;

import net.mindoth.ancientmagicks.item.effect.PotionEffectItem;
import net.mindoth.ancientmagicks.registries.AncientMagicksEffects;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class FlyEffectItem extends PotionEffectItem {

    public FlyEffectItem(Properties pProperties, int manaCost, int cooldown) {
        super(pProperties, manaCost, cooldown);
    }

    @Override
    public boolean isHarmful() {
        return false;
    }

    @Override
    public boolean mobTypeFilter(Entity target) {
        return target instanceof Player;
    }

    @Override
    protected MobEffect getEffect() {
        return AncientMagicksEffects.FLIGHT.get();
    }
}
