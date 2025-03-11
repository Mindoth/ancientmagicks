package net.mindoth.ancientmagicks.item.effect.polymorph;

import com.google.common.collect.Lists;
import net.mindoth.ancientmagicks.item.effect.PotionEffectItem;
import net.mindoth.ancientmagicks.registries.AncientMagicksEffects;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;

import java.util.List;

public class PolymorphEffectItem extends PotionEffectItem {

    public PolymorphEffectItem(Properties pProperties, int manaCost, int cooldown) {
        super(pProperties, manaCost, cooldown);
    }

    @Override
    public boolean mobTypeFilter(Entity target) {
        return target instanceof Mob;
    }

    @Override
    protected List<MobEffect> getEffects(String data) {
        List<MobEffect> effects = Lists.newArrayList();
        effects.add(AncientMagicksEffects.POLYMORPH.get());
        return effects;
    }
}
