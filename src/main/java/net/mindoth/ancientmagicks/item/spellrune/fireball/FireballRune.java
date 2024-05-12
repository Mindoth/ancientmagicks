package net.mindoth.ancientmagicks.item.spellrune.fireball;

import net.mindoth.ancientmagicks.item.modifierrune.ModifierRuneItem;
import net.mindoth.ancientmagicks.item.spellrune.SpellRuneItem;
import net.mindoth.ancientmagicks.item.spellrune.abstractspell.AbstractSpellEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import java.util.List;

public class FireballRune extends SpellRuneItem {

    public FireballRune(Properties pProperties, int cooldown) {
        super(pProperties, cooldown);
    }

    @Override
    public void shootMagic(PlayerEntity owner, Entity caster, Vector3d center, float xRot, float yRot, int useTime, List<ModifierRuneItem> modifierList) {
        World level = caster.level;
        int adjuster;
        if ( caster != owner ) adjuster = -1;
        else adjuster = 1;
        playFireShootSound(level, center);
        AbstractSpellEntity projectile = new FireballEntity(level, owner, caster, this);

        for ( ModifierRuneItem rune : modifierList ) rune.addModifiersToSpellEntity(projectile);
        projectile.setColor(AbstractSpellEntity.getSpellColor("gold"), 0.8F);
        projectile.setPos(center.x, center.y, center.z);
        projectile.shootFromRotation(caster, xRot * adjuster, yRot * adjuster, 0F, Math.max(0, projectile.speed), 1.0F);
        level.addFreshEntity(projectile);
    }
}
