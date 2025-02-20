package net.mindoth.ancientmagicks.item.spell.fly;

import net.mindoth.ancientmagicks.item.spell.EffectSpell;
import net.mindoth.ancientmagicks.item.temp.abstractspell.AbstractSpellRayCast;
import net.mindoth.ancientmagicks.registries.AncientMagicksEffects;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class FlyItem extends EffectSpell {

    public FlyItem(Properties pProperties, int manaCost, int cooldown) {
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
