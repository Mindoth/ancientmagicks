package net.mindoth.ancientmagicks.item.glyph;

import net.mindoth.ancientmagicks.entity.projectile.AbstractSpell;
import net.mindoth.ancientmagicks.item.GlyphItem;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class SkeletonGlyph extends GlyphItem {
    public SkeletonGlyph(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void onEntityHit(@Nullable AbstractSpell spell, LivingEntity owner, EntityHitResult result) {
        Entity entity = result.getEntity();
        if ( spell != null ) {
            Skeleton minion = new Skeleton(EntityType.SKELETON, spell.level());
            summonMinion(minion, spell.owner, spell.level(), new Vec3(spell.getX(), entity.getY(), spell.getZ()), result.getEntity());
        }
        else {
            Skeleton minion = new Skeleton(EntityType.SKELETON, owner.level());
            summonMinion(minion, owner, owner.level(), result.getEntity().position(), null);
        }
    }

    @Override
    public void onBlockHit(@Nullable AbstractSpell spell, LivingEntity owner, BlockHitResult result) {
        BlockPos blockPos = result.getBlockPos().relative(result.getDirection());
        Vec3 pos = new Vec3(blockPos.getX() + 0.5D, blockPos.getY(), blockPos.getZ() + 0.5D);
        if ( spell != null ) {
            Skeleton minion = new Skeleton(EntityType.SKELETON, spell.level());
            summonMinion(minion, spell.owner, spell.level(), pos, null);
        }
    }
}
