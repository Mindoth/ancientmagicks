package net.mindoth.ancientmagicks.item.spell.abstractspell;

import net.mindoth.ancientmagicks.item.SpellItem;
import net.mindoth.shadowizardlib.event.ShadowEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public abstract class AbstractSpellRayCast extends SpellItem {

    public AbstractSpellRayCast(Properties pProperties, int spellTier, int manaCost, int cooldown) {
        super(pProperties, spellTier, manaCost, cooldown);
    }

    protected boolean isHarmful() {
        return false;
    }

    protected int getLife() {
        return 2400;
    }

    protected float getRange() {
        return 14.0F;
    }

    protected float getSize() {
        return getRange() * 0.5F;
    }

    protected boolean canApply(Level level, Player owner, Entity caster, LivingEntity target) {
        return (isAlly(owner, target) && !isHarmful()) || (!isAlly(owner, target) && isHarmful());
    }

    protected void applyEffect(Level level, Player owner, Entity caster, LivingEntity target) {
        target.addEffect(new MobEffectInstance(MobEffects.GLOWING, getLife(), 0, false, isHarmful()));
    }

    protected int getRed() {
        return 255;
    }

    protected int getGreen() {
        return 255;
    }

    protected int getBlue() {
        return 255;
    }

    protected boolean hasMask() {
        return true;
    }

    protected void playSound(Level level, Vec3 center) {
        playMagicSound(level, center);
    }

    @Override
    public boolean castMagic(Player owner, Entity caster, Vec3 center, float xRot, float yRot, int useTime) {
        boolean state = false;
        Level level = caster.level();

        LivingEntity target;
        if ( caster == owner ) target = (LivingEntity) ShadowEvents.getPointedEntity(level, caster, getRange(), 0.25F, caster == owner, true);
        else target = (LivingEntity)ShadowEvents.getNearestEntity(caster, level, getSize(), null);
        if ( !isHarmful() && caster == owner && !isAlly(owner, target) ) target = owner;

        if ( canApply(level, owner, caster, target) ) {
            applyEffect(level, owner, caster, target);
            state = true;
        }

        if ( state ) {
            addEnchantParticles(target, getRed(), getGreen(), getBlue(), 0.15F, 8, hasMask());
            playSound(level, target.position());
        }

        return state;
    }
}
