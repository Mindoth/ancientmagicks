package net.mindoth.ancientmagicks.item.spell.abstractspell;

import net.mindoth.ancientmagicks.registries.AncientMagicksEffects;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.List;

public class AbstractSpellArmor extends AbstractSpellRayCast {
    public AbstractSpellArmor(Properties pProperties, int spellTier, int manaCost, int cooldown, SpellType spellType) {
        super(pProperties, spellTier, manaCost, cooldown, spellType);
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
    protected boolean canApply(Level level, LivingEntity owner, Entity caster, Entity target) {
        return caster == owner;
    }

    @Override
    protected void audiovisualEffects(Level level, LivingEntity owner, Entity caster, Entity target) {
        addEnchantParticles(owner, getParticleColor().r, getParticleColor().g, getParticleColor().b, 0.15F, 8, getRenderType());
        playSound(level, target.position());
    }

    protected MobEffect getEffect() {
        return null;
    }

    @Override
    protected void applyEffect(Level level, LivingEntity owner, Entity caster, Entity target) {
        List<MobEffectInstance> list = owner.getActiveEffects().stream()
                .filter(effect -> effect.getEffect() instanceof AbstractArmorEffect && effect.getEffect() != AncientMagicksEffects.FROST_ARMOR.get()).toList();
        for ( MobEffectInstance effect : list ) owner.removeEffect(effect.getEffect());
        owner.addEffect(new MobEffectInstance(getEffect(), getLife(), 0, false, false));
    }
}
