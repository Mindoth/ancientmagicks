package net.mindoth.ancientmagicks.item.spell.featherfall;

import net.mindoth.ancientmagicks.item.spell.abstractspell.AbstractSpellRayCast;
import net.mindoth.shadowizardlib.event.ShadowEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.List;

public class FeatherFallItem extends AbstractSpellRayCast {

    public FeatherFallItem(Properties pProperties, int spellTier, int manaCost, int cooldown) {
        super(pProperties, spellTier, manaCost, cooldown);
    }

    @Override
    protected boolean isHarmful() {
        return false;
    }

    @Override
    protected int getLife() {
        return 600;
    }

    @Override
    protected float getRange() {
        return 0.0F;
    }

    @Override
    protected float getSize() {
        return 5.0F;
    }

    @Override
    protected boolean canApply(Level level, Player owner, Entity caster, LivingEntity target) {
        return true;
    }

    @Override
    protected void applyEffect(Level level, Player owner, Entity caster, LivingEntity target) {
        List<Entity> list = ShadowEvents.getEntitiesAround(caster, getSize(), getSize(), getSize());
        if ( caster instanceof LivingEntity living ) list.add(living);
        for ( Entity entity : list ) {
            if ( entity instanceof LivingEntity living ) {
                living.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, getLife(), 0, false, false));
                if ( living != caster ) addEnchantParticles(living, getColor().r, getColor().g, getColor().b, 0.15F, 8, true);
            }
        }
    }
}
