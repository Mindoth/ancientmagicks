package net.mindoth.ancientmagicks.item.spell.firebolt;

import net.mindoth.ancientmagicks.item.SpellTabletItem;
import net.mindoth.ancientmagicks.item.spell.abstractspell.AbstractSpellEntity;
import net.mindoth.ancientmagicks.item.spell.fireball.FireballEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class FireBoltTablet extends SpellTabletItem {

    public FireBoltTablet(Properties pProperties, boolean isChannel, int cooldown) {
        super(pProperties, isChannel, cooldown);
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
        AbstractSpellEntity projectile = new FireBoltEntity(level, owner, caster, this);

        projectile.setColor(AbstractSpellEntity.getSpellColor("gold"), 0.2F);
        projectile.setPos(center.add(0, down, 0).add(caster.getForward()));
        projectile.anonShootFromRotation(xRot * adjuster, yRot * adjuster, 0F, Math.max(0, projectile.speed), 0.0F);
        level.addFreshEntity(projectile);
        state = true;

        if ( state ) playFireShootSound(level, center);

        return state;
    }
}