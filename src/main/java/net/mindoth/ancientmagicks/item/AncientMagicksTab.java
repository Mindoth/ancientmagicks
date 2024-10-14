package net.mindoth.ancientmagicks.item;

import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.registries.AncientMagicksItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class AncientMagicksTab {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB,
            AncientMagicks.MOD_ID);

    public static final RegistryObject<CreativeModeTab> ANCIENTMAGICKS_TAB = CREATIVE_MODE_TABS.register("ancientmagicks_tab", () ->
            CreativeModeTab.builder().icon(() -> new ItemStack(AncientMagicksItems.CHAOTIC_POLYMORPH_ITEM.get())).title(Component.translatable("itemGroup.ancientmagicks_tab")).build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
