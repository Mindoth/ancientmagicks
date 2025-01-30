package net.mindoth.ancientmagicks.item.spell.blind;

import net.mindoth.ancientmagicks.client.particle.ember.ParticleColor;
import net.mindoth.ancientmagicks.item.spell.abstractspell.AbstractSpellEntity;
import net.mindoth.ancientmagicks.item.spell.abstractspell.AbstractSpellRayCast;
import net.mindoth.ancientmagicks.item.spell.abstractspell.ColorCode;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class BlindItem extends AbstractSpellRayCast {

    public BlindItem(Properties pProperties, int spellTier, int manaCost, int cooldown) {
        super(pProperties, spellTier, manaCost, cooldown);
    }

    @Override
    public ParticleColor.IntWrapper getColor() {
        return AbstractSpellEntity.getSpellColor(ColorCode.BLACK);
    }

    @Override
    protected int getLife() {
        return 200;
    }

    @Override
    protected boolean hasMask() {
        return false;
    }

    @Override
    protected void applyEffect(Level level, Player owner, Entity caster, Entity target) {
        ((LivingEntity)target).addEffect(new MobEffectInstance(MobEffects.BLINDNESS, getLife(), 0, false, isHarmful()));
    }
}
