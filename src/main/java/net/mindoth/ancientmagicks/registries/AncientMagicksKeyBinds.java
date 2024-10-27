package net.mindoth.ancientmagicks.registries;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public class AncientMagicksKeyBinds {

    public static final String KEY_SPELL_SELECTOR = "key.ancientmagicks.spell_selector";
    public static final String KEY_CATEGORY_ANCIENTMAGICKS = "key.category.ancientmagicks";
    public static final KeyMapping SPELL_SELECTOR = new KeyMapping(KEY_SPELL_SELECTOR, KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_V, KEY_CATEGORY_ANCIENTMAGICKS);
}
