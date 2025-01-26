package net.mindoth.ancientmagicks;

import net.mindoth.ancientmagicks.client.screen.GuiSpellWheel;
import net.mindoth.ancientmagicks.client.screen.HudCurrentSpell;
import net.mindoth.ancientmagicks.client.screen.HudMana;
import net.mindoth.ancientmagicks.item.armor.ColorableMagicArmorItem;
import net.mindoth.ancientmagicks.item.DyeableMagicItem;
import net.mindoth.ancientmagicks.item.castingitem.CastingItem;
import net.mindoth.ancientmagicks.item.castingitem.ColorableStaffItem;
import net.mindoth.ancientmagicks.item.spell.abstractspell.spellpearl.SpellPearlRenderer;
import net.mindoth.ancientmagicks.item.spell.acidarrow.AcidArrowRenderer;
import net.mindoth.ancientmagicks.item.spell.burnlance.BurnLanceRenderer;
import net.mindoth.ancientmagicks.item.spell.fireball.FireballRenderer;
import net.mindoth.ancientmagicks.item.spell.firebolt.FireBoltRenderer;
import net.mindoth.ancientmagicks.item.spell.freezelance.FreezeLanceRenderer;
import net.mindoth.ancientmagicks.item.spell.blizzard.IcicleRenderer;
import net.mindoth.ancientmagicks.item.spell.waterbolt.WaterBoltRenderer;
import net.mindoth.ancientmagicks.item.spell.witcharrow.WitchArrowRenderer;
import net.mindoth.ancientmagicks.network.AncientMagicksNetwork;
import net.mindoth.ancientmagicks.network.PacketSendRuneData;
import net.mindoth.ancientmagicks.registries.AncientMagicksEntities;
import net.mindoth.ancientmagicks.registries.AncientMagicksKeyBinds;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.*;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;

public class AncientMagicksClient {

    public static void registerHandlers() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(AncientMagicksClient::registerEntityRenderers);
        modBus.addListener(AncientMagicksClient::registerItemColors);
    }

    public static void registerItemColors(RegisterColorHandlersEvent.Item event) {
        for ( Item item : ForgeRegistries.ITEMS.getValues() ) {
            if ( item instanceof ColorableMagicArmorItem || item instanceof ColorableStaffItem ) {
                event.getItemColors().register((color, armor) -> {
                    return armor > 0 ? -1 : ((DyeableMagicItem)color.getItem()).getColor(color);
                }, item);
            }
        }
    }


    private static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(AncientMagicksEntities.WITCH_ARROW.get(), WitchArrowRenderer::new);
        event.registerEntityRenderer(AncientMagicksEntities.FIREBALL.get(), FireballRenderer::new);
        event.registerEntityRenderer(AncientMagicksEntities.WATER_BOLT.get(), WaterBoltRenderer::new);
        event.registerEntityRenderer(AncientMagicksEntities.SPELL_PEARL.get(), SpellPearlRenderer::new);
        event.registerEntityRenderer(AncientMagicksEntities.ICICLE.get(), IcicleRenderer::new);
        event.registerEntityRenderer(AncientMagicksEntities.FREEZE_LANCE.get(), FreezeLanceRenderer::new);
        event.registerEntityRenderer(AncientMagicksEntities.BURN_LANCE.get(), BurnLanceRenderer::new);
        event.registerEntityRenderer(AncientMagicksEntities.FIRE_BOLT.get(), FireBoltRenderer::new);
        event.registerEntityRenderer(AncientMagicksEntities.ACID_ARROW.get(), AcidArrowRenderer::new);
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
            if ( mc.screen instanceof GuiSpellWheel ) {
                if ( key == inventory || (keyAction == 0 && key == spellSelector) ) player.closeContainer();
            }
            else if ( mc.screen == null && key == spellSelector && keyAction == 1 ) {
                if ( !CastingItem.getHeldStaff(player).isEmpty() ) AncientMagicksNetwork.sendToServer(new PacketSendRuneData());
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
