package net.mindoth.ancientmagicks.item.spell.experiencestream;

import net.mindoth.ancientmagicks.item.SpellItem;
import net.mindoth.ancientmagicks.item.spell.abstractspell.AbstractSpellEntity;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class ExperienceStream extends SpellItem {

    public ExperienceStream(Properties pProperties) {
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

        if ( owner.totalExperience > 0 || owner.isCreative() ) {
            state = true;
            if ( useTime % 2 == 0 ) {
                AbstractSpellEntity projectile = new ExperienceStreamEntity(level, owner, caster, this, !owner.isCreative());
                int powerBoost = owner.experienceLevel;
                projectile.power += powerBoost;
                projectile.setColor(AbstractSpellEntity.getSpellColor("green"), 0.3F);
                float min = -0.1F;
                float max = 0.1F;
                projectile.setPos(center.add((min + Math.random() * (max - min)), down + (min + Math.random() * (max - min)), (min + Math.random() * (max - min))).add(caster.getForward()));
                projectile.anonShootFromRotation(xRot * adjuster, yRot * adjuster, 0F, Math.max(0, projectile.speed), 0.0F);
                level.addFreshEntity(projectile);
                if ( !owner.isCreative() ) owner.giveExperiencePoints(-Mth.floor(projectile.power));
            }
        }

        if ( state ) {
            playXpSound(level, center);
            playWindSound(level, center);
        }

        return state;
    }
}
