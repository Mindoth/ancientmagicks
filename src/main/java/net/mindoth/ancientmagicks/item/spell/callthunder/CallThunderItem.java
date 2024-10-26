package net.mindoth.ancientmagicks.item.spell.callthunder;

import net.mindoth.ancientmagicks.item.spell.abstractspell.SpellItem;
import net.mindoth.ancientmagicks.item.spell.abstractspell.SpellSchool;
import net.mindoth.ancientmagicks.registries.attribute.AncientMagicksAttributes;
import net.mindoth.shadowizardlib.event.ShadowEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class CallThunderItem extends SpellItem {

    public CallThunderItem(Properties pProperties, int spellTier, int manaCost, int cooldown, SpellSchool spellSchool) {
        super(pProperties, spellTier, manaCost, cooldown, spellSchool);
    }

    @Override
    public boolean castMagic(Player owner, Entity caster, Vec3 center, float xRot, float yRot, int useTime) {
        boolean state = false;
        Level level = caster.level();
        float power = 20.0F * (float)owner.getAttributeValue(AncientMagicksAttributes.SPELL_POWER.get());
        float range = 64.0F;
        if ( owner != caster ) range = 0.0F;
        Vec3 point = ShadowEvents.getPoint(level, caster, range, 0, caster == owner, true, true, true, true);
        BlockPos blockPos = new BlockPos(Mth.floor(point.x), Mth.floor(point.y), Mth.floor(point.z));
        BlockState blockState = level.getBlockState(blockPos.below());
        if ( !blockState.isSolid() || !(blockState.getBlock() instanceof LiquidBlock) ) blockPos = getBlockBelow(level, blockPos);
        if ( level.canSeeSkyFromBelowWater(blockPos) ) {
            LightningBolt lightningbolt = EntityType.LIGHTNING_BOLT.create(level);
            if ( lightningbolt != null && !level.isClientSide ) {
                lightningbolt.moveTo(blockPos.getCenter().x, blockPos.getCenter().y - 0.5D, blockPos.getCenter().z);
                lightningbolt.setCause(caster instanceof ServerPlayer ? (ServerPlayer)caster : null);
                lightningbolt.setDamage(getPowerInRange(2.0F, power));
                level.addFreshEntity(lightningbolt);
                state = true;
            }
        }

        return state;
    }

    private BlockPos getBlockBelow(Level level, BlockPos blockPos) {
        for ( int i = blockPos.getY(); i > level.getMinBuildHeight(); i-- ) {
            BlockPos tempPos = new BlockPos(blockPos.getX(), i, blockPos.getZ());
            if ( level.getBlockState(tempPos.below()).isSolid() || level.getBlockState(tempPos).getBlock() instanceof LiquidBlock ) {
                blockPos = tempPos;
                break;
            }
        }
        return blockPos;
    }
}
