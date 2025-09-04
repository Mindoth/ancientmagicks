package net.mindoth.ancientmagicks.revamp.item.rune;

import net.mindoth.ancientmagicks.client.particle.ember.ParticleColor;
import net.mindoth.ancientmagicks.network.ModNetwork;
import net.mindoth.ancientmagicks.network.PacketSendCustomParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.Random;

public class EntityTargetTemplate extends RuneItem {
    public EntityTargetTemplate(Properties pProperties) {
        super(pProperties);
    }

    public static void attackEntity(Entity owner, Entity caster, Entity target, float amount) {
        target.hurt(target.damageSources().indirectMagic(caster, owner), amount);
    }

    public static void attackEntityWithoutKnockback(Entity owner, Entity caster, Entity target, float amount) {
        final double vx = target.getDeltaMovement().x;
        final double vy = target.getDeltaMovement().y;
        final double vz = target.getDeltaMovement().z;
        attackEntity(owner, target, caster, amount);
        target.setDeltaMovement(vx, vy, vz);
        target.hurtMarked = true;
    }

    public static boolean isPushable(Entity entity) {
        return ( entity instanceof LivingEntity || entity instanceof ItemEntity || entity instanceof PrimedTnt || entity instanceof FallingBlockEntity);
    }

    protected void aoeEntitySpellParticles(Level level, AABB box, float range, HashMap<String, Float> stats) {
        Vec3 center = box.getCenter();
        BlockPos pos = new BlockPos(Mth.floor(center.x), Mth.floor(center.y), Mth.floor(center.z));
        double tempY = center.y;
        for ( int i = pos.getY(); i >= Mth.floor(center.y - range); i-- ) {
            BlockPos tempPos = new BlockPos(pos.getX(), i, pos.getZ());
            if ( level.getBlockState(tempPos).isSolid() ) break;
            else tempY = i;
        }
        box = box.move(0, -(center.y - tempY), 0);
        addAoeParticles(false, level, box, 0.15F, 8, stats);
    }

    protected void addEnchantParticles(Entity target, float size, int age, HashMap<String, Float> stats) {
        double var = 0.15D;
        double maxX = target.getBoundingBox().maxX + var;
        double minX = target.getBoundingBox().minX - var;
        double maxZ = target.getBoundingBox().maxZ + var;
        double minZ = target.getBoundingBox().minZ - var;
        //double vecX = target.getDeltaMovement().x;
        double vecX = 0.0D;
        double vecY = 0.25D;
        //double vecZ = target.getDeltaMovement().z;
        double vecZ = 0.0D;
        for ( int i = 0; i < 4; i++ ) {
            double randX = maxX;
            double randY = target.getY() + ((target.getY() + (target.getBbHeight() / 2)) - target.getY()) * new Random().nextDouble();
            double randZ = minZ + (maxZ - minZ) * new Random().nextDouble();
            Vec3 pos = new Vec3(randX, randY, randZ);
            ParticleColor.IntWrapper color = getParticleColor(stats);
            ModNetwork.sendToPlayersTrackingEntity(new PacketSendCustomParticles(color.r, color.g, color.b, size, age, false, getRenderType(),
                    pos.x, pos.y, pos.z, vecX, vecY, vecZ), target, true);
        }
        for ( int i = 0; i < 4; i++ ) {
            double randX = minX;
            double randY = target.getY() + ((target.getY() + (target.getBbHeight() / 2)) - target.getY()) * new Random().nextDouble();
            double randZ = minZ + (maxZ - minZ) * new Random().nextDouble();
            Vec3 pos = new Vec3(randX, randY, randZ);
            ParticleColor.IntWrapper color = getParticleColor(stats);
            ModNetwork.sendToPlayersTrackingEntity(new PacketSendCustomParticles(color.r, color.g, color.b, size, age, false, getRenderType(),
                    pos.x, pos.y, pos.z, vecX, vecY, vecZ), target, true);
        }
        for ( int i = 0; i < 4; i++ ) {
            double randX = minX + (maxX - minX) * new Random().nextDouble();
            double randY = target.getY() + ((target.getY() + (target.getBbHeight() / 2)) - target.getY()) * new Random().nextDouble();
            double randZ = minZ;
            Vec3 pos = new Vec3(randX, randY, randZ);
            ParticleColor.IntWrapper color = getParticleColor(stats);
            ModNetwork.sendToPlayersTrackingEntity(new PacketSendCustomParticles(color.r, color.g, color.b, size, age, false, getRenderType(),
                    pos.x, pos.y, pos.z, vecX, vecY, vecZ), target, true);
        }
        for ( int i = 0; i < 4; i++ ) {
            double randX = minX + (maxX - minX) * new Random().nextDouble();
            double randY = target.getY() + ((target.getY() + (target.getBbHeight() / 2)) - target.getY()) * new Random().nextDouble();
            double randZ = maxZ;
            Vec3 pos = new Vec3(randX, randY, randZ);
            ParticleColor.IntWrapper color = getParticleColor(stats);
            ModNetwork.sendToPlayersTrackingEntity(new PacketSendCustomParticles(color.r, color.g, color.b, size, age, false, getRenderType(),
                    pos.x, pos.y, pos.z, vecX, vecY, vecZ), target, true);
        }
    }
}
