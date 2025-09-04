package net.mindoth.ancientmagicks.revamp.item.rune;

import net.mindoth.ancientmagicks.revamp.SpellData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.RelativeMovement;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.EntityTeleportEvent;

public class TeleportRuneItem extends UseOnEntityTemplate {
    public TeleportRuneItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    protected SpellData result(Entity caster, SpellData spellData, Entity entity) {
        if ( !spellData.getVectors().isEmpty() ) {
            Vec3 pos = spellData.getLatestVector();
            spellData.purgeVectors(1);
            Level level;
            if ( spellData.getDimensions().isEmpty() || spellData.getLatestDimension() == null ) level = caster.level();
            else level = spellData.getLatestDimension();
            spellData.purgeDimensions(1);
            handleTeleport(level, entity, pos);
        }
        else spellData.setValid(false);
        return spellData;
    }

    private void handleTeleport(Level level, Entity entity, Vec3 pos) {
        EntityTeleportEvent event = new EntityTeleportEvent(entity, Mth.floor(pos.x) + 0.5F, pos.y, Mth.floor(pos.z) + 0.5F);
        if ( !event.isCanceled() && level instanceof ServerLevel serverLevel ) {
            entity.teleportTo(serverLevel, event.getTargetX(), event.getTargetY(), event.getTargetZ(), RelativeMovement.ALL,
                    entity.getViewYRot(0), entity.getViewXRot(0));
            if ( entity instanceof ServerPlayer serverPlayer ) serverPlayer.connection.resetPosition();
            entity.fallDistance = 0;
            if ( entity.isInWall() ) entity.setPose(Pose.SWIMMING);
        }
    }
}
