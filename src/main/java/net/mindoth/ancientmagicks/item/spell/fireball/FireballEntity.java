package net.mindoth.ancientmagicks.item.spell.fireball;

import com.google.common.collect.Sets;
import net.mindoth.ancientmagicks.item.spell.abstractspell.AbstractSpellEntity;
import net.mindoth.ancientmagicks.item.SpellItem;
import net.mindoth.ancientmagicks.registries.AncientMagicksEntities;
import net.mindoth.shadowizardlib.event.ShadowEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PlayMessages;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;

public class FireballEntity extends AbstractSpellEntity {

    public FireballEntity(PlayMessages.SpawnEntity spawnEntity, Level level) {
        this(AncientMagicksEntities.FIREBALL.get(), level);
    }

    public FireballEntity(EntityType<FireballEntity> entityType, Level level) {
        super(entityType, level);
    }

    public FireballEntity(Level level, LivingEntity owner, Entity caster, SpellItem spell) {
        super(AncientMagicksEntities.FIREBALL.get(), level, owner, caster, spell);
    }

    @Override
    public int defaultPower() {
        return 4;
    }

    @Override
    public int defaultDie() {
        return 6;
    }

    @Override
    public float defaultSpeed() {
        return 1.2F;
    }

    @Override
    public float defaultSize() {
        return 0.8F;
    }

    @Override
    protected void doMobEffects(EntityHitResult result) {
        Entity target = result.getEntity();
        if ( this.getPower() > 0 && target instanceof LivingEntity ) {
            if ( this.entityData.get(ENEMY_PIERCE) <= 0 ) doSplashDamage(target);
            else causeDamage(target);
        }
    }

    private void causeDamage(Entity target) {
        if ( !target.fireImmune() ) {
            SpellItem.attackEntity(this.owner, target, this, calcDamage());
            if ( target instanceof LivingEntity ) target.setSecondsOnFire(8);
        }
    }

    @Override
    protected void doDeathEffects() {
        if ( this.getPower() > 0 ) {
            doSplashDamage(null);
            doSplashEffects();
        }
        this.discard();
    }

    private void doSplashDamage(@Nullable Entity hitTarget) {
        if ( this.level().isClientSide ) return;
        this.hitTarget = hitTarget;
        List<Entity> list = ShadowEvents.getEntitiesAround(this, this.level(), Math.max(0, this.getSize() + 1), this::isHit);
        if ( hitTarget != null ) {
            causeDamage(hitTarget);
            list.remove(hitTarget);
        }
        for ( Entity target : list ) causeDamage(target);
    }

    private Entity hitTarget = null;

    private boolean isHit(Entity owner, Entity target) {
        return target != this.hitTarget && checkTeamForHit(target);
    }

    private void doSplashEffects() {
        ServerLevel world = (ServerLevel)this.level();
        Vec3 pos = ShadowEvents.getEntityCenter(this);
        world.sendParticles(ParticleTypes.EXPLOSION, pos.x, this.getY(), pos.z,
                0, 0, 0, 0, 0);
        for ( int i = 0; i < 360; i++ ) {
            if ( i % (int)(6 / Math.max(1, this.getSize())) == 0 ) {
                float size = this.getSize() + 1;
                float randX = (float)((Math.random() * (size - (-size))) + (-size));
                float randY = (float)((Math.random() * (size - (-size))) + (-size));
                float randZ = (float)((Math.random() * (size - (-size))) + (-size));
                double randomValue = -2 + 4 * world.random.nextDouble();
                world.sendParticles(ParticleTypes.LARGE_SMOKE, pos.x + randX, this.getY() + randY, pos.z + randZ,
                        0, Math.cos(i), randomValue * 0.5D, Math.sin(i), 0.1D);
            }
        }

        fireExplosion();

        Vec3 center = ShadowEvents.getEntityCenter(this);
        this.level().playSound(null, center.x, center.y, center.z,
                SoundEvents.BLAZE_SHOOT, SoundSource.PLAYERS, 0.5F, 0.75F);
        this.level().playSound(null, center.x, center.y, center.z,
                SoundEvents.GENERIC_EXPLODE, SoundSource.PLAYERS, 1.0F, 0.75F);
    }

    private void fireExplosion() {
        Set<BlockPos> set = Sets.newHashSet();
        int i = 16;
        for ( int j = 0; j < 16; ++j ) {
            for ( int k = 0; k < 16; ++k ) {
                for ( int l = 0; l < 16; ++l ) {
                    if ( j == 0 || j == 15 || k == 0 || k == 15 || l == 0 || l == 15 ) {
                        double d0 = (double)((float)j / 15.0F * 2.0F - 1.0F);
                        double d1 = (double)((float)k / 15.0F * 2.0F - 1.0F);
                        double d2 = (double)((float)l / 15.0F * 2.0F - 1.0F);
                        double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
                        d0 /= d3;
                        d1 /= d3;
                        d2 /= d3;
                        float f = this.getSize() * (0.7F + this.level().random.nextFloat() * 0.6F);
                        double d4 = this.getX();
                        double d6 = this.getY();
                        double d8 = this.getZ();
                        for ( float f1 = 0.3F; f > 0.0F; f -= 0.22500001F ) {
                            BlockPos blockpos = BlockPos.containing(d4, d6, d8);
                            if ( !this.level().isInWorldBounds(blockpos) ) break;
                            set.add(blockpos);
                            d4 += d0 * (double)0.3F;
                            d6 += d1 * (double)0.3F;
                            d8 += d2 * (double)0.3F;
                        }
                    }
                }
            }
        }

        for ( BlockPos blockpos2 : set ) {
            if ( this.random.nextInt(3) == 0
                    && this.level().getBlockState(blockpos2).isAir()
                    && this.level().getBlockState(blockpos2.below()).isSolidRender(this.level(), blockpos2.below()) ) {
                this.level().setBlockAndUpdate(blockpos2, BaseFireBlock.getState(this.level(), blockpos2));
            }
        }
    }
}
