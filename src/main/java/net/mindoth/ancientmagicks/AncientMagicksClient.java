package net.mindoth.ancientmagicks;

import net.mindoth.ancientmagicks.item.spell.projectile.WitchSparkRenderer;
import net.mindoth.ancientmagicks.registries.AncientMagicksEntities;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class AncientMagicksClient {

    public static void registerHandlers() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(AncientMagicksClient::registerEntityRenderers);
    }

    private static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(AncientMagicksEntities.WITCH_SPARK.get(), WitchSparkRenderer::new);
    }
}
