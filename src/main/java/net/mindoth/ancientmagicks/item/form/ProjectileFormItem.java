package net.mindoth.ancientmagicks.item.form;

import com.google.common.collect.Lists;
import net.mindoth.ancientmagicks.item.ComponentItem;
import net.mindoth.ancientmagicks.item.form.entity.ProjectileSpellEntity;
import net.mindoth.ancientmagicks.item.modifier.SpellModifierItem;
import net.mindoth.ancientmagicks.item.spell.SpellItem;
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
    public boolean formSpell(LivingEntity owner, Entity caster, List<List<ComponentItem>> spellStack) {
        Level level = caster.level();
        List<ComponentItem> componentList = Lists.newArrayList();
        componentList.addAll(spellStack.get(0));
        spellStack.remove(0);

        SpellItem spellItem = null;
        for ( ComponentItem item : componentList ) if ( item instanceof SpellItem spell ) spellItem = spell;

        ProjectileSpellEntity projectile = new ProjectileSpellEntity(level, owner, caster, spellItem, spellStack);
        projectile.setNoGravity(true);

        HashMap<SpellModifierItem, Integer> map = new HashMap<>();
        for ( ComponentItem item : componentList ) if ( item instanceof SpellModifierItem modifier ) map.merge(modifier, 1, Integer::sum);
        for ( Map.Entry<SpellModifierItem, Integer> entry : map.entrySet() ) entry.getKey().addModifierOnEntityCreation(projectile, entry.getValue());

        if ( caster instanceof Player ) projectile.setPos(caster.getEyePosition().add(0, -0.2F, 0));
        else projectile.setPos(caster.getEyePosition());
        projectile.anonShootFromRotation(caster.getXRot(), caster.getYRot(), 0, Math.max(0, projectile.getSpeed()), 0.0F);
        level.addFreshEntity(projectile);
        return true;
    }
}
