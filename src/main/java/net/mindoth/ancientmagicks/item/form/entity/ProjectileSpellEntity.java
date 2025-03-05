package net.mindoth.ancientmagicks.item.form.entity;

import com.google.common.collect.Lists;
import net.mindoth.ancientmagicks.item.ComponentItem;
import net.mindoth.ancientmagicks.item.effect.SpellEffectItem;
import net.mindoth.ancientmagicks.item.modifier.SpellModifierItem;
import net.mindoth.ancientmagicks.registries.AncientMagicksEntities;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.HashMap;
import java.util.List;

public class ProjectileSpellEntity extends AbstractSpellEntity {

    public ProjectileSpellEntity(EntityType<ProjectileSpellEntity> entityType, Level level) {
        super(entityType, level);
    }

    public ProjectileSpellEntity(Level level, LivingEntity owner, Entity caster) {
        super(AncientMagicksEntities.SPELL_PROJECTILE.get(), level, owner, caster);
    }

    private void castMagick(HitResult result) {
        List<SpellModifierItem> modifiers = Lists.newArrayList();
        HashMap<String, Float> stats = SpellEffectItem.createDefaultStats();
        for ( ComponentItem item : getSpellStack() ) {
            if ( item instanceof SpellModifierItem modifier ) modifiers.add(modifier);
            if ( item instanceof SpellEffectItem effect ) {
                for ( SpellModifierItem modifier : modifiers ) modifier.addStatsToMap(stats);
                effect.castSpell(level(), this.owner, this.caster, result, stats, getAoe());
                modifiers = Lists.newArrayList();
                stats = SpellEffectItem.createDefaultStats();
            }
        }
    }

    @Override
    protected void doMobEffects(EntityHitResult result) {
        castMagick(new EntityHitResult(result.getEntity(), position()));
    }

    @Override
    protected void doBlockEffects(BlockHitResult result) {
        castMagick(result);
    }
}
