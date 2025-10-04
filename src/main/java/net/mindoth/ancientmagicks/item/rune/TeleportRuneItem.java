package net.mindoth.ancientmagicks.item.rune;

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
    protected SpellData result(Entity caster, SpellData spellData, Entity entity) {
        if ( !spellData.getPositions().isEmpty() ) {
            if ( spellData.getLatestPosition() != null ) {
                Vec3 position = spellData.getLatestPosition().getPos();
                Level level = spellData.getLatestPosition().getLevel();
                spellData.purgeVectors(1);
                handleTeleport(level, entity, position);
            }
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
