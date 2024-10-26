package net.mindoth.ancientmagicks.item.spell.slimeball;

import net.mindoth.ancientmagicks.item.SpellItem;
import net.mindoth.ancientmagicks.item.spell.abstractspell.AbstractSpellEntity;
import net.mindoth.ancientmagicks.registries.AncientMagicksEntities;
import net.mindoth.shadowizardlib.event.ShadowEvents;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PlayMessages;

public class SlimeballEntity extends AbstractSpellEntity {

    public SlimeballEntity(PlayMessages.SpawnEntity spawnEntity, Level level) {
        this(AncientMagicksEntities.SLIMEBALL.get(), level);
    }

    public SlimeballEntity(EntityType<SlimeballEntity> entityType, Level level) {
        super(entityType, level);
    }

    public SlimeballEntity(Level level, LivingEntity owner, Entity caster, SpellItem rune) {
        super(AncientMagicksEntities.SLIMEBALL.get(), level, owner, caster, rune);
        this.setNoGravity(false);
    }

    @Override
    public float getDefaultPower() {
        return 6.0F;
    }

    @Override
    public float getDefaultSpeed() {
        return 1.2F;
    }

    @Override
    public float getDefaultSize() {
        return 0.8F;
    }

    @Override
    public int getDefaultBounce() {
        return Integer.MAX_VALUE;
    }

    @Override
    protected void doMobEffects(EntityHitResult result) {
        if ( this.power > 0 && !SpellItem.isAlly(this.owner, (LivingEntity)result.getEntity()) ) {
            LivingEntity target = (LivingEntity)result.getEntity();
            SpellItem.attackEntity(this.owner, target, this, this.power, 2.0F);
        }
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
            world.addParticle(ParticleTypes.ITEM_SLIME, pos.x + randX, pos.y + randY, pos.z + randZ, 0, 0, 0);
        }
    }

    @Override
    protected void doBlockEffects(BlockHitResult result) {
        if ( this.blockPierce == 0 ) particleSplash();
    }

    @Override
    protected void doDeathEffects() {
        particleSplash();
        this.discard();
    }

    protected void particleSplash() {
        ServerLevel world = (ServerLevel)this.level();
        Vec3 center = ShadowEvents.getEntityCenter(this);
        Vec3 pos = new Vec3(center.x, this.getY(), center.z);
        for ( int r = 0; r < 360; r++ ) {
            if ( r % 60 == 0 ) {
                world.sendParticles(ParticleTypes.ITEM_SLIME, pos.x, pos.y, pos.z,
                        0, Math.cos(r),1, Math.sin(r), 0);
            }
        }
    }
}
