package net.mindoth.ancientmagicks.item.spell.lifetap;

import net.mindoth.ancientmagicks.client.particle.ember.ParticleColor;
import net.mindoth.ancientmagicks.event.MagickEvents;
import net.mindoth.ancientmagicks.item.spell.abstractspell.AbstractSpellEntity;
import net.mindoth.ancientmagicks.item.spell.abstractspell.AbstractSpellRayCast;
import net.mindoth.ancientmagicks.item.spell.abstractspell.ColorCode;
import net.mindoth.ancientmagicks.registries.attribute.AncientMagicksAttributes;
import net.minecraft.world.entity.Entity;
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
    protected boolean canApply(Level level, Player owner, Entity caster, Entity target) {
        return caster == owner && owner.getHealth() > (owner.getMaxHealth() * 0.20F);
    }

    @Override
    protected void audiovisualEffects(Level level, Player owner, Entity caster, Entity target) {
        addEnchantParticles(caster, getColor().r, getColor().g, getColor().b, 0.15F, 8, getRenderType());
        playSound(level, caster.position());
    }

    @Override
    protected void applyEffect(Level level, Player owner, Entity caster, Entity target) {
        final double vx = owner.getDeltaMovement().x;
        final double vy = owner.getDeltaMovement().y;
        final double vz = owner.getDeltaMovement().z;
        owner.hurt(owner.damageSources().wither(), owner.getMaxHealth() * 0.20F);
        owner.setDeltaMovement(vx, vy, vz);
        owner.hurtMarked = true;
        MagickEvents.changeMana(owner, owner.getAttributeValue(AncientMagicksAttributes.MP_MAX.get()) * 0.20F);
    }

    @Override
    protected int getRenderType() {
        return 3;
    }
}
