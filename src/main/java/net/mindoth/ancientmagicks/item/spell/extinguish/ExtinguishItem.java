package net.mindoth.ancientmagicks.item.spell.extinguish;

import net.mindoth.ancientmagicks.item.SpellItem;
import net.mindoth.shadowizardlib.event.ShadowEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class ExtinguishItem extends SpellItem {

    public ExtinguishItem(Properties pProperties, int spellTier, int manaCost, int cooldown, SpellSchool spellSchool) {
        super(pProperties, spellTier, manaCost, cooldown, spellSchool);
    }

    @Override
    public boolean castMagic(Player owner, Entity caster, Vec3 center, float xRot, float yRot, int useTime) {
        boolean state = false;
        Level level = caster.level();

        int size = 3;
        int entitySize = Math.max(0, size - 1);

        List<Entity> list = ShadowEvents.getEntitiesAround(caster, entitySize, entitySize, entitySize);
        if ( caster instanceof LivingEntity living ) list.add(living);
        for ( Entity entity : list ) {
            if ( entity instanceof LivingEntity living && living.isOnFire() ) {
                living.extinguishFire();
                state = true;
            }
        }
        BlockPos pos = caster.blockPosition();
        for ( int xPos = pos.getX() - size; xPos <= pos.getX() + size; xPos++ ) {
            for ( int yPos = pos.getY() - size; yPos <= pos.getY() + size; yPos++ ) {
                for ( int zPos = pos.getZ() - size; zPos <= pos.getZ() + size; zPos++ ) {
                    BlockPos blockPos = new BlockPos(xPos, yPos, zPos);
                    BlockState blockState = level.getBlockState(blockPos);
                    if ( blockState.getBlock() instanceof BaseFireBlock ) {
                        level.removeBlock(blockPos, false);
                        state = true;
                    }
                }
            }
        }

        if ( state ) {
            level.playSound(null, center.x, center.y, center.z,
                    SoundEvents.FIRE_EXTINGUISH, SoundSource.PLAYERS, 0.25F, 1.0F);
        }

        return state;
    }
}
