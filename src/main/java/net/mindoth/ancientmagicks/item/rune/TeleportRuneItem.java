package net.mindoth.ancientmagicks.item.rune;

import net.mindoth.ancientmagicks.event.MultiEntityHitResult;
import net.mindoth.ancientmagicks.event.SpellData;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.RelativeMovement;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.EntityTeleportEvent;

import javax.annotation.Nullable;
import java.util.List;

public class TeleportRuneItem extends UseOnEntityTemplate {
    public TeleportRuneItem(Properties pProperties) {
        super(pProperties);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(Component.translatable("tooltip.ancientmagicks.entity").append(Component.literal(", "))
                .append(Component.translatable("tooltip.ancientmagicks.position")).append(Component.literal(" ->")).withStyle(ChatFormatting.GRAY));
        super.appendHoverText(stack, world, tooltip, flagIn);
    }

    @Override
    protected SpellData result(Entity caster, SpellData spellData, MultiEntityHitResult result, Entity entity) {
        if ( !spellData.getPositions().isEmpty() ) {
            if ( spellData.getLatestPosition() != null ) {
                Vec3 position = spellData.getLatestPosition().getPos();
                Level level = spellData.getLatestPosition().getLevel();
                spellData.purgePositions(1);
                handleTeleport(level, entity, position);
            }
        }
        else spellData.setValid(false);
        return spellData;
    }

    private void handleTeleport(Level level, Entity entity, Vec3 position) {
        if ( arePositionsTooClose(entity.position(), position) ) return;
        EntityTeleportEvent event = new EntityTeleportEvent(entity, Mth.floor(position.x) + 0.5F, position.y, Mth.floor(position.z) + 0.5F);
        if ( !event.isCanceled() && level instanceof ServerLevel serverLevel ) {
            entity.teleportTo(serverLevel, event.getTargetX(), event.getTargetY(), event.getTargetZ(), RelativeMovement.ALL,
                    entity.getViewYRot(0), entity.getViewXRot(0));
            if ( entity instanceof ServerPlayer serverPlayer ) serverPlayer.connection.resetPosition();
            entity.fallDistance = 0;
            if ( entity.isInWall() ) entity.setPose(Pose.SWIMMING);
        }
    }

    private boolean arePositionsTooClose(Vec3 pos0, Vec3 pos1) {
        int x0 = Mth.floor(pos0.x);
        int y0 = Mth.floor(pos0.y);
        int z0 = Mth.floor(pos0.z);
        Vec3 vec0 = new Vec3(x0, y0, z0);
        int x1 = Mth.floor(pos1.x);
        int y1 = Mth.floor(pos1.y);
        int z1 = Mth.floor(pos1.z);
        Vec3 vec1 = new Vec3(x1, y1, z1);
        return vec0.equals(vec1);
    }
}
