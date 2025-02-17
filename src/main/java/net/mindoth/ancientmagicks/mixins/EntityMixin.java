package net.mindoth.ancientmagicks.mixins;

import net.mindoth.ancientmagicks.item.temp.greaterinvisibility.GreaterInvisibilityEffect;
import net.mindoth.ancientmagicks.registries.AncientMagicksEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeMod;
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

    @Inject(method = "dampensVibrations", at = @At("HEAD"), cancellable = true)
    public void hideVibrations(CallbackInfoReturnable<Boolean> callback) {
        Entity entity = (Entity)(Object)this;
        if ( entity instanceof LivingEntity living && living.hasEffect(AncientMagicksEffects.SPOOK.get()) ) callback.setReturnValue(true);
    }

    @Inject(method = "canSpawnSprintParticle", at = @At("HEAD"), cancellable = true)
    public void hideSprintParticles(CallbackInfoReturnable<Boolean> callback) {
        Entity entity = (Entity)(Object)this;
        if ( entity instanceof LivingEntity living ) {
            AttributeInstance nameTagDistance = living.getAttribute(ForgeMod.NAMETAG_DISTANCE.get());
            if ( nameTagDistance != null && nameTagDistance.hasModifier(GreaterInvisibilityEffect.DECREASED_NAME_TAG_DISTANCE) ) callback.setReturnValue(false);
        }
    }
}
