package net.mindoth.ancientmagicks.item.spell.collapse;

import net.mindoth.ancientmagicks.event.CommonEvents;
import net.mindoth.ancientmagicks.item.castingitem.TabletItem;
import net.mindoth.shadowizardlib.event.ShadowEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class CollapseTablet extends TabletItem {

    public CollapseTablet(Properties pProperties, int tier, boolean isChannel, int cooldown) {
        super(pProperties, tier, isChannel, cooldown);
    }

    @Override
    public boolean castMagic(Player owner, Entity caster, Vec3 center, float xRot, float yRot, int useTime) {
        boolean state = false;
        Level level = caster.level();

        int size = 4;
        float range = 14.0F;

        Vec3 returnPoint = CommonEvents.getBlockPoint(caster, range, caster == owner);
        BlockPos pos = new BlockPos(Mth.floor(returnPoint.x), Mth.floor(returnPoint.y), Mth.floor(returnPoint.z));
        for ( int xPos = pos.getX() - size; xPos <= pos.getX() + size; xPos++ ) {
            for ( int yPos = pos.getY() - size; yPos <= pos.getY() + size; yPos++ ) {
                for ( int zPos = pos.getZ() - size; zPos <= pos.getZ() + size; zPos++ ) {
                    BlockPos blockPos = new BlockPos(xPos, yPos, zPos);
                    BlockState blockState = level.getBlockState(blockPos);
                    if ( blockState.getBlock() != Blocks.BEDROCK && blockState.isSolid()
                            && (level.isEmptyBlock(blockPos.below()) || FallingBlock.isFree(level.getBlockState(blockPos.below()))) ) {
                        FallingBlockEntity.fall(level, blockPos, blockState);
                        state = true;
                    }
                }
            }
        }

        if ( state ) playMagicSummonSound(level, center);

        return state;
    }
}