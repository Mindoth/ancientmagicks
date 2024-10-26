package net.mindoth.ancientmagicks.item.spell.witcharmor;

import net.mindoth.ancientmagicks.item.spell.abstractspell.AbstractArmorEffect;
import net.mindoth.ancientmagicks.item.spell.abstractspell.AbstractSpellRayCast;
import net.mindoth.ancientmagicks.item.spell.frostarmor.FrostArmorEffect;
import net.mindoth.ancientmagicks.registries.AncientMagicksEffects;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class WitchArmorItem extends AbstractSpellRayCast {

    public WitchArmorItem(Properties pProperties, int spellTier, int manaCost, int cooldown, SpellSchool spellSchool) {
        super(pProperties, spellTier, manaCost, cooldown, spellSchool);
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
        for ( MobEffectInstance effect : owner.getActiveEffects() ) {
            MobEffect mobEffect = effect.getEffect();
            if ( mobEffect instanceof AbstractArmorEffect ) owner.removeEffect(mobEffect);
        }
        if ( !owner.hasEffect(AncientMagicksEffects.WITCH_ARMOR.get()) ) owner.addEffect(new MobEffectInstance(AncientMagicksEffects.WITCH_ARMOR.get(), getLife(), 0, false, isHarmful()));
    }
}
