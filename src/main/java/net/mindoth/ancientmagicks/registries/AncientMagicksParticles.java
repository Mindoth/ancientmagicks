package net.mindoth.ancientmagicks.registries;

import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.client.particle.*;
import net.minecraft.client.Minecraft;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

@Mod.EventBusSubscriber(modid = AncientMagicks.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AncientMagicksParticles {

    @ObjectHolder(AncientMagicks.MOD_ID + ":" + EmberParticleData.NAME) public static ParticleType<ColoredDynamicTypeData> EMBER_TYPE;

    @SubscribeEvent
    public static void registerParticles(RegistryEvent.Register<ParticleType<?>> event) {
        IForgeRegistry<ParticleType<?>> r = event.getRegistry();
        r.register(new EmberParticleType().setRegistryName(EmberParticleData.NAME));
    }

    @SuppressWarnings("resource")
    @SubscribeEvent
    public static void registerFactories(ParticleFactoryRegisterEvent event) {
        Minecraft.getInstance().particleEngine.register(EMBER_TYPE, EmberParticleData::new);
    }
}
