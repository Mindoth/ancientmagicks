package net.mindoth.ancientmagicks.item.form;

import com.google.common.collect.Lists;
import net.mindoth.ancientmagicks.item.CastingValidator;
import net.mindoth.ancientmagicks.item.ComponentItem;
import net.mindoth.ancientmagicks.item.form.entity.AbstractSpellEntity;
import net.mindoth.ancientmagicks.item.form.entity.ProjectileSpellEntity;
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
    public boolean formSpell(LivingEntity owner, Entity caster, List<ComponentItem> spellStack, List<String> data) {
        Level level = caster.level();

        ProjectileSpellEntity projectile = new ProjectileSpellEntity(level, owner, caster);
        projectile.ignoredEntities.put(caster.getId(), projectile.tickCount);
        projectile.setNoGravity(true);

        List<ComponentItem> newList = Lists.newArrayList();
        List<String> newData = Lists.newArrayList();
        boolean form = false;
        HashMap<SpellModifierItem, Integer> map = new HashMap<>();
        for ( int i = 0; i < spellStack.size(); i++ ) {
            ComponentItem item = spellStack.get(i);
            if ( !form ) {
                if ( item instanceof SpellFormItem ) form = true;
                if ( item instanceof SpellModifierItem modifier ) map.merge(modifier, 1, Integer::sum);
            }
            else {
                newList.add(item);
                newData.add(data.get(i));
            }
        }
        for ( Map.Entry<SpellModifierItem, Integer> entry : map.entrySet() ) entry.getKey().addEntityModifier(projectile, entry.getValue());
        projectile.getEntityData().set(AbstractSpellEntity.SPELLSTACK, CastingValidator.getStringFromSpellStack(newList));
        projectile.getEntityData().set(AbstractSpellEntity.DATA, CastingValidator.getDataStringFromList(newData));

        if ( caster instanceof Player ) projectile.setPos(caster.getEyePosition().add(0, -0.2F, 0));
        else if ( caster instanceof LivingEntity ) projectile.setPos(caster.getEyePosition());
        else projectile.setPos(caster.position());
        projectile.anonShootFromRotation(caster.getXRot(), caster.getYRot(), 0, projectile.getSpeed(), 0.0F);
        level.addFreshEntity(projectile);
        return true;
    }
}
