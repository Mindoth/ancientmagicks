package net.mindoth.ancientmagicks.item.spell.blind;

import net.mindoth.ancientmagicks.item.spell.abstractspell.AbstractSpellRayCast;
import net.mindoth.ancientmagicks.item.spell.abstractspell.SpellSchool;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class BlindItem extends AbstractSpellRayCast {

    public BlindItem(Properties pProperties, int spellTier, int manaCost, int cooldown, SpellSchool spellSchool) {
        super(pProperties, spellTier, manaCost, cooldown, spellSchool);
    }

    @Override
    protected boolean isHarmful() {
        return true;
    }

    @Override
    protected int getLife() {
        return 200;
    }

    @Override
    protected void applyEffect(Level level, Player owner, Entity caster, LivingEntity target) {
        target.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, getLife(), 0, false, isHarmful()));
    }

    @Override
    protected boolean hasMask() {
        return false;
    }
}
