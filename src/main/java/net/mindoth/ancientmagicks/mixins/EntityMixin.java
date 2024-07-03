package net.mindoth.ancientmagicks.mixins;

import net.mindoth.ancientmagicks.registries.AncientMagicksEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin {

    @Inject(method = "walkingStepSound", at = @At("HEAD"), cancellable = true)
    public void silenceFootsteps(BlockPos pPos, BlockState pState, CallbackInfo callback) {
        Entity entity = (Entity)(Object)this;
        if ( entity instanceof LivingEntity living && living.hasEffect(AncientMagicksEffects.SPOOK.get()) ) callback.cancel();
    }

    @Inject(method = "canSpawnSprintParticle", at = @At("HEAD"), cancellable = true)
    public void hideSprintParticles(CallbackInfoReturnable<Boolean> callback) {
        Entity entity = (Entity)(Object)this;
        if ( entity instanceof LivingEntity living && living.hasEffect(AncientMagicksEffects.SPOOK.get()) ) callback.setReturnValue(false);
    }


    /*@Inject(method = "causeFallDamage", at = @At("HEAD"), cancellable = true)
    protected void preventFalling(double pY, boolean pOnGround, BlockState pState, BlockPos pPos, CallbackInfo callback) {
        Entity entity = (Entity)(Object)this;
        if ( entity instanceof LivingEntity living && living.hasEffect(AncientMagicksEffects.SPOOK.get()) ) {
            callback.cancel();
            Entity.causeFallDamage();
        }
    }*/
}
