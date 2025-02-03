package net.mindoth.ancientmagicks.config;

import com.google.common.collect.Lists;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

public class AncientMagicksCommonConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Boolean> SPELL_FREE_FOR_ALL;
    public static final ForgeConfigSpec.ConfigValue<List<String>> DISABLED_SPELLS;
    public static final ForgeConfigSpec.ConfigValue<List<String>> DISABLED_POLYMOBS;
    public static final ForgeConfigSpec.ConfigValue<List<String>> DISABLED_ARCANE_DUST_RECIPE_ENTRIES;

    static {
        BUILDER.push("Configs for Ancient Magicks");

        SPELL_FREE_FOR_ALL = BUILDER.comment("true = All spells can target both friends and foes. false = Harmful spells will ignore friends and helpful spells will ignore foes.")
                .define("spellsFreeForAll", true);

        DISABLED_SPELLS = BUILDER.comment("Add any spells you wish to disable here.")
                .define("spellDisableList", Lists.newArrayList());

        DISABLED_POLYMOBS = BUILDER.comment("Add any mobs you wish to not summon with Chaotic Polymorph here.")
                .define("polyDisableList", Lists.newArrayList());

        DISABLED_ARCANE_DUST_RECIPE_ENTRIES = BUILDER.comment("Add any items you wish to not be required to craft Arcane Dust here.")
                .define("arcanedustRecipeEntryDisableList", disabledEntries());

        BUILDER.pop();
        SPEC = BUILDER.build();
    }

    private static List<String> disabledEntries() {
        List<String> list = Lists.newArrayList();

        list.add("minecraft:air");
        list.add("minecraft:command_block");
        list.add("minecraft:repeating_command_block");
        list.add("minecraft:chain_command_block");
        list.add("minecraft:command_block_minecart");
        list.add("minecraft:bedrock");
        list.add("minecraft:mob_spawner");
        list.add("minecraft:dragon_egg");
        list.add("minecraft:end_portal_frame");
        list.add("minecraft:farmland");
        list.add("minecraft:barrier");
        list.add("minecraft:structure_void");
        list.add("minecraft:structure_block");
        list.add("minecraft:potion");
        list.add("minecraft:splash_potion");
        list.add("minecraft:lingering_potion");
        list.add("minecraft:tipped_arrow");
        list.add("minecraft:white_shulker_box");
        list.add("minecraft:orange_shulker_box");
        list.add("minecraft:magenta_shulker_box");
        list.add("minecraft:light_blue_shulker_box");
        list.add("minecraft:yellow_shulker_box");
        list.add("minecraft:lime_shulker_box");
        list.add("minecraft:pink_shulker_box");
        list.add("minecraft:gray_shulker_box");
        list.add("minecraft:silver_shulker_box");
        list.add("minecraft:cyan_shulker_box");
        list.add("minecraft:purple_shulker_box");
        list.add("minecraft:blue_shulker_box");
        list.add("minecraft:brown_shulker_box");
        list.add("minecraft:green_shulker_box");
        list.add("minecraft:red_shulker_box");
        list.add("minecraft:black_shulker_box");
        list.add("minecraf:chorus_plant");
        list.add("minecraft:grass_path");

        list.add("minecraft:spawn_egg");
        list.add("minecraft:monster_egg");

        ForgeRegistries.ITEMS.getValues().forEach(item -> {
            if ( item instanceof SpawnEggItem ) list.add(ForgeRegistries.ITEMS.getKey(item).toString());
        });

        return list;
    }
}
