package net.mindoth.ancientmagicks.item.spell.freezelance;

import net.mindoth.ancientmagicks.item.spell.abstractspell.SpellItem;
import net.mindoth.ancientmagicks.item.spell.abstractspell.AbstractSpellEntity;
import net.mindoth.ancientmagicks.registries.AncientMagicksEntities;
import net.mindoth.shadowizardlib.event.ShadowEvents;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PlayMessages;

public class FreezeLanceEntity extends AbstractSpellEntity {

    public FreezeLanceEntity(PlayMessages.SpawnEntity spawnEntity, Level level) {
        this(AncientMagicksEntities.FREEZE_LANCE.get(), level);
    }

    public FreezeLanceEntity(EntityType<FreezeLanceEntity> entityType, Level level) {
        super(entityType, level);
    }

    public FreezeLanceEntity(Level level, LivingEntity owner, Entity caster, SpellItem rune) {
        super(AncientMagicksEntities.FREEZE_LANCE.get(), level, owner, caster, rune);
        this.setNoGravity(false);
    }

    @Override
    public float getDefaultPower() {
        return 24.0F;
    }

    @Override
    public float getDefaultSpeed() {
        return 1.6F;
    }

    @Override
    public int getDefaultEnemyPierce() {
        return 1;
    }

    @Override
    protected void doMobEffects(EntityHitResult result) {
        if ( this.getPower() > 0 && !SpellItem.isAlly(this.owner, (LivingEntity)result.getEntity()) ) {
            LivingEntity target = (LivingEntity)result.getEntity();
            SpellItem.attackEntity(this.owner, target, this, SpellItem.getPowerInRange(4.0F, this.getPower()));
            if ( target.getTicksFrozen() < 1440 ) target.setTicksFrozen(1440);
            spawnParticles();
        }
    }

    @Override
    protected void doBlockEffects(BlockHitResult result) {
        spawnParticles();
    }

    protected void spawnParticles() {
        if ( this.level().isClientSide ) return;
        ServerLevel world = (ServerLevel)this.level();
        Vec3 center = ShadowEvents.getEntityCenter(this);
        for ( int i = 0; i < 4; i++ ) {
            float size = 0.1F;
            float randX = (float)((Math.random() * (size - (-size))) + (-size));
            float randY = (float)((Math.random() * (size - (-size))) + (-size));
            float randZ = (float)((Math.random() * (size - (-size))) + (-size));
            world.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.PACKED_ICE.defaultBlockState()), center.x, center.y, center.z, 0, randX, randY, randZ, 0.1F);
        }
    }

    @Override
    protected void playHitSound() {
        Vec3 center = ShadowEvents.getEntityCenter(this);
        this.level().playSound(null, center.x, center.y, center.z,
                SoundEvents.GLASS_BREAK, SoundSource.PLAYERS, 1.0F, 1.0F);
    }

    @Override
    protected void doClientTickEffects() {
        if ( !this.level().isClientSide ) return;
        ClientLevel world = (ClientLevel)this.level();
        Vec3 center = ShadowEvents.getEntityCenter(this);
        Vec3 pos = new Vec3(center.x, this.getY(), center.z);

        //Trail twinkle
        if ( this.tickCount % 4 == 0 ) {
            float size = this.entityData.get(SIZE) / 3;
            float randX = (float)((Math.random() * (size - (-size))) + (-size));
            float randY = (float)((Math.random() * (size - (-size))) + (-size));
            float randZ = (float)((Math.random() * (size - (-size))) + (-size));
            world.addParticle(ParticleTypes.SNOWFLAKE, pos.x + randX, pos.y + randY, pos.z + randZ, 0, 0, 0);
        }
    }
}
