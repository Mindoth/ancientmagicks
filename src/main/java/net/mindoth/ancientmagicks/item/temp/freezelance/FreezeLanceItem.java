package net.mindoth.ancientmagicks.item.temp.freezelance;

import net.mindoth.ancientmagicks.client.particle.ember.ParticleColor;
import net.mindoth.ancientmagicks.item.form.AbstractSpellEntity;
import net.mindoth.ancientmagicks.item.temp.abstractspell.AbstractSpellShoot;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class FreezeLanceItem extends AbstractSpellShoot {

    public FreezeLanceItem(Properties pProperties, int spellTier, int manaCost, int cooldown, SpellType spellType) {
        super(pProperties, spellTier, manaCost, cooldown, spellType);
    }

    @Override
    public ParticleColor.IntWrapper getParticleColor() {
        return ColorCode.AQUA.getParticleColor();
    }

    @Override
    protected AbstractSpellEntity getProjectile(Level level, LivingEntity owner, Entity caster) {
        return new FreezeLanceEntity(level, owner, caster, this);
    }

    @Override
    protected boolean hasGravity() {
        return true;
    }
}
