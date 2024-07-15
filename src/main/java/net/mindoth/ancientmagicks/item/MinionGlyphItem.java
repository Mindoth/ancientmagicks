package net.mindoth.ancientmagicks.item;

import net.mindoth.ancientmagicks.effect.MindControlEffect;
import net.mindoth.ancientmagicks.entity.projectile.AbstractSpell;
import net.mindoth.ancientmagicks.registries.AncientMagicksEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;

import javax.annotation.Nullable;

public class MinionGlyphItem extends GlyphItem {
    public MinionGlyphItem(Properties pProperties) {
        super(pProperties);
    }

    protected Mob getMob(Level level) {
        return null;
    }

    @Override
    public void onEntityHit(@Nullable AbstractSpell spell, LivingEntity owner, EntityHitResult result) {
        Entity entity = result.getEntity();
        if ( spell != null ) {
            Mob minion = getMob(spell.level());
            minion.teleportTo(spell.getX(), entity.getY(), spell.getZ());
            summonMinion(minion, spell.owner, spell.level(), result.getEntity());
        }
        else {
            Mob minion = getMob(owner.level());
            Vec3 pos = result.getEntity().position();
            Vec3 newPos = null;
            while ( newPos == null || !minion.position().equals(newPos) ) {
                double d3 = pos.x + (minion.getRandom().nextDouble() - 0.5D) * 3.0D;
                double d4 = Mth.clamp(pos.y + (double)(minion.getRandom().nextInt(4) - 2),
                        (double)owner.level().getMinBuildHeight(), (double)(owner.level().getMinBuildHeight() + ((ServerLevel)owner.level()).getLogicalHeight() - 1));
                double d5 = pos.z + (minion.getRandom().nextDouble() - 0.5D) * 3.0D;
                net.minecraftforge.event.entity.EntityTeleportEvent.ChorusFruit event = net.minecraftforge.event.ForgeEventFactory.onChorusFruitTeleport(owner, d3, d4, d5);
                newPos = new Vec3(event.getTargetX(), event.getTargetY(), event.getTargetZ());
                minion.randomTeleport(newPos.x, newPos.y, newPos.z, true);
            }
            summonMinion(minion, owner, owner.level(), null);
        }
    }

    @Override
    public void onBlockHit(@Nullable AbstractSpell spell, LivingEntity owner, BlockHitResult result) {
        BlockPos blockPos = result.getBlockPos().relative(result.getDirection());
        Vec3 pos = new Vec3(blockPos.getX() + 0.5D, blockPos.getY(), blockPos.getZ() + 0.5D);
        if ( spell != null ) {
            Mob minion = getMob(spell.level());
            minion.teleportTo(pos.x, pos.y, pos.z);
            summonMinion(minion, spell.owner, spell.level(), null);
        }
    }

    public void summonMinion(Mob minion, LivingEntity owner, Level level, @Nullable Entity hitEntity) {
        minion.getPersistentData().putUUID(MindControlEffect.NBT_KEY, owner.getUUID());
        minion.getPersistentData().putBoolean("am_is_minion", true);
        minion.addEffect(new MobEffectInstance(AncientMagicksEffects.MIND_CONTROL.get(), 1200));
        ForgeEventFactory.onFinalizeSpawn(minion, (ServerLevel)level, level.getCurrentDifficultyAt(minion.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
        level.addFreshEntity(minion);
        minion.spawnAnim();
        //if ( hitEntity instanceof LivingEntity living && !isAlly(owner, living) ) minion.setTarget(living);
    }
}
