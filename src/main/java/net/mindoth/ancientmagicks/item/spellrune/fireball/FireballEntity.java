package net.mindoth.ancientmagicks.item.spellrune.fireball;

import com.google.common.collect.Lists;
import net.mindoth.ancientmagicks.item.spellrune.SpellRuneItem;
import net.mindoth.ancientmagicks.item.spellrune.abstractspell.AbstractSpellEntity;
import net.mindoth.ancientmagicks.registries.AncientMagicksEntities;
import net.mindoth.shadowizardlib.event.ShadowEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.FMLPlayMessages;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class FireballEntity extends AbstractSpellEntity {

    public FireballEntity(FMLPlayMessages.SpawnEntity spawnEntity, World level) {
        this(AncientMagicksEntities.FIREBALL.get(), level);
    }

    public FireballEntity(EntityType<FireballEntity> entityType, World level) {
        super(entityType, level);
    }

    public FireballEntity(World level, LivingEntity owner, Entity caster, SpellRuneItem rune) {
        super(AncientMagicksEntities.FIREBALL.get(), level, owner, caster, rune);
    }

    @Override
    public float getDefaultPower() {
        return 8.0F;
    }

    @Override
    public float getDefaultSpeed() {
        return 1.0F;
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
    protected void doMobEffects(EntityRayTraceResult result) {
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
        this.remove();
    }

    private void doSplashDamage(@Nullable LivingEntity hitTarget) {
        if ( this.level.isClientSide ) return;
        List<LivingEntity> exceptions = Lists.newArrayList();
        if ( hitTarget != null ) {
            dealDamage(hitTarget);
            hitTarget.setSecondsOnFire(8);
            exceptions.add(hitTarget);
        }
        ArrayList<LivingEntity> list = ShadowEvents.getEntitiesAround(this, this.level, Math.max(0, this.size), exceptions);
        for ( LivingEntity target : list ) {
            if ( !isAlly(target) ) {
                dealDamage(target);
                target.setSecondsOnFire(8);
            }
        }
    }

    private void doSplashEffects() {
        ServerWorld world = (ServerWorld)this.level;
        Vector3d pos = ShadowEvents.getEntityCenter(this);
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
        Vector3d center = ShadowEvents.getEntityCenter(this);
        this.level.playSound(null, center.x, center.y, center.z,
                SoundEvents.BLAZE_SHOOT, SoundCategory.PLAYERS, 0.5F, 0.75F);
        this.level.playSound(null, center.x, center.y, center.z,
                SoundEvents.GENERIC_EXPLODE, SoundCategory.PLAYERS, 1.0F, 0.75F);
    }
}
