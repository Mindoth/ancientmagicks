package net.mindoth.ancientmagicks.item.spell.abstractspell;

import net.mindoth.ancientmagicks.client.particle.ember.ParticleColor;
import net.mindoth.ancientmagicks.item.SpellItem;
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
        return AbstractSpellEntity.getSpellColor("dark_purple");
    }

    protected void playSound(Level level, Vec3 center) {
        playMagicSound(level, center);
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
            projectile.setColor(getColor());
            projectile.setPos(center.add(0, down, 0).add(caster.getForward()));
            projectile.anonShootFromRotation(xRot * adjuster, yRot * adjuster, 0F, Math.max(0, projectile.speed), 0.0F);
            projectile.power *= (float)owner.getAttributeValue(AncientMagicksAttributes.SPELL_POWER.get());
            level.addFreshEntity(projectile);

            playSound(level, center);
        }

        return state;
    }
}
