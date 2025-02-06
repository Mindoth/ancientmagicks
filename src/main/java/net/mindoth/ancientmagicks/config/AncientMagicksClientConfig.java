package net.mindoth.ancientmagicks.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class AncientMagicksClientConfig {

    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Boolean> GUI_SPELL_WHEEL_HOLD;
    public static final ForgeConfigSpec.ConfigValue<Integer> MANA_BAR_X_OFFSET;
    public static final ForgeConfigSpec.ConfigValue<Integer> MANA_BAR_Y_OFFSET;

    static {
        BUILDER.push("Client Configs for Ancient Magicks");

        GUI_SPELL_WHEEL_HOLD = BUILDER.comment("Set this to true if you want the Spell Wheel Gui to close when you let go of the key.")
                .define("guiSpellWheelHold", false);

        MANA_BAR_X_OFFSET = BUILDER.comment("X Offset for mana bar in hud.")
                .define("manaBarXOffset", 0);

        MANA_BAR_Y_OFFSET = BUILDER.comment("Y Offset for mana bar in hud.")
                .define("manaBarYOffset", 0);


        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
