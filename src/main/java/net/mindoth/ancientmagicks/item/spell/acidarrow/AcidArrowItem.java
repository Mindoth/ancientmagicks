package net.mindoth.ancientmagicks.item.spell.acidarrow;

import net.mindoth.ancientmagicks.client.particle.ember.ParticleColor;
import net.mindoth.ancientmagicks.item.spell.abstractspell.AbstractSpellEntity;
import net.mindoth.ancientmagicks.item.spell.abstractspell.AbstractSpellShoot;
import net.mindoth.ancientmagicks.item.spell.abstractspell.ColorCode;
import net.mindoth.ancientmagicks.item.spell.abstractspell.SpellSchool;
import net.mindoth.ancientmagicks.item.spell.witcharrow.WitchArrowEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class AcidArrowItem extends AbstractSpellShoot {

    public AcidArrowItem(Properties pProperties, int spellTier, int manaCost, int cooldown, SpellSchool spellSchool) {
        super(pProperties, spellTier, manaCost, cooldown, spellSchool);
    }

    @Override
    protected AbstractSpellEntity getProjectile(Level level, Player owner, Entity caster) {
        return new AcidArrowEntity(level, owner, caster, this);
    }

    @Override
    protected ParticleColor.IntWrapper getColor() {
        return AbstractSpellEntity.getSpellColor(ColorCode.DARK_GREEN);
    }

    @Override
    protected boolean hasGravity() {
        return true;
    }
}
