package net.mindoth.ancientmagicks.revamp.item.rune;

import com.google.common.collect.Lists;
import net.mindoth.ancientmagicks.item.effect.BlockTargetEffect;
import net.mindoth.ancientmagicks.item.effect.SpellEffectItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;

public class BlockTargetTemplate extends RuneItem {
    public BlockTargetTemplate(Properties pProperties) {
        super(pProperties);
    }

    protected boolean isInside() {
        return true;
    }

    protected void aoeBlockSpellParticles(Level level, List<BlockPos> blocks, HashMap<String, Float> stats) {
        BlockPos start = new BlockPos(blocks.get(0).getX(), blocks.get(0).getY(), blocks.get(0).getZ());
        BlockPos end = new BlockPos(blocks.get(blocks.size() - 1).getX() + 1, blocks.get(blocks.size() - 1).getY() + 1, blocks.get(blocks.size() - 1).getZ() + 1);
        AABB particleBox = new AABB(start, end);
        addAoeParticles(true, level, particleBox, 0.15F, 8, stats);
    }

    protected static @NotNull List<BlockPos> getBlockList(RuneItem component, BlockHitResult bResult, BlockPos pos, int range) {
        List<BlockPos> blocks = Lists.newArrayList();
        if ( component instanceof BlockTargetTemplate bte && !bte.isInside() ) pos = getPosOfFace(pos, bResult.getDirection());
        if ( bResult.getDirection() == Direction.UP || bResult.getDirection() == Direction.DOWN ) {
            for ( int xPos = pos.getX() - range; xPos <= pos.getX() + range; xPos++ ) {
                for ( int zPos = pos.getZ() - range; zPos <= pos.getZ() + range; zPos++ ) {
                    blocks.add(new BlockPos(xPos, pos.getY(), zPos));
                }
            }
        }
        else if ( bResult.getDirection() == Direction.NORTH || bResult.getDirection() == Direction.SOUTH ) {
            for ( int xPos = pos.getX() - range; xPos <= pos.getX() + range; xPos++ ) {
                for ( int yPos = pos.getY() - range; yPos <= pos.getY() + range; yPos++ ) {
                    blocks.add(new BlockPos(xPos, yPos, pos.getZ()));
                }
            }
        }
        else if ( bResult.getDirection() == Direction.EAST || bResult.getDirection() == Direction.WEST ) {
            for ( int yPos = pos.getY() - range; yPos <= pos.getY() + range; yPos++ ) {
                for ( int zPos = pos.getZ() - range; zPos <= pos.getZ() + range; zPos++ ) {
                    blocks.add(new BlockPos(pos.getX(), yPos, zPos));
                }
            }
        }
        if ( !blocks.contains(pos) ) blocks.add(pos);
        return blocks;
    }
}
