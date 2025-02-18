package net.mindoth.ancientmagicks.item.spell.abstractspell;

import net.mindoth.ancientmagicks.item.SpellItem;
import net.mindoth.ancientmagicks.registries.attribute.AncientMagicksAttributes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public abstract class AbstractSpellShoot extends SpellItem {

    public AbstractSpellShoot(Properties pProperties, int spellTier, int manaCost, int cooldown, SpellType spellType) {
        super(pProperties, spellTier, manaCost, cooldown, spellType);
    }

    //This needs to return an actual entity, it is not @Nullable
    protected AbstractSpellEntity getProjectile(Level level, LivingEntity owner, Entity caster) {
        return null;
    }

    protected boolean hasGravity() {
        return false;
    }

    protected void addData(LivingEntity owner, Entity caster, AbstractSpellEntity projectile) {
        projectile.setAdditionalData(getParticleColor());
        int power = projectile.getEntityData().get(AbstractSpellEntity.POWER) + (int)owner.getAttributeValue(AncientMagicksAttributes.SPELL_POWER.get());
        projectile.getEntityData().set(AbstractSpellEntity.POWER, power);
        projectile.setNoGravity(!hasGravity());
    }

    @Override
    public boolean castMagic(LivingEntity owner, Entity caster, Vec3 center, int useTime) {
        boolean state = false;
        Level level = caster.level();
        float down = caster instanceof Player ? -0.2F : 0.0F;
        state = true;

        if ( state ) {
            AbstractSpellEntity projectile = getProjectile(level, owner, caster);
            addData(owner, caster, projectile);
            projectile.setNoGravity(!hasGravity());
            projectile.setPos(center.add(0, down, 0).add(caster.getForward()));
            projectile.anonShootFromRotation(caster.getXRot(), caster.getYRot(), 0, Math.max(0, projectile.getSpeed()), 0.0F);
            level.addFreshEntity(projectile);

            playSound(level, center);
        }

        return state;
    }
}
