package net.mindoth.ancientmagicks.item.spell.witcharmor;

import net.mindoth.ancientmagicks.item.spell.abstractspell.AbstractArmorEffect;
import net.mindoth.ancientmagicks.item.spell.abstractspell.AbstractSpellRayCast;
import net.mindoth.ancientmagicks.registries.AncientMagicksEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.List;

public class WitchArmorItem extends AbstractSpellRayCast {

    public WitchArmorItem(Properties pProperties, int spellTier, int manaCost, int cooldown) {
        super(pProperties, spellTier, manaCost, cooldown);
    }

    @Override
    protected boolean isHarmful() {
        return false;
    }

    @Override
    protected int getLife() {
        return 36000;
    }

    @Override
    protected boolean canApply(Level level, Player owner, Entity caster, LivingEntity target) {
        return caster == owner;
    }

    @Override
    protected void applyEffect(Level level, Player owner, Entity caster, LivingEntity target) {
        List<MobEffectInstance> list = owner.getActiveEffects().stream()
                .filter(effect -> effect.getEffect() instanceof AbstractArmorEffect && effect.getEffect() != AncientMagicksEffects.WITCH_ARMOR.get()).toList();
        for ( MobEffectInstance effect : list ) owner.removeEffect(effect.getEffect());
        if ( !owner.hasEffect(AncientMagicksEffects.WITCH_ARMOR.get()) ) owner.addEffect(new MobEffectInstance(AncientMagicksEffects.WITCH_ARMOR.get(), getLife(), 0, false, isHarmful()));
    }
}
