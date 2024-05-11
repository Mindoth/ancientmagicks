package net.mindoth.ancientmagicks.item.spellrune.tornado;

import net.mindoth.ancientmagicks.item.RuneItem;
import net.mindoth.ancientmagicks.item.spellrune.SpellRuneItem;
import net.mindoth.ancientmagicks.item.spellrune.abstractspell.AbstractSpellEntity;
import net.mindoth.ancientmagicks.registries.AncientMagicksEntities;
import net.mindoth.shadowizardlib.event.ShadowEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.FMLPlayMessages;

import java.util.List;

public class TornadoEntity extends AbstractSpellEntity {

    public TornadoEntity(FMLPlayMessages.SpawnEntity spawnEntity, World level) {
        this(AncientMagicksEntities.TORNADO.get(), level);
    }

    public TornadoEntity(EntityType<TornadoEntity> entityType, World level) {
        super(entityType, level);
    }

    public TornadoEntity(World level, LivingEntity owner, Entity caster, SpellRuneItem rune) {
        super(AncientMagicksEntities.TORNADO.get(), level, owner, caster, rune);
    }

    @Override
    public float getDefaultPower() {
        return 1.0F;
    }

    @Override
    public float getDefaultSpeed() {
        return 0.0F;
    }

    @Override
    public float getDefaultLife() {
        return 100.0F;
    }

    @Override
    public float getDefaultSize() {
        return 3.0F;
    }

    @Override
    public int getDefaultEnemyPierce() {
        return Integer.MAX_VALUE;
    }

    @Override
    protected void doMobEffects(EntityRayTraceResult result) {
    }

    @Override
    protected void doTickEffects() {
        Vector3d center = this.position();
        int tickCount = this.tickCount;
        if ( tickCount % 5 == 0 ) RuneItem.playWindSound(this.level, center);
        List<Entity> targets = ShadowEvents.getEntitiesAround(this, this.size, this.size, this.size);
        for ( Entity target : targets ) {
            if ( SpellRuneItem.isPushable(target) ) {
                //if ( target instanceof LivingEntity && !isAlly((LivingEntity)target) ) dealDamage((LivingEntity)target.getEntity());
                target.push((center.x - target.getX()) / 6, (center.y - target.getY()) / 6, (center.z - target.getZ()) / 6);
            }
        }
        /*int lines = 6;
        int angle = this.tickCount * lines;
        int maxHeight = (int)this.size;
        double maxRadius = this.size * 0.5D;
        double heightIncreasement = 0.5D;
        double radiusIncreasement = maxRadius / maxHeight * 0.5D;
        for ( int l = 0; l < lines; l++ ) {
            for ( double y = 0; y < maxHeight; y += heightIncreasement ) {
                double radius = y * radiusIncreasement + 1;
                double x = (Math.cos(Math.toRadians((double)360 / lines * l + y * 25 - angle)) * radius);
                double z = (Math.sin(Math.toRadians((double)360 / lines * l + y * 25 - angle)) * radius);
                ServerWorld level = (ServerWorld)this.level;
                level.sendParticles(new BlockParticleData(ParticleTypes.BLOCK, blockUnder(this)), posX + x, posY + y, posZ + z,
                        0, this.getDeltaMovement().x, 10, this.getDeltaMovement().z, 1);
            }
        }*/
        double posX = center.x;
        double posY = center.y - 1;
        double posZ = center.z;
        double height = this.size;
        for ( int y = 0; y < height; y++ ) {
            double radius = 0.5D + y * 0.5D;
            int lines = 4 + y;
            double angle = tickCount * lines;
            for ( int l = 0; l < lines; l++ ) {
                double x = (Math.cos(Math.toRadians((double)360 / lines * l * 25 - angle)) * radius);
                double z = (Math.sin(Math.toRadians((double)360 / lines * l * 25 - angle)) * radius);
                ServerWorld level = (ServerWorld)this.level;
                level.sendParticles(ParticleTypes.FLAME, posX + x, posY + y, posZ + z,
                        0, this.getDeltaMovement().x, -10, this.getDeltaMovement().z, 1);
            }
        }
    }

    @Override
    protected void doClientTickEffects() {
    }

    private BlockState blockUnder(Entity tornado) {
        BlockPos tornadoPos = getOnTornadoPos();
        World level = tornado.level;
        BlockState returnState = Blocks.WHITE_WOOL.defaultBlockState();
        for ( int i = tornadoPos.getY(); i > 0; i-- ) {
            BlockPos particleBlockPost = new BlockPos(tornadoPos.getX(), i, tornadoPos.getZ());
            if ( level.getBlockState(particleBlockPost).getMaterial().isSolid() || level.getBlockState(particleBlockPost).getMaterial().isLiquid() ) {
                returnState = level.getBlockState(particleBlockPost).getBlock().defaultBlockState();
                break;
            }
        }
        return returnState;
    }

    protected BlockPos getOnTornadoPos() {
        int i = MathHelper.floor(this.position().x);
        int j = MathHelper.floor(this.position().y - (double)0.2F);
        int k = MathHelper.floor(this.position().z);
        BlockPos blockpos = new BlockPos(i, j, k);
        if (this.level.isEmptyBlock(blockpos)) {
            BlockPos blockpos1 = blockpos.below();
            BlockState blockstate = this.level.getBlockState(blockpos1);
            if (blockstate.collisionExtendsVertically(this.level, blockpos1, this)) {
                return blockpos1;
            }
        }
        return blockpos;
    }
}
