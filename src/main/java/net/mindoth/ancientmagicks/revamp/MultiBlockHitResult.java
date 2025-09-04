package net.mindoth.ancientmagicks.revamp;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.compress.utils.Lists;

import java.util.List;

public class MultiBlockHitResult extends BlockHitResult {

    public MultiBlockHitResult(Vec3 pLocation, Direction pDirection, BlockPos pBlockPos, boolean pInside) {
        super(pLocation, pDirection, pBlockPos, pInside);
        List<BlockPos> tempList = Lists.newArrayList();
        tempList.add(pBlockPos);
        this.blocks = tempList;
    }

    private final List<BlockPos> blocks;

    public MultiBlockHitResult(Vec3 pLocation, Direction pDirection, BlockPos pBlockPos, boolean pInside, List<BlockPos> blocks) {
        super(pLocation, pDirection, pBlockPos, pInside);
        this.blocks = blocks;
    }

    public List<BlockPos> getBlocks() {
        return this.blocks;
    }
}
