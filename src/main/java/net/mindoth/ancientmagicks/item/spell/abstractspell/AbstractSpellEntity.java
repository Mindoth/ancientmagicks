package net.mindoth.ancientmagicks.item.spell.abstractspell;

import com.google.common.collect.Lists;
import net.mindoth.ancientmagicks.client.particle.ember.EmberParticleProvider;
import net.mindoth.ancientmagicks.client.particle.ember.ParticleColor;
import net.mindoth.ancientmagicks.config.AncientMagicksCommonConfig;
import net.mindoth.ancientmagicks.item.SpellItem;
import net.mindoth.ancientmagicks.item.spell.mindcontrol.MindControlEffect;
import net.mindoth.ancientmagicks.registries.AncientMagicksEffects;
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
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.TheEndGatewayBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;

import java.util.List;

public abstract class AbstractSpellEntity extends Projectile {

    public AbstractSpellEntity(EntityType<? extends AbstractSpellEntity> entityType, Level level) {
        super(entityType, level);
    }

    protected boolean isHarmful() {
        return true;
    }

    protected float getGravity() {
        return 0.0F;
    }

    public float getDefaultPower() {
        return 1.0F;
    }

    public float getDefaultSpeed() {
        return 0.0F;
    }

    public float getDefaultLife() {
        return 160.0F;
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

    public boolean getDefaultHoming() {
        return false;
    }

    public AbstractSpellEntity(EntityType<? extends AbstractSpellEntity> entityType, Level pLevel, LivingEntity owner, Entity caster, SpellItem spell) {
        super(entityType, pLevel);

        this.setNoGravity(true);
        this.spell = spell;
        this.owner = owner;
        this.caster = caster;
        this.isHarmful = this.isHarmful();
        this.power = this.getDefaultPower();
        this.speed = this.getDefaultSpeed();
        this.life = this.getDefaultLife();
        this.size = this.getDefaultSize();
        this.bounce = this.getDefaultBounce();
        this.enemyPierce = this.getDefaultEnemyPierce();
        this.blockPierce = this.getDefaultBlockPierce();
        this.homing = this.getDefaultHoming();
        this.target = null;
    }

    protected SpellItem spell;
    protected LivingEntity owner;
    protected Entity caster;

    public boolean isHarmful;
    public float power;
    public float speed;
    public float life;
    public float size;
    public boolean homing;
    public int bounce;
    public int enemyPierce;
    public int blockPierce;
    public Entity target;

    public void anonShootFromRotation(float pX, float pY, float pZ, float pVelocity, float pInaccuracy) {
        float f = -Mth.sin(pY * ((float)Math.PI / 180F)) * Mth.cos(pX * ((float)Math.PI / 180F));
        float f1 = -Mth.sin((pX + pZ) * ((float)Math.PI / 180F));
        float f2 = Mth.cos(pY * ((float)Math.PI / 180F)) * Mth.cos(pX * ((float)Math.PI / 180F));
        this.shoot((double)f, (double)f1, (double)f2, pVelocity, pInaccuracy);
    }

    protected boolean isAlly(LivingEntity target) {
        if ( this.owner == null || target == null ) return false;
        if ( target instanceof Player && !AncientMagicksCommonConfig.PVP.get() ) return true;
        else return target == this.owner || !this.owner.canAttack(target) || this.owner.isAlliedTo(target)
                || (target instanceof TamableAnimal pet && pet.isOwnedBy(this.owner)) || (target instanceof Mob mob && isMinionsOwner(mob));
    }

    protected boolean isMinionsOwner(Mob mob) {
        return mob.hasEffect(AncientMagicksEffects.MIND_CONTROL.get()) && mob.getPersistentData().hasUUID(MindControlEffect.NBT_KEY)
                && mob.getPersistentData().getUUID(MindControlEffect.NBT_KEY).equals(this.owner.getUUID()) && mob.getTarget() != this.owner;
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
            doExtraServerEffects(result);
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
        if ( level().isClientSide ) doClientTickEffects();
        if ( !level().isClientSide ) {
            doTickEffects();
            if ( this.tickCount > this.life ) doExpirationEffects();
            if ( this.homing ) doHoming();
        }
        handleHitDetection();
        handleTravel();
    }

    public void handleHitDetection() {
        HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
        boolean flag = false;
        if ( hitresult.getType() == HitResult.Type.BLOCK ) {
            BlockPos blockpos = ((BlockHitResult)hitresult).getBlockPos();
            BlockState blockstate = this.level().getBlockState(blockpos);
            if ( blockstate.is(Blocks.NETHER_PORTAL) ) {
                this.handleInsidePortal(blockpos);
                flag = true;
            }
            else if ( blockstate.is(Blocks.END_GATEWAY) ) {
                BlockEntity blockentity = this.level().getBlockEntity(blockpos);
                if ( blockentity instanceof TheEndGatewayBlockEntity && TheEndGatewayBlockEntity.canEntityTeleport(this) ) {
                    TheEndGatewayBlockEntity.teleportEntity(this.level(), blockpos, blockstate, this, (TheEndGatewayBlockEntity)blockentity);
                }

                flag = true;
            }
        }

        if ( hitresult.getType() != HitResult.Type.MISS && !flag && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, hitresult) ) {
            this.onHit(hitresult);
        }
    }

