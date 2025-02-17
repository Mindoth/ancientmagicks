package net.mindoth.ancientmagicks.item.temp.firebolt;

import net.mindoth.ancientmagicks.client.particle.ember.ParticleColor;
import net.mindoth.ancientmagicks.item.form.AbstractSpellEntity;
import net.mindoth.ancientmagicks.item.temp.abstractspell.AbstractSpellShoot;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class FireBoltItem extends AbstractSpellShoot {

    public FireBoltItem(Properties pProperties, int spellTier, int manaCost, int cooldown, SpellType spellType) {
        super(pProperties, spellTier, manaCost, cooldown, spellType);
    }

    @Override
    public ParticleColor.IntWrapper getParticleColor() {
        return ColorCode.GOLD.getParticleColor();
    }

    @Override
    protected AbstractSpellEntity getProjectile(Level level, LivingEntity owner, Entity caster) {
        return new FireBoltEntity(level, owner, caster, this);
    }

    @Override
    protected void playSound(Level level, Vec3 center) {
        playFireShootSound(level, center);
    }
}
