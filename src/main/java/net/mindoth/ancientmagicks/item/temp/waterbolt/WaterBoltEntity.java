package net.mindoth.ancientmagicks.item.temp.waterbolt;

import net.mindoth.ancientmagicks.item.form.AbstractSpellEntity;
import net.mindoth.ancientmagicks.item.SpellItem;
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

    public WaterBoltEntity(Level level, LivingEntity owner, Entity caster, SpellItem spell) {
        super(AncientMagicksEntities.WATER_BOLT.get(), level, owner, caster, spell);
    }

    @Override
    protected int getRenderType() {
        return 2;
    }

    @Override
    protected void doMobEffects(EntityHitResult result) {
        Entity target = result.getEntity();
        SpellItem.attackEntity(this.owner, target, this, calcDamage());
    }
}
