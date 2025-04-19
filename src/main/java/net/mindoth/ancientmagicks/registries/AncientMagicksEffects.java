package net.mindoth.ancientmagicks.registries;

import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.item.effect.chaoticpolymorph.ChaoticPolymorphEffect;
import net.mindoth.ancientmagicks.item.effect.chaoticpolymorph.ChaoticPolymorphEffectItem;
import net.mindoth.ancientmagicks.mobeffect.FlightEffect;
import net.mindoth.ancientmagicks.mobeffect.FrostArmorEffect;
import net.mindoth.ancientmagicks.mobeffect.GreaterInvisibilityEffect;
import net.mindoth.ancientmagicks.item.effect.mindcontrol.MindControlEffect;
import net.mindoth.ancientmagicks.mobeffect.NumbnessEffect;
import net.mindoth.ancientmagicks.mobeffect.PerfectInvisibilityEffect;
import net.mindoth.ancientmagicks.item.effect.polymorph.PolymorphEffect;
import net.mindoth.ancientmagicks.mobeffect.SleepEffect;
import net.mindoth.ancientmagicks.mobeffect.FallControlEffect;
import net.mindoth.ancientmagicks.mobeffect.TeleblockEffect;
import net.mindoth.ancientmagicks.mobeffect.WitchArmorEffect;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class AncientMagicksEffects {
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, AncientMagicks.MOD_ID);

    //JUST Effects
    public static final RegistryObject<MindControlEffect> MIND_CONTROL = EFFECTS.register("mind_control", () -> new MindControlEffect(MobEffectCategory.HARMFUL, 0));
    public static final RegistryObject<PolymorphEffect> POLYMORPH = EFFECTS.register("polymorph", () -> new PolymorphEffect(MobEffectCategory.HARMFUL, 16733695));
    public static final RegistryObject<ChaoticPolymorphEffect> CHAOTIC_POLYMORPH = EFFECTS.register("chaotic_polymorph", () -> new ChaoticPolymorphEffect(MobEffectCategory.HARMFUL, 16733695));

    //Potions
    public static final RegistryObject<FlightEffect> FLIGHT = EFFECTS.register("flight", () -> new FlightEffect(MobEffectCategory.BENEFICIAL, 11397104));
    public static final RegistryObject<FallControlEffect> FALL_CONTROL = EFFECTS.register("fall_control", () -> new FallControlEffect(MobEffectCategory.BENEFICIAL, 2404069));
    public static final RegistryObject<SleepEffect> SLEEP = EFFECTS.register("sleep", () -> new SleepEffect(MobEffectCategory.HARMFUL, 6299744));
    public static final RegistryObject<TeleblockEffect> TELEBLOCK = EFFECTS.register("teleblock", () -> new TeleblockEffect(MobEffectCategory.HARMFUL, 740674));

    //Unused
    /*public static final RegistryObject<MobEffect> FROST_ARMOR = EFFECTS.register("frost_armor", () -> new FrostArmorEffect(MobEffectCategory.BENEFICIAL, 5636095)
            .addAttributeModifier(Attributes.ARMOR, "b8abc70f-e827-452a-9b54-85a8e0aacdcd", 5.0D, AttributeModifier.Operation.ADDITION));
    public static final RegistryObject<WitchArmorEffect> WITCH_ARMOR = EFFECTS.register("witch_armor", () -> new WitchArmorEffect(MobEffectCategory.BENEFICIAL, 16733695));
    public static final RegistryObject<NumbnessEffect> NUMBNESS = EFFECTS.register("numbness", () -> new NumbnessEffect(MobEffectCategory.BENEFICIAL, 0));
    public static final RegistryObject<GreaterInvisibilityEffect> GREATER_INVISIBILITY = EFFECTS.register("greater_invisibility", () -> new GreaterInvisibilityEffect(MobEffectCategory.BENEFICIAL, 16185078));
    public static final RegistryObject<PerfectInvisibilityEffect> PERFECT_INVISIBILITY = EFFECTS.register("perfect_invisibility", () -> new PerfectInvisibilityEffect(MobEffectCategory.BENEFICIAL, 16185078));*/
}
