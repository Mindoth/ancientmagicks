package net.mindoth.ancientmagicks.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class AncientMagicksCommonConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Boolean> FREE_SPELLS;
    public static final ForgeConfigSpec.ConfigValue<Boolean> PVP;
    public static final ForgeConfigSpec.ConfigValue<String> DISABLED_SPELLS;
    public static final ForgeConfigSpec.ConfigValue<String> DISABLED_POLYMOBS;

    static {
        BUILDER.push("Configs for Ancient Magicks");

        FREE_SPELLS = BUILDER.comment("true = Spellcasting is free. false = Spellcasting is not free (Default = true)")
                .define("freespells", true);

        PVP = BUILDER.comment("true = Harmful spells can hurt and target other players. false = Harmful spells will ignore players (Default = true)")
                .define("pvp", true);

        DISABLED_SPELLS = BUILDER.comment("Add any spells you wish to disable here in the following format: [ancientmagicks:ward, ancientmagicks:fireball] (Default = [])")
                .define("spelldisablelist", "[]");

        DISABLED_POLYMOBS = BUILDER.comment("Add any mobs you wish to not summon with Chaotic Polymorph here in the following format: [minecraft:sheep, minecraft:pig] (Default = [])")
                .define("polydisablelist", "[]");

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
