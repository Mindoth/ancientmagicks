package net.mindoth.ancientmagicks.item.spell.fireball;

import net.mindoth.ancientmagicks.item.castingitem.TabletItem;
import net.mindoth.ancientmagicks.item.spell.abstractspell.AbstractSpellEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class FireballTablet extends TabletItem {

    public FireballTablet(Properties pProperties, int cooldown) {
        super(pProperties, cooldown);
    }

    @Override
    public boolean castMagic(Player owner, Entity caster, Vec3 center, float xRot, float yRot, int useTime) {
        boolean state = false;
        Level level = caster.level();
        int adjuster;
        if ( caster != owner ) adjuster = -1;
        else adjuster = 1;
        AbstractSpellEntity projectile = new FireballEntity(level, owner, caster, this);

        projectile.setColor(AbstractSpellEntity.getSpellColor("gold"), 0.8F);
        projectile.setPos(center.x, center.y, center.z);
        projectile.shootFromRotation(caster, xRot * adjuster, yRot * adjuster, 0F, Math.max(0, projectile.speed), 1.0F);
        level.addFreshEntity(projectile);
        state = true;

        if ( state ) playFireShootSound(level, center);

        return state;
    }
}