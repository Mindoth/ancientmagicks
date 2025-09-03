package net.mindoth.ancientmagicks.revamp.item.rune;

import net.mindoth.ancientmagicks.revamp.SpellData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.RelativeMovement;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.EntityTeleportEvent;

public class TeleportRuneItem extends RuneItem {
    public TeleportRuneItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public SpellData resolve(Entity caster, SpellData spellData) {
        if ( !spellData.getEntities().isEmpty() && !spellData.getVectors().isEmpty() ) {
            if ( spellData.getDimensions().isEmpty() ) spellData.addDimension(caster.level());
            Entity entity = spellData.getEntities().get(spellData.getEntities().size() - 1);
            spellData.purgeEntities(1);
            Vec3 pos = spellData.getVectors().get(spellData.getVectors().size() - 1);
            spellData.purgeVectors(1);
            Level level = spellData.getDimensions().get(spellData.getDimensions().size() - 1);
            spellData.purgeDimensions(1);
            if ( entity != null && pos != null && level != null ) handleTeleport(level, entity, pos);
        }
        else spellData.setValid(false);
        return spellData;
    }

    private void handleTeleport(Level level, Entity entity, Vec3 pos) {
        EntityTeleportEvent event = new EntityTeleportEvent(entity, pos.x, pos.y, pos.z);
        if ( !event.isCanceled() && level instanceof ServerLevel serverLevel ) {
            entity.teleportTo(serverLevel, event.getTargetX(), event.getTargetY(), event.getTargetZ(), RelativeMovement.ALL,
                    entity.getViewYRot(0), entity.getViewXRot(0));
            if ( entity instanceof ServerPlayer serverPlayer ) serverPlayer.connection.resetPosition();
            entity.fallDistance = 0;
            if ( entity.isInWall() ) entity.setPose(Pose.SWIMMING);
        }
    }
}
