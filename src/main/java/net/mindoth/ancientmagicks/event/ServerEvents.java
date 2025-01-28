package net.mindoth.ancientmagicks.event;

import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.capabilities.playermagic.PlayerMagicProvider;
import net.mindoth.ancientmagicks.item.ColorRuneItem;
import net.mindoth.ancientmagicks.network.*;
import net.mindoth.ancientmagicks.registries.attribute.AncientMagicksAttributes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;

@Mod.EventBusSubscriber(modid = AncientMagicks.MOD_ID)
public class ServerEvents {

    public static final String TAG_NOT_FIRST_LOGIN = ("notFirstLogIn");

    @SubscribeEvent
    public static void onPlayerJoin(final PlayerEvent.PlayerLoggedInEvent event) {
        if ( event.getEntity().level().isClientSide ) return;
        Player player = event.getEntity();
        CompoundTag playerData = player.getPersistentData();
        CompoundTag data = playerData.getCompound(Player.PERSISTED_NBT_TAG);
        if ( event.getEntity() instanceof ServerPlayer serverPlayer ) {
            AncientMagicksNetwork.sendToPlayer(new PacketSyncSpellCombos(ColorRuneItem.CURRENT_COMBO_TAG), serverPlayer);
            serverPlayer.getCapability(PlayerMagicProvider.PLAYER_MAGIC).ifPresent(magic -> {
                CompoundTag tag = new CompoundTag();
                if ( magic.getCurrentSpell() != null ) tag.putString("am_spell", magic.getCurrentSpell());
                else magic.setCurrentSpell("minecraft:air");
                if ( magic.getKnownSpells() != null ) tag.putString("am_known_spells", magic.getKnownSpells());
                else magic.setKnownSpells("");
                AncientMagicksNetwork.sendToPlayer(new PacketSyncClientMagic(tag), serverPlayer);
                if ( data.getBoolean(TAG_NOT_FIRST_LOGIN) ) AncientMagicksNetwork.sendToPlayer(new PacketSyncClientMana(magic.getCurrentMana()), serverPlayer);
                else MagickEvents.changeMana(serverPlayer, serverPlayer.getAttributeValue(AncientMagicksAttributes.MP_MAX.get()));
            });
        }

        //KEEP THIS LAST
        if ( !data.getBoolean(TAG_NOT_FIRST_LOGIN) ) {
            data.putBoolean(TAG_NOT_FIRST_LOGIN, true);
            playerData.put(Player.PERSISTED_NBT_TAG, data);
        }
    }

    @SubscribeEvent
    public static void onServerStart(final ServerStartingEvent event) {
        ServerLevel level = event.getServer().getLevel(ServerLevel.OVERWORLD);
        long seed = level.getSeed();
        Random seededRand = new Random(seed);
        AncientMagicks.createLists(seededRand);
    }

    //public static final String COMBOFILEPATH = "am_spellrecipes.json";
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
