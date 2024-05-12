package net.mindoth.ancientmagicks.item.spellrune.blackhole;

import net.mindoth.ancientmagicks.client.particle.ember.EmberParticleProvider;
import net.mindoth.ancientmagicks.item.spellrune.SpellRuneItem;
import net.mindoth.ancientmagicks.item.spellrune.abstractspell.AbstractSpellEntity;
import net.mindoth.ancientmagicks.registries.AncientMagicksEntities;
import net.mindoth.shadowizardlib.event.ShadowEvents;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PlayMessages;
import net.minecraft.world.level.Level;

public class BlackHoleEntity extends AbstractSpellEntity {

    public BlackHoleEntity(PlayMessages.SpawnEntity spawnEntity, Level level) {
        this(AncientMagicksEntities.BLACK_HOLE.get(), level);
    }

    public BlackHoleEntity(EntityType<BlackHoleEntity> entityType, Level level) {
        super(entityType, level);
    }

    public BlackHoleEntity(Level level, LivingEntity owner, Entity caster, SpellRuneItem rune) {
        super(AncientMagicksEntities.BLACK_HOLE.get(), level, owner, caster, rune);
    }

    @Override
    protected float getGravity() {
        return 0.0F;
    }

    @Override
    public float getDefaultPower() {
        return 0.0F;
    }

    @Override
    public float getDefaultSpeed() {
        return 0.25F;
    }

    @Override
    public float getDefaultLife() {
        return 50.0F;
    }

    @Override
    public float getDefaultSize() {
        return 1.0F;
    }

    @Override
    public int getDefaultBlockPierce() {
        return Integer.MAX_VALUE;
    }

    @Override
    public int getDefaultEnemyPierce() {
        return Integer.MAX_VALUE;
    }

    @Override
    protected void doTickEffects() {
        Vec3 point = this.position();
        BlockPos pos = new BlockPos((int)point.x, (int)point.y, (int)point.z);
        int size = (int)this.size;
        for ( int xPos = pos.getX() - size; xPos <= pos.getX() + size; xPos++ ) {
            for ( int yPos = pos.getY() - size; yPos <= pos.getY() + size; yPos++ ) {
                for ( int zPos = pos.getZ() - size; zPos <= pos.getZ() + size; zPos++ ) {
                    BlockPos blockPos = new BlockPos(xPos, yPos, zPos);
                    BlockState blockState = this.level().getBlockState(blockPos);
                    if ( blockState.getBlock() != Blocks.BEDROCK && blockState.isSolid() ) {
                        FallingBlockEntity.fall(this.level(), blockPos, blockState);
                    }
                }
            }
        }
        for ( Entity target : ShadowEvents.getEntitiesAround(this, this.size, this.size, this.size) ) {
            if ( SpellRuneItem.isPushable(target) ) {
                target.push((point.x - target.getX()) / 6, (point.y - target.getY()) / 6, (point.z - target.getZ()) / 6);
                if ( target.getBoundingBox().intersects(this.getBoundingBox().inflate(this.size, this.size, this.size)) && !(target instanceof LivingEntity) ) target.discard();
            }
        }
    }

    @Override
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
            world.addParticle(EmberParticleProvider.createData(getParticleColor(), this.entityData.get(SIZE), 1, false, false), true,
                    pos.x + randX, pos.y + randY, pos.z + randZ, 0, 0, 0);
        }
    }
}
