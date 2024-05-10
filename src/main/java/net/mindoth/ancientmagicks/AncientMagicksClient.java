package net.mindoth.ancientmagicks;

import net.mindoth.ancientmagicks.client.gui.GuiWand;
import net.mindoth.ancientmagicks.client.keybinds.AncientMagicksKeyBinds;
import net.mindoth.ancientmagicks.item.spellrune.blackhole.BlackHoleRenderer;
import net.mindoth.ancientmagicks.item.spellrune.enderbolt.EnderBoltRenderer;
import net.mindoth.ancientmagicks.item.spellrune.fireball.FireballRenderer;
import net.mindoth.ancientmagicks.item.spellrune.witchspark.WitchSparkRenderer;
import net.mindoth.ancientmagicks.registries.AncientMagicksContainers;
import net.mindoth.ancientmagicks.registries.AncientMagicksEntities;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.entity.SkeletonRenderer;
import net.minecraft.client.renderer.entity.TNTRenderer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class AncientMagicksClient {

    public static void registerHandlers() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(AncientMagicksClient::clientSetup);
    }

    public static void clientSetup(FMLClientSetupEvent event) {
        AncientMagicksKeyBinds.register(event);
        ScreenManager.register(AncientMagicksContainers.WAND_CONTAINER.get(), GuiWand::new);
        RenderingRegistry.registerEntityRenderingHandler(AncientMagicksEntities.FIREBALL.get(), FireballRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(AncientMagicksEntities.WITCH_SPARK.get(), WitchSparkRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(AncientMagicksEntities.DYNAMITE.get(), TNTRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(AncientMagicksEntities.SKELETON_MINION.get(), SkeletonRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(AncientMagicksEntities.ENDER_BOLT.get(), EnderBoltRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(AncientMagicksEntities.BLACK_HOLE.get(), BlackHoleRenderer::new);
    }
}
