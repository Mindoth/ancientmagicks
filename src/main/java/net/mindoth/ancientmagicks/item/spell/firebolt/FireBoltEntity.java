package net.mindoth.ancientmagicks.item.spell.firebolt;

import net.mindoth.ancientmagicks.item.SpellItem;
import net.mindoth.ancientmagicks.item.spell.abstractspell.AbstractSpellEntity;
import net.mindoth.ancientmagicks.registries.AncientMagicksEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.CandleCakeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.network.PlayMessages;

public class FireBoltEntity extends AbstractSpellEntity {

    public FireBoltEntity(PlayMessages.SpawnEntity spawnEntity, Level level) {
        this(AncientMagicksEntities.FIRE_BOLT.get(), level);
    }

    public FireBoltEntity(EntityType<FireBoltEntity> entityType, Level level) {
        super(entityType, level);
    }

    public FireBoltEntity(Level level, LivingEntity owner, Entity caster, SpellItem spell) {
        super(AncientMagicksEntities.FIRE_BOLT.get(), level, owner, caster, spell);
    }

    @Override
    public int defaultDie() {
        return 10;
    }

    @Override
    protected void doMobEffects(EntityHitResult result) {
        Entity target = result.getEntity();
        if ( !target.fireImmune() ) {
            SpellItem.attackEntity(this.owner, target, this, calcDamage());
            if ( target instanceof LivingEntity && this.random.nextBoolean() ) target.setSecondsOnFire(8);
        }
    }

    @Override
    protected void doBlockEffects(BlockHitResult result) {
        Direction face = result.getDirection();
        BlockPos pos = result.getBlockPos();
        BlockState state = level().getBlockState(pos);
        BlockPos facePos = getPosOfFace(result.getBlockPos(), face);
        if ( CampfireBlock.canLight(state) || CandleBlock.canLight(state) || CandleCakeBlock.canLight(state) ) {
            level().setBlock(pos, state.setValue(BlockStateProperties.LIT, Boolean.valueOf(true)), 11);
        }
        else if ( this.random.nextBoolean() && level().getBlockState(facePos).isAir() && level().getBlockState(facePos.below()).isSolidRender(level(), facePos.below()) ) {
            level().setBlockAndUpdate(facePos, BaseFireBlock.getState(level(), facePos));
        }
    }

    private static BlockPos getPosOfFace(BlockPos blockPos, Direction face) {
        return switch (face) {
            case UP -> blockPos.above();
            case EAST -> blockPos.east();
            case WEST -> blockPos.west();
            case SOUTH -> blockPos.south();
            case NORTH -> blockPos.north();
            case DOWN -> blockPos.below();
        };
    }
}
