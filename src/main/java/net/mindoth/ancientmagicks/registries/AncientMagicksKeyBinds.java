package net.mindoth.ancientmagicks.registries;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public class AncientMagicksKeyBinds {

    public static final String KEY_SPELLSELECTOR = "key.ancientmagicks.spellselector";
    public static final String KEY_CATEGORY_ANCIENTMAGICKS = "key.category.ancientmagicks";
    public static final KeyMapping SPELLSELECTOR = new KeyMapping(KEY_SPELLSELECTOR, KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_V, KEY_CATEGORY_ANCIENTMAGICKS);
}
