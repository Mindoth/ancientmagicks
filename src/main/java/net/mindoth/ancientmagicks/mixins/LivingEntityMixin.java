package net.mindoth.ancientmagicks.mixins;

import net.mindoth.ancientmagicks.registries.AncientMagicksEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Inject(method = "updateInvisibilityStatus", at = @At(value = "TAIL"))
    public void updateInvisibilityStatus(CallbackInfo callback) {
        LivingEntity living = (LivingEntity)(Object) this;
        if ( living.hasEffect(AncientMagicksEffects.GHOSTWALK.get()) ) living.setInvisible(true);
    }

    //The block's innate particle is still player when you land but oh well, what can you do...
    @ModifyVariable(method = "checkFallDamage", at = @At("STORE"), ordinal = 0)
    protected int preventFallingParticles(int i) {
        LivingEntity living = (LivingEntity)(Object)this;
        if ( living.hasEffect(AncientMagicksEffects.SPOOK.get()) ) return 0;
        else return i;
    }
}
