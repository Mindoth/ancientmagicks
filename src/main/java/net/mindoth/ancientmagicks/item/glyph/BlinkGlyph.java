package net.mindoth.ancientmagicks.item.glyph;

import net.mindoth.ancientmagicks.entity.projectile.AbstractSpell;
import net.mindoth.ancientmagicks.item.GlyphItem;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

import javax.annotation.Nullable;

public class BlinkGlyph extends GlyphItem {
    public BlinkGlyph(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void onEntityHit(@Nullable AbstractSpell spell, LivingEntity owner, EntityHitResult result) {
        if ( spell != null ) teleportToBolt(spell,result);
    }

    @Override
    public void onBlockHit(@Nullable AbstractSpell spell, LivingEntity owner, BlockHitResult result) {
        if ( spell != null ) teleportToBolt(spell, result);
    }

    private void teleportToBolt(AbstractSpell spell, HitResult hitResult) {
        if ( hitResult instanceof EntityHitResult result ) {
            Entity entity = result.getEntity();
            spell.caster.teleportTo(spell.getX(), entity.getY(), spell.getZ());
        }
        else if ( hitResult instanceof BlockHitResult result ) {
            BlockPos blockPos = result.getBlockPos().relative(result.getDirection());
            spell.caster.teleportTo(blockPos.getX() + 0.5D, blockPos.getY(), blockPos.getZ() + 0.5D);
        }
    }
}
