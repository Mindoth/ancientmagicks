package net.mindoth.ancientmagicks.item.spellrune.abstractspell;

import com.google.common.collect.Lists;
import net.mindoth.ancientmagicks.client.particle.ember.EmberParticleData;
import net.mindoth.ancientmagicks.client.particle.ember.ParticleColor;
import net.mindoth.ancientmagicks.config.AncientMagicksCommonConfig;
import net.mindoth.ancientmagicks.item.spellrune.SpellRuneItem;
import net.mindoth.shadowizardlib.event.ShadowEvents;
import net.minecraft.block.BlockState;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.List;

public class AbstractSpellEntity extends ThrowableEntity {

    public AbstractSpellEntity(EntityType<? extends AbstractSpellEntity> entityType, World level) {
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

    public AbstractSpellEntity(EntityType<? extends AbstractSpellEntity> entityType, World pLevel, LivingEntity owner, Entity caster, SpellRuneItem rune) {
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
        if ( target instanceof PlayerEntity && !AncientMagicksCommonConfig.PVP.get() ) return true;
        else return target == this.owner || !target.canAttack(this.owner) || target.isAlliedTo(this.owner) || (target instanceof TameableEntity && ((TameableEntity)target).isOwnedBy(this.owner));
    }

    protected void dealDamage(LivingEntity target) {
        target.hurt(DamageSource.indirectMagic(this, this.owner), this.power);
    }

    @Override
    protected void onHit(RayTraceResult result) {
        if ( this.level.isClientSide ) {
            doClientHitEffects();
        }
        if ( !this.level.isClientSide ) {
            if ( result.getType() == RayTraceResult.Type.ENTITY && ((EntityRayTraceResult)result).getEntity() instanceof LivingEntity ) {
                doMobEffects((EntityRayTraceResult)result);
                if ( this.enemyPierce > 0 ) this.enemyPierce--;
                else doDeathEffects();
            }
            if ( result.getType() == RayTraceResult.Type.BLOCK ) {
                doBlockEffects((BlockRayTraceResult)result);
                BlockRayTraceResult traceResult = (BlockRayTraceResult)result;
                BlockState blockState = this.level.getBlockState(traceResult.getBlockPos());
                this.level.playSound(null, this.getX(), this.getY(), this.getZ(), blockState.getSoundType().getBreakSound(), SoundCategory.PLAYERS, 0.3F, 2);

                if ( this.blockPierce > 0 ) {
                    this.blockPierce--;
                }
                else if ( this.bounce > 0 ) {
                    this.bounce--;
                    Direction face = traceResult.getDirection();
                    blockState.onProjectileHit(this.level, blockState, traceResult, this);
                    Vector3d motion = this.getDeltaMovement();
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
        if ( level.isClientSide ) {
            doClientTickEffects();
        }
        if ( !level.isClientSide ) {
            doTickEffects();
            if ( this.tickCount > this.life ) {
                doDeathEffects();
            }
            if ( this.homing ) {
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
                if ( nearest != null && !this.getBoundingBox().inflate(1.5F).intersects(nearest.getBoundingBox()) ) {
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
    }

    protected Vector3d lerpVector(float arc, Vector3d start, Vector3d end) {
        return new Vector3d(MathHelper.lerp(arc, start.x, end.x), MathHelper.lerp(arc, start.y, end.y), MathHelper.lerp(arc, start.z, end.z));
    }

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
            world.addParticle(EmberParticleData.createData(getParticleColor(), this.entityData.get(SIZE), 10, true, true), true,
                    pos.x + randX, pos.y + randY, pos.z + randZ, 0, 0, 0);
        }
        //Trail twinkle
        for ( int i = 0; i < 8; i++ ) {
            float size = this.entityData.get(SIZE) / 3;
            float randX = (float)((Math.random() * (size - (-size))) + (-size));
            float randY = (float)((Math.random() * (size - (-size))) + (-size));
            float randZ = (float)((Math.random() * (size - (-size))) + (-size));
            int life = 4 + level.random.nextInt(20);
            world.addParticle(EmberParticleData.createData(getParticleColor(), size, life, true, true), true,
                    pos.x + randX, pos.y + randY, pos.z + randZ, 0, 0, 0);
        }
    }

    protected void doClientHitEffects() {
    }

    protected void doMobEffects(EntityRayTraceResult result) {
    }

    protected void doBlockEffects(BlockRayTraceResult result) {
    }

    protected void playHitSound() {
    }

    protected void doTickEffects() {
    }

    protected void doDeathEffects() {
        this.remove();
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

    public static final DataParameter<Integer> RED = EntityDataManager.defineId(AbstractSpellEntity.class, DataSerializers.INT);
    public static final DataParameter<Integer> GREEN = EntityDataManager.defineId(AbstractSpellEntity.class, DataSerializers.INT);
    public static final DataParameter<Integer> BLUE = EntityDataManager.defineId(AbstractSpellEntity.class, DataSerializers.INT);
    public static final DataParameter<Float> SIZE = EntityDataManager.defineId(AbstractSpellEntity.class, DataSerializers.FLOAT);

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
    public void load(CompoundNBT compound) {
        super.load(compound);
        this.entityData.set(RED, compound.getInt("red"));
        this.entityData.set(GREEN, compound.getInt("green"));
        this.entityData.set(BLUE, compound.getInt("blue"));
        this.entityData.set(SIZE, compound.getFloat("size"));
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT compound) {
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
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
