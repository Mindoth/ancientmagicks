package net.mindoth.ancientmagicks.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class AncientMagicksClientConfig {

    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Boolean> GUI_SPELL_WHEEL_HOLD;

    static {
        BUILDER.push("Client Configs for Ancient Magicks");

        GUI_SPELL_WHEEL_HOLD = BUILDER.comment("Set this to true if you want the Spell Wheel Gui to close when you let go of the key.")
                .define("guiSpellWheelHold", false);


        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
