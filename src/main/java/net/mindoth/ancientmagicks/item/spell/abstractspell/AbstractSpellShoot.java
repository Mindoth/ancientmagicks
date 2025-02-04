package net.mindoth.ancientmagicks.item.spell.abstractspell;

import net.mindoth.ancientmagicks.item.SpellItem;
import net.mindoth.ancientmagicks.registries.attribute.AncientMagicksAttributes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public abstract class AbstractSpellShoot extends SpellItem {

    public AbstractSpellShoot(Properties pProperties, int spellTier, int manaCost, int cooldown) {
        super(pProperties, spellTier, manaCost, cooldown);
    }

    //This needs to return an actual entity, it is not @Nullable
    protected AbstractSpellEntity getProjectile(Level level, Player owner, Entity caster) {
        return null;
    }

    protected boolean hasGravity() {
        return false;
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
            int power = projectile.getEntityData().get(AbstractSpellEntity.POWER) + (int)owner.getAttributeValue(AncientMagicksAttributes.SPELL_POWER.get());
            projectile.getEntityData().set(AbstractSpellEntity.POWER, power);
            projectile.setNoGravity(!hasGravity());
            projectile.setPos(center.add(0, down, 0).add(caster.getForward()));
            projectile.anonShootFromRotation(xRot * adjuster, yRot * adjuster, 0, Math.max(0, projectile.getSpeed()), 0.0F);
            level.addFreshEntity(projectile);

            playSound(level, center);
        }

        return state;
    }
}
