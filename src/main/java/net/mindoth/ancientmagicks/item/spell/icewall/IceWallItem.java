package net.mindoth.ancientmagicks.item.spell.icewall;

import net.mindoth.ancientmagicks.client.particle.ember.ParticleColor;
import net.mindoth.ancientmagicks.item.spell.abstractspell.AbstractSpellEntity;
import net.mindoth.ancientmagicks.item.SpellItem;
import net.mindoth.shadowizardlib.event.ShadowEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class IceWallItem extends SpellItem {

    public IceWallItem(Properties pProperties, int spellTier, int manaCost, int cooldown, SpellType spellType) {
        super(pProperties, spellTier, manaCost, cooldown, spellType);
    }

    @Override
    public ParticleColor.IntWrapper getParticleColor() {
        return ColorCode.AQUA.getParticleColor();
    }

    @Override
    public boolean isChannel() {
        return true;
    }

    @Override
    public boolean castMagic(Player owner, Entity caster, Vec3 center, float xRot, float yRot, int useTime) {
        boolean state = false;
        Level level = caster.level();
        float down = -0.2F;
        if ( caster != owner ) {
            down = 0.0F;
        }

        float range = 3.5F;
        if ( owner != caster ) range = 0.0F;
        int height = 2;
        Block wallMaterial = Blocks.PACKED_ICE;

        Vec3 point = ShadowEvents.getPoint(level, caster, range, 0, caster == owner, false, false, false, false);
        Vec3 blockPoint = ShadowEvents.getPoint(level, caster, range, 0, caster == owner, false, false, true, false);
        BlockPos pos;
        if ( point == blockPoint ) pos = new BlockPos(Mth.floor(point.x), Mth.floor(point.y), Mth.floor(point.z));
        else pos = new BlockPos(Mth.floor(blockPoint.x), Mth.floor(blockPoint.y), Mth.floor(blockPoint.z));

        if ( checkHeight(level, pos, height) != null ) {
            state = true;
            BlockPos lookAtBlockPoint = ShadowEvents.getBlockPoint(caster, range, caster == owner);
            Block block = level.getBlockState(lookAtBlockPoint).getBlock();
            if ( block != wallMaterial ) {
                BlockPos placePos = checkHeight(level, pos, height);
                for ( int i = placePos.getY(); i < placePos.getY() + height; i++ ) {
                    BlockPos tempPos = new BlockPos(placePos.getX(), i, placePos.getZ());
                    BlockState tempState = level.getBlockState(tempPos);
                    if ( !tempState.isSolid() || tempState.canBeReplaced() ) {
                        level.setBlockAndUpdate(tempPos, wallMaterial.defaultBlockState());
                    }
                }
            }
        }

        if ( state ) {
            ServerLevel serverLevel = (ServerLevel)level;
            Vec3 lookVec = caster.getLookAngle();
            serverLevel.sendParticles(ParticleTypes.SNOWFLAKE, center.x, center.y + down, center.z, 0, lookVec.x, lookVec.y, lookVec.z, 0.5D);

            if ( useTime % 3 == 0 ) playWindSound(level, center);
        }

        return state;
    }

    private BlockPos checkHeight(Level level, BlockPos pos, int height) {
        BlockPos returnPos = null;
        for ( int i = pos.getY(); i > pos.getY() - height; i-- ) {
            BlockPos tempPos = new BlockPos(pos.getX(), i, pos.getZ()).below();
            BlockState tempState = level.getBlockState(tempPos);
            if ( tempState.isSolid() ) {
                returnPos = tempPos.above();
                break;
            }
        }
        return returnPos;
    }
}
