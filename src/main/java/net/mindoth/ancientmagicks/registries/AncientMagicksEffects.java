package net.mindoth.ancientmagicks.registries;

import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.item.spell.alacrity.AlacrityEffect;
import net.mindoth.ancientmagicks.item.spell.fly.FlightEffect;
import net.mindoth.ancientmagicks.item.spell.frostarmor.FrostArmorEffect;
import net.mindoth.ancientmagicks.item.spell.greaterinvisibility.GreaterInvisibilityEffect;
import net.mindoth.ancientmagicks.item.spell.mindcontrol.MindControlEffect;
import net.mindoth.ancientmagicks.item.spell.numbpain.NumbnessEffect;
import net.mindoth.ancientmagicks.item.spell.perfectinvisibility.PerfectInvisibilityEffect;
import net.mindoth.ancientmagicks.item.spell.polymorph.PolymorphEffect;
import net.mindoth.ancientmagicks.item.spell.sleep.SleepEffect;
import net.mindoth.ancientmagicks.item.spell.spook.SpookEffect;
import net.mindoth.ancientmagicks.item.spell.teleblock.TeleblockEffect;
import net.mindoth.ancientmagicks.item.spell.witcharmor.WitchArmorEffect;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class AncientMagicksEffects {
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, AncientMagicks.MOD_ID);

    public static final RegistryObject<MobEffect> FROST_ARMOR = EFFECTS.register("frost_armor", () -> new FrostArmorEffect(MobEffectCategory.BENEFICIAL, 5636095)
            .addAttributeModifier(Attributes.ARMOR, "b8abc70f-e827-452a-9b54-85a8e0aacdcd", 5.0D, AttributeModifier.Operation.ADDITION));
    public static final RegistryObject<FlightEffect> FLIGHT = EFFECTS.register("flight", () -> new FlightEffect(MobEffectCategory.BENEFICIAL, 16733695));
    public static final RegistryObject<NumbnessEffect> NUMBNESS = EFFECTS.register("numbness", () -> new NumbnessEffect(MobEffectCategory.BENEFICIAL, 0));
    public static final RegistryObject<SpookEffect> SPOOK = EFFECTS.register("spook", () -> new SpookEffect(MobEffectCategory.BENEFICIAL, 0));
    public static final RegistryObject<GreaterInvisibilityEffect> GREATER_INVISIBILITY = EFFECTS.register("greater_invisibility", () -> new GreaterInvisibilityEffect(MobEffectCategory.BENEFICIAL, 16185078));
    public static final RegistryObject<MindControlEffect> MIND_CONTROL = EFFECTS.register("mind_control", () -> new MindControlEffect(MobEffectCategory.HARMFUL, 0));
    public static final RegistryObject<MobEffect> ALACRITY = EFFECTS.register("alacrity", () -> new AlacrityEffect(MobEffectCategory.BENEFICIAL, 16733695)
            .addAttributeModifier(Attributes.MOVEMENT_SPEED, "9ca9c968-0f1e-4125-97ba-c6ef04276c7a", 0.2D, AttributeModifier.Operation.MULTIPLY_TOTAL)
            .addAttributeModifier(Attributes.ATTACK_SPEED, "9ca9c968-0f1e-4125-97ba-c6ef04276c7a", 0.1D, AttributeModifier.Operation.MULTIPLY_TOTAL));
    public static final RegistryObject<SleepEffect> SLEEP = EFFECTS.register("sleep", () -> new SleepEffect(MobEffectCategory.HARMFUL, 0));
    public static final RegistryObject<TeleblockEffect> TELEBLOCK = EFFECTS.register("teleblock", () -> new TeleblockEffect(MobEffectCategory.HARMFUL, 16733695));
    public static final RegistryObject<WitchArmorEffect> WITCH_ARMOR = EFFECTS.register("witch_armor", () -> new WitchArmorEffect(MobEffectCategory.BENEFICIAL, 16733695));
    public static final RegistryObject<PerfectInvisibilityEffect> PERFECT_INVISIBILITY = EFFECTS.register("perfect_invisibility", () -> new PerfectInvisibilityEffect(MobEffectCategory.BENEFICIAL, 16185078));
    public static final RegistryObject<PolymorphEffect> POLYMORPH = EFFECTS.register("polymorph", () -> new PolymorphEffect(MobEffectCategory.HARMFUL, 16733695));
}
