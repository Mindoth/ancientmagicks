package net.mindoth.ancientmagicks.mixins;

import net.mindoth.ancientmagicks.registries.AncientMagicksEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin {

    //This seems to be unnecessary with the silenceFootsteps
    /*@Inject(method = "vibrationAndSoundEffectsFromBlock", at = @At("HEAD"), cancellable = true)
    public void hideVibrations(CallbackInfoReturnable<Boolean> callback) {
        Entity entity = (Entity)(Object)this;
        if ( entity instanceof LivingEntity living && living.hasEffect(AncientMagicksEffects.SPOOK.get()) ) callback.setReturnValue(false);
    }*/

    @Inject(method = "walkingStepSound", at = @At("HEAD"), cancellable = true)
    public void silenceFootsteps(BlockPos pPos, BlockState pState, CallbackInfo callback) {
        Entity entity = (Entity)(Object)this;
        if ( entity instanceof LivingEntity living && living.hasEffect(AncientMagicksEffects.SPOOK.get()) ) callback.cancel();
    }

    @Inject(method = "dampensVibrations", at = @At("HEAD"), cancellable = true)
    public void hideVibrations(CallbackInfoReturnable<Boolean> callback) {
        Entity entity = (Entity)(Object)this;
        if ( entity instanceof LivingEntity living && living.hasEffect(AncientMagicksEffects.SPOOK.get()) ) callback.setReturnValue(true);
    }

    @Inject(method = "canSpawnSprintParticle", at = @At("HEAD"), cancellable = true)
    public void hideSprintParticles(CallbackInfoReturnable<Boolean> callback) {
        Entity entity = (Entity)(Object)this;
        if ( entity instanceof LivingEntity living && living.hasEffect(AncientMagicksEffects.GHOSTWALK.get()) ) callback.setReturnValue(false);
    }
}
