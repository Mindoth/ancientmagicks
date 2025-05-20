package net.mindoth.ancientmagicks.item.form.entity;

import com.google.common.collect.Lists;
import net.mindoth.ancientmagicks.item.ComponentItem;
import net.mindoth.ancientmagicks.item.effect.BlockTargetEffect;
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

    private void castMagick(HitResult hitResult) {
        HashMap<String, Float> stats = SpellEffectItem.createDefaultStats();
        List<SpellModifierItem> modifiers = Lists.newArrayList();
        List<String> modifierData = Lists.newArrayList();
        for ( int i = 0; i < getSpellStack().size(); i++ ) {
            ComponentItem item = getSpellStack().get(i);
            if ( item instanceof SpellModifierItem modifier ) {
                modifiers.add(modifier);
                modifierData.add(getData().get(i));
            }
            else if ( item instanceof SpellEffectItem effect ) {
                Level level = level();
                Vec3 posVec = hitResult.getLocation();
                if ( hitResult instanceof BlockHitResult bRes && effect instanceof BlockTargetEffect ) {
                    posVec = new Vec3(bRes.getBlockPos().getX(), bRes.getBlockPos().getY(), bRes.getBlockPos().getZ());
                }
                for ( int j = 0; j < modifiers.size(); j++ ) {
                    SpellModifierItem modifier = modifiers.get(j);
                    modifier.addStatsToMap(stats);
                    if ( modifier.isEncodeable() ) {
                        SpellModifierItem.EncodeableData ed = modifier.addDataFromEncodeable(modifierData.get(j), level, posVec);
                        level = ed.level;
                        posVec = ed.posVec;
                    }
                }
                if ( hitResult instanceof EntityHitResult eRes ) hitResult = new EntityHitResult(eRes.getEntity(), posVec);
                else if ( hitResult instanceof BlockHitResult bRes ) {
                    BlockPos bPos = new BlockPos(Mth.floor(posVec.x), Mth.floor(posVec.y), Mth.floor(posVec.z));
                    hitResult = new BlockHitResult(posVec, bRes.getDirection(), bPos, bRes.isInside());
                }

                effect.castSpell(level, this.owner, this.caster, hitResult, getAoe(), stats, getData().get(i));
                stats = SpellEffectItem.createDefaultStats();
                modifiers = Lists.newArrayList();
                modifierData = Lists.newArrayList();
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
