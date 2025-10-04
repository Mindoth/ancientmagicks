package net.mindoth.ancientmagicks.item.rune.shelf.effect;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.RelativeMovement;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;
import net.minecraftforge.event.entity.EntityTeleportEvent;

import java.util.HashMap;

public class TelebringEffectItem extends EntityTargetEffect {

    public TelebringEffectItem(Properties pProperties, int cost) {
        super(pProperties, cost);
    }

    @Override
    protected boolean doSpell(Level level, LivingEntity owner, Entity caster, HitResult result, HashMap<String, Float> stats, String data) {
        boolean state = false;

        Vec3 pos = caster.position();
        Level pointLevel = caster.level();

        if ( pointLevel instanceof ServerLevel serverLevel ) {
            if ( result instanceof EntityHitResult eResult ) {
                Entity entity = eResult.getEntity();
                EntityTeleportEvent.TeleportCommand event = net.minecraftforge.event.ForgeEventFactory.onEntityTeleportCommand(entity, pos.x, pos.y, pos.z);
                if ( !event.isCanceled() ) {
                    entity.teleportTo(serverLevel, event.getTargetX(), event.getTargetY(), event.getTargetZ(), RelativeMovement.ALL,
                            entity.getViewYRot(0), entity.getViewXRot(0));
                    state = true;
                }
            }
        }

        return state;
    }
}
