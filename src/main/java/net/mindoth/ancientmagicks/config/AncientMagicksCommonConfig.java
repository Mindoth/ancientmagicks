package net.mindoth.ancientmagicks.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class AncientMagicksCommonConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;
    public static final ForgeConfigSpec.ConfigValue<Boolean> PVP;

    static {
        BUILDER.push("Configs for Ancient Magicks");

        PVP = BUILDER.comment("true = Harmful spells can hurt and target other players. false = Harmful spells will ignore players (Default = true)")
                .define("pvp", true);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
