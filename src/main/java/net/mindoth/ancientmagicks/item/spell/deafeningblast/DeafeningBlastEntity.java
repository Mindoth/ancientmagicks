package net.mindoth.ancientmagicks.item.spell.deafeningblast;

import net.mindoth.ancientmagicks.client.particle.ember.EmberParticleProvider;
import net.mindoth.ancientmagicks.item.castingitem.TabletItem;
import net.mindoth.ancientmagicks.item.spell.abstractspell.AbstractSpellEntity;
import net.mindoth.ancientmagicks.registries.AncientMagicksEntities;
import net.mindoth.shadowizardlib.event.ShadowEvents;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PlayMessages;

public class DeafeningBlastEntity extends AbstractSpellEntity {

    public DeafeningBlastEntity(PlayMessages.SpawnEntity spawnEntity, Level level) {
        this(AncientMagicksEntities.DEAFENING_BLAST.get(), level);
    }

    public DeafeningBlastEntity(EntityType<DeafeningBlastEntity> entityType, Level level) {
        super(entityType, level);
    }

    public DeafeningBlastEntity(Level level, LivingEntity owner, Entity caster, TabletItem rune) {
        super(AncientMagicksEntities.DEAFENING_BLAST.get(), level, owner, caster, rune);
    }

    @Override
    public float getDefaultPower() {
        return 4.0F;
    }

    @Override
    public float getDefaultSpeed() {
        return 1.0F;
    }

    @Override
    public float getDefaultLife() {
        return 60.0F;
    }

    @Override
    public int getDefaultEnemyPierce() {
        return Integer.MAX_VALUE;
    }

    @Override
    protected void doMobEffects(EntityHitResult result) {
        if ( this.power > 0 && !isAlly((LivingEntity)result.getEntity()) ) {
            if ( !(result.getEntity() instanceof LivingEntity target) ) return;
            dealDamage(target);
            ItemStack mainHand = target.getMainHandItem();
            ItemStack offHand = target.getOffhandItem();
            Vec3 pos = target.getEyePosition();
            if ( !mainHand.isEmpty() ) {
                ItemStack dropStack = mainHand.copyAndClear();
                ItemEntity drop = new ItemEntity(target.level(), pos.x, pos.y, pos.z, dropStack);
                drop.setDeltaMovement(target.getLookAngle().x * 0.25F, target.getLookAngle().y, target.getLookAngle().z * 0.25F);
                target.level().addFreshEntity(drop);
            }
            if ( !offHand.isEmpty() ) {
                ItemStack dropStack = offHand.copyAndClear();
                ItemEntity drop = new ItemEntity(target.level(), pos.x, pos.y, pos.z, dropStack);
                drop.setDeltaMovement(target.getLookAngle().x * 0.25F, target.getLookAngle().y, target.getLookAngle().z * 0.25F);
                target.level().addFreshEntity(drop);
            }
        }
    }

    protected void doClientTickEffects() {
        if ( this.isRemoved() ) return;
        if ( !this.level().isClientSide ) return;
        ClientLevel world = (ClientLevel)this.level();
        Vec3 center = ShadowEvents.getEntityCenter(this);
        final Vec3 pos = new Vec3(center.x, this.getY(), center.z);

        world.addParticle(EmberParticleProvider.createData(getParticleColor(), this.entityData.get(SIZE), 10, true, true), true,
                pos.x, pos.y, pos.z, 0, 0, 0);
        for ( int i = 1; i < 4; i++ ) {
            float mult = (i * 0.25F);
            Vec3 right = Vec3.directionFromRotation(0, this.getYRot() * -1 + (90 + i * 10));
            Vec3 vecRight = pos.add(right.x * mult, 0, right.z * mult);
            world.addParticle(EmberParticleProvider.createData(getParticleColor(), this.entityData.get(SIZE), 10, true, true), true,
                    vecRight.x, vecRight.y, vecRight.z, 0, 0, 0);

            Vec3 left = Vec3.directionFromRotation(0, this.getYRot() * -1 + (-90 - i * 10));
            Vec3 vecLeft = pos.add(left.x * mult, 0, left.z * mult);
            world.addParticle(EmberParticleProvider.createData(getParticleColor(), this.entityData.get(SIZE), 10, true, true), true,
                    vecLeft.x, vecLeft.y, vecLeft.z, 0, 0, 0);
        }
        /*for ( int j = -4; j < 0; ++j ) {
            //Main body
            //Trail twinkle
            if ( j == -1 ) {
                for ( int i = 0; i < 8; i++ ) {
                    float size = this.entityData.get(SIZE) / 3;
                    float randX = (float)((Math.random() * (size - (-size))) + (-size));
                    float randY = (float)((Math.random() * (size - (-size))) + (-size));
                    float randZ = (float)((Math.random() * (size - (-size))) + (-size));
                    int life = 4 + level().random.nextInt(20);
                    world.addParticle(EmberParticleProvider.createData(getParticleColor(), size, life, true, true), true,
                            pos.x + randX, pos.y + randY, pos.z + randZ, 0, 0, 0);
                }
            }
        }*/
    }
}
