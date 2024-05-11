package net.mindoth.ancientmagicks.item.spellrune.collapse;

import net.mindoth.ancientmagicks.item.modifierrune.ModifierRuneItem;
import net.mindoth.ancientmagicks.item.spellrune.SpellRuneItem;
import net.mindoth.shadowizardlib.event.ShadowEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FallingBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.FallingBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.List;

public class CollapseRune extends SpellRuneItem {

    public CollapseRune(Properties pProperties, int cooldown) {
        super(pProperties, cooldown);
    }

    @Override
    public void shootMagic(PlayerEntity owner, Entity caster, Vector3d center, float xRot, float yRot, int useTime, List<ModifierRuneItem> modifierList) {
        World level = caster.level;
        Vector3d casterPos = caster.getEyePosition(1.0F);
        playMagicSummonSound(level, casterPos);
        HashMap<String, Float> valueMap = new HashMap<>();

        valueMap.put("size", 1.0F);
        for ( ModifierRuneItem rune : modifierList ) rune.addModifiersToValues(valueMap);

        if ( valueMap.get("size") < 0 ) valueMap.put("size", 1.0F);
        float sizeF = valueMap.get("size");
        int size = (int)sizeF;
        float range = 3.5F + (float)casterPos.distanceTo(center);

        Vector3d point = getBlockPoint(caster, range, 0, caster == owner);
        BlockPos pos = new BlockPos(point.x, point.y, point.z);
        for ( int xPos = pos.getX() - size; xPos <= pos.getX() + size; xPos++ ) {
            for ( int yPos = pos.getY() - size; yPos <= pos.getY() + size; yPos++ ) {
                for ( int zPos = pos.getZ() - size; zPos <= pos.getZ() + size; zPos++ ) {
                    BlockPos blockPos = new BlockPos(xPos, yPos, zPos);
                    BlockState blockState = level.getBlockState(blockPos);
                    if ( blockState.getBlock() != Blocks.BEDROCK && blockState.getMaterial().isSolid()
                            && (level.isEmptyBlock(blockPos.below()) || FallingBlock.isFree(level.getBlockState(blockPos.below()))) ) {
                        FallingBlockEntity fallingBlock = new FallingBlockEntity(level, blockPos.getX() + 0.5D, blockPos.getY(), blockPos.getZ() + 0.5D, blockState);
                        fallingBlock.time = 1;
                        level.removeBlock(blockPos, false);
                        level.addFreshEntity(fallingBlock);
                    }
                }
            }
        }
    }

    private static Vector3d getBlockPoint(Entity caster, float range, float error, boolean isPlayer) {
        int adjuster = 1;
        if ( !isPlayer ) adjuster = -1;

        Vector3d direction = ShadowEvents.calculateViewVector(caster.xRot * (float)adjuster, caster.yRot * (float)adjuster).normalize();
        direction = direction.multiply(range, range, range);
        Vector3d center = caster.getEyePosition(1.0F).add(direction);
        Vector3d returnPoint = center;
        double playerX = ShadowEvents.getEntityCenter(caster).x;
        double playerY = caster.getEyePosition(1.0F).y;
        double playerZ = ShadowEvents.getEntityCenter(caster).z;
        double listedEntityX = center.x();
        double listedEntityY = center.y();
        double listedEntityZ = center.z();
        int particleInterval = (int)Math.round(caster.distanceToSqr(center));

        for ( int k = 1; k < 1 + particleInterval; ++k ) {
            double lineX = playerX * (1.0 - (double)k / (double)particleInterval) + listedEntityX * ((double)k / (double)particleInterval);
            double lineY = playerY * (1.0 - (double)k / (double)particleInterval) + listedEntityY * ((double)k / (double)particleInterval);
            double lineZ = playerZ * (1.0 - (double)k / (double)particleInterval) + listedEntityZ * ((double)k / (double)particleInterval);

            returnPoint = new Vector3d(lineX - (double)error, lineY - (double)error, lineZ - (double)error);
            if ( caster.level.getBlockState(new BlockPos(lineX, lineY, lineZ)).getMaterial().isSolid() ) break;
        }
        return returnPoint;
    }
}