package net.mindoth.ancientmagicks.registries;

import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.item.spell.alacrity.AlacrityEffect;
import net.mindoth.ancientmagicks.item.spell.flight.FlightEffect;
import net.mindoth.ancientmagicks.item.spell.ghostwalk.GhostwalkEffect;
import net.mindoth.ancientmagicks.item.spell.magearmor.MageArmorEffect;
import net.mindoth.ancientmagicks.item.spell.mindcontrol.MindControlEffect;
import net.mindoth.ancientmagicks.item.spell.numbpain.NumbnessEffect;
import net.mindoth.ancientmagicks.item.spell.sleep.SleepEffect;
import net.mindoth.ancientmagicks.item.spell.spook.SpookEffect;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class AncientMagicksEffects {
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, AncientMagicks.MOD_ID);

    public static final RegistryObject<MageArmorEffect> MAGE_ARMOR = EFFECTS.register("mage_armor", () -> new MageArmorEffect(MobEffectCategory.BENEFICIAL, 9520880));
    public static final RegistryObject<FlightEffect> FLIGHT = EFFECTS.register("flight", () -> new FlightEffect(MobEffectCategory.BENEFICIAL, 16773073));
    public static final RegistryObject<NumbnessEffect> NUMBNESS = EFFECTS.register("numbness", () -> new NumbnessEffect(MobEffectCategory.BENEFICIAL, 4738376));
    public static final RegistryObject<SpookEffect> SPOOK = EFFECTS.register("spook", () -> new SpookEffect(MobEffectCategory.BENEFICIAL, 3402751));
    public static final RegistryObject<GhostwalkEffect> GHOSTWALK = EFFECTS.register("ghostwalk", () -> new GhostwalkEffect(MobEffectCategory.BENEFICIAL, 16185078));
    public static final RegistryObject<MindControlEffect> MIND_CONTROL = EFFECTS.register("mind_control", () -> new MindControlEffect(MobEffectCategory.HARMFUL, 2039587));
    public static final RegistryObject<MobEffect> ALACRITY = EFFECTS.register("alacrity", () -> new AlacrityEffect(MobEffectCategory.BENEFICIAL, 16750848)
            .addAttributeModifier(Attributes.MOVEMENT_SPEED, "9ca9c968-0f1e-4125-97ba-c6ef04276c7a", 0.2D, AttributeModifier.Operation.MULTIPLY_TOTAL)
            .addAttributeModifier(Attributes.ATTACK_SPEED, "9ca9c968-0f1e-4125-97ba-c6ef04276c7a", 0.1D, AttributeModifier.Operation.MULTIPLY_TOTAL));
    public static final RegistryObject<SleepEffect> SLEEP = EFFECTS.register("sleep", () -> new SleepEffect(MobEffectCategory.HARMFUL, 16185078));
}
