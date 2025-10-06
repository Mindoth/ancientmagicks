package net.mindoth.ancientmagicks.entity;

import net.mindoth.ancientmagicks.client.particle.ember.EmberParticleProvider;
import net.mindoth.ancientmagicks.client.particle.ember.ParticleColor;
import net.mindoth.ancientmagicks.event.CastingValidator;
import net.mindoth.ancientmagicks.event.SpellData;
import net.mindoth.ancientmagicks.item.RuneItem;
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
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.EnderDragonPart;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Predicate;

public abstract class AbstractSpellEntity extends Projectile {

    public AbstractSpellEntity(EntityType<? extends AbstractSpellEntity> entityType, Level level) {
        super(entityType, level);
    }

    public AbstractSpellEntity(EntityType<? extends AbstractSpellEntity> entityType, Level pLevel, Entity caster) {
        super(entityType, pLevel);
        this.caster = caster;
    }

    protected Entity caster;
    public SpellData spellData;

    public final HashMap<Integer, Integer> ignoredEntities = new HashMap<>();
    public final HashMap<BlockPos, Integer> ignoredBlocks = new HashMap<>();
    public Entity target = null;
    protected int bounces = 0;

    private void timeIgnoredLists() {
        int timeout = 10;
        if ( this.ignoredEntities != null && !this.ignoredEntities.isEmpty() ) {
            for ( Map.Entry<Integer, Integer> entry : this.ignoredEntities.entrySet() ) {
                int id = entry.getKey();
                if ( entry.getValue() + timeout < this.tickCount ) {
                    if ( getEntityById(id) == null || (getEntityById(id) != null && !getEntityById(id).getBoundingBox().intersects(this.getBoundingBox()) ) ) {
                        this.ignoredEntities.remove(id);
                        break;
                    }
                }
            }
        }
        if ( this.ignoredBlocks != null && !this.ignoredBlocks.isEmpty() ) {
            for ( Map.Entry<BlockPos, Integer> entry : this.ignoredBlocks.entrySet() ) {
                if ( entry.getValue() + timeout < this.tickCount ) {
                    if ( entry.getKey() != this.blockPosition() ) {
                        this.ignoredBlocks.remove(entry.getKey());
                        break;
                    }
                }
            }
        }
    }

    private Entity getEntityById(int id) {
        if ( !(level() instanceof ServerLevel serverLevel) ) return null;
        for ( Entity entity : serverLevel.getAllEntities() ) if ( entity != null && entity.getId() == id ) return entity;
        return null;
    }

    @Override
    public void tick() {
        super.tick();
        if ( level().isClientSide ) doClientTickEffects();
        if ( !level().isClientSide ) {
            doTickEffects();
            if ( this.tickCount > getLife() ) doExpirationEffects();
            if ( getHoming() ) doHoming();
            timeIgnoredLists();
        }
        handleHitDetection();
        handleTravel();
    }

    public void anonShootFromRotation(float pX, float pY, float pZ, float pVelocity, float pInaccuracy) {
        float f = -Mth.sin(pY * ((float)Math.PI / 180F)) * Mth.cos(pX * ((float)Math.PI / 180F));
        float f1 = -Mth.sin((pX + pZ) * ((float)Math.PI / 180F));
        float f2 = Mth.cos(pY * ((float)Math.PI / 180F)) * Mth.cos(pX * ((float)Math.PI / 180F));
        this.shoot((double)f, (double)f1, (double)f2, pVelocity, pInaccuracy);
    }

    public void handleTravel() {
        this.xOld = getX();
        this.yOld = getY();
        this.zOld = getZ();
        setPos(position().add(getDeltaMovement()));
        this.updateRotation();
        if ( !this.isNoGravity() ) {
            Vec3 vec34 = this.getDeltaMovement();
            this.setDeltaMovement(vec34.x, vec34.y - (double)getGravity(), vec34.z);
        }
    }

