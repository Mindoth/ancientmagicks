package net.mindoth.ancientmagicks.item.spell.numbpain;

import net.mindoth.ancientmagicks.client.particle.ember.ParticleColor;
import net.mindoth.ancientmagicks.item.spell.abstractspell.AbstractSpellEntity;
import net.mindoth.ancientmagicks.item.spell.abstractspell.AbstractSpellRayCast;
import net.mindoth.ancientmagicks.item.spell.abstractspell.ColorCode;
import net.mindoth.ancientmagicks.registries.AncientMagicksEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class NumbPainItem extends AbstractSpellRayCast {

    public NumbPainItem(Properties pProperties, int spellTier, int manaCost, int cooldown) {
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
    protected int getRenderType() {
        return 3;
    }

    @Override
    protected boolean canApply(Level level, Player owner, Entity caster, Entity target) {
        return true;
    }

    @Override
    protected void applyEffect(Level level, Player owner, Entity caster, Entity target) {
        ((LivingEntity)target).addEffect(new MobEffectInstance(AncientMagicksEffects.NUMBNESS.get(), getLife(), 0));
    }
}
