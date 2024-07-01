package net.mindoth.ancientmagicks.item.spell.deafeningblast;

import net.mindoth.ancientmagicks.item.castingitem.TabletItem;
import net.mindoth.shadowizardlib.event.ShadowEvents;
import net.minecraft.core.particles.ParticleTypes;
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

        float range = 3.5F;
        float size = 1.5F;

        Vec3 point = ShadowEvents.getPoint(level, caster, range, 0, caster == owner, false, true, true, false);
        List<Entity> targets = level.getEntities(caster, new AABB(new Vec3(point.x + size, point.y + size, point.z + size),
                new Vec3(point.x - size, point.y - size, point.z - size)));
        for ( Entity target : targets ) {
            if ( target != caster && target instanceof LivingEntity living && !isAlly(owner, living) ) {
                ItemStack mainHand = living.getMainHandItem();
                ItemStack offHand = living.getOffhandItem();
                if ( !mainHand.isEmpty() ) dropItemEntity(mainHand.copyAndClear(), living);
                if ( !offHand.isEmpty() ) dropItemEntity(offHand.copyAndClear(), living);
                state = true;
            }
        }

        if ( state ) {
            addParticles(level, point);
            playMagicShootSound(level, center);
        }

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

    private static void addParticles(Level world, Vec3 center) {
        ServerLevel level = (ServerLevel)world;
        level.sendParticles(ParticleTypes.SONIC_BOOM, center.x, center.y, center.z,
                0, 0, 0, 0, 0);
    }
}
