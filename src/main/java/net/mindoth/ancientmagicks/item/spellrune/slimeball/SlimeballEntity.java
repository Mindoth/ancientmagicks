package net.mindoth.ancientmagicks.item.spellrune.slimeball;

import net.mindoth.ancientmagicks.item.spellrune.SpellRuneItem;
import net.mindoth.ancientmagicks.item.spellrune.abstractspell.AbstractSpellEntity;
import net.mindoth.ancientmagicks.registries.AncientMagicksEntities;
import net.mindoth.shadowizardlib.event.ShadowEvents;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.FMLPlayMessages;

public class SlimeballEntity extends AbstractSpellEntity {

    public SlimeballEntity(FMLPlayMessages.SpawnEntity spawnEntity, World level) {
        this(AncientMagicksEntities.SLIMEBALL.get(), level);
    }

    public SlimeballEntity(EntityType<SlimeballEntity> entityType, World level) {
        super(entityType, level);
    }

    public SlimeballEntity(World level, LivingEntity owner, Entity caster, SpellRuneItem rune) {
        super(AncientMagicksEntities.SLIMEBALL.get(), level, owner, caster, rune);
    }

    @Override
    protected float getGravity() {
        return 0.01F;
    }

    @Override
    public float getDefaultPower() {
        return 4.0F;
    }

    @Override
    public float getDefaultSpeed() {
        return 0.8F;
    }

    @Override
    public float getDefaultLife() {
        return 60.0F;
    }

    @Override
    public int getDefaultBounce() {
        return 3;
    }

    @Override
    protected void doMobEffects(EntityRayTraceResult result) {
        if ( this.power > 0 && !isAlly((LivingEntity)result.getEntity()) ) {
            dealDamage((LivingEntity)result.getEntity());
        }
    }

    @Override
    protected void doClientTickEffects() {
        if ( !this.level.isClientSide ) return;
        ClientWorld world = (ClientWorld)this.level;
        Vector3d center = ShadowEvents.getEntityCenter(this);
        Vector3d pos = new Vector3d(center.x, this.getY(), center.z);

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
    protected void doBlockEffects(BlockRayTraceResult result) {
        if ( this.blockPierce == 0 ) particleSplash();
    }

    @Override
    protected void doDeathEffects() {
        particleSplash();
        this.remove();
    }

    protected void particleSplash() {
        ServerWorld world = (ServerWorld)this.level;
        Vector3d center = ShadowEvents.getEntityCenter(this);
        Vector3d pos = new Vector3d(center.x, this.getY(), center.z);
        for ( int r = 0; r < 360; r++ ) {
            if ( r % 60 == 0 ) {
                world.sendParticles(ParticleTypes.ITEM_SLIME, pos.x, pos.y, pos.z,
                        0, Math.cos(r),1, Math.sin(r), 0);
            }
        }
    }
}
