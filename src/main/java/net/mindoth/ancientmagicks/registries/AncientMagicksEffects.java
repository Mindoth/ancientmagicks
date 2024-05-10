package net.mindoth.ancientmagicks.registries;

import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.item.spellrune.abstractspell.summon.SummonTimer;
import net.mindoth.ancientmagicks.item.spellrune.flight.FlightEffect;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class AncientMagicksEffects {
    public static final DeferredRegister<Effect> EFFECTS = DeferredRegister.create(ForgeRegistries.POTIONS, AncientMagicks.MOD_ID);

    //public static final RegistryObject<GhostWalkEffect> GHOST_WALK = EFFECTS.register("ghost_walk", () -> new GhostWalkEffect(EffectType.BENEFICIAL, 3124687));
    public static final RegistryObject<SummonTimer> SKELETON_TIMER = EFFECTS.register("skeleton_timer", () -> new SummonTimer(EffectType.BENEFICIAL, 0xbea925));
    public static final RegistryObject<FlightEffect> FLIGHT = EFFECTS.register("flight", () -> new FlightEffect(EffectType.BENEFICIAL, 16773073));
}
