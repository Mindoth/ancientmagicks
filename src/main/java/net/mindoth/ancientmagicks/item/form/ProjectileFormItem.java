package net.mindoth.ancientmagicks.item.form;

import com.google.common.collect.Lists;
import net.mindoth.ancientmagicks.item.CastingValidator;
import net.mindoth.ancientmagicks.item.SpellComponentItem;
import net.mindoth.ancientmagicks.item.form.entity.AbstractSpellEntity;
import net.mindoth.ancientmagicks.item.form.entity.ProjectileSpellEntity;
import net.mindoth.ancientmagicks.item.modifier.SpellModifierItem;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProjectileFormItem extends SpellFormItem {

    public ProjectileFormItem(Properties pProperties, int cost) {
        super(pProperties, cost);
    }

    /*@Override
    public boolean formSpell(LivingEntity owner, Entity caster, List<SpellComponentItem> spellStack, List<String> data) {
        List<SpellComponentItem> newList = Lists.newArrayList();
        List<String> newData = Lists.newArrayList();
        List<SpellModifierItem> formModifiers = Lists.newArrayList();
        HashMap<SpellModifierItem, Integer> map = new HashMap<>();
        boolean form = false;
        for ( int i = 0; i < spellStack.size(); i++ ) {
            SpellComponentItem item = spellStack.get(i);
            if ( !form ) {
                if ( item instanceof SpellFormItem ) form = true;
                if ( item instanceof SpellModifierItem modifier ) {
                    formModifiers.add(modifier);
                    map.merge(modifier, 1, Integer::sum);
                }
            }
            else {
                newList.add(item);
                newData.add(data.get(i));
            }
        }
        Level level = caster.level();
        Vec3 vecPos;
        if ( caster instanceof Player ) vecPos = caster.getEyePosition().add(0, -0.2F, 0);
        else if ( caster instanceof LivingEntity ) vecPos = caster.getEyePosition();
        else vecPos = caster.position();
        for ( int i = 0; i < formModifiers.size(); i++ ) {
            SpellModifierItem modifier = formModifiers.get(i);
            SpellModifierItem.EncodeableData ed = modifier.addDataFromEncodeable(data.get(i), level, vecPos);
            level = ed.level;
            vecPos = ed.posVec;
        }
        ProjectileSpellEntity projectile = new ProjectileSpellEntity(level, owner, caster);
        projectile.ignoredEntities.put(caster.getId(), projectile.tickCount);
        projectile.setNoGravity(true);
        for ( Map.Entry<SpellModifierItem, Integer> entry : map.entrySet() ) entry.getKey().addEntityModifier(projectile, entry.getValue());
        projectile.getEntityData().set(AbstractSpellEntity.SPELLSTACK, CastingValidator.getStringFromSpellStack(newList));
        projectile.getEntityData().set(AbstractSpellEntity.DATA, CastingValidator.getStringFromDataList(newData));
        projectile.setPos(vecPos);
        projectile.anonShootFromRotation(caster.getXRot(), caster.getYRot(), 0, projectile.getSpeed(), 0.0F);
        level.addFreshEntity(projectile);
        return true;
    }*/
}
