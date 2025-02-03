package net.mindoth.ancientmagicks.item.spell.firebolt;

import net.mindoth.ancientmagicks.item.SpellItem;
import net.mindoth.ancientmagicks.item.spell.abstractspell.AbstractSpellEntity;
import net.mindoth.ancientmagicks.registries.AncientMagicksEntities;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.network.PlayMessages;

public class FireBoltEntity extends AbstractSpellEntity {

    public FireBoltEntity(PlayMessages.SpawnEntity spawnEntity, Level level) {
        this(AncientMagicksEntities.FIRE_BOLT.get(), level);
    }

    public FireBoltEntity(EntityType<FireBoltEntity> entityType, Level level) {
        super(entityType, level);
    }

    public FireBoltEntity(Level level, LivingEntity owner, Entity caster, SpellItem spell) {
        super(AncientMagicksEntities.FIRE_BOLT.get(), level, owner, caster, spell);
    }

    @Override
    public int defaultDie() {
        return 10;
    }

    @Override
    protected void doMobEffects(EntityHitResult result) {
        Entity target = result.getEntity();
        SpellItem.attackEntity(this.owner, target, this, calcDamage());
        if ( target instanceof LivingEntity ) target.setSecondsOnFire(8);
    }
}
