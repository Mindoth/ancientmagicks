package net.mindoth.ancientmagicks.item.spell.lifetap;

import net.mindoth.ancientmagicks.event.MagickEvents;
import net.mindoth.ancientmagicks.item.spell.abstractspell.AbstractSpellRayCast;
import net.mindoth.ancientmagicks.item.spell.abstractspell.SpellSchool;
import net.mindoth.ancientmagicks.registries.attribute.AncientMagicksAttributes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class LifeTapItem extends AbstractSpellRayCast {

    public LifeTapItem(Properties pProperties, int spellTier, int manaCost, int cooldown, SpellSchool spellSchool) {
        super(pProperties, spellTier, manaCost, cooldown, spellSchool);
    }

    @Override
    protected boolean isHarmful() {
        return false;
    }

    @Override
    protected boolean canApply(Level level, Player owner, Entity caster, LivingEntity target) {
        return caster == owner && owner.getHealth() > (owner.getMaxHealth() * 0.20F);
    }

    @Override
    protected void applyEffect(Level level, Player owner, Entity caster, LivingEntity target) {
        attackEntityWithoutKnockback(owner, owner, owner, owner.getMaxHealth() * 0.20F);
        MagickEvents.changeMana(owner, owner.getAttributeValue(AncientMagicksAttributes.MANA_MAXIMUM.get()) * 0.20F);
    }
}
