package net.mindoth.ancientmagicks.item.form;

import net.mindoth.ancientmagicks.item.SpellItem;
import net.mindoth.ancientmagicks.item.spell.abstractspell.AbstractSpellEntity;
import net.mindoth.ancientmagicks.item.spell.abstractspell.spellpearl.SpellPearlEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class ProjectileFormItem extends SpellFormItem {
    public ProjectileFormItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public boolean castSpell(SpellItem spell, LivingEntity owner, Entity caster, int useTime) {
        boolean state = false;
        Level level = caster.level();
        float down = caster instanceof Player ? -0.2F : 0.0F;
        state = true;

        if ( state ) {
            AbstractSpellEntity projectile = new ProjectileEntity(level, owner, caster, spell);
            projectile.setNoGravity(true);
            projectile.setPos(caster.getEyePosition().add(0, down, 0).add(caster.getForward()));
            projectile.anonShootFromRotation(caster.getXRot(), caster.getYRot(), 0, Math.max(0, projectile.getSpeed()), 0.0F);
            level.addFreshEntity(projectile);
        }

        return state;
    }
}
