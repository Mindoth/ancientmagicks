package net.mindoth.ancientmagicks.item.spell.waterbolt;

import net.mindoth.ancientmagicks.item.spell.abstractspell.AbstractSpellEntity;
import net.mindoth.ancientmagicks.item.spell.abstractspell.SpellItem;
import net.mindoth.ancientmagicks.registries.AncientMagicksEntities;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.network.PlayMessages;

public class WaterBoltEntity extends AbstractSpellEntity {

    public WaterBoltEntity(PlayMessages.SpawnEntity spawnEntity, Level level) {
        this(AncientMagicksEntities.WATER_BOLT.get(), level);
    }

    public WaterBoltEntity(EntityType<WaterBoltEntity> entityType, Level level) {
        super(entityType, level);
    }

    public WaterBoltEntity(Level level, LivingEntity owner, Entity caster, SpellItem rune) {
        super(AncientMagicksEntities.WATER_BOLT.get(), level, owner, caster, rune);
    }

    @Override
    public float getDefaultPower() {
        return 4.0F;
    }

    @Override
    public float getDefaultSpeed() {
        return 0.8F;
    }

    @Override
    public int getDefaultLife() {
        return 100;
    }

    @Override
    public int getDefaultEnemyPierce() {
        return Integer.MAX_VALUE;
    }

    @Override
    protected void doMobEffects(EntityHitResult result) {
        if ( this.getPower() > 0 && !SpellItem.isAlly(this.owner, (LivingEntity)result.getEntity()) ) {
            LivingEntity target = (LivingEntity)result.getEntity();
            SpellItem.attackEntity(this.owner, target, this, SpellItem.getPowerInRange(1.0F, this.getPower()));
        }
    }
}
