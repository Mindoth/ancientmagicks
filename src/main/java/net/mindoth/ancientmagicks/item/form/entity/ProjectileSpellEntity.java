package net.mindoth.ancientmagicks.item.form.entity;

import net.mindoth.ancientmagicks.item.ComponentItem;
import net.mindoth.ancientmagicks.item.spell.SpellItem;
import net.mindoth.ancientmagicks.registries.AncientMagicksEntities;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.network.PlayMessages;

import java.util.List;

public class ProjectileSpellEntity extends AbstractSpellEntity {

    public ProjectileSpellEntity(EntityType<ProjectileSpellEntity> entityType, Level level) {
        super(entityType, level);
    }

    public ProjectileSpellEntity(Level level, LivingEntity owner, Entity caster, SpellItem spell, List<List<ComponentItem>> spellRecipe) {
        super(AncientMagicksEntities.SPELL_PROJECTILE.get(), level, owner, caster, spell, spellRecipe);
    }

    private void castMagick(HitResult result) {
        getSpell().castSpell(level(), this.owner, this.caster, result, getStats());
    }

    @Override
    protected void doMobEffects(EntityHitResult result) {
        if ( getSpell() != null ) castMagick(new EntityHitResult(result.getEntity(), position()));
    }

    @Override
    protected void doBlockEffects(BlockHitResult result) {
        if ( getSpell() != null ) castMagick(result);
    }
}
