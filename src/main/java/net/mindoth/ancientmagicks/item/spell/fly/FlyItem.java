package net.mindoth.ancientmagicks.item.spell.fly;

import net.mindoth.ancientmagicks.item.spell.abstractspell.AbstractSpellRayCast;
import net.mindoth.ancientmagicks.registries.AncientMagicksEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class FlyItem extends AbstractSpellRayCast {

    public FlyItem(Properties pProperties, int spellTier, int manaCost, int cooldown, SpellType spellType) {
        super(pProperties, spellTier, manaCost, cooldown, spellType);
    }

    @Override
    protected int getLife() {
        return 600;
    }

    @Override
    protected boolean canApply(Level level, Player owner, Entity caster, Entity target) {
        return target instanceof Player && filter(owner, target);
    }

    @Override
    protected void applyEffect(Level level, Player owner, Entity caster, Entity target) {
        ((LivingEntity)target).addEffect(new MobEffectInstance(AncientMagicksEffects.FLIGHT.get(), getLife(), 0, false, isHarmful()));
    }
}
