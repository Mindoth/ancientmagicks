package net.mindoth.ancientmagicks.item.spellrune.blackhole;

import net.mindoth.ancientmagicks.client.particle.ember.EmberParticleData;
import net.mindoth.ancientmagicks.item.spellrune.SpellRuneItem;
import net.mindoth.ancientmagicks.item.spellrune.abstractspell.AbstractSpellEntity;
import net.mindoth.ancientmagicks.registries.AncientMagicksEntities;
import net.mindoth.shadowizardlib.event.ShadowEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.FallingBlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.FMLPlayMessages;

public class BlackHoleEntity extends AbstractSpellEntity {

    public BlackHoleEntity(FMLPlayMessages.SpawnEntity spawnEntity, World level) {
        this(AncientMagicksEntities.BLACK_HOLE.get(), level);
    }

    public BlackHoleEntity(EntityType<BlackHoleEntity> entityType, World level) {
        super(entityType, level);
    }

    public BlackHoleEntity(World level, LivingEntity owner, Entity caster, SpellRuneItem rune) {
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
        Vector3d point = this.position();
        BlockPos pos = new BlockPos(point.x, point.y, point.z);
        int size = (int)this.size;
        for ( int xPos = pos.getX() - size; xPos <= pos.getX() + size; xPos++ ) {
            for ( int yPos = pos.getY() - size; yPos <= pos.getY() + size; yPos++ ) {
                for ( int zPos = pos.getZ() - size; zPos <= pos.getZ() + size; zPos++ ) {
                    BlockPos blockPos = new BlockPos(xPos, yPos, zPos);
                    BlockState blockState = this.level.getBlockState(blockPos);
                    if ( blockState.getBlock() != Blocks.BEDROCK && blockState.getMaterial().isSolid() ) {
                        FallingBlockEntity fallingBlock = new FallingBlockEntity(this.level, blockPos.getX() + 0.5D, blockPos.getY(), blockPos.getZ() + 0.5D, blockState);
                        fallingBlock.time = 1;
                        this.level.removeBlock(blockPos, false);
                        this.level.addFreshEntity(fallingBlock);
                    }
                }
            }
        }
        for ( Entity target : ShadowEvents.getEntitiesAround(this, this.size, this.size, this.size) ) {
            if ( SpellRuneItem.isPushable(target) && !(target instanceof LivingEntity && isAlly((LivingEntity)target)) ) {
                target.push((point.x - target.getX()) / 6, (point.y - target.getY()) / 6, (point.z - target.getZ()) / 6);
                target.hurtMarked = true;
                if ( target.getBoundingBox().intersects(this.getBoundingBox().inflate(this.size, this.size, this.size)) && !(target instanceof LivingEntity) ) target.remove();
            }
        }
    }

    @Override
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
            world.addParticle(EmberParticleData.createData(getParticleColor(), this.entityData.get(SIZE), 1, false, false), true,
                    pos.x + randX, pos.y + randY, pos.z + randZ, 0, 0, 0);
        }
    }
}
