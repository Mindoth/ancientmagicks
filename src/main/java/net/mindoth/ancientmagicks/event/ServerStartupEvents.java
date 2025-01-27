package net.mindoth.ancientmagicks.event;

import com.google.common.collect.Lists;
import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.config.AncientMagicksCommonConfig;
import net.mindoth.ancientmagicks.item.ColorRuneItem;
import net.mindoth.ancientmagicks.item.spell.abstractspell.SpellItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.Random;

@Mod.EventBusSubscriber(modid = AncientMagicks.MOD_ID)
public class ServerStartupEvents {

    @SubscribeEvent
    public static void onServerStart(final ServerStartingEvent event) {
        Logger logger = AncientMagicks.getLogger();
        long seed = event.getServer().getLevel(ServerLevel.OVERWORLD).getSeed();
        Random seededRand = new Random(seed);

        spellComboFileHandling(seededRand);
        arcaneDustRecipeHandling(seededRand);
    }

    public static List<Item> ARCANE_DUST_LIST = Lists.newArrayList();

    private static void arcaneDustRecipeHandling(Random seededRand) {
        List<Item> vanillaList = Lists.newArrayList();
        List<Item> disabledList = Lists.newArrayList();
        List<String> configString = AncientMagicksCommonConfig.DISABLED_ARCANE_DUST_RECIPE_ENTRIES.get();
        configString.forEach(string -> disabledList.add(ForgeRegistries.ITEMS.getValue(new ResourceLocation(string))));
        ForgeRegistries.ITEMS.getValues().forEach(item -> {
            if ( (ForgeRegistries.ITEMS.getKey(item).toString().split(":")[0]).equals("minecraft")
            && !(disabledList.contains(item)) ) vanillaList.add(item);
        });
        for ( int i = 0; i < 9; i++ ) {
            int index = seededRand.nextInt(vanillaList.size());
            Item item = vanillaList.get(index);
            ARCANE_DUST_LIST.add(item);
        }
        System.out.println("ARCANE LIST: " + ARCANE_DUST_LIST);
    }

    //TODO remove the ENTIRE system changing COMBO_MAP to string and back in ColorRuneItem. Maybe iterate through itemStacks into a list when sending packets?
    //public static final String COMBOFILEPATH = "am_spellrecipes.json";
    private static void spellComboFileHandling(Random seededRand) {
        AncientMagicks.randomizeSpells(seededRand);

        StringBuilder tempString = new StringBuilder();
        for ( Map.Entry<SpellItem, List<ColorRuneItem>> entry : AncientMagicks.COMBO_MAP.entrySet() ) {
            tempString.append(ForgeRegistries.ITEMS.getKey(entry.getKey())).append("=").append(entry.getValue()).append(";").append("\n");
        }

        String comboString = tempString.toString().replaceAll("\n", "").replaceAll(".$", "");

        ColorRuneItem.CURRENT_COMBO_TAG.putString("am_combostring", comboString);
        ColorRuneItem.CURRENT_COMBO_MAP = ColorRuneItem.buildComboMap(ColorRuneItem.CURRENT_COMBO_TAG.getString("am_combostring"));
    }

    //Overcomplicated file creating/reading system for spell combos...
    /*private static void spellComboFileHandling(ServerStartingEvent event) {
        Logger logger = AncientMagicks.getLogger();
        Path savePath = event.getServer().getWorldPath(LevelResource.ROOT).normalize().toAbsolutePath();
        String filepath = savePath + "/" + COMBOFILEPATH;

        //Creating file
        try {
            File file = new File(filepath);
            if ( !file.isFile() ) {
                AncientMagicks.randomizeSpells();

                FileOutputStream fos = new FileOutputStream(file);
                PrintWriter pw = new PrintWriter(fos);

                for ( Map.Entry<SpellItem, List<ColorRuneItem>> entry : AncientMagicks.COMBO_MAP.entrySet() ) {
                    pw.print(ForgeRegistries.ITEMS.getKey(entry.getKey()) + "=" + entry.getValue() + ";" + "\n");
                }

                pw.flush();
                pw.close();
                fos.close();
                logger.info("Successfully created spell combo recipes for Ancient Magicks.");
            }
        }
        catch ( IOException exception ) {
            logger.error("ERROR! CREATING SPELL COMBO RECIPES FOR ANCIENT MAGICKS.");
        }

        //Reading file
        try {
            File file = new File(filepath);
            if ( file.isFile() ) {
                FileInputStream fis = new FileInputStream(file);
                String comboString = Files.readString(savePath.resolve(COMBOFILEPATH)).replaceAll("\n", "").replaceAll(".$", "");
                ColorRuneItem.CURRENT_COMBO_TAG.putString("am_combostring", comboString);

                ColorRuneItem.CURRENT_COMBO_MAP = ColorRuneItem.buildComboMap(ColorRuneItem.CURRENT_COMBO_TAG.getString("am_combostring"));
                //Splitter.on(";").withKeyValueSeparator("=").split(ColorRuneItem.CURRENT_COMBO_TAG.getString("am_combostring"))

                fis.close();
                logger.info("Successfully read spell combo recipes for Ancient Magicks.");
            }
            else logger.error("ERROR! SPELL COMBO RECIPE FILE NOT FOUND.");
        }
        catch ( Exception exception ) {
            exception.printStackTrace();
            logger.error("ERROR! READING SPELL COMBO RECIPES FOR ANCIENT MAGICKS.");
        }
    }*/
}
