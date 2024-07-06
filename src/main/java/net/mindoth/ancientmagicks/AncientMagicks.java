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
        createTieredSpellLists();
        comboRuneInit();
    }

    public static List<SpellTabletItem> SPELL_LIST = Lists.newArrayList();
    public static List<ColorRuneItem> COLOR_RUNE_LIST = Lists.newArrayList();

    public static List<SpellTabletItem> DISABLED_SPELLS = Lists.newArrayList();
    public static List<SpellTabletItem> TIER1_SPELLS = Lists.newArrayList();
    public static List<SpellTabletItem> TIER2_SPELLS = Lists.newArrayList();
    public static List<SpellTabletItem> TIER3_SPELLS = Lists.newArrayList();
    public static List<SpellTabletItem> TIER4_SPELLS = Lists.newArrayList();
    public static List<SpellTabletItem> TIER5_SPELLS = Lists.newArrayList();
    public static List<SpellTabletItem> TIER6_SPELLS = Lists.newArrayList();
    public static List<SpellTabletItem> TIER7_SPELLS = Lists.newArrayList();
    public static List<SpellTabletItem> TIER8_SPELLS = Lists.newArrayList();

    public static HashMap<SpellTabletItem, List<ColorRuneItem>> COMBO_MAP = new HashMap<>();

    public static void clearLists() {
        if ( !SPELL_LIST.isEmpty() ) SPELL_LIST.clear();
        if ( !COLOR_RUNE_LIST.isEmpty() ) COLOR_RUNE_LIST.clear();
        if ( !TIER1_SPELLS.isEmpty() ) TIER1_SPELLS.clear();
        if ( !TIER2_SPELLS.isEmpty() ) TIER2_SPELLS.clear();
        if ( !TIER3_SPELLS.isEmpty() ) TIER3_SPELLS.clear();
        if ( !TIER4_SPELLS.isEmpty() ) TIER4_SPELLS.clear();
        if ( !TIER5_SPELLS.isEmpty() ) TIER5_SPELLS.clear();
        if ( !TIER6_SPELLS.isEmpty() ) TIER6_SPELLS.clear();
        if ( !TIER7_SPELLS.isEmpty() ) TIER7_SPELLS.clear();
        if ( !TIER8_SPELLS.isEmpty() ) TIER8_SPELLS.clear();
        if ( !COMBO_MAP.isEmpty() ) COMBO_MAP.clear();
    }

    public static boolean isSpellEnabled(SpellTabletItem spell) {
        return DISABLED_SPELLS.isEmpty() || !DISABLED_SPELLS.contains(spell);
    }

    public static void createTieredSpellLists() {
        for ( SpellTabletItem spellTabletItem : SPELL_LIST ) {
            if ( spellTabletItem.tier == 1 ) TIER1_SPELLS.add(spellTabletItem);
            if ( spellTabletItem.tier == 2 ) TIER2_SPELLS.add(spellTabletItem);
            if ( spellTabletItem.tier == 3 ) TIER3_SPELLS.add(spellTabletItem);
            if ( spellTabletItem.tier == 4 ) TIER4_SPELLS.add(spellTabletItem);
            if ( spellTabletItem.tier == 5 ) TIER5_SPELLS.add(spellTabletItem);
            if ( spellTabletItem.tier == 6 ) TIER6_SPELLS.add(spellTabletItem);
            if ( spellTabletItem.tier == 7 ) TIER7_SPELLS.add(spellTabletItem);
            if ( spellTabletItem.tier == 8 ) TIER8_SPELLS.add(spellTabletItem);
        }
    }

    public static void comboRuneInit() {
        Logger logger = getLogger();
        for ( Item item : ITEM_LIST ) if ( item instanceof ColorRuneItem ) COLOR_RUNE_LIST.add((ColorRuneItem)item);

        List<List<ColorRuneItem>> comboList1 = Lists.newArrayList();
        List<List<ColorRuneItem>> comboList2 = Lists.newArrayList();
        List<List<ColorRuneItem>> comboList3 = Lists.newArrayList();
        List<List<ColorRuneItem>> comboList4 = Lists.newArrayList();
        List<List<ColorRuneItem>> comboList5 = Lists.newArrayList();
        List<List<ColorRuneItem>> comboList6 = Lists.newArrayList();
        List<List<ColorRuneItem>> comboList7 = Lists.newArrayList();
        List<List<ColorRuneItem>> comboList8 = Lists.newArrayList();

        for ( int i = 0; i < COLOR_RUNE_LIST.size(); i++ ) {
            for ( int j = 0; j < COLOR_RUNE_LIST.size(); j++ ) {
                if ( !TIER1_SPELLS.isEmpty() ) {
                    List<ColorRuneItem> tempList1 = Lists.newArrayList();
                    tempList1.add(COLOR_RUNE_LIST.get(i));
                    tempList1.add(COLOR_RUNE_LIST.get(j));
                    if ( hasNoDupeInList(comboList1, tempList1) ) comboList1.add(tempList1);
                }
                for ( int k = 0; k < COLOR_RUNE_LIST.size(); k++ ) {
                    if ( !TIER2_SPELLS.isEmpty() ) {
                        List<ColorRuneItem> tempList2 = Lists.newArrayList();
                        tempList2.add(COLOR_RUNE_LIST.get(i));
                        tempList2.add(COLOR_RUNE_LIST.get(j));
                        tempList2.add(COLOR_RUNE_LIST.get(k));
                        if ( hasNoDupeInList(comboList2, tempList2) ) comboList2.add(tempList2);
                    }
                    for ( int l = 0; l < COLOR_RUNE_LIST.size(); l++ ) {
                        if ( !TIER3_SPELLS.isEmpty() ) {
                            List<ColorRuneItem> tempList3 = Lists.newArrayList();
                            tempList3.add(COLOR_RUNE_LIST.get(i));
                            tempList3.add(COLOR_RUNE_LIST.get(j));
                            tempList3.add(COLOR_RUNE_LIST.get(k));
                            tempList3.add(COLOR_RUNE_LIST.get(l));
                            if ( hasNoDupeInList(comboList3, tempList3) ) comboList3.add(tempList3);
                        }
                        for ( int m = 0; m < COLOR_RUNE_LIST.size(); m++ ) {
                            if ( !TIER4_SPELLS.isEmpty() ) {
                                List<ColorRuneItem> tempList4 = Lists.newArrayList();
                                tempList4.add(COLOR_RUNE_LIST.get(i));
                                tempList4.add(COLOR_RUNE_LIST.get(j));
                                tempList4.add(COLOR_RUNE_LIST.get(k));
                                tempList4.add(COLOR_RUNE_LIST.get(l));
                                tempList4.add(COLOR_RUNE_LIST.get(m));
                                if ( hasNoDupeInList(comboList4, tempList4) ) comboList4.add(tempList4);
                            }
                            for ( int n = 0; n < COLOR_RUNE_LIST.size(); n++ ) {
                                if ( !TIER5_SPELLS.isEmpty() ) {
                                    List<ColorRuneItem> tempList5 = Lists.newArrayList();
                                    tempList5.add(COLOR_RUNE_LIST.get(i));
                                    tempList5.add(COLOR_RUNE_LIST.get(j));
                                    tempList5.add(COLOR_RUNE_LIST.get(k));
                                    tempList5.add(COLOR_RUNE_LIST.get(l));
                                    tempList5.add(COLOR_RUNE_LIST.get(m));
                                    tempList5.add(COLOR_RUNE_LIST.get(n));
                                    if ( hasNoDupeInList(comboList5, tempList5) ) comboList5.add(tempList5);
                                }
                                for ( int o = 0; o < COLOR_RUNE_LIST.size(); o++ ) {
                                    if ( !TIER6_SPELLS.isEmpty() ) {
                                        List<ColorRuneItem> tempList6 = Lists.newArrayList();
                                        tempList6.add(COLOR_RUNE_LIST.get(i));
                                        tempList6.add(COLOR_RUNE_LIST.get(j));
                                        tempList6.add(COLOR_RUNE_LIST.get(k));
                                        tempList6.add(COLOR_RUNE_LIST.get(l));
                                        tempList6.add(COLOR_RUNE_LIST.get(m));
                                        tempList6.add(COLOR_RUNE_LIST.get(n));
                                        tempList6.add(COLOR_RUNE_LIST.get(o));
                                        if ( hasNoDupeInList(comboList6, tempList6) ) comboList6.add(tempList6);
                                    }
                                    for ( int p = 0; p < COLOR_RUNE_LIST.size(); p++ ) {
                                        if ( !TIER7_SPELLS.isEmpty() ) {
                                            List<ColorRuneItem> tempList7 = Lists.newArrayList();
                                            tempList7.add(COLOR_RUNE_LIST.get(i));
                                            tempList7.add(COLOR_RUNE_LIST.get(j));
                                            tempList7.add(COLOR_RUNE_LIST.get(k));
                                            tempList7.add(COLOR_RUNE_LIST.get(l));
                                            tempList7.add(COLOR_RUNE_LIST.get(m));
                                            tempList7.add(COLOR_RUNE_LIST.get(n));
                                            tempList7.add(COLOR_RUNE_LIST.get(o));
                                            tempList7.add(COLOR_RUNE_LIST.get(p));
                                            if ( hasNoDupeInList(comboList7, tempList7) ) comboList7.add(tempList7);
                                        }
                                        for ( int q = 0; q < COLOR_RUNE_LIST.size(); q++ ) {
                                            if ( !TIER8_SPELLS.isEmpty() ) {
                                                List<ColorRuneItem> tempList8 = Lists.newArrayList();
                                                tempList8.add(COLOR_RUNE_LIST.get(i));
                                                tempList8.add(COLOR_RUNE_LIST.get(j));
                                                tempList8.add(COLOR_RUNE_LIST.get(k));
                                                tempList8.add(COLOR_RUNE_LIST.get(l));
                                                tempList8.add(COLOR_RUNE_LIST.get(m));
                                                tempList8.add(COLOR_RUNE_LIST.get(n));
                                                tempList8.add(COLOR_RUNE_LIST.get(o));
                                                tempList8.add(COLOR_RUNE_LIST.get(p));
                                                tempList8.add(COLOR_RUNE_LIST.get(q));
                                                if ( hasNoDupeInList(comboList8, tempList8) ) comboList8.add(tempList8);
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
        if ( comboList1.size() < TIER1_SPELLS.size() ) logger.warn("WARN! THERE ARE NOT ENOUGH SPELL COMBINATIONS FOR EVERY TIER 1 SPELL.");
        else if ( comboList2.size() < TIER2_SPELLS.size() ) logger.warn("WARN! THERE ARE NOT ENOUGH SPELL COMBINATIONS FOR EVERY TIER 2 SPELL.");
        else if ( comboList3.size() < TIER3_SPELLS.size() ) logger.warn("WARN! THERE ARE NOT ENOUGH SPELL COMBINATIONS FOR EVERY TIER 3 SPELL.");
        else if ( comboList4.size() < TIER4_SPELLS.size() ) logger.warn("WARN! THERE ARE NOT ENOUGH SPELL COMBINATIONS FOR EVERY TIER 4 SPELL.");
        else if ( comboList5.size() < TIER5_SPELLS.size() ) logger.warn("WARN! THERE ARE NOT ENOUGH SPELL COMBINATIONS FOR EVERY TIER 5 SPELL.");
        else if ( comboList6.size() < TIER6_SPELLS.size() ) logger.warn("WARN! THERE ARE NOT ENOUGH SPELL COMBINATIONS FOR EVERY TIER 6 SPELL.");
        else if ( comboList7.size() < TIER7_SPELLS.size() ) logger.warn("WARN! THERE ARE NOT ENOUGH SPELL COMBINATIONS FOR EVERY TIER 7 SPELL.");
        else if ( comboList8.size() < TIER8_SPELLS.size() ) logger.warn("WARN! THERE ARE NOT ENOUGH SPELL COMBINATIONS FOR EVERY TIER 8 SPELL. THAT MEANS YOU HAVE OVER 2000 TIER 8 SPELLS. HOW DID YOU MANAGE THIS?");
        else {
            if ( !TIER1_SPELLS.isEmpty() ) {
                Collections.shuffle(comboList1);
                for ( int i = 0; i < comboList1.size(); i++ ) {
                    if ( i < TIER1_SPELLS.size() ) COMBO_MAP.put(TIER1_SPELLS.get(i), comboList1.get(i));
                    else break;
                }
            }
            if ( !TIER2_SPELLS.isEmpty() ) {
                Collections.shuffle(comboList2);
                for ( int i = 0; i < comboList2.size(); i++ ) {
                    if ( i < TIER2_SPELLS.size() ) COMBO_MAP.put(TIER2_SPELLS.get(i), comboList2.get(i));
                    else break;
                }
            }
            if ( !TIER3_SPELLS.isEmpty() ) {
                Collections.shuffle(comboList3);
                for ( int i = 0; i < comboList3.size(); i++ ) {
                    if ( i < TIER3_SPELLS.size() ) COMBO_MAP.put(TIER3_SPELLS.get(i), comboList3.get(i));
                    else break;
                }
            }
            if ( !TIER4_SPELLS.isEmpty() ) {
                Collections.shuffle(comboList4);
                for ( int i = 0; i < comboList4.size(); i++ ) {
                    if ( i < TIER4_SPELLS.size() ) COMBO_MAP.put(TIER4_SPELLS.get(i), comboList4.get(i));
                    else break;
                }
            }
            if ( !TIER5_SPELLS.isEmpty() ) {
                Collections.shuffle(comboList5);
                for ( int i = 0; i < comboList5.size(); i++ ) {
                    if ( i < TIER5_SPELLS.size() ) COMBO_MAP.put(TIER5_SPELLS.get(i), comboList5.get(i));
                    else break;
                }
            }
            if ( !TIER6_SPELLS.isEmpty() ) {
                Collections.shuffle(comboList6);
                for ( int i = 0; i < comboList6.size(); i++ ) {
                    if ( i < TIER6_SPELLS.size() ) COMBO_MAP.put(TIER6_SPELLS.get(i), comboList6.get(i));
                    else break;
                }
            }
            if ( !TIER7_SPELLS.isEmpty() ) {
                Collections.shuffle(comboList7);
                for ( int i = 0; i < comboList7.size(); i++ ) {
                    if ( i < TIER7_SPELLS.size() ) COMBO_MAP.put(TIER7_SPELLS.get(i), comboList7.get(i));
                    else break;
                }
            }
            if ( !TIER8_SPELLS.isEmpty() ) {
                Collections.shuffle(comboList8);
                for ( int i = 0; i < comboList8.size(); i++ ) {
                    if ( i < TIER8_SPELLS.size() ) COMBO_MAP.put(TIER8_SPELLS.get(i), comboList8.get(i));
                    else break;
                }
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
