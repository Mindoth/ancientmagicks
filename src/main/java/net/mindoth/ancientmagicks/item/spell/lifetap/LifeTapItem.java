package net.mindoth.ancientmagicks.item.spell.lifetap;

import net.mindoth.ancientmagicks.client.particle.ember.ParticleColor;
import net.mindoth.ancientmagicks.event.MagickEvents;
import net.mindoth.ancientmagicks.item.spell.abstractspell.AbstractSpellEntity;
import net.mindoth.ancientmagicks.item.spell.abstractspell.AbstractSpellRayCast;
import net.mindoth.ancientmagicks.item.spell.abstractspell.ColorCode;
import net.mindoth.ancientmagicks.registries.attribute.AncientMagicksAttributes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class LifeTapItem extends AbstractSpellRayCast {

    public LifeTapItem(Properties pProperties, int spellTier, int manaCost, int cooldown) {
        super(pProperties, spellTier, manaCost, cooldown);
    }

    @Override
    public ParticleColor.IntWrapper getColor() {
        return AbstractSpellEntity.getSpellColor(ColorCode.BLACK);
    }

    @Override
    protected boolean isHarmful() {
        return false;
    }

    @Override
    protected boolean canApply(Level level, Player owner, Entity caster, LivingEntity target) {
        return caster == owner && owner.getHealth() > (owner.getMaxHealth() * 0.20F);
    }

    @Override
    protected void applyEffect(Level level, Player owner, Entity caster, LivingEntity target) {
        final double vx = target.getDeltaMovement().x;
        final double vy = target.getDeltaMovement().y;
        final double vz = target.getDeltaMovement().z;
        target.hurt(target.damageSources().wither(), owner.getMaxHealth() * 0.20F);
        target.setDeltaMovement(vx, vy, vz);
        target.hurtMarked = true;
        MagickEvents.changeMana(owner, owner.getAttributeValue(AncientMagicksAttributes.MP_MAX.get()) * 0.20F);
    }

    @Override
    protected boolean hasMask() {
        return false;
    }
}
