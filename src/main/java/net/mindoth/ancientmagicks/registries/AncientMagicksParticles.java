package net.mindoth.ancientmagicks.registries;

import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.client.particle.ember.ColoredDynamicTypeData;
import net.mindoth.ancientmagicks.client.particle.ember.EmberParticleProvider;
import net.mindoth.ancientmagicks.client.particle.ember.EmberParticleType;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleType;
<<<<<<< Updated upstream
=======
import net.minecraftforge.api.distmarker.Dist;
>>>>>>> Stashed changes
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = AncientMagicks.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class AncientMagicksParticles {

    public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, AncientMagicks.MOD_ID);

    public static final RegistryObject<ParticleType<ColoredDynamicTypeData>> EMBER_TYPE = PARTICLES.register(EmberParticleProvider.NAME, EmberParticleType::new);

    @SubscribeEvent
    public static void registerFactories(RegisterParticleProvidersEvent evt) {
        Minecraft.getInstance().particleEngine.register(EMBER_TYPE.get(), EmberParticleProvider::new);
    }
}
