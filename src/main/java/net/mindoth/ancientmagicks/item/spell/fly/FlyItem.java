package net.mindoth.ancientmagicks.item.spell.fly;

import net.mindoth.ancientmagicks.item.spell.abstractspell.AbstractSpellRayCast;
import net.mindoth.ancientmagicks.registries.AncientMagicksEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class FlyItem extends AbstractSpellRayCast {

    public FlyItem(Properties pProperties, int spellTier, int manaCost, int cooldown) {
        super(pProperties, spellTier, manaCost, cooldown);
    }

    @Override
    protected int getLife() {
        return 1200;
    }

    @Override
    protected boolean canApply(Level level, Player owner, Entity caster, LivingEntity target) {
        return target instanceof Player && isAlly(owner, target);
    }

    @Override
    protected void applyEffect(Level level, Player owner, Entity caster, LivingEntity target) {
        target.addEffect(new MobEffectInstance(AncientMagicksEffects.FLIGHT.get(), getLife(), 0, false, isHarmful()));
    }

    @Override
    protected int getRed() {
        return 170;
    }

    @Override
    protected int getGreen() {
        return 25;
    }

    @Override
    protected int getBlue() {
        return 170;
    }
}