    public void handleHitDetection() {
        HitResult result = getHitResult(position(), this, this::hitFilter, getDeltaMovement(), level());
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
            if ( this.ignoredEntities != null && !this.ignoredEntities.isEmpty() && this.ignoredEntities.containsKey(entity.getId()) ) flag = true;
        }
        if ( result.getType() != HitResult.Type.MISS && !flag && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, result) ) onHit(result);
    }

    protected boolean hitFilter(Entity target) {
        return true;
        //return RuneItem.allyFilter(this.caster, target, isHarmful());
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
                if ( !this.ignoredEntities.containsKey(living.getId()) ) {
                    doMobEffects(result);
                    playHitSound(result);
                    this.ignoredEntities.put(living.getId(), this.tickCount);
                }
                if ( this.ignoredEntities.size() > getEntityPierce() ) doDeathEffects();
            }
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        if ( level().isClientSide ) doClientHitEffects();
        else {
            BlockState blockState = level().getBlockState(result.getBlockPos());
            if ( !this.ignoredBlocks.containsKey(result.getBlockPos()) ) {
                doBlockEffects(result);
                playHitSound(result);
                this.ignoredBlocks.put(result.getBlockPos(), this.tickCount);
            }
            if ( this.ignoredBlocks.size() > getBlockPierce() ) {
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
                    shoot(motionX, motionY, motionZ, getSpeed() * 0.75F, 0);
                }
                else doDeathEffects();
            }
        }
    }

    protected boolean homingFilter(Entity owner, Entity target) {
        return hitFilter(target) && !this.ignoredEntities.containsKey(target.getId());
    }

    private void doHoming() {
        int range = (int)getReach();
        if ( this.target == null || !this.target.isAlive() ) this.target = ShadowEvents.getNearestEntity(this, level(), range, this::homingFilter);
        if ( this.target != null ) {
            if ( !isNoGravity() ) setNoGravity(true);
            Vec3 targetPos = ShadowEvents.getEntityCenter(this.target);
            if ( this.target instanceof EnderDragon || this.target instanceof EnderDragonPart ) targetPos = new Vec3(targetPos.x, this.target.getY(), targetPos.z);
            Vec3 lookVec = targetPos.subtract(position()).normalize();
            Vec3 spellMotion = new Vec3(getDeltaMovement().x(), getDeltaMovement().y(), getDeltaMovement().z());
            //float arc = 0.1F;
            //if ( position().distanceTo(this.target.position()) < 2.0D ) arc = 1.0F;
            float arc = 0.5F;
            Vec3 lerpVec = new Vec3(Mth.lerp(arc, spellMotion.x, lookVec.x), Mth.lerp(arc, spellMotion.y, lookVec.y), Mth.lerp(arc, spellMotion.z, lookVec.z));
            shoot(lerpVec.x, lerpVec.y, lerpVec.z, getSpeed() * 0.75F, 0);
            if ( this.ignoredEntities.containsKey(this.target.getId()) ) this.target = null;
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
        double speed = 0.05D;
        for ( int j = 0; j < 4; j++ ) {
            //----------------------------------------------------------------------------------------------------------------------//
            if ( -this.tickCount < j - 4 ) {
                double vecX = new Random().nextDouble(1.0D - -1.0D) + -1.0D;
                double vecY = new Random().nextDouble(1.0D - -1.0D) + -1.0D;
                double vecZ = new Random().nextDouble(1.0D - -1.0D) + -1.0D;
                world.addParticle(EmberParticleProvider.createData(getParticleColor(), getSize(), 8, false, getRenderType()),
                        pos.x + d5 * (double) j / 4.0D, pos.y + d6 * (double) j / 4.0D, pos.z + d1 * (double) j / 4.0D,
                        vecX * speed, vecY * speed, vecZ * speed);
            }
            //----------------------------------------------------------------------------------------------------------------------//

            /*
            //----------------------------------------------------------------------------------------------------------------------//
            if ( -this.tickCount < j - 4 ) {
                //Main body
                float particleSize = Math.min(getSize(), (getSize() * 0.1F) * this.tickCount);
                for ( int i = 0; i < 2; i++ ) {
                    float sphereSize = getSize() / 4;
                    float randX = (float)((Math.random() * (sphereSize - (-sphereSize))) + (-sphereSize));
                    float randY = (float)((Math.random() * (sphereSize - (-sphereSize))) + (-sphereSize));
                    float randZ = (float)((Math.random() * (sphereSize - (-sphereSize))) + (-sphereSize));
                    world.addParticle(EmberParticleProvider.createData(getParticleColor(), particleSize, 10, true, getRenderType()), true,
                            pos.x + randX + d5 * (double)j / 4.0D, pos.y + randY + d6 * (double)j / 4.0D, pos.z + randZ + d1 * (double)j / 4.0D, 0, 0, 0);
                }
                //Trail twinkle
                if ( j == 3 ) {
                    for ( int i = 0; i < 8; i++ ) {
                        float sphereSize = getSize() / 3;
                        float randX = (float)((Math.random() * (sphereSize - (-sphereSize))) + (-sphereSize));
                        float randY = (float)((Math.random() * (sphereSize - (-sphereSize))) + (-sphereSize));
                        float randZ = (float)((Math.random() * (sphereSize - (-sphereSize))) + (-sphereSize));
                        int life = 4 + level().random.nextInt(20);
                        world.addParticle(EmberParticleProvider.createData(getParticleColor(), sphereSize, life, true, getRenderType()), true,
                                pos.x + randX, pos.y + randY, pos.z + randZ, 0, 0, 0);
                    }
                }
            }
            //----------------------------------------------------------------------------------------------------------------------//
            */
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
        HashMap<String, Float> map = new HashMap<>();
        map.put("red", (float)this.entityData.get(RED));
        map.put("green", (float)this.entityData.get(GREEN));
        map.put("blue", (float)this.entityData.get(BLUE));
        return RuneItem.getParticleColor(map);
    }

    public float getSize() {
        return this.entityData.get(SIZE);
    }

    public List<RuneItem> getSpellStack() {
        return CastingValidator.getSpellStackFromString(this.entityData.get(SPELLSTACK));
    }

    public List<String> getData() {
        return List.of(this.entityData.get(DATA).split(","));
    }

    public float getSpeed() {
        return 1.6F;
    }

    public int getLife() {
        return 100;
    }

    public float getReach() {
        return 0;
    }

    public int getEntityPierce() {
        return 0;
    }

    public int getBlockPierce() {
        return 0;
    }

    public int getBlockBounce() {
        return 0;
    }

    public boolean getHoming() {
        return false;
    }

    public float getGravity() {
        //return 0.015F;
        return 0.03F;
    }

    public boolean isHarmful() {
        return true;
        /*for ( int i = 0; i < getSpellStack().size(); i++ ) {
            RuneItem item = getSpellStack().get(i);
            if ( item instanceof SpellEffectItem effect && effect.isHarmful(getData().get(i)) ) return true;
        }
        return false;*/
    }

    public static final EntityDataAccessor<Integer> RED = SynchedEntityData.defineId(AbstractSpellEntity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> GREEN = SynchedEntityData.defineId(AbstractSpellEntity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> BLUE = SynchedEntityData.defineId(AbstractSpellEntity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Float> SIZE = SynchedEntityData.defineId(AbstractSpellEntity.class, EntityDataSerializers.FLOAT);

    public static final EntityDataAccessor<String> SPELLSTACK = SynchedEntityData.defineId(AbstractSpellEntity.class, EntityDataSerializers.STRING);
    public static final EntityDataAccessor<String> DATA = SynchedEntityData.defineId(AbstractSpellEntity.class, EntityDataSerializers.STRING);

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        this.entityData.set(RED, compound.getInt("red"));
        this.entityData.set(GREEN, compound.getInt("green"));
        this.entityData.set(BLUE, compound.getInt("blue"));
        this.entityData.set(SIZE, compound.getFloat("size"));

        this.entityData.set(SPELLSTACK, compound.getString("spellstack"));
        this.entityData.set(DATA, compound.getString("data"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("red", this.entityData.get(RED));
        compound.putInt("green", this.entityData.get(GREEN));
        compound.putInt("blue", this.entityData.get(BLUE));
        compound.putFloat("size", this.entityData.get(SIZE));

        compound.putString("spellstack", this.entityData.get(SPELLSTACK));
        compound.putString("data", this.entityData.get(DATA));
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(RED, -1);
        this.entityData.define(GREEN, -1);
        this.entityData.define(BLUE, -1);
        this.entityData.define(SIZE, 0.1F);

        this.entityData.define(SPELLSTACK, "");
        this.entityData.define(DATA, "");
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
