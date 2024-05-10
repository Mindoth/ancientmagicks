package net.mindoth.ancientmagicks.client.keybinds;

import net.mindoth.ancientmagicks.AncientMagicks;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.awt.event.KeyEvent;

@OnlyIn(Dist.CLIENT)
public class AncientMagicksKeyBinds {
    public static KeyBinding spellSelector;

    public static void register(final FMLClientSetupEvent event) {
        spellSelector = create("spell_selector", KeyEvent.VK_V);

        ClientRegistry.registerKeyBinding(spellSelector);
    }

    private static KeyBinding create(String name, int key) {
        return new KeyBinding("key." + AncientMagicks.MOD_ID + "." + name, key, "key.category." + AncientMagicks.MOD_ID);
    }
}
