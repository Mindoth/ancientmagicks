package net.mindoth.ancientmagicks.item.spellrune.enderbolt;

import net.mindoth.ancientmagicks.client.particle.ember.EmberParticleData;
import net.mindoth.ancientmagicks.item.spellrune.SpellRuneItem;
import net.mindoth.ancientmagicks.item.spellrune.abstractspell.AbstractSpellEntity;
import net.mindoth.ancientmagicks.registries.AncientMagicksEntities;
import net.mindoth.shadowizardlib.event.ShadowEvents;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.FMLPlayMessages;

public class EnderBoltEntity extends AbstractSpellEntity {

    public EnderBoltEntity(FMLPlayMessages.SpawnEntity spawnEntity, World level) {
        this(AncientMagicksEntities.ENDER_BOLT.get(), level);
    }

    public EnderBoltEntity(EntityType<EnderBoltEntity> entityType, World level) {
        super(entityType, level);
    }

    public EnderBoltEntity(World level, LivingEntity owner, Entity caster, SpellRuneItem rune) {
        super(AncientMagicksEntities.ENDER_BOLT.get(), level, owner, caster, rune);
    }

    @Override
    public float getDefaultPower() {
        return 0.0F;
    }

    @Override
    public float getDefaultSpeed() {
        return 1.0F;
    }

    @Override
    public float getDefaultLife() {
        return 20.0F;
    }

    @Override
    public int getDefaultBounce() {
        return Integer.MAX_VALUE;
    }

    @Override
    public int getDefaultEnemyPierce() {
        return Integer.MAX_VALUE;
    }

    @Override
    protected void doDeathEffects() {
        Vector3d centerPos = ShadowEvents.getEntityCenter(this);
        if ( this.caster.level == this.level ) {
            this.caster.teleportTo(centerPos.x, this.position().y, centerPos.z);
            this.level.playSound(null, centerPos.x, centerPos.y, centerPos.z,
                    SoundEvents.ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1.0F, 1.0F);
            this.caster.level.broadcastEntityEvent(this.caster, (byte)46);
        }
        this.remove();
    }

    @Override
    protected void doClientTickEffects() {
        if ( !this.level.isClientSide ) return;
        ClientWorld world = (ClientWorld)this.level;
        Vector3d center = ShadowEvents.getEntityCenter(this);
        Vector3d pos = new Vector3d(center.x, this.getY(), center.z);

        //Main body
        for ( int i = 0; i < 4; i++ ) {
            float size = this.entityData.get(SIZE) / 4;
            float randX = (float)((Math.random() * (size - (-size))) + (-size));
            float randY = (float)((Math.random() * (size - (-size))) + (-size));
            float randZ = (float)((Math.random() * (size - (-size))) + (-size));
            world.addParticle(EmberParticleData.createData(getParticleColor(), this.entityData.get(SIZE), 10, true, false), true,
                    pos.x + randX, pos.y + randY, pos.z + randZ, 0, 0, 0);
        }
        //Trail twinkle
        for ( int i = 0; i < 8; i++ ) {
            float size = this.entityData.get(SIZE) / 3;
            float randX = (float)((Math.random() * (size - (-size))) + (-size));
            float randY = (float)((Math.random() * (size - (-size))) + (-size));
            float randZ = (float)((Math.random() * (size - (-size))) + (-size));
            int life = 4 + level.random.nextInt(20);
            world.addParticle(EmberParticleData.createData(getParticleColor(), size, life, true, false), true,
                    pos.x + randX, pos.y + randY, pos.z + randZ, 0, 0, 0);
        }
    }
}
