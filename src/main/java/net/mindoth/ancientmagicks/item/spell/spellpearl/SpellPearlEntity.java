package net.mindoth.ancientmagicks.item.spell.spellpearl;

import net.mindoth.ancientmagicks.item.SpellItem;
import net.mindoth.ancientmagicks.item.castingitem.CastingItem;
import net.mindoth.ancientmagicks.item.spell.abstractspell.AbstractSpellEntity;
import net.mindoth.ancientmagicks.registries.AncientMagicksEntities;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PlayMessages;

public class SpellPearlEntity extends AbstractSpellEntity {

    public SpellPearlEntity(PlayMessages.SpawnEntity spawnEntity, Level level) {
        this(AncientMagicksEntities.SPELL_PEARL.get(), level);
    }

    public SpellPearlEntity(EntityType<SpellPearlEntity> entityType, Level level) {
        super(entityType, level);
    }

    public SpellPearlEntity(Level level, LivingEntity owner, Entity caster, SpellItem spell) {
        super(AncientMagicksEntities.SPELL_PEARL.get(), level, owner, caster, spell);
        this.setNoGravity(false);
    }

    @Override
    protected float getGravity() {
        return 0.03F;
    }

    @Override
    public float getDefaultSpeed() {
        return 1.5F;
    }

    @Override
    protected void doClientTickEffects() {
    }

    @Override
    protected void doExpirationEffects() {
    }

    @Override
    protected void doBlockEffects(BlockHitResult traceResult) {
        if ( !(this.owner instanceof Player player) ) return;
        BlockState blockState = this.level().getBlockState(traceResult.getBlockPos());
        Direction face = traceResult.getDirection();
        blockState.onProjectileHit(this.level(), blockState, traceResult, this);
        Vec3 motion = this.getDeltaMovement();
        double motionX = motion.x();
        double motionY = motion.y();
        double motionZ = motion.z();
        if (face == Direction.EAST) {
            motionX = -motionX;
        }
        else if (face == Direction.SOUTH) {
            motionZ = -motionZ;
        }
        else if (face == Direction.WEST) {
            motionX = -motionX;
        }
        else if (face == Direction.NORTH) {
            motionZ = -motionZ;
        }
        else if (face == Direction.UP) {
            motionY = -motionY;
        }
        else if (face == Direction.DOWN) {
            motionY = -motionY;
        }
        shoot(motionX, motionY, motionZ, this.speed * 0.5F, 0);

        CastingItem.doSpell(player, this, null, this.spell, 0);
    }

    @Override
    protected void doMobEffects(EntityHitResult result) {
        if ( !(this.owner instanceof Player player) ) return;
        CastingItem.doSpell(player, this, null, this.spell, 0);
    }
}
