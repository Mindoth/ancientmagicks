package net.mindoth.ancientmagicks.item.form;

import com.google.common.collect.Lists;
import net.mindoth.ancientmagicks.client.particle.ember.EmberParticleProvider;
import net.mindoth.ancientmagicks.client.particle.ember.ParticleColor;
import net.mindoth.ancientmagicks.config.AncientMagicksCommonConfig;
import net.mindoth.ancientmagicks.item.SpellItem;
import net.mindoth.shadowizardlib.event.ShadowEvents;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.EnderDragonPart;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.TheEndGatewayBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.*;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Predicate;

public abstract class AbstractSpellEntity extends Projectile {

    public AbstractSpellEntity(EntityType<? extends AbstractSpellEntity> entityType, Level level) {
        super(entityType, level);
    }

    public AbstractSpellEntity(EntityType<? extends AbstractSpellEntity> entityType, Level pLevel, LivingEntity owner, Entity caster, SpellItem spell) {
        super(entityType, pLevel);
        this.owner = owner;
        this.caster = caster;
        this.spell = spell;
    }

    private final List<Integer> ignoredEntities = Lists.newArrayList();
    protected LivingEntity owner;
    protected Entity caster;
    protected SpellItem spell;
    protected int bounces = 0;
    public Entity target = null;

    public int defaultPower() {
        return 1;
    }

    public int defaultDie() {
        return 1;
    }

    public float defaultSpeed() {
        return 1.6F;
    }

    public int defaultLife() {
        return 160;
    }

    public float defaultSize() {
        return 0.2F;
    }

    public int defaultEnemyPierce() {
        return 0;
    }

    public int defaultBlockBounce() {
        return 0;
    }

    public boolean defaultHarmful() {
        return true;
    }

    public boolean defaultHoming() {
        return false;
    }

    public float defaultGravity() {
        return 0.015F;
    }

    public void anonShootFromRotation(float pX, float pY, float pZ, float pVelocity, float pInaccuracy) {
        float f = -Mth.sin(pY * ((float)Math.PI / 180F)) * Mth.cos(pX * ((float)Math.PI / 180F));
        float f1 = -Mth.sin((pX + pZ) * ((float)Math.PI / 180F));
        float f2 = Mth.cos(pY * ((float)Math.PI / 180F)) * Mth.cos(pX * ((float)Math.PI / 180F));
        this.shoot((double)f, (double)f1, (double)f2, pVelocity, pInaccuracy);
    }

    @Override
    public void tick() {
        super.tick();
        if ( level().isClientSide ) doClientTickEffects();
        if ( !level().isClientSide ) {
            doTickEffects();
            if ( this.tickCount > getLife() ) doExpirationEffects();
            if ( getHoming() ) doHoming();
        }
        handleHitDetection();
        handleTravel();
    }

    public void handleTravel() {
        this.xOld = getX();
        this.yOld = getY();
        this.zOld = getZ();
        setPos(position().add(getDeltaMovement()));
        this.updateRotation();
        if ( !this.isNoGravity() ) {
            Vec3 vec34 = this.getDeltaMovement();
            this.setDeltaMovement(vec34.x, vec34.y - (double) defaultGravity(), vec34.z);
        }
    }

