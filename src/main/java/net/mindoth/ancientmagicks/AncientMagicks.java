package net.mindoth.ancientmagicks;

import com.google.common.collect.Lists;
import net.mindoth.ancientmagicks.config.AncientMagicksCommonConfig;
import net.mindoth.ancientmagicks.item.AncientMagicksTab;
import net.mindoth.ancientmagicks.item.ColorRuneItem;
import net.mindoth.ancientmagicks.item.castingitem.SpellTabletItem;
import net.mindoth.ancientmagicks.item.spell.abstractspell.summon.SummonedMinion;
import net.mindoth.ancientmagicks.item.spell.summonskeleton.SkeletonMinionEntity;
import net.mindoth.ancientmagicks.network.AncientMagicksNetwork;
import net.mindoth.ancientmagicks.registries.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Mod(AncientMagicks.MOD_ID)
public class AncientMagicks {
    public static final String MOD_ID = "ancientmagicks";
    public static Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static Logger getLogger() {
        return LOGGER;
    }

    public AncientMagicks() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        if ( FMLEnvironment.dist == Dist.CLIENT ) AncientMagicksClient.registerHandlers();
        addRegistries(modEventBus);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, AncientMagicksCommonConfig.SPEC, "ancientmagicks-common.toml");
    }

    private void addRegistries(final IEventBus modEventBus) {
        AncientMagicksTab.register(modEventBus);
        AncientMagicksItems.ITEMS.register(modEventBus);
        AncientMagicksEntities.ENTITIES.register(modEventBus);
        AncientMagicksEffects.EFFECTS.register(modEventBus);
        AncientMagicksParticles.PARTICLES.register(modEventBus);
        AncientMagicksContainers.CONTAINERS.register(modEventBus);

        modEventBus.addListener(this::addCreative);
        //KEEP THIS LAST
        modEventBus.addListener(this::commonSetup);
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if ( event.getTab() == AncientMagicksTab.ANCIENTMAGICKS_TAB.get() ) {
            for ( RegistryObject<Item> item : AncientMagicksItems.ITEMS.getEntries() ) {
                event.accept(item);
            }
        }
    }

    @Mod.EventBusSubscriber(modid = AncientMagicks.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class EventBusEvents {

        @SubscribeEvent
        public static void entityAttributeEvent(EntityAttributeCreationEvent event) {
            event.put(AncientMagicksEntities.SKELETON_MINION.get(), SkeletonMinionEntity.setAttributes());
        }
    }

    //Spell list stuff
    public static List<Item> ITEM_LIST = Lists.newArrayList();
    public static List<EntityType<?>> MOB_LIST = Lists.newArrayList();
    public static List<EntityType<?>> DISABLED_POLYMOBS = Lists.newArrayList();

    public void commonSetup(final FMLCommonSetupEvent event) {
        ITEM_LIST = new ArrayList<>(ForgeRegistries.ITEMS.getValues());
        AncientMagicksNetwork.init();
        createSpellList();
        createSpellDisableList();
        createPolymobDisableList();
    }

    private static void createSpellList() {
        clearLists();
        for ( Item item : ITEM_LIST ) if ( item instanceof SpellTabletItem spellTabletItem && isSpellEnabled(spellTabletItem) ) SPELL_LIST.add(spellTabletItem);
    }

    public static boolean isSpellEnabled(SpellTabletItem spell) {
        return DISABLED_SPELLS.isEmpty() || !DISABLED_SPELLS.contains(spell);
    }

    private static void createSpellDisableList() {
        String configString = AncientMagicksCommonConfig.DISABLED_SPELLS.get();
        for ( String string : List.of(configString.replaceAll("[\\[\\]]", "").replaceAll(" ", "").replaceAll("\n", "").split(",")) ) {
            Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(string));
            if ( item instanceof SpellTabletItem spellTabletItem) DISABLED_SPELLS.add(spellTabletItem);
        }
    }

    private static void createPolymobDisableList() {
        String configString = AncientMagicksCommonConfig.DISABLED_POLYMOBS.get();
        for ( String string : List.of(configString.replaceAll("[\\[\\]]", "").replaceAll(" ", "").replaceAll("\n", "").split(",")) ) {
            DISABLED_POLYMOBS.add(ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(string)));
        }
    }

    public static void createMobList(ServerLevel serverLevel) {
        for ( EntityType<?> entityType : ForgeRegistries.ENTITY_TYPES.getValues() ) {
            if ( DISABLED_POLYMOBS.isEmpty() || !DISABLED_POLYMOBS.contains(entityType) ) {
                Entity tempEntity = entityType.create(serverLevel);
                if ( tempEntity instanceof Mob && !(tempEntity instanceof SummonedMinion) ) MOB_LIST.add(entityType);
            }
        }
    }

    public static void randomizeSpells() {
        comboRuneInit();
    }

    public static List<SpellTabletItem> SPELL_LIST = Lists.newArrayList();
    public static List<ColorRuneItem> COLOR_RUNE_LIST = Lists.newArrayList();
    public static List<SpellTabletItem> DISABLED_SPELLS = Lists.newArrayList();

    public static HashMap<SpellTabletItem, List<ColorRuneItem>> COMBO_MAP = new HashMap<>();

    public static void clearLists() {
        if ( !SPELL_LIST.isEmpty() ) SPELL_LIST.clear();
        if ( !COLOR_RUNE_LIST.isEmpty() ) COLOR_RUNE_LIST.clear();
        if ( !COMBO_MAP.isEmpty() ) COMBO_MAP.clear();
    }

    public static void comboRuneInit() {
        Logger logger = getLogger();
        for ( Item item : ITEM_LIST ) if ( item instanceof ColorRuneItem ) COLOR_RUNE_LIST.add((ColorRuneItem)item);

        List<List<ColorRuneItem>> comboList = Lists.newArrayList();

        for ( int i = 0; i < COLOR_RUNE_LIST.size(); i++ ) {
            for ( int j = 0; j < COLOR_RUNE_LIST.size(); j++ ) {
                for ( int k = 0; k < COLOR_RUNE_LIST.size(); k++ ) {
                    List<ColorRuneItem> tempList = Lists.newArrayList();
                    tempList.add(COLOR_RUNE_LIST.get(i));
                    tempList.add(COLOR_RUNE_LIST.get(j));
                    tempList.add(COLOR_RUNE_LIST.get(k));
                    if ( hasNoDupeInList(comboList, tempList) ) comboList.add(tempList);
                }
            }
        }
        if ( comboList.size() < SPELL_LIST.size() ) logger.warn("WARN! THERE ARE NOT ENOUGH SPELL COMBINATIONS FOR EVERY SPELL.");
        else {
            Collections.shuffle(comboList);
            for ( int i = 0; i < comboList.size(); i++ ) {
                if ( i < SPELL_LIST.size() ) COMBO_MAP.put(SPELL_LIST.get(i), comboList.get(i));
                else break;
            }
        }
    }

    //Thank god for Stack Overflow (the website)
    //https://stackoverflow.com/questions/1075656/simple-way-to-find-if-two-different-lists-contain-exactly-the-same-elements/67986292#67986292
    public static boolean listsMatch(List<ColorRuneItem> firstList, List<ColorRuneItem> secondList) {
        if ( firstList == secondList ) return true;
        if ( firstList != null && secondList != null ) {
            if ( firstList.isEmpty() && secondList.isEmpty() ) return true;
            if ( firstList.size() != secondList.size() ) return false;
            List<ColorRuneItem> tmpSecondList = new ArrayList<>(secondList);
            Object currFirstObject;
            for ( int i=1 ; i<=firstList.size() ; i++ ) {
                currFirstObject = firstList.get(i-1);
                boolean removed = tmpSecondList.remove(currFirstObject);
                if ( !removed ) return false;
                if ( i != firstList.size() ) {
                    if ( tmpSecondList.isEmpty() ) return false;
                }
            }
            return tmpSecondList.isEmpty();
        }
        return false;
    }

    private static boolean hasNoDupeInList(List<List<ColorRuneItem>> comboList, List<ColorRuneItem> tempList) {
        boolean state = true;
        for ( List<ColorRuneItem> list : comboList ) {
            if ( listsMatch(tempList, list) ) {
                state = false;
                break;
            }
        }
        return state;
    }
}
