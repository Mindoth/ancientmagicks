package net.mindoth.ancientmagicks.item.spellrune.calllightning;

import net.mindoth.ancientmagicks.item.spellrune.SpellRuneItem;
import net.mindoth.shadowizardlib.event.ShadowEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class CallLightningSpell extends SpellRuneItem {

    public CallLightningSpell(Item.Properties pProperties, int cooldown) {
        super(pProperties, cooldown);
    }

    @Override
    public boolean castMagic(Player owner, Entity caster, Vec3 center, float xRot, float yRot, int useTime) {
        boolean state = false;
        Level level = caster.level();
        Vec3 point = ShadowEvents.getPoint(level, caster, 64.0F, 0, caster == owner, true, true, true);
        BlockPos blockPos = new BlockPos((int)point.x, (int)point.y, (int)point.z);
        if ( level.canSeeSky(blockPos) ) {
            if ( !level.getBlockState(blockPos.below()).isSolid() ) {
                point = getBlockBelow(level, blockPos);
            }
            LightningBolt lightningbolt = EntityType.LIGHTNING_BOLT.create(level);
            if ( lightningbolt != null && !level.isClientSide ) {
                lightningbolt.moveTo(point);
                lightningbolt.setCause(caster instanceof ServerPlayer ? (ServerPlayer)caster : null);
                level.addFreshEntity(lightningbolt);
                state = true;
            }
        }

        if ( state ) playLightningSummonSound(level, point);
        else playWhiffSound(level, center);

        return state;
    }

    private Vec3 getBlockBelow(Level level, BlockPos blockPos) {
        Vec3 tempVec = new Vec3(blockPos.getX(), blockPos.getY(), blockPos.getZ());
        for ( int i = blockPos.getY(); i > level.getMinBuildHeight(); i-- ) {
            BlockPos tempPost = new BlockPos(blockPos.getX(), i, blockPos.getZ());
            if ( level.getBlockState(tempPost.below()).isSolid() ) {
                tempVec = new Vec3(blockPos.getX(), i, blockPos.getZ());
                break;
            }
        }
        return tempVec;
    }
}
