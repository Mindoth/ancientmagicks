package net.mindoth.ancientmagicks.item.spell.frostarmor;

import net.mindoth.ancientmagicks.client.particle.ember.ParticleColor;
import net.mindoth.ancientmagicks.item.spell.abstractspell.AbstractArmorEffect;
import net.mindoth.ancientmagicks.item.spell.abstractspell.AbstractSpellEntity;
import net.mindoth.ancientmagicks.item.spell.abstractspell.AbstractSpellRayCast;
import net.mindoth.ancientmagicks.registries.AncientMagicksEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.List;

public class FrostArmorItem extends AbstractSpellRayCast {

    public FrostArmorItem(Properties pProperties, int spellTier, int manaCost, int cooldown, SpellType spellType) {
        super(pProperties, spellTier, manaCost, cooldown, spellType);
    }

    @Override
    public ParticleColor.IntWrapper getParticleColor() {
        return ColorCode.AQUA.getParticleColor();
    }

    @Override
    public boolean isHarmful() {
        return false;
    }

    @Override
    protected int getLife() {
        return 36000;
    }

    @Override
    protected boolean canApply(Level level, Player owner, Entity caster, Entity target) {
        return caster == owner;
    }

    @Override
    protected void applyEffect(Level level, Player owner, Entity caster, Entity target) {
        List<MobEffectInstance> list = owner.getActiveEffects().stream()
                .filter(effect -> effect.getEffect() instanceof AbstractArmorEffect && effect.getEffect() != AncientMagicksEffects.FROST_ARMOR.get()).toList();
        for ( MobEffectInstance effect : list ) owner.removeEffect(effect.getEffect());
        owner.addEffect(new MobEffectInstance(AncientMagicksEffects.FROST_ARMOR.get(), getLife(), 0, false, false));
    }
}
