package net.mindoth.ancientmagicks.registries;

import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.item.spell.abstractspell.summon.SummonTimer;
import net.mindoth.ancientmagicks.item.spell.alacrity.AlacrityEffect;
import net.mindoth.ancientmagicks.item.spell.flight.FlightEffect;
import net.mindoth.ancientmagicks.item.spell.numbpain.NumbnessEffect;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class AncientMagicksEffects {
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, AncientMagicks.MOD_ID);

    public static final RegistryObject<SummonTimer> SKELETON_TIMER = EFFECTS.register("skeleton_timer", () -> new SummonTimer(MobEffectCategory.BENEFICIAL, 0xbea925));
    public static final RegistryObject<FlightEffect> FLIGHT = EFFECTS.register("flight", () -> new FlightEffect(MobEffectCategory.BENEFICIAL, 16773073));
    public static final RegistryObject<NumbnessEffect> NUMBNESS = EFFECTS.register("numbness", () -> new NumbnessEffect(MobEffectCategory.BENEFICIAL, 4738376));
    public static final RegistryObject<MobEffect> ALACRITY = EFFECTS.register("alacrity", () -> new AlacrityEffect(MobEffectCategory.BENEFICIAL, 16750848)
            .addAttributeModifier(Attributes.MOVEMENT_SPEED, "9ca9c968-0f1e-4125-97ba-c6ef04276c7a", 0.2D, AttributeModifier.Operation.MULTIPLY_TOTAL)
            .addAttributeModifier(Attributes.ATTACK_SPEED, "9ca9c968-0f1e-4125-97ba-c6ef04276c7a", 0.1D, AttributeModifier.Operation.MULTIPLY_TOTAL));
}
