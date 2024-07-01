package net.mindoth.ancientmagicks.item.spell.deafeningblast;

import net.mindoth.ancientmagicks.item.castingitem.TabletItem;
import net.mindoth.ancientmagicks.network.AncientMagicksNetwork;
import net.mindoth.ancientmagicks.network.PacketSendCustomParticles;
import net.mindoth.shadowizardlib.event.ShadowEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class DeafeningBlastTablet extends TabletItem {

    public DeafeningBlastTablet(Properties pProperties, int tier, boolean isChannel, int cooldown) {
        super(pProperties, tier, isChannel, cooldown);
    }

    @Override
    public boolean castMagic(Player owner, Entity caster, Vec3 center, float xRot, float yRot, int useTime) {
        boolean state = false;
        Level level = caster.level();
        int adjuster = 1;
        float down = -0.2F;
        if ( caster != owner ) {
            adjuster = -1;
            down = 0.0F;
        }

        float range = 2.0F;

        Vec3 point = ShadowEvents.getPoint(level, caster, range, 0, caster == owner, false, true, true, true);
        List<Entity> targets = level.getEntities(caster, new AABB(new Vec3(point.x + 2, point.y + 2, point.z + 2),
                new Vec3(point.x - range, point.y - range, point.z - 2)));
        for ( Entity target : targets ) {
            if ( target != caster && target instanceof LivingEntity living && !isAlly(owner, living) ) {
                ItemStack mainHand = living.getMainHandItem();
                ItemStack offHand = living.getOffhandItem();
                if ( !mainHand.isEmpty() ) dropItemEntity(mainHand.copyAndClear(), living);
                if ( !offHand.isEmpty() ) dropItemEntity(offHand.copyAndClear(), living);
                addParticles(level, owner, caster, adjuster);
                state = true;
            }
        }

        if ( state ) playMagicShootSound(level, center);

        return state;
    }

    private void dropItemEntity(ItemStack dropStack, LivingEntity target) {
        Vec3 pos = target.getEyePosition();
        ItemEntity drop = new ItemEntity(target.level(), pos.x, pos.y - 0.3D, pos.z, dropStack);
        float f8 = Mth.sin(target.getXRot() * ((float)Math.PI / 180F));
        float f2 = Mth.cos(target.getXRot() * ((float)Math.PI / 180F));
        float f3 = Mth.sin(target.getYRot() * ((float)Math.PI / 180F));
        float f4 = Mth.cos(target.getYRot() * ((float)Math.PI / 180F));
        float f5 = target.getRandom().nextFloat() * ((float)Math.PI * 2F);
        float f6 = 0.02F * target.getRandom().nextFloat();
        drop.setDeltaMovement((double)(-f3 * f2 * 0.3F) + Math.cos((double)f5) * (double)f6, (double)(-f8 * 0.3F + 0.1F + (target.getRandom().nextFloat() - target.getRandom().nextFloat()) * 0.1F), (double)(f4 * f2 * 0.3F) + Math.sin((double)f5) * (double)f6);
        drop.setPickUpDelay(40);
        target.level().addFreshEntity(drop);
    }

    private static void addParticles(Level world, Player owner, Entity caster, int adjuster) {
        ServerLevel level = (ServerLevel)world;
        Vec3 casterPos = caster.getEyePosition();
        Vec3 lookPos = ShadowEvents.getPoint(world, caster, 1, 0, caster == owner, false, false, false, false);
        final Vec3 pos = new Vec3(casterPos.x, casterPos.y, casterPos.z);
        int r = 85;
        int g = 255;
        int b = 255;
        float size = 0.3F;
        int age = 10;
        double vx = lookPos.x - casterPos.x;
        double vy = lookPos.y - casterPos.y;
        double vz = lookPos.z - casterPos.z;

        AncientMagicksNetwork.sendToNearby(level, caster, new PacketSendCustomParticles(r, g, b, size, age, false, true,
                pos.x, pos.y, pos.z, vx, vy, vz));
        for ( int i = 1; i < 4; i++ ) {
            float mult = i * 0.25F;
            Vec3 right = Vec3.directionFromRotation(0, caster.getYRot() * adjuster + (90 + i * 5));
            Vec3 vecRight = pos.add(right.x * mult, 0, right.z * mult);
            AncientMagicksNetwork.sendToNearby(level, caster, new PacketSendCustomParticles(r, g, b, size, age, false, true,
                    vecRight.x, vecRight.y, vecRight.z, vx, vy, vz));

            Vec3 left = Vec3.directionFromRotation(0, caster.getYRot() * adjuster + (-90 - i * 5));
            Vec3 vecLeft = pos.add(left.x * mult, 0, left.z * mult);
            AncientMagicksNetwork.sendToNearby(level, caster, new PacketSendCustomParticles(r, g, b, size, age, false, true,
                    vecLeft.x, vecLeft.y, vecLeft.z, vx, vy, vz));
        }
    }
}
