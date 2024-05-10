package net.mindoth.ancientmagicks.item.spellrune.abstractspell.summon.goal;

import net.mindoth.ancientmagicks.item.spellrune.abstractspell.summon.SummonerGetter;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.pathfinding.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IWorldReader;

import java.util.EnumSet;

public class GenericFollowSummonerGoal extends Goal {

    private final CreatureEntity entity;
    private LivingEntity summoner;
    private final IWorldReader level;
    private final double speedModifier;
    private final PathNavigator navigation;
    private int timeToRecalcPath;
    private final float stopDistance;
    private final float startDistance;
    private float oldWaterCost;
    private final boolean canFly;
    private final SummonerGetter summonerGetter;
    private final float teleportDistance;

    public GenericFollowSummonerGoal(CreatureEntity entity, SummonerGetter summonerGetter, double pSpeedModifier, float pStartDistance, float pStopDistance, boolean pCanFly, float teleportDistance) {
        this.entity = entity;
        this.summonerGetter = summonerGetter;
        this.level = entity.level;
        this.speedModifier = pSpeedModifier;
        this.navigation = entity.getNavigation();
        this.startDistance = pStartDistance;
        this.stopDistance = pStopDistance;
        this.canFly = pCanFly;
        this.teleportDistance = teleportDistance * teleportDistance;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        if (!(entity.getNavigation() instanceof GroundPathNavigator) && !(entity.getNavigation() instanceof FlyingPathNavigator)) {
            throw new IllegalArgumentException("Unsupported mob type for FollowSummonerGoal");
        }
    }

    /**
     * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
     * method as well.
     */
    public boolean canUse() {
        LivingEntity livingentity = this.summonerGetter.get();
        if (livingentity == null) {
            return false;
        } else if (livingentity.isSpectator()) {
            return false;
        } else if (this.entity.distanceToSqr(livingentity) < (double) (this.startDistance * this.startDistance)) {
            return false;
        } else {
            this.summoner = livingentity;
            return true;
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean canContinueToUse() {
        if (this.navigation.isDone()) {
            return false;
        } else {
            return !(this.entity.distanceToSqr(this.summoner) <= (double) (this.stopDistance * this.stopDistance));
        }
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void start() {
        this.timeToRecalcPath = 0;
        this.oldWaterCost = this.entity.getPathfindingMalus(PathNodeType.WATER);
        this.entity.setPathfindingMalus(PathNodeType.WATER, 0.0F);
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    public void stop() {
        this.summoner = null;
        this.navigation.stop();
        this.entity.setPathfindingMalus(PathNodeType.WATER, this.oldWaterCost);
    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    public void tick() {
        this.entity.getLookControl().setLookAt(this.summoner, 10.0F, (float) this.entity.getMaxHeadXRot());
        if (--this.timeToRecalcPath <= 0) {
            this.timeToRecalcPath = 10;
            if (!this.entity.isLeashed() && !this.entity.isPassenger()) {
                if (this.entity.distanceToSqr(this.summoner) >= teleportDistance) {
                    this.teleportToSummoner();
                } else {
                    if(canFly && !entity.isOnGround()){
                        Vector3d vec3 = summoner.position();
                        this.entity.getMoveControl().setWantedPosition(vec3.x, vec3.y + 2, vec3.z, this.speedModifier);

                    }else{
                        this.navigation.moveTo(this.summoner, this.speedModifier);

                    }
                }

            }
        }
    }

    private void teleportToSummoner() {
        BlockPos blockpos = this.summoner.blockPosition();

        for (int i = 0; i < 10; ++i) {
            int j = this.randomIntInclusive(-3, 3);
            int k = this.randomIntInclusive(-1, 1);
            int l = this.randomIntInclusive(-3, 3);
            boolean flag = this.maybeTeleportTo(blockpos.getX() + j, blockpos.getY() + k, blockpos.getZ() + l);
            if (flag) {
                return;
            }
        }

    }

    private boolean maybeTeleportTo(int pX, int pY, int pZ) {
        if (Math.abs((double) pX - this.summoner.getX()) < 2.0D && Math.abs((double) pZ - this.summoner.getZ()) < 2.0D) {
            return false;
        } else if (!this.canTeleportTo(new BlockPos(pX, pY, pZ))) {
            return false;
        } else {
            this.entity.moveTo((double) pX + 0.5D, (double) pY + (canFly && !entity.isOnGround() ? 3 : 0), (double) pZ + 0.5D, this.entity.yRot, this.entity.xRot);
            this.navigation.stop();
            return true;
        }
    }

    private boolean canTeleportTo(BlockPos pPos) {
        PathNodeType pathnodetype = WalkNodeProcessor.getBlockPathTypeStatic(this.level, pPos.mutable());
        if (pathnodetype != PathNodeType.WALKABLE) {
            return false;
        } else {
            BlockState blockstate = this.level.getBlockState(pPos.below());
            if (!this.canFly && blockstate.getBlock() instanceof LeavesBlock) {
                return false;
            } else {
                BlockPos blockpos = pPos.subtract(this.entity.blockPosition());
                return this.level.noCollision(this.entity, this.entity.getBoundingBox().move(blockpos));
            }
        }
    }

    private int randomIntInclusive(int pMin, int pMax) {
        return this.entity.getRandom().nextInt(pMax - pMin + 1) + pMin;
    }
}
