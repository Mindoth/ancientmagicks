package net.mindoth.ancientmagicks.item.spell.burnlance;

import net.mindoth.ancientmagicks.client.particle.ember.ParticleColor;
import net.mindoth.ancientmagicks.item.spell.abstractspell.AbstractSpellEntity;
import net.mindoth.ancientmagicks.item.spell.abstractspell.AbstractSpellProjectile;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class BurnLanceItem extends AbstractSpellProjectile {

    public BurnLanceItem(Properties pProperties, int spellTier, int manaCost, int cooldown) {
        super(pProperties, spellTier, manaCost, cooldown);
    }

    @Override
    protected AbstractSpellEntity getProjectile(Level level, Player owner, Entity caster) {
        return new BurnLanceEntity(level, owner, caster, this);
    }

    @Override
    protected ParticleColor.IntWrapper getColor() {
        return AbstractSpellEntity.getSpellColor("gold");
    }

    @Override
    protected void playSound(Level level, Vec3 center) {
        playFireShootSound(level, center);
    }
}