    public void handleTravel() {
        setPos(position().add(getDeltaMovement()));
        this.updateRotation();
        if ( !this.isNoGravity() ) {
            Vec3 vec31 = this.getDeltaMovement();
            this.setDeltaMovement(vec31.x, vec31.y - (double)this.getGravity(), vec31.z);
        }
    }

    private void doHoming() {
        int range = 3;
        List<LivingEntity> entitiesAround = ShadowEvents.getEntitiesAround(this, this.level(), range, null);
        //Need to do this haxxy way to still exclude targeting allies
        List<LivingEntity> excludeList = Lists.newArrayList();
        for ( LivingEntity exception : entitiesAround ) {
            if ( (this.isHarmful && isAlly(exception)) || (!this.isHarmful && !isAlly(exception)) || exception.isDeadOrDying() || !exception.isAttackable() ) {
                excludeList.add(exception);
            }
        }

        if ( this.target == null || !this.target.isAlive() ) this.target = ShadowEvents.getNearestEntity(this, this.level(), range, excludeList);
        if ( this.target != null ) {
            if ( !this.isNoGravity() ) this.setNoGravity(true);
            double mX = getDeltaMovement().x();
            double mY = getDeltaMovement().y();
            double mZ = getDeltaMovement().z();
            Vec3 lookVec = ShadowEvents.getEntityCenter(this.target).subtract(this.position()).normalize();
            Vec3 spellMotion = new Vec3(mX, mY, mZ);
            float arc = 0.2F;
            if ( this.position().distanceTo(this.target.position()) < 2.0D ) arc = 1.0F;
            Vec3 lerpVec = new Vec3(Mth.lerp(arc, spellMotion.x, lookVec.x), Mth.lerp(arc, spellMotion.y, lookVec.y), Mth.lerp(arc, spellMotion.z, lookVec.z));
            this.setDeltaMovement(lerpVec);
        }
        else if ( this.isNoGravity() ) this.setNoGravity(false);
    }

    protected void doClientTickEffects() {
        if ( this.isRemoved() ) return;
        if ( !this.level().isClientSide ) return;
        ClientLevel world = (ClientLevel)this.level();
        Vec3 center = ShadowEvents.getEntityCenter(this);
        Vec3 pos = new Vec3(center.x, this.getY(), center.z);

        if ( this.isRemoved() ) return;
        Vec3 vec3 = this.getDeltaMovement();
        double d5 = vec3.x;
        double d6 = vec3.y;
        double d1 = vec3.z;
        for ( int j = -4; j < 0; j++ ) {
            if ( -this.tickCount - 2 < j ) {
                //Main body
                float particleSize = Math.min(this.entityData.get(SIZE), (this.entityData.get(SIZE) * 0.1F) * this.tickCount);
                for ( int i = 0; i < 2; i++ ) {
                    float sphereSize = this.entityData.get(SIZE) / 4;
                    float randX = (float)((Math.random() * (sphereSize - (-sphereSize))) + (-sphereSize));
                    float randY = (float)((Math.random() * (sphereSize - (-sphereSize))) + (-sphereSize));
                    float randZ = (float)((Math.random() * (sphereSize - (-sphereSize))) + (-sphereSize));
                    world.addParticle(EmberParticleProvider.createData(getParticleColor(), particleSize, 10, true, true), true,
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
                        world.addParticle(EmberParticleProvider.createData(getParticleColor(), sphereSize, life, true, true), true,
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

    protected void doExtraServerEffects(HitResult result) {
    }

    protected void playHitSound() {
    }

    protected void doTickEffects() {
    }

    protected void doDeathEffects() {
        this.discard();
    }

    protected void doExpirationEffects() {
        doDeathEffects();
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
