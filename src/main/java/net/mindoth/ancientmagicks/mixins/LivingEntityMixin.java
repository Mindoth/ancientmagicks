package net.mindoth.ancientmagicks.mixins;

import net.mindoth.ancientmagicks.registries.AncientMagicksEffects;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Inject(method = "updateInvisibilityStatus", at = @At(value = "TAIL"))
    public void updateInvisibilityStatus(CallbackInfo callback) {
        LivingEntity living = (LivingEntity)(Object) this;
        if ( living.hasEffect(AncientMagicksEffects.GHOSTWALK.get()) ) living.setInvisible(true);
    }
}
