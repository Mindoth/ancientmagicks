package net.mindoth.ancientmagicks.item.spell.polymorph;

import net.mindoth.ancientmagicks.item.spell.abstractspell.AbstractSpellRayCast;
import net.mindoth.ancientmagicks.registries.AncientMagicksEffects;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class PolymorphItem extends AbstractSpellRayCast {

    public PolymorphItem(Properties pProperties, int spellTier, int manaCost, int cooldown, SpellType spellType) {
        super(pProperties, spellTier, manaCost, cooldown, spellType);
    }

    @Override
    protected int getLife() {
        return 600;
    }

    @Override
    protected boolean canApply(Level level, LivingEntity owner, Entity caster, Entity target) {
        return level instanceof ServerLevel && target instanceof Mob && !(target instanceof Sheep);
    }

    @Override
    protected void applyEffect(Level level, LivingEntity owner, Entity caster, Entity target) {
        addEnchantParticles(target, getParticleColor().r, getParticleColor().g, getParticleColor().b, 0.15F, 8, getRenderType());
        ((LivingEntity)target).addEffect(new MobEffectInstance(AncientMagicksEffects.POLYMORPH.get(), getLife(), 0, false, isHarmful()));
    }
}
