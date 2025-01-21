package net.mindoth.ancientmagicks.item.spell.abstractspell;

import net.mindoth.ancientmagicks.client.particle.ember.ParticleColor;
import net.mindoth.ancientmagicks.item.spell.witcharrow.WitchArrowEntity;
import net.mindoth.ancientmagicks.registries.attribute.AncientMagicksAttributes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public abstract class AbstractSpellShoot extends SpellItem {

    public AbstractSpellShoot(Properties pProperties, int spellTier, int manaCost, int cooldown, SpellSchool spellSchool) {
        super(pProperties, spellTier, manaCost, cooldown, spellSchool);
    }

    protected AbstractSpellEntity getProjectile(Level level, Player owner, Entity caster) {
        return new WitchArrowEntity(level, owner, caster, this);
    }

    protected ParticleColor.IntWrapper getColor() {
        return AbstractSpellEntity.getSpellColor(ColorCode.DARK_PURPLE);
    }

    @Override
    public boolean castMagic(Player owner, Entity caster, Vec3 center, float xRot, float yRot, int useTime) {
        boolean state = false;
        Level level = caster.level();
        int adjuster = 1;
        float down = -0.2F;
        if ( caster != owner ) {
            adjuster = -1;
            down = 0.0F;
        }
        state = true;

        if ( state ) {
            AbstractSpellEntity projectile = getProjectile(level, owner, caster);
            projectile.setAdditionalData(getColor());
            projectile.setPos(center.add(0, down, 0).add(caster.getForward()));
            float power = projectile.getPower();
            power *= (float)owner.getAttributeValue(AncientMagicksAttributes.SPELL_POWER.get());
            projectile.getEntityData().set(AbstractSpellEntity.POWER, power);
            projectile.setNoGravity(!hasGravity());
            projectile.anonShootFromRotation(xRot * adjuster, yRot * adjuster, 0F, Math.max(0, projectile.getSpeed()), 0.0F);
            level.addFreshEntity(projectile);

            playSound(level, center);
        }

        return state;
    }

    protected boolean hasGravity() {
        return false;
    }
}
