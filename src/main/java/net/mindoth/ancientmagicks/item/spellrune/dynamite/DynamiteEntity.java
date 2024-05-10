package net.mindoth.ancientmagicks.item.spellrune.dynamite;

import com.google.common.collect.Lists;
import net.mindoth.ancientmagicks.registries.AncientMagicksEntities;
import net.mindoth.shadowizardlib.event.ShadowEvents;
import net.minecraft.entity.*;
import net.minecraft.entity.item.TNTEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.List;

public class DynamiteEntity extends TNTEntity {
    private static final DataParameter<Integer> DATA_FUSE_ID = EntityDataManager.defineId(DynamiteEntity.class, DataSerializers.INT);
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

    public DynamiteEntity(FMLPlayMessages.SpawnEntity spawnEntity, World level) {
        this(AncientMagicksEntities.DYNAMITE.get(), level);
    }

    public DynamiteEntity(EntityType<? extends DynamiteEntity> entityType, World level) {
        super(entityType, level);
        this.blocksBuilding = true;
    }

    public DynamiteEntity(World pLevel, double pX, double pY, double pZ, @Nullable LivingEntity pOwner) {
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
    protected boolean isMovementNoisy() {
        return false;
    }

    @Override
    public boolean isPickable() {
        return !this.removed;
    }

    @Override
    protected void explode() {
        float f = 4 + this.power + this.size;
        Explosion.Mode mode = Explosion.Mode.BREAK;
        if ( f <= 0 ) mode = Explosion.Mode.NONE;
        this.level.explode(this, this.getX(), this.getY(0.0625D), this.getZ(), f, mode);
    }

    @Override
    public void tick() {
        if ( !this.isNoGravity() ) {
            this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.04D, 0.0D));
        }
        this.move(MoverType.SELF, this.getDeltaMovement());
        this.setDeltaMovement(this.getDeltaMovement().scale(0.98D));
        if ( this.onGround ) {
            this.setDeltaMovement(this.getDeltaMovement().multiply(0.7D, -0.5D, 0.7D));
        }
        --this.life;
        if ( this.life <= 0 ) {
            this.remove();
            if ( !this.level.isClientSide ) this.explode();
        }
        else {
            this.updateInWaterStateAndDoFluidPushing();
            if ( this.level.isClientSide ) {
                this.level.addParticle(ParticleTypes.SMOKE, this.getX(), this.getY() + 0.5D, this.getZ(), 0.0D, 0.0D, 0.0D);
            }
        }

        if ( !this.level.isClientSide && this.homing ) {
            int range = 3;
            List<LivingEntity> entitiesAround = ShadowEvents.getEntitiesAround(this, this.level, range, null);

            //Need to do this haxxy way to still exclude targeting allies
            List<LivingEntity> excludeList = Lists.newArrayList();
            for ( LivingEntity exception : entitiesAround ) {
                if ( isAlly(exception) || exception.isDeadOrDying() || !exception.isAttackable() ) {
                    excludeList.add(exception);
                }
            }

            Entity nearest = ShadowEvents.getNearestEntity(this, this.level, range, excludeList);
            if ( nearest != null ) {
                if ( !this.isNoGravity() ) this.setNoGravity(true);
                double mX = getDeltaMovement().x();
                double mY = getDeltaMovement().y();
                double mZ = getDeltaMovement().z();
                Vector3d spellPos = new Vector3d(getX(), getY(), getZ());
                Vector3d targetPos = new Vector3d(ShadowEvents.getEntityCenter(nearest).x, ShadowEvents.getEntityCenter(nearest).y, ShadowEvents.getEntityCenter(nearest).z);
                Vector3d lookVec = targetPos.subtract(spellPos);
                Vector3d spellMotion = new Vector3d(mX, mY, mZ);
                float arc = 1.0F;
                Vector3d lerpVec = lerpVector(arc, spellMotion, lookVec);
                float multiplier = this.speed * 0.25F;
                this.setDeltaMovement(lerpVec.multiply(multiplier, multiplier, multiplier));
            }
            else if ( this.isNoGravity() ) this.setNoGravity(false);
        }
    }

    protected Vector3d lerpVector(float arc, Vector3d start, Vector3d end) {
        return new Vector3d(MathHelper.lerp(arc, start.x, end.x), MathHelper.lerp(arc, start.y, end.y), MathHelper.lerp(arc, start.z, end.z));
    }

    protected boolean isAlly(LivingEntity target) {
        return target == this.owner || (this.owner != null && target.isAlliedTo(this.owner)) || (target instanceof TameableEntity && ((TameableEntity)target).isOwnedBy(this.owner));
    }

    @Override
    protected void addAdditionalSaveData(CompoundNBT pCompound) {
        pCompound.putShort("Fuse", (short)this.getLife());
    }

    @Override
    protected void readAdditionalSaveData(CompoundNBT pCompound) {
        this.setFuse(pCompound.getShort("Fuse"));
    }

    @Nullable
    @Override
    public LivingEntity getOwner() {
        return this.owner;
    }

    @Override
    protected float getEyeHeight(Pose pPose, EntitySize pSize) {
        return 0.15F;
    }

    @Override
    public void setFuse(int pLife) {
        this.entityData.set(DATA_FUSE_ID, pLife);
        this.life = pLife;
    }

    @Override
    public void onSyncedDataUpdated(DataParameter<?> pKey) {
        if ( DATA_FUSE_ID.equals(pKey) ) {
            this.life = this.getFuse();
        }
    }

    @Override
    public int getFuse() {
        return this.entityData.get(DATA_FUSE_ID);
    }

    @Override
    public int getLife() {
        return this.life;
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
