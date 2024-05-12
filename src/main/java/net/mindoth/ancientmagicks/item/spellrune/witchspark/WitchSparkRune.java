package net.mindoth.ancientmagicks.item.spellrune.witchspark;

import net.mindoth.ancientmagicks.item.modifierrune.ModifierRuneItem;
import net.mindoth.ancientmagicks.item.spellrune.SpellRuneItem;
import net.mindoth.ancientmagicks.item.spellrune.abstractspell.AbstractSpellEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class WitchSparkRune extends SpellRuneItem {

    public WitchSparkRune(Properties pProperties, int cooldown) {
        super(pProperties, cooldown);
    }

    @Override
    public void shootMagic(Player owner, Entity caster, Vec3 center, float xRot, float yRot, int useTime, List<ModifierRuneItem> modifierList) {
        Level level = caster.level();
        int adjuster;
        if ( caster != owner ) adjuster = -1;
        else adjuster = 1;
        playMagicShootSound(level, center);
        AbstractSpellEntity projectile = new WitchSparkEntity(level, owner, caster, this);

        for ( ModifierRuneItem rune : modifierList ) rune.addModifiersToSpellEntity(projectile);
        projectile.setColor(AbstractSpellEntity.getSpellColor("dark_purple"), 0.3F);
        projectile.setPos(center.x, center.y, center.z);
        projectile.shootFromRotation(caster, xRot * adjuster, yRot * adjuster, 0F, Math.max(0, projectile.speed), 1.0F);
        level.addFreshEntity(projectile);
    }
}
