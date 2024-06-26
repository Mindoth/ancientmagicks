package net.mindoth.ancientmagicks.item.spellrune.fireball;

import net.mindoth.ancientmagicks.item.spellrune.SpellRuneItem;
import net.mindoth.ancientmagicks.item.spellrune.abstractspell.AbstractSpellEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class FireballSpell extends SpellRuneItem {

    public FireballSpell(Properties pProperties, int cooldown) {
        super(pProperties, cooldown);
    }

    @Override
    public boolean castMagic(Player owner, Entity caster, Vec3 center, float xRot, float yRot, int useTime) {
        boolean state = false;
        Level level = caster.level();
        int adjuster;
        if ( caster != owner ) adjuster = -1;
        else adjuster = 1;
        playFireShootSound(level, center);
        AbstractSpellEntity projectile = new FireballEntity(level, owner, caster, this);

        projectile.setColor(AbstractSpellEntity.getSpellColor("gold"), 0.8F);
        projectile.setPos(center.x, center.y, center.z);
        projectile.shootFromRotation(caster, xRot * adjuster, yRot * adjuster, 0F, Math.max(0, projectile.speed), 1.0F);
        level.addFreshEntity(projectile);
        state = true;

        return state;
    }
}
