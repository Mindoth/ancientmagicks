package net.mindoth.ancientmagicks.item.spell.sleep;

import net.mindoth.ancientmagicks.item.spell.abstractspell.AbstractSpellRayCast;
import net.mindoth.ancientmagicks.registries.AncientMagicksEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class SleepItem extends AbstractSpellRayCast {

    public SleepItem(Properties pProperties, int spellTier, int manaCost, int cooldown) {
        super(pProperties, spellTier, manaCost, cooldown);
    }

    @Override
    protected boolean isHarmful() {
        return true;
    }

    @Override
    protected int getLife() {
        return 1200;
    }

    @Override
    protected boolean canApply(Level level, Player owner, Entity caster, LivingEntity target) {
        return !isAlly(owner, target) && target instanceof Mob;
    }

    @Override
    protected void applyEffect(Level level, Player owner, Entity caster, LivingEntity target) {
        target.addEffect(new MobEffectInstance(AncientMagicksEffects.SLEEP.get(), getLife(), 0, false, false));
    }
}
