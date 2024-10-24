package net.mindoth.ancientmagicks.registries;

import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.item.spell.alacrity.AlacrityEffect;
import net.mindoth.ancientmagicks.item.spell.fly.FlightEffect;
import net.mindoth.ancientmagicks.item.spell.ghostwalk.GhostwalkEffect;
import net.mindoth.ancientmagicks.item.spell.greaterinvisibility.GreaterInvisibilityEffect;
import net.mindoth.ancientmagicks.item.spell.manashield.ManaShieldEffect;
import net.mindoth.ancientmagicks.item.spell.witcharmor.WitchArmorEffect;
import net.mindoth.ancientmagicks.item.spell.mindcontrol.MindControlEffect;
import net.mindoth.ancientmagicks.item.spell.numbpain.NumbnessEffect;
import net.mindoth.ancientmagicks.item.spell.sleep.SleepEffect;
import net.mindoth.ancientmagicks.item.spell.spook.SpookEffect;
import net.mindoth.ancientmagicks.item.spell.teleblock.TeleblockEffect;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class AncientMagicksEffects {
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, AncientMagicks.MOD_ID);

    public static final RegistryObject<WitchArmorEffect> WITCH_ARMOR = EFFECTS.register("witch_armor", () -> new WitchArmorEffect(MobEffectCategory.BENEFICIAL, 16733695));
    public static final RegistryObject<FlightEffect> FLIGHT = EFFECTS.register("flight", () -> new FlightEffect(MobEffectCategory.BENEFICIAL, 16733695));
    public static final RegistryObject<NumbnessEffect> NUMBNESS = EFFECTS.register("numbness", () -> new NumbnessEffect(MobEffectCategory.BENEFICIAL, 16777215));
    public static final RegistryObject<SpookEffect> SPOOK = EFFECTS.register("spook", () -> new SpookEffect(MobEffectCategory.BENEFICIAL, 0));
    public static final RegistryObject<GhostwalkEffect> GHOSTWALK = EFFECTS.register("ghostwalk", () -> new GhostwalkEffect(MobEffectCategory.BENEFICIAL, 0));
    public static final RegistryObject<MindControlEffect> MIND_CONTROL = EFFECTS.register("mind_control", () -> new MindControlEffect(MobEffectCategory.HARMFUL, 0));
    public static final RegistryObject<MobEffect> ALACRITY = EFFECTS.register("alacrity", () -> new AlacrityEffect(MobEffectCategory.BENEFICIAL, 16733695)
            .addAttributeModifier(Attributes.MOVEMENT_SPEED, "9ca9c968-0f1e-4125-97ba-c6ef04276c7a", 0.2D, AttributeModifier.Operation.MULTIPLY_TOTAL)
            .addAttributeModifier(Attributes.ATTACK_SPEED, "9ca9c968-0f1e-4125-97ba-c6ef04276c7a", 0.1D, AttributeModifier.Operation.MULTIPLY_TOTAL));
    public static final RegistryObject<SleepEffect> SLEEP = EFFECTS.register("sleep", () -> new SleepEffect(MobEffectCategory.HARMFUL, 16185078));
    public static final RegistryObject<TeleblockEffect> TELEBLOCK = EFFECTS.register("teleblock", () -> new TeleblockEffect(MobEffectCategory.HARMFUL, 16733695));
    public static final RegistryObject<GreaterInvisibilityEffect> GREATER_INVISIBILITY = EFFECTS.register("greater_invisibility",
            () -> new GreaterInvisibilityEffect(MobEffectCategory.BENEFICIAL, 16733695));
    public static final RegistryObject<ManaShieldEffect> MANA_SHIELD = EFFECTS.register("mana_shield", () -> new ManaShieldEffect(MobEffectCategory.BENEFICIAL, 5592575));
}
