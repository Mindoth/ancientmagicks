package net.mindoth.ancientmagicks;

import net.mindoth.ancientmagicks.client.gui.CurrentSpellHud;
import net.mindoth.ancientmagicks.client.gui.GuiAMBag;
import net.mindoth.ancientmagicks.client.gui.GuiSpellWheel;
import net.mindoth.ancientmagicks.client.gui.ManaHud;
import net.mindoth.ancientmagicks.item.castingitem.CastingItem;
import net.mindoth.ancientmagicks.item.spell.burnlance.BurnLanceRenderer;
import net.mindoth.ancientmagicks.item.spell.fireball.FireballRenderer;
import net.mindoth.ancientmagicks.item.spell.firebolt.FireBoltRenderer;
import net.mindoth.ancientmagicks.item.spell.freezelance.FreezeLanceRenderer;
import net.mindoth.ancientmagicks.item.spell.icicle.IcicleRenderer;
import net.mindoth.ancientmagicks.item.spell.slimeball.SlimeballRenderer;
import net.mindoth.ancientmagicks.item.spell.abstractspell.spellpearl.SpellPearlRenderer;
import net.mindoth.ancientmagicks.item.spell.thunderball.ThunderballRenderer;
import net.mindoth.ancientmagicks.item.spell.witcharrow.WitchArrowRenderer;
import net.mindoth.ancientmagicks.network.AncientMagicksNetwork;
import net.mindoth.ancientmagicks.network.PacketSendRuneData;
import net.mindoth.ancientmagicks.registries.AncientMagicksContainers;
import net.mindoth.ancientmagicks.registries.AncientMagicksEntities;
import net.mindoth.ancientmagicks.registries.AncientMagicksKeyBinds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class AncientMagicksClient {

    public static void registerHandlers() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(AncientMagicksClient::clientSetup);
        modBus.addListener(AncientMagicksClient::registerEntityRenderers);
    }

    private static void clientSetup(final FMLClientSetupEvent event) {
        MenuScreens.register(AncientMagicksContainers.AMBAG_CONTAINER.get(), GuiAMBag::new);
    }

    private static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(AncientMagicksEntities.WITCH_ARROW.get(), WitchArrowRenderer::new);
        event.registerEntityRenderer(AncientMagicksEntities.FIREBALL.get(), FireballRenderer::new);
        event.registerEntityRenderer(AncientMagicksEntities.SLIMEBALL.get(), SlimeballRenderer::new);
        event.registerEntityRenderer(AncientMagicksEntities.SPELL_PEARL.get(), SpellPearlRenderer::new);
        event.registerEntityRenderer(AncientMagicksEntities.ICICLE.get(), IcicleRenderer::new);
        event.registerEntityRenderer(AncientMagicksEntities.FREEZE_LANCE.get(), FreezeLanceRenderer::new);
        event.registerEntityRenderer(AncientMagicksEntities.BURN_LANCE.get(), BurnLanceRenderer::new);
        event.registerEntityRenderer(AncientMagicksEntities.THUNDERBALL.get(), ThunderballRenderer::new);
        event.registerEntityRenderer(AncientMagicksEntities.FIRE_BOLT.get(), FireBoltRenderer::new);
    }

    @Mod.EventBusSubscriber(modid = AncientMagicks.MOD_ID, value = Dist.CLIENT)
    public static class ClientForgeEvents {

        @SubscribeEvent
        public static void onKeyInput(InputEvent.Key event) {
            Minecraft mc = Minecraft.getInstance();
            if ( mc.level == null ) return;
            onInput(mc, event.getKey(), event.getAction());
        }

        private static void onInput(Minecraft MINECRAFT, int key, int keyAction) {
            int SPELLSELECTOR = AncientMagicksKeyBinds.SPELLSELECTOR.getKey().getValue();
            int INVENTORY = MINECRAFT.options.keyInventory.getKey().getValue();
            ItemStack wand = CastingItem.getHeldCastingItem(MINECRAFT.player);
            if ( MINECRAFT.screen instanceof GuiSpellWheel ) {
                if ( key == INVENTORY || (keyAction == 0 && key == SPELLSELECTOR) ) MINECRAFT.player.closeContainer();
            }
            else if ( MINECRAFT.screen == null && key == SPELLSELECTOR && keyAction == 1 ) {
                if ( CastingItem.isValidCastingItem(wand) ) AncientMagicksNetwork.sendToServer(new PacketSendRuneData());
            }
        }
    }

    @Mod.EventBusSubscriber(modid = AncientMagicks.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModBusEvents {

        @SubscribeEvent
        public static void onKeyRegister(RegisterKeyMappingsEvent event) {
            event.register(AncientMagicksKeyBinds.SPELLSELECTOR);
        }

        @SubscribeEvent
        public static void registerGuiOverlays(RegisterGuiOverlaysEvent event) {
            event.registerAboveAll("current_spell_hud", CurrentSpellHud.OVERLAY);
            event.registerAbove(VanillaGuiOverlay.EXPERIENCE_BAR.id(), "mana_hud", ManaHud.OVERLAY);
        }
    }
}
