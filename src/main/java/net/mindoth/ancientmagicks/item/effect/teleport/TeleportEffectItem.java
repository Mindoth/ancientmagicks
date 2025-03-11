package net.mindoth.ancientmagicks.item.effect.teleport;

import net.mindoth.ancientmagicks.item.effect.SpellEffectItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;
import net.minecraftforge.event.entity.EntityTeleportEvent;

import java.util.HashMap;

public class TeleportEffectItem extends SpellEffectItem {

    public TeleportEffectItem(Properties pProperties, int manaCost, int cooldown) {
        super(pProperties, manaCost, cooldown);
    }

    @Override
    protected boolean doSpell(Level level, LivingEntity owner, Entity caster, HitResult result, HashMap<String, Float> stats, String data) {
        boolean state = false;

        Vec3 pos;
        if ( result instanceof BlockHitResult blockHitResult ) {
            BlockPos blockPos = getPosOfFace(blockHitResult.getBlockPos(), blockHitResult.getDirection());
            pos = new Vec3(blockPos.getX() + 0.5D, blockPos.getY(), blockPos.getZ() + 0.5D);
        }
        else pos = result.getLocation();

        EntityTeleportEvent.TeleportCommand event = net.minecraftforge.event.ForgeEventFactory.onEntityTeleportCommand(caster, pos.x, pos.y, pos.z);
        if ( !event.isCanceled() ) {
            if ( caster instanceof LivingEntity ) caster.teleportTo(event.getTargetX(), event.getTargetY(), event.getTargetZ());
            state = true;
        }

        return state;
    }

    private static BlockPos getPosOfFace(BlockPos blockPos, Direction face) {
        return switch (face) {
            case UP -> blockPos.above();
            case EAST -> blockPos.east();
            case WEST -> blockPos.west();
            case SOUTH -> blockPos.south();
            case NORTH -> blockPos.north();
            case DOWN -> blockPos.below();
        };
    }
}
