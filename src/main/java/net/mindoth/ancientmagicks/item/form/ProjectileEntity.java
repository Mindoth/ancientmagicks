package net.mindoth.ancientmagicks.item.form;

import net.mindoth.ancientmagicks.item.SpellItem;
import net.mindoth.ancientmagicks.registries.AncientMagicksEntities;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.network.PlayMessages;

public class ProjectileEntity extends AbstractSpellEntity {

    public ProjectileEntity(PlayMessages.SpawnEntity spawnEntity, Level level) {
        this(AncientMagicksEntities.SPELL_PROJECTILE.get(), level);
    }

    public ProjectileEntity(EntityType<ProjectileEntity> entityType, Level level) {
        super(entityType, level);
    }

    public ProjectileEntity(Level level, LivingEntity owner, Entity caster, SpellItem spell) {
        super(AncientMagicksEntities.SPELL_PROJECTILE.get(), level, owner, caster, spell);
    }

    @Override
    protected void doMobEffects(EntityHitResult result) {
        Entity target = result.getEntity();
        this.spell.doSpell(level(), this.owner, this.caster, target);
    }
}
