package net.mindoth.ancientmagicks.item.spell.icicle;

import net.mindoth.ancientmagicks.client.particle.ember.ParticleColor;
import net.mindoth.ancientmagicks.item.spell.abstractspell.AbstractSpellEntity;
import net.mindoth.ancientmagicks.item.spell.abstractspell.AbstractSpellShoot;
import net.mindoth.ancientmagicks.item.spell.abstractspell.ColorCode;
import net.mindoth.ancientmagicks.item.spell.abstractspell.SpellSchool;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class IcicleItem extends AbstractSpellShoot {

    public IcicleItem(Properties pProperties, int spellTier, int manaCost, int cooldown, SpellSchool spellSchool) {
        super(pProperties, spellTier, manaCost, cooldown, spellSchool);
    }

    @Override
    protected AbstractSpellEntity getProjectile(Level level, Player owner, Entity caster) {
        return new IcicleEntity(level, owner, caster, this);
    }

    @Override
    protected ParticleColor.IntWrapper getColor() {
        return AbstractSpellEntity.getSpellColor(ColorCode.AQUA);
    }
}
