package net.mindoth.ancientmagicks.event;

import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.item.ColorRuneItem;
import net.mindoth.ancientmagicks.item.castingitem.TabletItem;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber(modid = AncientMagicks.MOD_ID)
public class RandomizeSpellCombos {

    @SubscribeEvent
    public static void onServerStart(final ServerAboutToStartEvent event) {
        Logger logger = AncientMagicks.getLogger();
        Path savePath = event.getServer().getWorldPath(LevelResource.ROOT).normalize().toAbsolutePath();
        String filepath = savePath + "/ancientmagicks.json";

        //Creating file
        try {
            File file = new File(filepath);
            if ( !file.isFile() ) {
                AncientMagicks.randomizeSpells();

                FileOutputStream fos = new FileOutputStream(file);
                PrintWriter pw = new PrintWriter(fos);

                //TODO MAKE FILE NOT BE WHITESPACE SENSITIVE
                for ( Map.Entry<TabletItem, List<ColorRuneItem>> entry : AncientMagicks.COMBO_MAP.entrySet() ) {
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
                String comboString = Files.readString(savePath.resolve("ancientmagicks.json")).replaceAll("\n", "").replaceAll(".$", "");
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
    }
}
