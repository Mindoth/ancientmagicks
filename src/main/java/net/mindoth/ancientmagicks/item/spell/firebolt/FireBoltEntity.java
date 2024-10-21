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

    public FireBoltEntity(Level level, LivingEntity owner, Entity caster, SpellItem rune) {
        super(AncientMagicksEntities.FIRE_BOLT.get(), level, owner, caster, rune);
    }

    @Override
    public float getDefaultPower() {
        return 4.0F;
    }

    @Override
    public float getDefaultSpeed() {
        return 1.6F;
    }

    @Override
    public float getDefaultSize() {
        return 0.2F;
    }

    @Override
    protected void doMobEffects(EntityHitResult result) {
        if ( this.power > 0 && !isAlly((LivingEntity)result.getEntity()) ) {
            LivingEntity living = (LivingEntity)result.getEntity();
            dealDamage(living, 1.0F);
            living.setSecondsOnFire(8);
        }
    }
}
