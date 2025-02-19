package net.mindoth.ancientmagicks.item.form;

import net.mindoth.ancientmagicks.item.SpellItem;
import net.mindoth.ancientmagicks.item.modifier.SpellModifierItem;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProjectileFormItem extends SpellFormItem {

    public ProjectileFormItem(Properties pProperties, int manaCost, int cooldown) {
        super(pProperties, manaCost, cooldown);
    }

    @Override
    public boolean formSpell(SpellItem spell, LivingEntity owner, Entity caster, List<SpellModifierItem> modifiers) {
        boolean state = false;
        Level level = caster.level();
        float down = caster instanceof Player ? -0.2F : 0.0F;
        state = true;

        if ( state ) {
            ProjectileSpellEntity projectile = new ProjectileSpellEntity(level, owner, caster, spell);
            projectile.setNoGravity(true);
            HashMap<SpellModifierItem, Integer> map = new HashMap<>();
            if ( !modifiers.isEmpty() ) {
                for ( SpellModifierItem modifier : modifiers ) map.merge(modifier, 1, Integer::sum);
                for ( Map.Entry<SpellModifierItem, Integer> entry : map.entrySet() ) entry.getKey().addModifierToEntity(projectile, entry.getValue());
            }
            projectile.setPos(caster.getEyePosition().add(0, down, 0).add(caster.getForward()));
            projectile.anonShootFromRotation(caster.getXRot(), caster.getYRot(), 0, Math.max(0, projectile.getSpeed()), 0.0F);
            level.addFreshEntity(projectile);
        }

        return state;
    }
}
