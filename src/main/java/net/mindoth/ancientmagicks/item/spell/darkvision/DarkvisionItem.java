package net.mindoth.ancientmagicks.item.spell.darkvision;

import net.mindoth.ancientmagicks.item.spell.abstractspell.AbstractSpellRayCast;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class DarkvisionItem extends AbstractSpellRayCast {

    public DarkvisionItem(Properties pProperties, int spellTier, int manaCost, int cooldown) {
        super(pProperties, spellTier, manaCost, cooldown);
    }

    @Override
    protected void applyEffect(Level level, Player owner, Entity caster, LivingEntity target) {
        target.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, getLife(), 0, false, isHarmful()));
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
