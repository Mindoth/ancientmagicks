package net.mindoth.ancientmagicks.item.spell.firebolt;

import net.mindoth.ancientmagicks.client.particle.ember.ParticleColor;
import net.mindoth.ancientmagicks.item.spell.abstractspell.AbstractSpellEntity;
import net.mindoth.ancientmagicks.item.spell.abstractspell.AbstractSpellShoot;
import net.mindoth.ancientmagicks.registries.AncientMagicksItems;
import net.mindoth.ancientmagicks.registries.attribute.AncientMagicksAttributes;
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
    protected void addData(LivingEntity owner, Entity caster, AbstractSpellEntity projectile) {
        projectile.setAdditionalData(getParticleColor());
        int power = projectile.getEntityData().get(AbstractSpellEntity.POWER) + (int)owner.getAttributeValue(AncientMagicksAttributes.SPELL_POWER.get());
        if ( caster instanceof LivingEntity living && living.getMainHandItem().getItem() == AncientMagicksItems.CANDLE_STAFF.get() ) power += 1;
        projectile.getEntityData().set(AbstractSpellEntity.POWER, power);
    }

    @Override
    protected void playSound(Level level, Vec3 center) {
        playFireShootSound(level, center);
    }
}
