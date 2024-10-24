package net.mindoth.ancientmagicks.item.spell.thunderball;

import com.google.common.collect.Lists;
import net.mindoth.ancientmagicks.client.particle.ember.EmberParticleProvider;
import net.mindoth.ancientmagicks.item.SpellItem;
import net.mindoth.ancientmagicks.item.spell.abstractspell.AbstractSpellEntity;
import net.mindoth.ancientmagicks.registries.AncientMagicksEntities;
import net.mindoth.shadowizardlib.event.ShadowEvents;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PlayMessages;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ThunderballEntity extends AbstractSpellEntity {

    public ThunderballEntity(PlayMessages.SpawnEntity spawnEntity, Level level) {
        this(AncientMagicksEntities.THUNDERBALL.get(), level);
    }

    public ThunderballEntity(EntityType<ThunderballEntity> entityType, Level level) {
        super(entityType, level);
    }

    public ThunderballEntity(Level level, LivingEntity owner, Entity caster, SpellItem rune) {
        super(AncientMagicksEntities.THUNDERBALL.get(), level, owner, caster, rune);
        this.setNoGravity(false);
    }

    @Override
    protected float getGravity() {
        return 0.03F;
    }

    @Override
    public float getDefaultPower() {
        return 24.0F;
    }

    @Override
    public float getDefaultSpeed() {
        return 1.2F;
    }

    @Override
    public float getDefaultSize() {
        return 0.8F;
    }

    private void causeDamage(LivingEntity target) {
        if ( !isAlly(target) ) {
            dealDamage(target, 8.0F);
        }
    }

    @Override
    protected void doMobEffects(EntityHitResult result) {
        LivingEntity target = (LivingEntity)result.getEntity();
        if ( this.power > 0 ) {
            if ( this.enemyPierce == 0 ) doSplashDamage(target);
            else causeDamage(target);
        }
    }

    @Override
    protected void doDeathEffects() {
        if ( this.power > 0 ) {
            doSplashDamage(null);
            doSplashEffects();
        }
        this.discard();
    }

    private void doSplashDamage(@Nullable LivingEntity hitTarget) {
        if ( this.level().isClientSide ) return;
        List<LivingEntity> exceptions = Lists.newArrayList();
        if ( hitTarget != null ) {
            causeDamage(hitTarget);
            exceptions.add(hitTarget);
        }
        ArrayList<LivingEntity> list = ShadowEvents.getEntitiesAround(this, this.level(), Math.max(0, this.size + 1), exceptions);
        for ( LivingEntity target : list ) {
            causeDamage(target);
        }
    }

    private void doSplashEffects() {
        ServerLevel world = (ServerLevel)this.level();
        Vec3 pos = ShadowEvents.getEntityCenter(this);
        world.sendParticles(ParticleTypes.EXPLOSION, pos.x, this.getY(), pos.z,
                0, 0, 0, 0, 0);
        for ( int i = 0; i < 360; i++ ) {
            if ( i % (int)(6 / Math.max(1, this.size)) == 0 ) {
                float size = this.size + 1;
                float randX = (float)((Math.random() * (size - (-size))) + (-size));
                float randY = (float)((Math.random() * (size - (-size))) + (-size));
                float randZ = (float)((Math.random() * (size - (-size))) + (-size));
                double randomValue = -2 + 4 * world.random.nextDouble();
                world.sendParticles(ParticleTypes.LARGE_SMOKE, pos.x, this.getY(), pos.z,
                        0, Math.cos(i), randomValue * 0.5D, Math.sin(i), 0.25D);
            }
        }
        Vec3 center = ShadowEvents.getEntityCenter(this);
        this.level().playSound(null, center.x, center.y, center.z,
                SoundEvents.LIGHTNING_BOLT_IMPACT, SoundSource.PLAYERS, 1.0F, 1.0F);
    }

    @Override

    protected void doClientTickEffects() {
        if ( this.isRemoved() ) return;
        if ( !this.level().isClientSide ) return;
        ClientLevel world = (ClientLevel)this.level();
        Vec3 center = ShadowEvents.getEntityCenter(this);
        Vec3 pos = new Vec3(center.x, this.getY(), center.z);

        if ( this.isRemoved() ) return;
        //Main body
        float particleSize = Math.min(this.entityData.get(SIZE), (this.entityData.get(SIZE) * 0.1F) * this.tickCount);
        for ( int i = 0; i < 4; i++ ) {
            float sphereSize = this.entityData.get(SIZE) / 4;
            float randX = (float)((Math.random() * (sphereSize - (-sphereSize))) + (-sphereSize));
            float randY = (float)((Math.random() * (sphereSize - (-sphereSize))) + (-sphereSize));
            float randZ = (float)((Math.random() * (sphereSize - (-sphereSize))) + (-sphereSize));
            world.addParticle(EmberParticleProvider.createData(getParticleColor(), particleSize, 1, false, true), true,
                    pos.x + randX, pos.y + randY, pos.z + randZ, 0, 0, 0);
        }
    }
}
