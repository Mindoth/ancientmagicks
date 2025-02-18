package net.mindoth.ancientmagicks.item.spell.polymorph;

import net.mindoth.ancientmagicks.item.spell.EffectSpell;
import net.mindoth.ancientmagicks.registries.AncientMagicksEffects;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.Sheep;

public class PolymorphItem extends EffectSpell {

    public PolymorphItem(Properties pProperties, int manaCost, int cooldown) {
        super(pProperties, manaCost, cooldown);
    }

    @Override
    public boolean mobTypeFilter(Entity target) {
        return target instanceof Mob && !(target instanceof Sheep);
    }

    @Override
    public MobEffect getEffect() {
        return AncientMagicksEffects.POLYMORPH.get();
    }
}
