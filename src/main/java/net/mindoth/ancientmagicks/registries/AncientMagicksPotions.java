package net.mindoth.ancientmagicks.registries;

import net.mindoth.ancientmagicks.AncientMagicks;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class AncientMagicksPotions {
    public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(ForgeRegistries.POTIONS, AncientMagicks.MOD_ID);

    public static final RegistryObject<Potion> FLIGHT_POTION = POTIONS.register("flight_potion",
            () -> new Potion(new MobEffectInstance(AncientMagicksEffects.FLIGHT.get(), 600, 0)));

    public static final RegistryObject<Potion> FALL_CONTROL_POTION = POTIONS.register("fall_control_potion",
            () -> new Potion(new MobEffectInstance(AncientMagicksEffects.FALL_CONTROL.get(), 1800, 0)));

    public static final RegistryObject<Potion> MIND_CONTROL_POTION = POTIONS.register("mind_control_potion",
            () -> new Potion(new MobEffectInstance(AncientMagicksEffects.MIND_CONTROL.get(), 600, 0)));

    public static final RegistryObject<Potion> SLEEP_POTION = POTIONS.register("sleep_potion",
            () -> new Potion(new MobEffectInstance(AncientMagicksEffects.SLEEP.get(), 600, 0)));

    public static final RegistryObject<Potion> POLYMORPH_POTION = POTIONS.register("polymorph_potion",
            () -> new Potion(new MobEffectInstance(AncientMagicksEffects.POLYMORPH.get(), 600, 0)));

    public static final RegistryObject<Potion> TELEBLOCK_POTION = POTIONS.register("teleblock_potion",
            () -> new Potion(new MobEffectInstance(AncientMagicksEffects.TELEBLOCK.get(), 600, 0)));

    public static final RegistryObject<Potion> ALACRITY_POTION = POTIONS.register("alacrity_potion",
            () -> new Potion(new MobEffectInstance(AncientMagicksEffects.ALACRITY.get(), 1800, 0)));
}
