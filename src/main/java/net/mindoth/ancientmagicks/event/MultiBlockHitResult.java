package net.mindoth.ancientmagicks.event;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class MultiBlockHitResult extends BlockHitResult {

    private final List<BlockPos> blocks;
    public List<BlockPos> getBlocks() {
        return this.blocks;
    }

    private final Level level;
    public Level getLevel() {
        return this.level;
    }

    //TODO: add dimension to hitresult
    public MultiBlockHitResult(Vec3 pLocation, Direction pDirection, BlockPos pBlockPos, boolean pInside, List<BlockPos> blocks, Level level) {
        super(pLocation, pDirection, pBlockPos, pInside);
        this.blocks = blocks;
        this.level = level;
    }
}
