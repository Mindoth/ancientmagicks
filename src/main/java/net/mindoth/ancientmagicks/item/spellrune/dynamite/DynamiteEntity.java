package net.mindoth.ancientmagicks.item.spellrune.dynamite;

import com.google.common.collect.Lists;
import net.mindoth.ancientmagicks.registries.AncientMagicksEntities;
import net.mindoth.shadowizardlib.event.ShadowEvents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;

import javax.annotation.Nullable;
import java.util.List;

public class DynamiteEntity extends PrimedTnt implements TraceableEntity {
    private static final EntityDataAccessor<Integer> DATA_FUSE_ID = SynchedEntityData.defineId(DynamiteEntity.class, EntityDataSerializers.INT);
    private static final int DEFAULT_FUSE_TIME = 80;
    @Nullable
    public LivingEntity owner;
    public float power;
    public float speed;
    public int life;
    public float size;
    public float blockPierce;
    public boolean homing;

    public float getDefaultPower() {
        return 0.0F;
    }

    public float getDefaultSpeed() {
        return 0.2F;
    }

    public float getDefaultLife() {
        return 80.0F;
    }

    public float getDefaultSize() {
        return 0.0F;
    }

    public float getDefaultBlockPierce() {
        return 0;
    }

    public DynamiteEntity(PlayMessages.SpawnEntity spawnEntity, Level level) {
        this(AncientMagicksEntities.DYNAMITE.get(), level);
    }

    public DynamiteEntity(EntityType<? extends DynamiteEntity> entityType, Level level) {
        super(entityType, level);
        this.blocksBuilding = true;
    }

    public DynamiteEntity(Level pLevel, double pX, double pY, double pZ, @Nullable LivingEntity pOwner) {
        this(AncientMagicksEntities.DYNAMITE.get(), pLevel);
        this.setPos(pX, pY, pZ);
        //double d0 = pLevel.random.nextDouble() * (double)((float)Math.PI * 2F);
        //this.setDeltaMovement(-Math.sin(d0) * 0.02D, (double)0.2F, -Math.cos(d0) * 0.02D);
        this.xo = pX;
        this.yo = pY;
        this.zo = pZ;
        this.owner = pOwner;

        this.power = this.getDefaultPower();
        this.speed = this.getDefaultSpeed();
        this.life = (int)this.getDefaultLife();
        this.size = this.getDefaultSize();
        this.blockPierce = (int)this.getDefaultBlockPierce();
        this.homing = false;
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(DATA_FUSE_ID, 80);
    }

    @Override
    protected Entity.MovementEmission getMovementEmission() {
        return Entity.MovementEmission.NONE;
    }

    @Override
    public boolean isPickable() {
        return !this.isRemoved();
    }

    @Override
    protected void explode() {
        float f = 4 + this.power + this.size;
        Level.ExplosionInteraction mode = Level.ExplosionInteraction.TNT;
        if ( f <= 0 ) mode = Level.ExplosionInteraction.NONE;
        this.level().explode(this, this.getX(), this.getY(0.0625D), this.getZ(), f, mode);
    }

    @Override
    public void tick() {
        if ( !this.isNoGravity() ) {
            this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.04D, 0.0D));
        }
        this.move(MoverType.SELF, this.getDeltaMovement());
        this.setDeltaMovement(this.getDeltaMovement().scale(0.98D));
        if ( this.onGround() ) {
            this.setDeltaMovement(this.getDeltaMovement().multiply(0.7D, -0.5D, 0.7D));
        }
        int i = this.getFuse() - 1;
        this.setFuse(i);
        if ( i <= 0 ) {
            this.discard();
            if ( !this.level().isClientSide ) this.explode();
        }
        else {
            this.updateInWaterStateAndDoFluidPushing();
            if ( this.level().isClientSide ) {
                this.level().addParticle(ParticleTypes.SMOKE, this.getX(), this.getY() + 0.5D, this.getZ(), 0.0D, 0.0D, 0.0D);
            }
        }

        if ( !this.level().isClientSide && this.homing ) {
            int range = 3;
            List<LivingEntity> entitiesAround = ShadowEvents.getEntitiesAround(this, this.level(), range, null);

            //Need to do this haxxy way to still exclude targeting allies
            List<LivingEntity> excludeList = Lists.newArrayList();
            for ( LivingEntity exception : entitiesAround ) {
                if ( isAlly(exception) || exception.isDeadOrDying() || !exception.isAttackable() ) {
                    excludeList.add(exception);
                }
            }

            Entity nearest = ShadowEvents.getNearestEntity(this, this.level(), range, excludeList);
            if ( nearest != null ) {
                if ( !this.isNoGravity() ) this.setNoGravity(true);
                double mX = getDeltaMovement().x();
                double mY = getDeltaMovement().y();
                double mZ = getDeltaMovement().z();
                Vec3 spellPos = new Vec3(getX(), getY(), getZ());
                Vec3 targetPos = new Vec3(ShadowEvents.getEntityCenter(nearest).x, ShadowEvents.getEntityCenter(nearest).y, ShadowEvents.getEntityCenter(nearest).z);
                Vec3 lookVec = targetPos.subtract(spellPos);
                Vec3 spellMotion = new Vec3(mX, mY, mZ);
                float arc = 1.0F;
                Vec3 lerpVec = lerpVector(arc, spellMotion, lookVec);
                float multiplier = this.speed * 0.25F;
                this.setDeltaMovement(lerpVec.multiply(multiplier, multiplier, multiplier));
            }
            else if ( this.isNoGravity() ) this.setNoGravity(false);
        }
    }

    protected Vec3 lerpVector(float arc, Vec3 start, Vec3 end) {
        return new Vec3(Mth.lerp(arc, start.x, end.x), Mth.lerp(arc, start.y, end.y), Mth.lerp(arc, start.z, end.z));
    }

    protected boolean isAlly(LivingEntity target) {
        return target == this.owner || (this.owner != null && target.isAlliedTo(this.owner)) || (target instanceof TamableAnimal && ((TamableAnimal)target).isOwnedBy(this.owner));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag pCompound) {
        pCompound.putShort("Fuse", (short)this.getFuse());
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag pCompound) {
        this.setFuse(pCompound.getShort("Fuse"));
    }

    @Nullable
    @Override
    public LivingEntity getOwner() {
        return this.owner;
    }

    @Override
    protected float getEyeHeight(Pose pPose, EntityDimensions pSize) {
        return 0.15F;
    }

    @Override
    public void setFuse(int pLife) {
        this.entityData.set(DATA_FUSE_ID, pLife);
        this.life = pLife;
    }

    @Override
    public int getFuse() {
        return this.entityData.get(DATA_FUSE_ID);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
