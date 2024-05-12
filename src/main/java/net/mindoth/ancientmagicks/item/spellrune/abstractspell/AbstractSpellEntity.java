package net.mindoth.ancientmagicks.item.spellrune.abstractspell;

import com.google.common.collect.Lists;
import net.mindoth.ancientmagicks.client.particle.ember.EmberParticleProvider;
import net.mindoth.ancientmagicks.client.particle.ember.ParticleColor;
import net.mindoth.ancientmagicks.config.AncientMagicksCommonConfig;
import net.mindoth.ancientmagicks.item.spellrune.SpellRuneItem;
import net.mindoth.shadowizardlib.event.ShadowEvents;
import net.minecraft.client.multiplayer.ClientLevel;
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
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;

import java.util.List;

public class AbstractSpellEntity extends ThrowableProjectile {

    public AbstractSpellEntity(EntityType<? extends AbstractSpellEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected float getGravity() {
        return 0.005F;
    }

    public float getDefaultPower() {
        return 1.0F;
    }

    public float getDefaultSpeed() {
        return 0.0F;
    }

    public float getDefaultLife() {
        return 20.0F;
    }

    public float getDefaultSize() {
        return 1.0F;
    }

    public int getDefaultBounce() {
        return 0;
    }

    public int getDefaultEnemyPierce() {
        return 0;
    }

    public int getDefaultBlockPierce() {
        return 0;
    }

    public AbstractSpellEntity(EntityType<? extends AbstractSpellEntity> entityType, Level pLevel, LivingEntity owner, Entity caster, SpellRuneItem rune) {
        super(entityType, owner, pLevel);

        this.owner = owner;
        this.caster = caster;
        this.power = this.getDefaultPower();
        this.speed = this.getDefaultSpeed();
        this.life = this.getDefaultLife();
        this.size = this.getDefaultSize();
        this.bounce = this.getDefaultBounce();
        this.enemyPierce = this.getDefaultEnemyPierce();
        this.blockPierce = this.getDefaultBlockPierce();
    }

    protected LivingEntity owner;
    protected Entity caster;

    public float power;
    public float speed;
    public float life;
    public float size;
    public boolean homing = false;
    public int bounce;
    public int enemyPierce;
    public int blockPierce;

    protected boolean isAlly(LivingEntity target) {
        if ( target instanceof Player && !AncientMagicksCommonConfig.PVP.get() ) return true;
        return target == this.owner || !target.canAttack(this.owner) || target.isAlliedTo(this.owner) || (target instanceof TamableAnimal && ((TamableAnimal)target).isOwnedBy(this.owner));
    }

    protected void dealDamage(LivingEntity target) {
        target.hurt(target.damageSources().indirectMagic(this, this.owner), this.power);
    }

    @Override
    protected void onHit(HitResult result) {
        if ( this.level().isClientSide ) {
            doClientHitEffects();
        }
        if ( !this.level().isClientSide ) {
            if ( result.getType() == HitResult.Type.ENTITY && ((EntityHitResult)result).getEntity() instanceof LivingEntity ) {
                doMobEffects((EntityHitResult)result);
                if ( this.enemyPierce > 0 ) this.enemyPierce--;
                else doDeathEffects();
            }
            if ( result.getType() == HitResult.Type.BLOCK ) {
                doBlockEffects((BlockHitResult)result);
                BlockHitResult traceResult = (BlockHitResult)result;
                BlockState blockState = this.level().getBlockState(traceResult.getBlockPos());
                this.level().playSound(null, this.getX(), this.getY(), this.getZ(), blockState.getSoundType().getBreakSound(), SoundSource.PLAYERS, 0.3F, 2);

                if ( this.blockPierce > 0 ) {
                    this.blockPierce--;
                }
                else if ( this.bounce > 0 ) {
                    this.bounce--;
                    Direction face = traceResult.getDirection();
                    blockState.onProjectileHit(this.level(), blockState, traceResult, this);
                    Vec3 motion = this.getDeltaMovement();
                    double motionX = motion.x();
                    double motionY = motion.y();
                    double motionZ = motion.z();
                    if (face == Direction.EAST) {
                        motionX = -motionX;
                    }
                    else if (face == Direction.SOUTH) {
                        motionZ = -motionZ;
                    }
                    else if (face == Direction.WEST) {
                        motionX = -motionX;
                    }
                    else if (face == Direction.NORTH) {
                        motionZ = -motionZ;
                    }
                    else if (face == Direction.UP) {
                        motionY = -motionY;
                    }
                    else if (face == Direction.DOWN) {
                        motionY = -motionY;
                    }
                    //this.setDeltaMovement(motionX, motionY, motionZ);

                    //This seems to work better with low velocity projectiles
                    shoot(motionX, motionY, motionZ, this.speed * 0.5F, 0);
                }
                else doDeathEffects();
            }
            playHitSound();
        }
    }

    @Override
    public void tick() {
        super.tick();
        if ( level().isClientSide ) {
            doClientTickEffects();
        }
        if ( !level().isClientSide ) {
            doTickEffects();
            if ( this.tickCount > this.life ) {
                doDeathEffects();
            }
            if ( this.homing ) {
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
                if ( nearest != null && !this.getBoundingBox().inflate(1.5F).intersects(nearest.getBoundingBox()) ) {
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
    }

    protected Vec3 lerpVector(float arc, Vec3 start, Vec3 end) {
        return new Vec3(Mth.lerp(arc, start.x, end.x), Mth.lerp(arc, start.y, end.y), Mth.lerp(arc, start.z, end.z));
    }

    protected void doClientTickEffects() {
        if ( !this.level().isClientSide ) return;
        ClientLevel world = (ClientLevel)this.level();
        Vec3 center = ShadowEvents.getEntityCenter(this);
        Vec3 pos = new Vec3(center.x, this.getY(), center.z);
        
        //Main body
        for ( int i = 0; i < 4; i++ ) {
            float size = this.entityData.get(SIZE) / 4;
            float randX = (float)((Math.random() * (size - (-size))) + (-size));
            float randY = (float)((Math.random() * (size - (-size))) + (-size));
            float randZ = (float)((Math.random() * (size - (-size))) + (-size));
            world.addParticle(EmberParticleProvider.createData(getParticleColor(), this.entityData.get(SIZE), 10, true, true), true,
                    pos.x + randX, pos.y + randY, pos.z + randZ, 0, 0, 0);
        }
        //Trail twinkle
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

    protected void doClientHitEffects() {
    }

    protected void doMobEffects(EntityHitResult result) {
    }

    protected void doBlockEffects(BlockHitResult result) {
    }

    protected void playHitSound() {
    }

    protected void doTickEffects() {
    }

    protected void doDeathEffects() {
        this.discard();
    }

    public static ParticleColor.IntWrapper getSpellColor(String element) {
        ParticleColor.IntWrapper returnColor = null;
        if ( element.equals("dark_red") ) returnColor = new ParticleColor.IntWrapper(170, 25, 25);
        if ( element.equals("red") ) returnColor = new ParticleColor.IntWrapper(255, 85, 85);
        if ( element.equals("gold") ) returnColor = new ParticleColor.IntWrapper(255, 170, 25);
        if ( element.equals("yellow") ) returnColor = new ParticleColor.IntWrapper(255, 255, 85);
        if ( element.equals("dark_green") ) returnColor = new ParticleColor.IntWrapper(25, 170, 25);
        if ( element.equals("green") ) returnColor = new ParticleColor.IntWrapper(85, 225, 85);
        if ( element.equals("aqua") ) returnColor = new ParticleColor.IntWrapper(85, 255, 255);
        if ( element.equals("dark_aqua") ) returnColor = new ParticleColor.IntWrapper(25, 170, 170);
        if ( element.equals("dark_blue") ) returnColor = new ParticleColor.IntWrapper(25, 25, 170);
        if ( element.equals("blue") ) returnColor = new ParticleColor.IntWrapper(85, 85, 255);
        if ( element.equals("light_purple") ) returnColor = new ParticleColor.IntWrapper(255, 85, 255);
        if ( element.equals("dark_purple") ) returnColor = new ParticleColor.IntWrapper(170, 25, 170);
        if ( element.equals("white") ) returnColor = new ParticleColor.IntWrapper(255, 255, 255);
        if ( element.equals("gray") ) returnColor = new ParticleColor.IntWrapper(170, 170, 170);
        if ( element.equals("dark_gray") ) returnColor = new ParticleColor.IntWrapper(85, 85, 85);
        if ( element.equals("black") ) returnColor = new ParticleColor.IntWrapper(25, 25, 25);
        return returnColor;
    }

    public static final EntityDataAccessor<Integer> RED = SynchedEntityData.defineId(AbstractSpellEntity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> GREEN = SynchedEntityData.defineId(AbstractSpellEntity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> BLUE = SynchedEntityData.defineId(AbstractSpellEntity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Float> SIZE = SynchedEntityData.defineId(AbstractSpellEntity.class, EntityDataSerializers.FLOAT);

    public ParticleColor getParticleColor() {
        return new ParticleColor(this.entityData.get(RED), this.entityData.get(GREEN), this.entityData.get(BLUE));
    }

    public void setColor(ParticleColor.IntWrapper colors, float size) {
        this.entityData.set(RED, colors.r);
        this.entityData.set(GREEN, colors.g);
        this.entityData.set(BLUE, colors.b);
        this.entityData.set(SIZE, size);
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        this.entityData.set(RED, compound.getInt("red"));
        this.entityData.set(GREEN, compound.getInt("green"));
        this.entityData.set(BLUE, compound.getInt("blue"));
        this.entityData.set(SIZE, compound.getFloat("size"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("red", this.entityData.get(RED));
        compound.putInt("green", this.entityData.get(GREEN));
        compound.putInt("blue", this.entityData.get(BLUE));
        compound.putFloat("size", this.entityData.get(SIZE));
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(RED, 255);
        this.entityData.define(GREEN, 25);
        this.entityData.define(BLUE, 180);
        this.entityData.define(SIZE, 0.3F);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