    public void handleHitDetection() {
        HitResult result = getHitResult(position(), this, this::checkTeamForHit, getDeltaMovement(), level());
        boolean flag = false;
        if ( result.getType() == HitResult.Type.BLOCK ) {
            BlockPos blockpos = ((BlockHitResult)result).getBlockPos();
            BlockState blockstate = this.level().getBlockState(blockpos);
            if ( blockstate.is(Blocks.NETHER_PORTAL) ) {
                handleInsidePortal(blockpos);
                flag = true;
            }
            else if ( blockstate.is(Blocks.END_GATEWAY) ) {
                BlockEntity blockentity = level().getBlockEntity(blockpos);
                if ( blockentity instanceof TheEndGatewayBlockEntity && TheEndGatewayBlockEntity.canEntityTeleport(this) ) {
                    TheEndGatewayBlockEntity.teleportEntity(level(), blockpos, blockstate, this, (TheEndGatewayBlockEntity)blockentity);
                }
                flag = true;
            }
        }
        if ( result.getType() == HitResult.Type.ENTITY ) {
            Entity entity = ((EntityHitResult)result).getEntity();
            if ( this.ignoredEntities != null && !this.ignoredEntities.isEmpty() ) {
                for ( int id : this.ignoredEntities ) {
                    if ( entity.getId() == id ) {
                        flag = true;
                        break;
                    }
                }
            }
        }
        if ( result.getType() != HitResult.Type.MISS && !flag && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, result) ) onHit(result);
    }

    //Similar to SpellItem filter() method. CHANGE BOTH WHEN EDITING!
    protected boolean checkTeamForHit(Entity target) {
        return target instanceof LivingEntity && !(target instanceof ArmorStand) && (this.owner != target || !isHarmful())
                && (AncientMagicksCommonConfig.SPELL_FREE_FOR_ALL.get()
                || ((SpellItem.isAlly(this.owner, target) && !isHarmful()) || (!SpellItem.isAlly(this.owner, target) && isHarmful())));
    }

    protected HitResult getHitResult(Vec3 pStartVec, Entity pProjectile, Predicate<Entity> pFilter, Vec3 pEndVecOffset, Level pLevel) {
        Vec3 vec3 = pStartVec.add(pEndVecOffset);
        HitResult hitresult = pLevel.clip(new ClipContext(pStartVec, vec3, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, pProjectile));
        if ( hitresult.getType() != HitResult.Type.MISS ) vec3 = hitresult.getLocation();
        HitResult hitresult1 = getEntityHitResult(pLevel, pProjectile, pStartVec, vec3, pProjectile.getBoundingBox().expandTowards(pEndVecOffset).inflate(1.0D), pFilter);
        if ( hitresult1 != null ) hitresult = hitresult1;
        return hitresult;
    }

    @Nullable
    protected EntityHitResult getEntityHitResult(Level pLevel, Entity pProjectile, Vec3 pStartVec, Vec3 pEndVec, AABB pBoundingBox, Predicate<Entity> pFilter) {
        return ProjectileUtil.getEntityHitResult(pLevel, pProjectile, pStartVec, pEndVec, pBoundingBox, pFilter, 0.3F);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if ( level().isClientSide ) doClientHitEffects();
        else {
            if ( result.getEntity() instanceof LivingEntity living ) {
                if ( !this.ignoredEntities.contains(living.getId()) ) {
                    doMobEffects(result);
                    playHitSound(result);
                    this.ignoredEntities.add(living.getId());
                }
                if ( this.ignoredEntities.size() > getEnemyPierce() ) doDeathEffects();
            }
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        if ( level().isClientSide ) doClientHitEffects();
        else {
            doBlockEffects(result);
            playHitSound(result);
            BlockState blockState = level().getBlockState(result.getBlockPos());
            level().playSound(null, getX(), getY(), getZ(), blockState.getSoundType().getBreakSound(), SoundSource.PLAYERS, 0.3F, 2);
            if ( this.bounces < getBlockBounce() ) {
                this.bounces++;
                Direction face = result.getDirection();
                blockState.onProjectileHit(level(), blockState, result, this);
                Vec3 motion = getDeltaMovement();
                double motionX = motion.x();
                double motionY = motion.y();
                double motionZ = motion.z();
                if ( face == Direction.EAST ) motionX = -motionX;
                else if ( face == Direction.SOUTH ) motionZ = -motionZ;
                else if ( face == Direction.WEST ) motionX = -motionX;
                else if ( face == Direction.NORTH ) motionZ = -motionZ;
                else if ( face == Direction.UP ) motionY = -motionY;
                else if ( face == Direction.DOWN ) motionY = -motionY;
                //This seems to work better with low velocity projectiles than "this.setDeltaMovement(motionX, motionY, motionZ)";
                shoot(motionX, motionY, motionZ, getSpeed() * 0.5F, 0);
            }
            else doDeathEffects();
        }
    }

    protected int calcDamage() {
        return SpellItem.rollForPower(getPower(), getDie());
    }

    protected boolean homingFilter(Entity owner, Entity target) {
        return checkTeamForHit(target);
    }

    private void doHoming() {
        int range = 3;

        if ( this.target == null || !this.target.isAlive() ) this.target = ShadowEvents.getNearestEntity(this, level(), range, this::homingFilter);
        if ( this.target != null ) {
            if ( !isNoGravity() ) setNoGravity(true);
            double mX = getDeltaMovement().x();
            double mY = getDeltaMovement().y();
            double mZ = getDeltaMovement().z();
            Vec3 targetPos = ShadowEvents.getEntityCenter(this.target);
            if ( this.target instanceof EnderDragon || this.target instanceof EnderDragonPart ) targetPos = new Vec3(targetPos.x, this.target.getY(), targetPos.z);
            Vec3 lookVec = targetPos.subtract(position()).normalize();
            Vec3 spellMotion = new Vec3(mX, mY, mZ);
            float arc = 0.2F;
            if ( position().distanceTo(this.target.position()) < 2.0D ) arc = 1.0F;
            Vec3 lerpVec = new Vec3(Mth.lerp(arc, spellMotion.x, lookVec.x), Mth.lerp(arc, spellMotion.y, lookVec.y), Mth.lerp(arc, spellMotion.z, lookVec.z));
            setDeltaMovement(lerpVec);
        }
    }

    protected int getRenderType() {
        return 1;
    }

    protected void doClientTickEffects() {
        if ( this.isRemoved() ) return;
        if ( !this.level().isClientSide ) return;
        ClientLevel world = (ClientLevel)level();
        Vec3 center = ShadowEvents.getEntityCenter(this);
        Vec3 pos = new Vec3(center.x, getY(), center.z);

        if ( isRemoved() ) return;
        Vec3 vec3 = getDeltaMovement();
        double d5 = vec3.x;
        double d6 = vec3.y;
        double d1 = vec3.z;
        for ( int j = -4; j < 0; j++ ) {
            if ( -this.tickCount < j ) {
                //Main body
                float particleSize = Math.min(this.entityData.get(SIZE), (this.entityData.get(SIZE) * 0.1F) * this.tickCount);
                for ( int i = 0; i < 2; i++ ) {
                    float sphereSize = this.entityData.get(SIZE) / 4;
                    float randX = (float)((Math.random() * (sphereSize - (-sphereSize))) + (-sphereSize));
                    float randY = (float)((Math.random() * (sphereSize - (-sphereSize))) + (-sphereSize));
                    float randZ = (float)((Math.random() * (sphereSize - (-sphereSize))) + (-sphereSize));
                    world.addParticle(EmberParticleProvider.createData(getParticleColor(), particleSize, 10, true, getRenderType()), true,
                            pos.x + randX + d5 * (double)j / 4.0D, pos.y + randY + d6 * (double)j / 4.0D, pos.z + randZ + d1 * (double)j / 4.0D, 0, 0, 0);
                }
                //Trail twinkle
                if ( j == -1 ) {
                    for ( int i = 0; i < 8; i++ ) {
                        float sphereSize = this.entityData.get(SIZE) / 3;
                        float randX = (float)((Math.random() * (sphereSize - (-sphereSize))) + (-sphereSize));
                        float randY = (float)((Math.random() * (sphereSize - (-sphereSize))) + (-sphereSize));
                        float randZ = (float)((Math.random() * (sphereSize - (-sphereSize))) + (-sphereSize));
                        int life = 4 + level().random.nextInt(20);
                        world.addParticle(EmberParticleProvider.createData(getParticleColor(), sphereSize, life, true, getRenderType()), true,
                                pos.x + randX, pos.y + randY, pos.z + randZ, 0, 0, 0);
                    }
                }
            }
        }
    }

    protected void doClientHitEffects() {
    }

    protected void doMobEffects(EntityHitResult result) {
    }

    protected void doBlockEffects(BlockHitResult result) {
    }

    protected void playHitSound(HitResult result) {
    }

    protected void doTickEffects() {
    }

    protected void doExpirationEffects() {
        doDeathEffects();
    }

    protected void doDeathEffects() {
        this.discard();
    }

    public ParticleColor getParticleColor() {
        return new ParticleColor(this.entityData.get(RED), this.entityData.get(GREEN), this.entityData.get(BLUE));
    }

    public float getSize() {
        return this.entityData.get(SIZE);
    }

    public int getPower() {
        return this.entityData.get(POWER);
    }

    public int getDie() {
        return this.entityData.get(DIE);
    }

    public float getSpeed() {
        return this.entityData.get(SPEED);
    }

    public int getLife() {
        return this.entityData.get(LIFE);
    }

    public int getEnemyPierce() {
        return this.entityData.get(ENEMY_PIERCE);
    }

    public int getBlockBounce() {
        return this.entityData.get(BLOCK_BOUNCE);
    }

    public boolean isHarmful() {
        return this.entityData.get(IS_HARMFUL);
    }

    public boolean getHoming() {
        return this.entityData.get(IS_HOMING);
    }

    public static final EntityDataAccessor<Integer> RED = SynchedEntityData.defineId(AbstractSpellEntity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> GREEN = SynchedEntityData.defineId(AbstractSpellEntity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> BLUE = SynchedEntityData.defineId(AbstractSpellEntity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Float> SIZE = SynchedEntityData.defineId(AbstractSpellEntity.class, EntityDataSerializers.FLOAT);

    public static final EntityDataAccessor<Integer> POWER = SynchedEntityData.defineId(AbstractSpellEntity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> DIE = SynchedEntityData.defineId(AbstractSpellEntity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Float> SPEED = SynchedEntityData.defineId(AbstractSpellEntity.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Integer> LIFE = SynchedEntityData.defineId(AbstractSpellEntity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> ENEMY_PIERCE = SynchedEntityData.defineId(AbstractSpellEntity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> BLOCK_BOUNCE = SynchedEntityData.defineId(AbstractSpellEntity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Boolean> IS_HARMFUL = SynchedEntityData.defineId(AbstractSpellEntity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> IS_HOMING = SynchedEntityData.defineId(AbstractSpellEntity.class, EntityDataSerializers.BOOLEAN);

    public void setAdditionalData(ParticleColor.IntWrapper colors) {
        this.entityData.set(RED, colors.r);
        this.entityData.set(GREEN, colors.g);
        this.entityData.set(BLUE, colors.b);
        this.entityData.set(SIZE, this.defaultSize());

        this.entityData.set(POWER, this.defaultPower());
        this.entityData.set(DIE, this.defaultDie());
        this.entityData.set(SPEED, this.defaultSpeed());
        this.entityData.set(LIFE, this.defaultLife());
        this.entityData.set(ENEMY_PIERCE, this.defaultEnemyPierce());
        this.entityData.set(BLOCK_BOUNCE, this.defaultBlockBounce());
        this.entityData.set(IS_HARMFUL, this.defaultHarmful());
        this.entityData.set(IS_HOMING, this.defaultHoming());
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        this.entityData.set(RED, compound.getInt("red"));
        this.entityData.set(GREEN, compound.getInt("green"));
        this.entityData.set(BLUE, compound.getInt("blue"));
        this.entityData.set(SIZE, compound.getFloat("size"));

        this.entityData.set(POWER, compound.getInt("power"));
        this.entityData.set(DIE, compound.getInt("die"));
        this.entityData.set(SPEED, compound.getFloat("speed"));
        this.entityData.set(LIFE, compound.getInt("life"));
        this.entityData.set(ENEMY_PIERCE, compound.getInt("enemyPierce"));
        this.entityData.set(BLOCK_BOUNCE, compound.getInt("blockBounce"));
        this.entityData.set(IS_HARMFUL, compound.getBoolean("isHarmful"));
        this.entityData.set(IS_HOMING, compound.getBoolean("isHoming"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("red", this.entityData.get(RED));
        compound.putInt("green", this.entityData.get(GREEN));
        compound.putInt("blue", this.entityData.get(BLUE));
        compound.putFloat("size", this.entityData.get(SIZE));

        compound.putInt("power", this.entityData.get(POWER));
        compound.putInt("die", this.entityData.get(DIE));
        compound.putFloat("speed", this.entityData.get(SPEED));
        compound.putInt("life", this.entityData.get(LIFE));
        compound.putInt("enemyPierce", this.entityData.get(ENEMY_PIERCE));
        compound.putInt("blockBounce", this.entityData.get(BLOCK_BOUNCE));
        compound.putBoolean("isHarmful", this.entityData.get(IS_HARMFUL));
        compound.putBoolean("isHoming", this.entityData.get(IS_HOMING));
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(RED, 255);
        this.entityData.define(GREEN, 25);
        this.entityData.define(BLUE, 180);
        this.entityData.define(SIZE, 0.2F);

        this.entityData.define(POWER, 1);
        this.entityData.define(DIE, 1);
        this.entityData.define(SPEED, 1.6F);
        this.entityData.define(LIFE, 160);
        this.entityData.define(ENEMY_PIERCE, 0);
        this.entityData.define(BLOCK_BOUNCE, 0);
        this.entityData.define(IS_HARMFUL, true);
        this.entityData.define(IS_HOMING, false);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
