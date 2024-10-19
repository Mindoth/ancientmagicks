package net.mindoth.ancientmagicks.item.spell.witcharrow;

import net.mindoth.ancientmagicks.item.SpellItem;
import net.mindoth.ancientmagicks.item.spell.abstractspell.AbstractSpellEntity;
import net.mindoth.shadowizardlib.event.ShadowEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class WitchArrowItem extends SpellItem {

    public WitchArrowItem(Properties pProperties, int spellLevel) {
        super(pProperties, spellLevel);
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

        float range = 64.0F;
        if ( owner != caster ) range = 0.0F;

        AbstractSpellEntity projectile = new WitchSparkEntity(level, owner, caster, this);

        projectile.setColor(AbstractSpellEntity.getSpellColor("dark_purple"), 0.2F);
        projectile.setPos(center.add(0, down, 0).add(caster.getForward()));
        Entity target = ShadowEvents.getPointedEntity(level, caster, range, 0.5F, caster == owner, true);
        if ( target != null && target != caster && (target instanceof LivingEntity living && !isAlly(owner, living)) ) projectile.target = target;
        projectile.anonShootFromRotation(xRot * adjuster, yRot * adjuster, 0F, Math.max(0, projectile.speed), 0.0F);
        level.addFreshEntity(projectile);
        state = true;

        if ( state ) playMagicShootSound(level, center);

        return state;
    }
}
