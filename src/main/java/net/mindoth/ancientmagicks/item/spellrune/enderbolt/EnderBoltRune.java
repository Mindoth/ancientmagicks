package net.mindoth.ancientmagicks.item.spellrune.enderbolt;

import net.mindoth.ancientmagicks.item.modifierrune.ModifierRuneItem;
import net.mindoth.ancientmagicks.item.spellrune.SpellRuneItem;
import net.mindoth.ancientmagicks.item.spellrune.abstractspell.AbstractSpellEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import java.util.List;

public class EnderBoltRune extends SpellRuneItem {

    public EnderBoltRune(Properties pProperties, int cooldown) {
        super(pProperties, cooldown);
    }

    @Override
    public void shootMagic(PlayerEntity owner, Entity caster, Vector3d center, float xRot, float yRot, int useTime, List<ModifierRuneItem> modifierList) {
        World level = caster.level;
        int adjuster;
        if ( caster != owner ) adjuster = -1;
        else adjuster = 1;
        playMagicShootSound(level, center);
        AbstractSpellEntity projectile = new EnderBoltEntity(level, owner, caster, this);

        for ( ModifierRuneItem rune : modifierList ) rune.addModifiersToSpellEntity(projectile);
        projectile.setColor(AbstractSpellEntity.getSpellColor("aqua"), 0.2F);
        projectile.setPos(center.x, center.y, center.z);
        projectile.shootFromRotation(caster, xRot * adjuster, yRot * adjuster, 0F, projectile.speed, 1.0F);
        level.addFreshEntity(projectile);
    }
}
