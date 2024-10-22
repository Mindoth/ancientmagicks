package net.mindoth.ancientmagicks.mixins;

import net.mindoth.ancientmagicks.registries.AncientMagicksEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    //The block's innate particle is still played when you land but oh well, what can you do...
    @ModifyVariable(method = "checkFallDamage", at = @At("STORE"), ordinal = 0)
    protected int preventFallingParticles(int i) {
        LivingEntity living = (LivingEntity)(Object)this;
        if ( living.hasEffect(AncientMagicksEffects.SPOOK.get()) ) return 0;
        else return i;
    }

    @Inject(method = "updateInvisibilityStatus", at = @At(value = "TAIL"))
    public void updateInvisibilityStatus(CallbackInfo callback) {
        LivingEntity living = (LivingEntity)(Object) this;
        if ( living.hasEffect(AncientMagicksEffects.GHOSTWALK.get()) || living.hasEffect(AncientMagicksEffects.GREATER_INVISIBILITY.get()) ) living.setInvisible(true);
    }

    @Inject(method = "checkBedExists", at = @At(value = "HEAD"), cancellable = true)
    public void allowSleepWithPotionEffect(CallbackInfoReturnable<Boolean> callback) {
        LivingEntity living = (LivingEntity)(Object) this;
        if ( living.hasEffect(AncientMagicksEffects.SLEEP.get()) ) callback.setReturnValue(true);
    }

    @Inject(method = "isImmobile", at = @At(value = "HEAD"), cancellable = true)
    public void stopMovementWhileSleeping(CallbackInfoReturnable<Boolean> callback) {
        LivingEntity living = (LivingEntity)(Object) this;
        if ( living.hasEffect(AncientMagicksEffects.SLEEP.get()) && living instanceof Mob ) callback.setReturnValue(true);
    }
}
