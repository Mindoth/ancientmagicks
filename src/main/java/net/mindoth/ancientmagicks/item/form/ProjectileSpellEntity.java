package net.mindoth.ancientmagicks.item.form;

import net.mindoth.ancientmagicks.item.SpellItem;
import net.mindoth.ancientmagicks.registries.AncientMagicksEntities;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.network.PlayMessages;

public class ProjectileSpellEntity extends AbstractSpellEntity {

    public ProjectileSpellEntity(PlayMessages.SpawnEntity spawnEntity, Level level) {
        this(AncientMagicksEntities.SPELL_PROJECTILE.get(), level);
    }

    public ProjectileSpellEntity(EntityType<ProjectileSpellEntity> entityType, Level level) {
        super(entityType, level);
    }

    public ProjectileSpellEntity(Level level, LivingEntity owner, Entity caster, SpellItem spell) {
        super(AncientMagicksEntities.SPELL_PROJECTILE.get(), level, owner, caster, spell);
    }

    @Override
    protected void doMobEffects(EntityHitResult result) {
        if ( getSpell() != null ) getSpell().castSpell(level(), this.owner, this.caster, new EntityHitResult(result.getEntity(), position()), getStats());
    }

    @Override
    protected void doBlockEffects(BlockHitResult result) {
        if ( getSpell() != null ) getSpell().castSpell(level(), this.owner, this.caster, result, getStats());
    }
}
