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

    public AbstractSpellRayCast(Properties pProperties, int spellTier, int manaCost, int cooldown, SpellType spellType) {
        super(pProperties, spellTier, manaCost, cooldown, spellType);
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

    protected boolean canApply(Level level, LivingEntity owner, Entity caster, Entity target) {
        return filter(owner, target);
    }

    //Example result
    protected void applyEffect(Level level, LivingEntity owner, Entity caster, Entity target) {
        ((LivingEntity)target).addEffect(new MobEffectInstance(MobEffects.GLOWING, getLife(), 0, false, isHarmful()));
    }

    protected void audiovisualEffects(Level level, LivingEntity owner, Entity caster, Entity target) {
        addEnchantParticles(target, getParticleColor().r, getParticleColor().g, getParticleColor().b, 0.15F, 8, getRenderType());
        playSound(level, target.position());
    }

    protected int getRenderType() {
        return 1;
    }

    @Override
    public boolean castMagic(LivingEntity owner, Entity caster, Vec3 center, float xRot, float yRot, int useTime) {
        boolean state = false;
        Level level = caster.level();

        Entity target;
        if ( caster == owner ) target = ShadowEvents.getPointedEntity(level, caster, getRange(), 0.25F, caster == owner, true, this::filter);
        else target = ShadowEvents.getNearestEntity(caster, level, getSize(), this::filter);
        if ( caster == owner && !isHarmful() && !filter(owner, target) ) target = owner;

        if ( canApply(level, owner, caster, target) ) state = true;

        if ( state ) {
            applyEffect(level, owner, caster, target);
            audiovisualEffects(level, owner, caster, target);
        }

        return state;
    }
}
