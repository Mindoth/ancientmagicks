package net.mindoth.ancientmagicks.item.temp.createordestroywater;

import net.mindoth.ancientmagicks.item.SpellItem;
import net.mindoth.ancientmagicks.item.temp.abstractspell.spellpearl.SpellPearlEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class CreateOrDestroyWaterItem extends SpellItem {

    public CreateOrDestroyWaterItem(Properties pProperties, int spellTier, int manaCost, int cooldown, SpellType spellType) {
        super(pProperties, spellTier, manaCost, cooldown, spellType);
    }

    @Override
    public boolean castMagic(LivingEntity owner, Entity caster, Vec3 center, int useTime) {
        boolean state = false;
        Level level = caster.level();

        float range = 4.5F;
        if ( caster instanceof SpellPearlEntity ) range = 0.0F;

        BlockPos pos = caster.getOnPos();
        SoundEvent sound = SoundEvents.BUCKET_EMPTY;

        BlockHitResult blockhitresult = getPlayerPOVHitResult(level, caster, ClipContext.Fluid.SOURCE_ONLY, range);
        if ( blockhitresult.getType() == HitResult.Type.BLOCK ) {
            pos = blockhitresult.getBlockPos();
            Block block = level.getBlockState(pos).getBlock();
            if ( block == Blocks.WATER ) {
                sound = SoundEvents.BUCKET_FILL;
                level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
                state = true;
            }
            else {
                pos = getPosOfFace(pos, blockhitresult.getDirection());
                if ( level.getBlockState(pos).canBeReplaced(Fluids.WATER) ) {
                    level.destroyBlock(pos, true);
                    level.setBlockAndUpdate(pos, Blocks.WATER.defaultBlockState());
                    state = true;
                }
            }
        }

        if ( state ) {
            playMagicSound(level, pos.getCenter());
            level.playSound(null, pos.getCenter().x, pos.getCenter().y, pos.getCenter().z,
                    sound, SoundSource.PLAYERS, 1.0F, 1.0F);
        }

        return state;
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

    protected static BlockHitResult getPlayerPOVHitResult(Level pLevel, Entity pPlayer, ClipContext.Fluid pFluidMode, float range) {
        float f = pPlayer.getXRot();
        float f1 = pPlayer.getYRot();
        Vec3 vec3 = pPlayer.getEyePosition();
        float f2 = Mth.cos(-f1 * ((float)Math.PI / 180F) - (float)Math.PI);
        float f3 = Mth.sin(-f1 * ((float)Math.PI / 180F) - (float)Math.PI);
        float f4 = -Mth.cos(-f * ((float)Math.PI / 180F));
        float f5 = Mth.sin(-f * ((float)Math.PI / 180F));
        float f6 = f3 * f4;
        float f7 = f2 * f4;
        double d0 = range;
        Vec3 vec31 = vec3.add((double)f6 * d0, (double)f5 * d0, (double)f7 * d0);
        return pLevel.clip(new ClipContext(vec3, vec31, ClipContext.Block.OUTLINE, pFluidMode, pPlayer));
    }
}
