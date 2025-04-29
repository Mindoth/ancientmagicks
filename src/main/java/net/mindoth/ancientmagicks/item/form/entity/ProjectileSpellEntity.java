package net.mindoth.ancientmagicks.item.form.entity;

import com.google.common.collect.Lists;
import net.mindoth.ancientmagicks.item.ComponentItem;
import net.mindoth.ancientmagicks.item.effect.SpellEffectItem;
import net.mindoth.ancientmagicks.item.modifier.SpellModifierItem;
import net.mindoth.ancientmagicks.registries.AncientMagicksEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

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
        HashMap<String, Float> stats = SpellEffectItem.createDefaultStats();
        List<SpellModifierItem> modifiers = Lists.newArrayList();
        for ( int i = 0; i < getSpellStack().size(); i++ ) {
            ComponentItem item = getSpellStack().get(i);
            if ( item instanceof SpellModifierItem modifier ) modifiers.add(modifier);
            if ( item instanceof SpellEffectItem effect ) {
                Level level = level();
                Vec3 posVec = position();
                for ( int j = 0; j < modifiers.size(); j++ ) {
                    modifiers.get(j).addStatsToMap(stats);
                    SpellModifierItem.EncodeableData ed = modifiers.get(j).addDataFromEncodeable(getData().get(j), level, posVec);
                    level = ed.level;
                    posVec = ed.posVec;
                }
                if ( result instanceof EntityHitResult eRes ) result = new EntityHitResult(eRes.getEntity(), posVec);
                else if ( result instanceof BlockHitResult bRes ) result = new BlockHitResult(posVec, bRes.getDirection(), new BlockPos(Mth.floor(posVec.x), Mth.floor(posVec.y), Mth.floor(posVec.z)), bRes.isInside());
                effect.castSpell(level, this.owner, this.caster, result, getAoe(), stats, getData().get(i));
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
