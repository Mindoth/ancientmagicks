package net.mindoth.ancientmagicks.item.spell.ghostwalk;

import net.mindoth.ancientmagicks.item.spell.abstractspell.AbstractSpellRayCast;
import net.mindoth.ancientmagicks.registries.AncientMagicksEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class GhostwalkItem extends AbstractSpellRayCast {

    public GhostwalkItem(Properties pProperties, int spellTier, int manaCost, int cooldown) {
        super(pProperties, spellTier, manaCost, cooldown);
    }

    @Override
    protected boolean isHarmful() {
        return false;
    }

    @Override
    protected void applyEffect(Level level, Player owner, Entity caster, LivingEntity target) {
        target.addEffect(new MobEffectInstance(AncientMagicksEffects.GHOSTWALK.get(), getLife(), 0, false, isHarmful()));
    }
}
