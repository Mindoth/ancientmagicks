package net.mindoth.ancientmagicks.item.spell.icicle;

import net.mindoth.ancientmagicks.item.SpellItem;
import net.mindoth.ancientmagicks.item.spell.abstractspell.AbstractSpellEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class Icicle extends SpellItem {

    public Icicle(Properties pProperties) {
        super(pProperties);
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
        AbstractSpellEntity projectile = new IcicleEntity(level, owner, caster, this);

        projectile.setColor(AbstractSpellEntity.getSpellColor("white"), 0.3F);
        projectile.setPos(center.add(0, down, 0).add(caster.getForward()));
        projectile.anonShootFromRotation(xRot * adjuster, yRot * adjuster, 0F, Math.max(0, projectile.speed), 0.0F);
        level.addFreshEntity(projectile);
        state = true;

        if ( state ) playMagicShootSound(level, center);

        return state;
    }
}
