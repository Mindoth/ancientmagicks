package net.mindoth.ancientmagicks.item.glyph;

import net.mindoth.ancientmagicks.entity.projectile.AbstractSpell;
import net.mindoth.ancientmagicks.item.GlyphItem;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
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

import javax.annotation.Nullable;

public class FireGlyph extends GlyphItem {
    public FireGlyph(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void onEntityHit(@Nullable AbstractSpell spell, LivingEntity owner, EntityHitResult result) {
        Entity target = result.getEntity();
        if ( target instanceof LivingEntity living && isAlly(owner, living) ) return;
        if ( !target.fireImmune() ) target.setSecondsOnFire(8);
    }

    @Override
    public void onBlockHit(@Nullable AbstractSpell spell, LivingEntity owner, BlockHitResult result) {
        Level level = spell != null ? spell.level() : owner.level();
        BlockPos pos = result.getBlockPos();
        BlockState state = level.getBlockState(pos);
        BlockPos side = pos.relative(result.getDirection());
        if ( !CampfireBlock.canLight(state) && !CandleBlock.canLight(state) && !CandleCakeBlock.canLight(state) ) {
            if ( BaseFireBlock.canBePlacedAt(level, side, result.getDirection()) ) {
                level.playSound(null, side, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.4F + 0.8F);
                BlockState sideState = BaseFireBlock.getState(level, side);
                level.setBlock(side, sideState, 11);
            }
        }
        else {
            level.playSound(null, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.4F + 0.8F);
            level.setBlock(pos, state.setValue(BlockStateProperties.LIT, true), 11);
        }
    }
}
