package net.mindoth.ancientmagicks.item.spellrune.fireball;

import com.google.common.collect.Lists;
import net.mindoth.ancientmagicks.item.spellrune.SpellRuneItem;
import net.mindoth.ancientmagicks.item.spellrune.abstractspell.AbstractSpellEntity;
import net.mindoth.ancientmagicks.registries.AncientMagicksEntities;
import net.mindoth.shadowizardlib.event.ShadowEvents;
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

public class FireballEntity extends AbstractSpellEntity {

    public FireballEntity(PlayMessages.SpawnEntity spawnEntity, Level level) {
        this(AncientMagicksEntities.FIREBALL.get(), level);
    }

    public FireballEntity(EntityType<FireballEntity> entityType, Level level) {
        super(entityType, level);
    }

    public FireballEntity(Level level, LivingEntity owner, Entity caster, SpellRuneItem rune) {
        super(AncientMagicksEntities.FIREBALL.get(), level, owner, caster, rune);
    }

    @Override
    public float getDefaultPower() {
        return 8.0F;
    }

    @Override
    public float getDefaultSpeed() {
        return 0.8F;
    }

    @Override
    public float getDefaultLife() {
        return 40.0F;
    }

    @Override
    public float getDefaultSize() {
        return 0.8F;
    }

    @Override
    protected void doMobEffects(EntityHitResult result) {
        LivingEntity target = (LivingEntity)result.getEntity();
        if ( this.power > 0 ) {
            if ( this.enemyPierce == 0 ) {
                doSplashDamage(target);
            }
            else {
                dealDamage(target);
                target.setSecondsOnFire(8);
            }
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
            dealDamage(hitTarget);
            hitTarget.setSecondsOnFire(8);
            exceptions.add(hitTarget);
        }
        ArrayList<LivingEntity> list = ShadowEvents.getEntitiesAround(this, this.level(), Math.max(0, this.size), exceptions);
        for ( LivingEntity target : list ) {
            if ( !isAlly(target) ) {
                dealDamage(target);
                target.setSecondsOnFire(8);
            }
        }
    }

    private void doSplashEffects() {
        ServerLevel world = (ServerLevel)this.level();
        Vec3 pos = ShadowEvents.getEntityCenter(this);
        for ( int i = 0; i < 360; i++ ) {
            if ( i % (int)(36 / Math.max(1, (this.size * 2))) == 0 ) {
                float size = this.size + 1;
                float randX = (float)((Math.random() * (size - (-size))) + (-size));
                float randY = (float)((Math.random() * (size - (-size))) + (-size));
                float randZ = (float)((Math.random() * (size - (-size))) + (-size));
                double randomValue = -2 + 4 * world.random.nextDouble();
                world.sendParticles(ParticleTypes.LAVA, pos.x + randX, this.getY() + randY, pos.z + randZ, 0, Math.cos(i), randomValue * 0.5D, Math.sin(i), 0);
            }
        }
        Vec3 center = ShadowEvents.getEntityCenter(this);
        this.level().playSound(null, center.x, center.y, center.z,
                SoundEvents.BLAZE_SHOOT, SoundSource.PLAYERS, 0.5F, 0.75F);
        this.level().playSound(null, center.x, center.y, center.z,
                SoundEvents.GENERIC_EXPLODE, SoundSource.PLAYERS, 1.0F, 0.75F);
    }
}
