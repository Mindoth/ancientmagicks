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
            () -> new Potion(new MobEffectInstance(AncientMagicksEffects.FLIGHT.get(), 600)));

    public static final RegistryObject<Potion> LONG_FLIGHT_POTION = POTIONS.register("long_flight_potion",
            () -> new Potion(new MobEffectInstance(AncientMagicksEffects.FLIGHT.get(), 1200)));

    public static final RegistryObject<Potion> FALL_CONTROL_POTION = POTIONS.register("fall_control_potion",
            () -> new Potion(new MobEffectInstance(AncientMagicksEffects.FALL_CONTROL.get(), 1800)));

    public static final RegistryObject<Potion> LONG_FALL_CONTROL_POTION = POTIONS.register("long_fall_control_potion",
            () -> new Potion(new MobEffectInstance(AncientMagicksEffects.FALL_CONTROL.get(), 4800)));

    public static final RegistryObject<Potion> SLEEP_POTION = POTIONS.register("sleep_potion",
            () -> new Potion(new MobEffectInstance(AncientMagicksEffects.SLEEP.get(), 600)));

    public static final RegistryObject<Potion> LONG_SLEEP_POTION = POTIONS.register("long_sleep_potion",
            () -> new Potion(new MobEffectInstance(AncientMagicksEffects.SLEEP.get(), 1200)));

    public static final RegistryObject<Potion> TELEBLOCK_POTION = POTIONS.register("teleblock_potion",
            () -> new Potion(new MobEffectInstance(AncientMagicksEffects.TELEBLOCK.get(), 600)));

    public static final RegistryObject<Potion> LONG_TELEBLOCK_POTION = POTIONS.register("long_teleblock_potion",
            () -> new Potion(new MobEffectInstance(AncientMagicksEffects.TELEBLOCK.get(), 1200)));

    public static final RegistryObject<Potion> POLYMORPH_POTION = POTIONS.register("polymorph_potion",
            () -> new Potion(new MobEffectInstance(AncientMagicksEffects.POLYMORPH.get(), 600)));

    public static final RegistryObject<Potion> LONG_POLYMORPH_POTION = POTIONS.register("long_polymorph_potion",
            () -> new Potion(new MobEffectInstance(AncientMagicksEffects.POLYMORPH.get(), 1200)));

    public static final RegistryObject<Potion> CHAOTIC_POLYMORPH_POTION = POTIONS.register("chaotic_polymorph_potion",
            () -> new Potion(new MobEffectInstance(AncientMagicksEffects.CHAOTIC_POLYMORPH.get())));
}
