package net.mindoth.ancientmagicks;

import com.google.common.collect.Lists;
import net.mindoth.ancientmagicks.config.AncientMagicksCommonConfig;
import net.mindoth.ancientmagicks.item.AncientMagicksTab;
import net.mindoth.ancientmagicks.item.ColorRuneItem;
import net.mindoth.ancientmagicks.item.castingitem.TabletItem;
import net.mindoth.ancientmagicks.item.spell.raisedead.SkeletonMinionEntity;
import net.mindoth.ancientmagicks.network.AncientMagicksNetwork;
import net.mindoth.ancientmagicks.registries.*;
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
        if (FMLEnvironment.dist == Dist.CLIENT) {
            AncientMagicksClient.registerHandlers();
        }
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
                //if ( !(item.get() instanceof SpellItem) ) event.accept(item);
                event.accept(item);
            }
        }
    }

    public static List<Item> ITEM_LIST = Lists.newArrayList();
    public static List<Mob> MOB_LIST = Lists.newArrayList();

    public void commonSetup(final FMLCommonSetupEvent event) {
        ITEM_LIST = new ArrayList<>(ForgeRegistries.ITEMS.getValues());
        AncientMagicksNetwork.init();
    }

    public static void createMobList(ServerLevel serverLevel) {
        for ( EntityType<?> entityType : ForgeRegistries.ENTITY_TYPES.getValues() ) {
            Entity tempEntity = entityType.create(serverLevel);
            if ( tempEntity instanceof Mob mob ) MOB_LIST.add(mob);
        }
    }

    public static void randomizeSpells() {
        clearLists();
        createSpellLists();
        comboRuneInit();
    }

    public static List<TabletItem> SPELL_RUNES = Lists.newArrayList();
    public static List<ColorRuneItem> COLOR_RUNES = Lists.newArrayList();

    public static List<TabletItem> TIER1_SPELL_RUNES = Lists.newArrayList();
    public static List<TabletItem> TIER2_SPELL_RUNES = Lists.newArrayList();
    public static List<TabletItem> TIER3_SPELL_RUNES = Lists.newArrayList();
    public static List<TabletItem> TIER4_SPELL_RUNES = Lists.newArrayList();
    public static List<TabletItem> TIER5_SPELL_RUNES = Lists.newArrayList();
    public static List<TabletItem> TIER6_SPELL_RUNES = Lists.newArrayList();
    public static List<TabletItem> TIER7_SPELL_RUNES = Lists.newArrayList();
    public static List<TabletItem> TIER8_SPELL_RUNES = Lists.newArrayList();

    public static HashMap<TabletItem, List<ColorRuneItem>> COMBO_MAP = new HashMap<>();

    public static void clearLists() {
        if ( !SPELL_RUNES.isEmpty() ) SPELL_RUNES.clear();
        if ( !COLOR_RUNES.isEmpty() ) COLOR_RUNES.clear();
        if ( !TIER1_SPELL_RUNES.isEmpty() ) TIER1_SPELL_RUNES.clear();
        if ( !TIER2_SPELL_RUNES.isEmpty() ) TIER2_SPELL_RUNES.clear();
        if ( !TIER3_SPELL_RUNES.isEmpty() ) TIER3_SPELL_RUNES.clear();
        if ( !TIER4_SPELL_RUNES.isEmpty() ) TIER4_SPELL_RUNES.clear();
        if ( !TIER5_SPELL_RUNES.isEmpty() ) TIER5_SPELL_RUNES.clear();
        if ( !TIER6_SPELL_RUNES.isEmpty() ) TIER6_SPELL_RUNES.clear();
        if ( !TIER7_SPELL_RUNES.isEmpty() ) TIER7_SPELL_RUNES.clear();
        if ( !TIER8_SPELL_RUNES.isEmpty() ) TIER8_SPELL_RUNES.clear();
        if ( !COMBO_MAP.isEmpty() ) COMBO_MAP.clear();
    }

    public static void createSpellLists() {
        for ( Item item : ITEM_LIST ) if ( item instanceof TabletItem) SPELL_RUNES.add((TabletItem)item);
        for ( TabletItem tabletItem : SPELL_RUNES ) {
            if ( tabletItem.tier == 1 ) TIER1_SPELL_RUNES.add(tabletItem);
            if ( tabletItem.tier == 2 ) TIER2_SPELL_RUNES.add(tabletItem);
            if ( tabletItem.tier == 3 ) TIER3_SPELL_RUNES.add(tabletItem);
            if ( tabletItem.tier == 4 ) TIER4_SPELL_RUNES.add(tabletItem);
            if ( tabletItem.tier == 5 ) TIER5_SPELL_RUNES.add(tabletItem);
            if ( tabletItem.tier == 6 ) TIER6_SPELL_RUNES.add(tabletItem);
            if ( tabletItem.tier == 7 ) TIER7_SPELL_RUNES.add(tabletItem);
            if ( tabletItem.tier == 8 ) TIER8_SPELL_RUNES.add(tabletItem);
        }
    }

    public static void comboRuneInit() {
        Logger logger = getLogger();
        for ( Item item : ITEM_LIST ) if ( item instanceof ColorRuneItem ) COLOR_RUNES.add((ColorRuneItem)item);

        List<List<ColorRuneItem>> comboList1 = Lists.newArrayList();
        List<List<ColorRuneItem>> comboList2 = Lists.newArrayList();
        List<List<ColorRuneItem>> comboList3 = Lists.newArrayList();
        List<List<ColorRuneItem>> comboList4 = Lists.newArrayList();
        List<List<ColorRuneItem>> comboList5 = Lists.newArrayList();
        List<List<ColorRuneItem>> comboList6 = Lists.newArrayList();
        List<List<ColorRuneItem>> comboList7 = Lists.newArrayList();
        List<List<ColorRuneItem>> comboList8 = Lists.newArrayList();

        for ( int i = 0; i < COLOR_RUNES.size(); i++ ) {
            for ( int j = 0; j < COLOR_RUNES.size(); j++ ) {
                if ( !TIER1_SPELL_RUNES.isEmpty() ) {
                    List<ColorRuneItem> tempList1 = Lists.newArrayList();
                    tempList1.add(COLOR_RUNES.get(i));
                    tempList1.add(COLOR_RUNES.get(j));
                    if ( !hasDupeInList(comboList1, tempList1) ) comboList1.add(tempList1);
                }
                for ( int k = 0; k < COLOR_RUNES.size(); k++ ) {
                    if ( !TIER2_SPELL_RUNES.isEmpty() ) {
                        List<ColorRuneItem> tempList2 = Lists.newArrayList();
                        tempList2.add(COLOR_RUNES.get(i));
                        tempList2.add(COLOR_RUNES.get(j));
                        tempList2.add(COLOR_RUNES.get(k));
                        if ( !hasDupeInList(comboList2, tempList2) ) comboList2.add(tempList2);
                    }
                    for ( int l = 0; l < COLOR_RUNES.size(); l++ ) {
                        if ( !TIER3_SPELL_RUNES.isEmpty() ) {
                            List<ColorRuneItem> tempList3 = Lists.newArrayList();
                            tempList3.add(COLOR_RUNES.get(i));
                            tempList3.add(COLOR_RUNES.get(j));
                            tempList3.add(COLOR_RUNES.get(k));
                            tempList3.add(COLOR_RUNES.get(l));
                            if ( !hasDupeInList(comboList3, tempList3) ) comboList3.add(tempList3);
                        }
                        for ( int m = 0; m < COLOR_RUNES.size(); m++ ) {
                            if ( !TIER4_SPELL_RUNES.isEmpty() ) {
                                List<ColorRuneItem> tempList4 = Lists.newArrayList();
                                tempList4.add(COLOR_RUNES.get(i));
                                tempList4.add(COLOR_RUNES.get(j));
                                tempList4.add(COLOR_RUNES.get(k));
                                tempList4.add(COLOR_RUNES.get(l));
                                tempList4.add(COLOR_RUNES.get(m));
                                if ( !hasDupeInList(comboList4, tempList4) ) comboList4.add(tempList4);
                            }
                            for ( int n = 0; n < COLOR_RUNES.size(); n++ ) {
                                if ( !TIER5_SPELL_RUNES.isEmpty() ) {
                                    List<ColorRuneItem> tempList5 = Lists.newArrayList();
                                    tempList5.add(COLOR_RUNES.get(i));
                                    tempList5.add(COLOR_RUNES.get(j));
                                    tempList5.add(COLOR_RUNES.get(k));
                                    tempList5.add(COLOR_RUNES.get(l));
                                    tempList5.add(COLOR_RUNES.get(m));
                                    tempList5.add(COLOR_RUNES.get(n));
                                    if ( !hasDupeInList(comboList5, tempList5) ) comboList5.add(tempList5);
                                }
                                for ( int o = 0; o < COLOR_RUNES.size(); o++ ) {
                                    if ( !TIER6_SPELL_RUNES.isEmpty() ) {
                                        List<ColorRuneItem> tempList6 = Lists.newArrayList();
                                        tempList6.add(COLOR_RUNES.get(i));
                                        tempList6.add(COLOR_RUNES.get(j));
                                        tempList6.add(COLOR_RUNES.get(k));
                                        tempList6.add(COLOR_RUNES.get(l));
                                        tempList6.add(COLOR_RUNES.get(m));
                                        tempList6.add(COLOR_RUNES.get(n));
                                        tempList6.add(COLOR_RUNES.get(o));
                                        if ( !hasDupeInList(comboList6, tempList6) ) comboList6.add(tempList6);
                                    }
                                    for ( int p = 0; p < COLOR_RUNES.size(); p++ ) {
                                        if ( !TIER7_SPELL_RUNES.isEmpty() ) {
                                            List<ColorRuneItem> tempList7 = Lists.newArrayList();
                                            tempList7.add(COLOR_RUNES.get(i));
                                            tempList7.add(COLOR_RUNES.get(j));
                                            tempList7.add(COLOR_RUNES.get(k));
                                            tempList7.add(COLOR_RUNES.get(l));
                                            tempList7.add(COLOR_RUNES.get(m));
                                            tempList7.add(COLOR_RUNES.get(n));
                                            tempList7.add(COLOR_RUNES.get(o));
                                            tempList7.add(COLOR_RUNES.get(p));
                                            if ( !hasDupeInList(comboList7, tempList7) ) comboList7.add(tempList7);
                                        }
                                        for ( int q = 0; q < COLOR_RUNES.size(); q++ ) {
                                            if ( !TIER8_SPELL_RUNES.isEmpty() ) {
                                                List<ColorRuneItem> tempList8 = Lists.newArrayList();
                                                tempList8.add(COLOR_RUNES.get(i));
                                                tempList8.add(COLOR_RUNES.get(j));
                                                tempList8.add(COLOR_RUNES.get(k));
                                                tempList8.add(COLOR_RUNES.get(l));
                                                tempList8.add(COLOR_RUNES.get(m));
                                                tempList8.add(COLOR_RUNES.get(n));
                                                tempList8.add(COLOR_RUNES.get(o));
                                                tempList8.add(COLOR_RUNES.get(p));
                                                tempList8.add(COLOR_RUNES.get(q));
                                                if ( !hasDupeInList(comboList8, tempList8) ) comboList8.add(tempList8);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if ( comboList1.size() < TIER1_SPELL_RUNES.size() ) logger.warn("WARN! THERE ARE NOT ENOUGH SPELL COMBINATIONS FOR EVERY TIER 1 SPELL.");
        else if ( comboList2.size() < TIER2_SPELL_RUNES.size() ) logger.warn("WARN! THERE ARE NOT ENOUGH SPELL COMBINATIONS FOR EVERY TIER 2 SPELL.");
        else if ( comboList3.size() < TIER3_SPELL_RUNES.size() ) logger.warn("WARN! THERE ARE NOT ENOUGH SPELL COMBINATIONS FOR EVERY TIER 3 SPELL.");
        else if ( comboList4.size() < TIER4_SPELL_RUNES.size() ) logger.warn("WARN! THERE ARE NOT ENOUGH SPELL COMBINATIONS FOR EVERY TIER 4 SPELL.");
        else if ( comboList5.size() < TIER5_SPELL_RUNES.size() ) logger.warn("WARN! THERE ARE NOT ENOUGH SPELL COMBINATIONS FOR EVERY TIER 5 SPELL.");
        else if ( comboList6.size() < TIER6_SPELL_RUNES.size() ) logger.warn("WARN! THERE ARE NOT ENOUGH SPELL COMBINATIONS FOR EVERY TIER 6 SPELL.");
        else if ( comboList7.size() < TIER7_SPELL_RUNES.size() ) logger.warn("WARN! THERE ARE NOT ENOUGH SPELL COMBINATIONS FOR EVERY TIER 7 SPELL.");
        else if ( comboList8.size() < TIER8_SPELL_RUNES.size() ) logger.warn("WARN! THERE ARE NOT ENOUGH SPELL COMBINATIONS FOR EVERY TIER 8 SPELL. THAT MEANS YOU HAVE OVER 2002 TIER 8 SPELLS. HOW DID YOU MANAGE THIS?");
        else {
            if ( !TIER1_SPELL_RUNES.isEmpty() ) {
                Collections.shuffle(comboList1);
                for ( int i = 0; i < comboList1.size(); i++ ) {
                    if ( i < TIER1_SPELL_RUNES.size() ) COMBO_MAP.put(TIER1_SPELL_RUNES.get(i), comboList1.get(i));
                    else break;
                }
            }
            if ( !TIER2_SPELL_RUNES.isEmpty() ) {
                Collections.shuffle(comboList2);
                for ( int i = 0; i < comboList2.size(); i++ ) {
                    if ( i < TIER2_SPELL_RUNES.size() ) COMBO_MAP.put(TIER2_SPELL_RUNES.get(i), comboList2.get(i));
                    else break;
                }
            }
            if ( !TIER3_SPELL_RUNES.isEmpty() ) {
                Collections.shuffle(comboList3);
                for ( int i = 0; i < comboList3.size(); i++ ) {
                    if ( i < TIER3_SPELL_RUNES.size() ) COMBO_MAP.put(TIER3_SPELL_RUNES.get(i), comboList3.get(i));
                    else break;
                }
            }
            if ( !TIER4_SPELL_RUNES.isEmpty() ) {
                Collections.shuffle(comboList4);
                for ( int i = 0; i < comboList4.size(); i++ ) {
                    if ( i < TIER4_SPELL_RUNES.size() ) COMBO_MAP.put(TIER4_SPELL_RUNES.get(i), comboList4.get(i));
                    else break;
                }
            }
            if ( !TIER5_SPELL_RUNES.isEmpty() ) {
                Collections.shuffle(comboList5);
                for ( int i = 0; i < comboList5.size(); i++ ) {
                    if ( i < TIER5_SPELL_RUNES.size() ) COMBO_MAP.put(TIER5_SPELL_RUNES.get(i), comboList5.get(i));
                    else break;
                }
            }
            if ( !TIER6_SPELL_RUNES.isEmpty() ) {
                Collections.shuffle(comboList6);
                for ( int i = 0; i < comboList6.size(); i++ ) {
                    if ( i < TIER6_SPELL_RUNES.size() ) COMBO_MAP.put(TIER6_SPELL_RUNES.get(i), comboList6.get(i));
                    else break;
                }
            }
            if ( !TIER7_SPELL_RUNES.isEmpty() ) {
                Collections.shuffle(comboList7);
                for ( int i = 0; i < comboList7.size(); i++ ) {
                    if ( i < TIER7_SPELL_RUNES.size() ) COMBO_MAP.put(TIER7_SPELL_RUNES.get(i), comboList7.get(i));
                    else break;
                }
            }
            if ( !TIER8_SPELL_RUNES.isEmpty() ) {
                Collections.shuffle(comboList8);
                for ( int i = 0; i < comboList8.size(); i++ ) {
                    if ( i < TIER8_SPELL_RUNES.size() ) COMBO_MAP.put(TIER8_SPELL_RUNES.get(i), comboList8.get(i));
                    else break;
                }
            }
        }
    }

    public static boolean hasDupeInList(List<List<ColorRuneItem>> comboList, List<ColorRuneItem> tempList) {
        boolean state = false;

        //TODO FIND A BETTER WAY TO DO THIS SINCE THIS METHOD IS SLOW
        for ( List<ColorRuneItem> list : comboList ) {
            if ( list.containsAll(tempList) && tempList.containsAll(list) ) {
                state = true;
                break;
            }
        }

        return state;
    }

    @Mod.EventBusSubscriber(modid = AncientMagicks.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class EventBusEvents {

        @SubscribeEvent
        public static void entityAttributeEvent(EntityAttributeCreationEvent event) {
            event.put(AncientMagicksEntities.SKELETON_MINION.get(), SkeletonMinionEntity.setAttributes());
        }
    }
}
