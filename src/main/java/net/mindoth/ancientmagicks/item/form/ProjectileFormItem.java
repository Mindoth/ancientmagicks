package net.mindoth.ancientmagicks.item.form;

import net.mindoth.ancientmagicks.item.SpellItem;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class ProjectileFormItem extends SpellFormItem {

    public ProjectileFormItem(Properties pProperties, int manaCost, int cooldown) {
        super(pProperties, manaCost, cooldown);
    }

    @Override
    public boolean castSpell(SpellItem spell, LivingEntity owner, Entity caster) {
        boolean state = false;
        Level level = caster.level();
        float down = caster instanceof Player ? -0.2F : 0.0F;
        state = true;

        if ( state ) {
            ProjectileEntity projectile = new ProjectileEntity(level, owner, caster, spell);
            projectile.setNoGravity(true);
            projectile.setPos(caster.getEyePosition().add(0, down, 0).add(caster.getForward()));
            projectile.anonShootFromRotation(caster.getXRot(), caster.getYRot(), 0, Math.max(0, projectile.getSpeed()), 0.0F);
            level.addFreshEntity(projectile);
        }

        return state;
    }
}
