package net.mindoth.ancientmagicks.item.glyph;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.CandleCakeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public class FireGlyphItem extends GlyphItem {
    public FireGlyphItem(Properties pProperties, String tag) {
        super(pProperties, tag);
    }

    @Override
    public void onEntityHit(Level level, EntityHitResult result) {
        Entity target = result.getEntity();
        if ( !target.fireImmune() ) target.setSecondsOnFire(8);
    }

    @Override
    public void onBlockHit(Level level, BlockHitResult result) {
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
