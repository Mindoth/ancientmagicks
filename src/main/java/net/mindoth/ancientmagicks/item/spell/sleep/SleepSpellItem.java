package net.mindoth.ancientmagicks.item.spell.sleep;

import net.mindoth.ancientmagicks.item.spell.EffectSpell;
import net.mindoth.ancientmagicks.registries.AncientMagicksEffects;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;

public class SleepSpellItem extends EffectSpell {

    public SleepSpellItem(Properties pProperties, int manaCost, int cooldown) {
        super(pProperties, manaCost, cooldown);
    }

    @Override
    public boolean mobTypeFilter(Entity target) {
        return target instanceof Mob;
    }

    @Override
    protected MobEffect getEffect() {
        return AncientMagicksEffects.SLEEP.get();
    }
}
