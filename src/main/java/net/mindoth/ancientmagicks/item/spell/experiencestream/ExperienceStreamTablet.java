package net.mindoth.ancientmagicks.item.spell.experiencestream;

import net.mindoth.ancientmagicks.item.castingitem.TabletItem;
import net.mindoth.ancientmagicks.item.spell.abstractspell.AbstractSpellEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class ExperienceStreamTablet extends TabletItem {

    public ExperienceStreamTablet(Properties pProperties, int tier, boolean isChannel, int cooldown) {
        super(pProperties, tier, isChannel, cooldown);
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

        if ( owner.totalExperience > 0 || owner.experienceLevel > 0 ) {
            state = true;
            if ( useTime % 2 == 0 ) {
                AbstractSpellEntity projectile = new ExperienceStreamEntity(level, owner, caster, this, !owner.isCreative());
                projectile.power += owner.experienceLevel;
                projectile.setColor(AbstractSpellEntity.getSpellColor("green"), 0.3F);
                float min = -0.1F;
                float max = 0.1F;
                projectile.setPos(center.x + (min + Math.random() * (max - min)), center.y + down + (min + Math.random() * (max - min)), center.z + (min + Math.random() * (max - min)));
                projectile.anonShootFromRotation(xRot * adjuster, yRot * adjuster, 0F, Math.max(0, projectile.speed), 0.0F);
                level.addFreshEntity(projectile);
                if ( !owner.isCreative() ) owner.giveExperiencePoints(-(int)projectile.power);
            }
        }

        if ( state ) {
            playXpSound(level, center);
            playWindSound(level, center);
        }

        return state;
    }
}
