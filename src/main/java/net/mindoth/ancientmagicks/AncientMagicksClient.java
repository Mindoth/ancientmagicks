package net.mindoth.ancientmagicks;

import net.mindoth.ancientmagicks.client.model.SimpleRobeModel;
import net.mindoth.ancientmagicks.client.screen.AncientMagicksScreen;
import net.mindoth.ancientmagicks.client.screen.GuiSpellWheel;
import net.mindoth.ancientmagicks.client.screen.HudCurrentSpell;
import net.mindoth.ancientmagicks.client.screen.HudMana;
import net.mindoth.ancientmagicks.config.AncientMagicksClientConfig;
import net.mindoth.ancientmagicks.item.DyeableMagicItem;
import net.mindoth.ancientmagicks.item.SpellBookItem;
import net.mindoth.ancientmagicks.item.armor.ColorableMagickArmorItem;
import net.mindoth.ancientmagicks.item.castingitem.CastingItem;
import net.mindoth.ancientmagicks.item.castingitem.ColorableStaffItem;
import net.mindoth.ancientmagicks.item.form.ProjectileRenderer;
import net.mindoth.ancientmagicks.network.AncientMagicksNetwork;
import net.mindoth.ancientmagicks.network.PacketSendRuneData;
import net.mindoth.ancientmagicks.registries.AncientMagicksEntities;
import net.mindoth.ancientmagicks.registries.AncientMagicksKeyBinds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.*;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;

public class AncientMagicksClient {

    public static void registerHandlers() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(AncientMagicksClient::registerEntityRenderers);
        modBus.addListener(AncientMagicksClient::registerItemColors);
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> modBus.addListener(AncientMagicksClient::registerLayerDefinitions));
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, AncientMagicksClientConfig.SPEC, "ancientmagicks-client.toml");
    }

    public static void registerItemColors(RegisterColorHandlersEvent.Item event) {
        for ( Item item : ForgeRegistries.ITEMS.getValues() ) {
            if ( item instanceof ColorableMagickArmorItem || item instanceof ColorableStaffItem || item instanceof SpellBookItem ) {
                event.getItemColors().register((color, armor) -> armor > 0 ? -1 : ((DyeableMagicItem)color.getItem()).getColor(color), item);
            }
        }
    }

    private static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(AncientMagicksEntities.SPELL_PROJECTILE.get(), ProjectileRenderer::new);
    }

    public static final ModelLayerLocation SIMPLE_ROBE = new ModelLayerLocation(new ResourceLocation(AncientMagicks.MOD_ID, "main"), "simple_robe");

    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(SIMPLE_ROBE, SimpleRobeModel::createBodyLayer);
    }

    @Mod.EventBusSubscriber(modid = AncientMagicks.MOD_ID, value = Dist.CLIENT)
    public static class ClientForgeEvents {

        @SubscribeEvent
        public static void onKeyInput(InputEvent.Key event) {
            Minecraft mc = Minecraft.getInstance();
            if ( mc.level == null ) return;
            onInput(mc, event.getKey(), event.getAction());
        }

        private static void onInput(Minecraft mc, int key, int keyAction) {
            int spellSelector = AncientMagicksKeyBinds.SPELL_SELECTOR.getKey().getValue();
            int inventory = mc.options.keyInventory.getKey().getValue();
            Player player = mc.player;
            if ( mc.screen instanceof AncientMagicksScreen ) {
                if ( key == inventory ) player.closeContainer();
                if ( mc.screen instanceof GuiSpellWheel && key == spellSelector ) {
                    if ( (keyAction == 0 && AncientMagicksClientConfig.GUI_SPELL_WHEEL_HOLD.get())
                            || (keyAction == 1 && !AncientMagicksClientConfig.GUI_SPELL_WHEEL_HOLD.get()) ) {
                        player.closeContainer();
                    }
                }
            }
            else if ( mc.screen == null && key == spellSelector && keyAction == 1 ) {
                if ( CastingItem.canOpenWheel(player) ) AncientMagicksNetwork.sendToServer(new PacketSendRuneData());
            }
        }
    }

    @Mod.EventBusSubscriber(modid = AncientMagicks.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModBusEvents {

        @SubscribeEvent
        public static void onKeyRegister(RegisterKeyMappingsEvent event) {
            event.register(AncientMagicksKeyBinds.SPELL_SELECTOR);
        }

        @SubscribeEvent
        public static void registerGuiOverlays(RegisterGuiOverlaysEvent event) {
            event.registerAboveAll("current_spell_hud", HudCurrentSpell.OVERLAY);
            event.registerAbove(VanillaGuiOverlay.EXPERIENCE_BAR.id(), "mana_hud", HudMana.OVERLAY);
        }
    }
}
