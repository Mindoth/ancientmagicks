package net.mindoth.ancientmagicks.item.spellrune.enderbolt;

import net.mindoth.ancientmagicks.item.modifierrune.ModifierRuneItem;
import net.mindoth.ancientmagicks.item.spellrune.SpellRuneItem;
import net.mindoth.ancientmagicks.item.spellrune.abstractspell.AbstractSpellEntity;
import net.mindoth.ancientmagicks.registries.AncientMagicksItems;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Arrays;
import java.util.List;

public class EnderBoltRune extends SpellRuneItem {

    public EnderBoltRune(Properties pProperties, int cooldown) {
        super(pProperties, cooldown);
    }

    @Override
    public void shootMagic(Player owner, Entity caster, Vec3 center, float xRot, float yRot, int useTime, List<ModifierRuneItem> modifierList) {
        Level level = caster.level();
        int adjuster;
        if ( caster != owner ) adjuster = -1;
        else adjuster = 1;
        playMagicShootSound(level, center);
        AbstractSpellEntity projectile = new EnderBoltEntity(level, owner, caster, this);

        for ( ModifierRuneItem rune : modifierList ) rune.addModifiersToSpellEntity(projectile);
        projectile.setColor(AbstractSpellEntity.getSpellColor("aqua"), 0.2F);
        projectile.setPos(center.x, center.y, center.z);
        projectile.shootFromRotation(caster, xRot * adjuster, yRot * adjuster, 0F, Math.max(0, projectile.speed), 1.0F);
        level.addFreshEntity(projectile);
    }
}
