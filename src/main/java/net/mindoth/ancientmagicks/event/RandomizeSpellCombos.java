package net.mindoth.ancientmagicks.event;

import com.google.common.base.Splitter;
import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.item.ColorRuneItem;
import net.mindoth.ancientmagicks.item.spellrune.SpellRuneItem;
import net.mindoth.ancientmagicks.network.AncientMagicksNetwork;
import net.mindoth.ancientmagicks.network.PacketSetSpellRune;
import net.mindoth.ancientmagicks.network.PacketSyncSpellCombos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@Mod.EventBusSubscriber(modid = AncientMagicks.MOD_ID)
public class RandomizeSpellCombos {

    @SubscribeEvent
    public static void onServerStart(final ServerAboutToStartEvent event) {
        Path savePath = event.getServer().getWorldPath(LevelResource.ROOT).normalize().toAbsolutePath();
        String filepath = savePath + "/ancientmagicks.json";

        //Creating file
        try {
            File file = new File(filepath);
            if ( !file.isFile() ) {
                AncientMagicks.randomizeSpells();

                FileOutputStream fos = new FileOutputStream(file);
                PrintWriter pw = new PrintWriter(fos);

                //TODO MAKE FILE NOT BE LINE OR WHITESPACE SENSITIVE
                for ( Map.Entry<SpellRuneItem, List<ColorRuneItem>> m : AncientMagicks.COMBO_MAP.entrySet() ) {
                    pw.print(ForgeRegistries.ITEMS.getKey(m.getKey()) + "=" + m.getValue() + ";");
                }

                pw.flush();
                pw.close();
                fos.close();
                System.out.println("Successfully created spell combo recipes for Ancient Magicks.");
            }
        }
        catch ( IOException exception ) {
            System.out.println("ERROR! CREATING SPELL COMBO RECIPES FOR ANCIENT MAGICKS. CONTACT THE MOD AUTHOR OR DOUBLE CHECK YOUR MOD-LIST.");
        }

        //Reading file
        try {
            File file = new File(filepath);
            if ( file.isFile() ) {
                FileInputStream fis = new FileInputStream(file);
                String comboString = Files.readString(savePath.resolve("ancientmagicks.json"));
                ColorRuneItem.CURRENT_COMBO_TAG.putString("am_combostring", comboString.replaceAll(".$", ""));

                ColorRuneItem.CURRENT_COMBO_MAP = Splitter.on(";").withKeyValueSeparator("=").split(ColorRuneItem.CURRENT_COMBO_TAG.getString("am_combostring"));

                fis.close();
                System.out.println("Successfully read spell combo recipes for Ancient Magicks.");
            }
            else System.out.println("ERROR! SPELL COMBO RECIPE FILE NOT FOUND. CONTACT THE MOD AUTHOR OR DOUBLE CHECK YOUR MOD-LIST.");
        }
        catch ( Exception exception ) {
            System.out.println("ERROR! READING SPELL COMBO RECIPES FOR ANCIENT MAGICKS. CONTACT THE MOD AUTHOR OR DOUBLE CHECK YOUR MOD-LIST.");
        }
    }

    @SubscribeEvent
    public static void onPlayerJoin(final PlayerEvent.PlayerLoggedInEvent event) {
        if ( event.getEntity() instanceof ServerPlayer player ) {
            AncientMagicksNetwork.sendToPlayer(new PacketSyncSpellCombos(ColorRuneItem.CURRENT_COMBO_TAG), player);
        }
    }
}
