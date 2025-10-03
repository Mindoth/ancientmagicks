package net.mindoth.ancientmagicks.entity;

import net.mindoth.ancientmagicks.event.CastingValidator;
import net.mindoth.ancientmagicks.registries.ModEntities;
import net.mindoth.ancientmagicks.event.MultiBlockHitResult;
import net.mindoth.ancientmagicks.event.MultiEntityHitResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.Collections;

public class ProjectileSpellEntity extends AbstractSpellEntity {

    public ProjectileSpellEntity(EntityType<ProjectileSpellEntity> entityType, Level level) {
        super(entityType, level);
    }

    public ProjectileSpellEntity(Level level, Entity caster) {
        super(ModEntities.SPELL_PROJECTILE.get(), level, caster);
    }

    private void castMagick(HitResult hitResult) {
        if ( this.spellData != null ) {
            this.spellData.addObject(hitResult);
            CastingValidator.resolveSpell(this.caster, this.spellData, this.getSpellStack());
        }
    }

    @Override
    protected void doMobEffects(EntityHitResult result) {
        if ( this.spellData != null ) {
            MultiEntityHitResult mEntityHitResult = new MultiEntityHitResult(this.caster, result.getEntity().position(), Collections.singletonList(result.getEntity()));
            castMagick(mEntityHitResult);
        }
    }

    @Override
    protected void doBlockEffects(BlockHitResult result) {
        if ( this.spellData != null ) {
            MultiBlockHitResult mBlockHitResult = new MultiBlockHitResult(result.getLocation(), result.getDirection(), result.getBlockPos(), result.isInside(), Collections.singletonList(result.getBlockPos()), this.level());
            castMagick(mBlockHitResult);
        }
    }
}
