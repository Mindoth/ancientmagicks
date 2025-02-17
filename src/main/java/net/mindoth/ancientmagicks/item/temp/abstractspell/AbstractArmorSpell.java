package net.mindoth.ancientmagicks.item.temp.abstractspell;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class AbstractArmorSpell extends AbstractSpellRayCast {
    public AbstractArmorSpell(Properties pProperties, int spellTier, int manaCost, int cooldown, SpellType spellType) {
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
        return caster instanceof LivingEntity;
    }

    @Override
    protected void audiovisualEffects(Level level, LivingEntity owner, Entity caster, Entity target) {
        addEnchantParticles(caster, getParticleColor().r, getParticleColor().g, getParticleColor().b, 0.15F, 8);
        playSound(level, caster.position());
    }
}
