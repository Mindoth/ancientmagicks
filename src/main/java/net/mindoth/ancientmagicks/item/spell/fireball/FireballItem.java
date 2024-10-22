package net.mindoth.ancientmagicks.item.spell.fireball;

import net.mindoth.ancientmagicks.item.SpellItem;
import net.mindoth.ancientmagicks.item.spell.abstractspell.AbstractSpellEntity;
import net.mindoth.ancientmagicks.registries.attributes.AncientMagicksAttributes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class FireballItem extends SpellItem {

    public FireballItem(Properties pProperties, int spellTier, int manaCost, int cooldown) {
        super(pProperties, spellTier, manaCost, cooldown);
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
        AbstractSpellEntity projectile = new FireballEntity(level, owner, caster, this);

        projectile.setColor(AbstractSpellEntity.getSpellColor("gold"));
        projectile.setPos(center.add(0, down, 0).add(caster.getForward()));
        projectile.anonShootFromRotation(xRot * adjuster, yRot * adjuster, 0F, Math.max(0, projectile.speed), 0.0F);
        projectile.power *= (float)owner.getAttributeValue(AncientMagicksAttributes.SPELL_POWER.get());
        level.addFreshEntity(projectile);
        state = true;

        if ( state ) {
            playFireShootSound(level, center);
        }

        return state;
    }
}
