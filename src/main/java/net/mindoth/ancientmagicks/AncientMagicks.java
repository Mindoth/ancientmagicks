package net.mindoth.ancientmagicks;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.mindoth.ancientmagicks.config.AncientMagicksCommonConfig;
import net.mindoth.ancientmagicks.item.AncientMagicksTab;
import net.mindoth.ancientmagicks.item.ColorRuneItem;
import net.mindoth.ancientmagicks.item.spellrune.SpellRuneItem;
import net.mindoth.ancientmagicks.item.spellrune.raisedead.SkeletonMinionEntity;
import net.mindoth.ancientmagicks.network.AncientMagicksNetwork;
import net.mindoth.ancientmagicks.registries.*;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Mod(AncientMagicks.MOD_ID)
public class AncientMagicks {
    public static final String MOD_ID = "ancientmagicks";

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
                event.accept(item);
            }
        }
    }

    public static List<Item> ITEM_LIST = Lists.newArrayList();

    public void commonSetup(final FMLCommonSetupEvent event) {
        ITEM_LIST = new ArrayList<>(ForgeRegistries.ITEMS.getValues());
        AncientMagicksNetwork.init();
        //randomizeSpells();
    }

    public static void randomizeSpells() {
        spellRuneInit();
        createSpellLists();
        comboRuneInit();
    }

    public static void spellRuneInit() {
        for ( Item item : ITEM_LIST ) if ( item instanceof SpellRuneItem ) SPELL_RUNES.add((SpellRuneItem)item);
    }

    public static List<SpellRuneItem> SPELL_RUNES = Lists.newArrayList();
    public static List<ColorRuneItem> COLOR_RUNES = Lists.newArrayList();

    public static void createSpellLists() {
        for ( SpellRuneItem spellRuneItem : SPELL_RUNES ) {
            if ( spellRuneItem.tier == 1 ) TIER1_SPELL_RUNES.add(spellRuneItem);
            if ( spellRuneItem.tier == 2 ) TIER2_SPELL_RUNES.add(spellRuneItem);
            if ( spellRuneItem.tier == 3 ) TIER3_SPELL_RUNES.add(spellRuneItem);
            if ( spellRuneItem.tier == 4 ) TIER4_SPELL_RUNES.add(spellRuneItem);
        }
    }

    public static List<SpellRuneItem> TIER1_SPELL_RUNES = Lists.newArrayList();
    public static List<SpellRuneItem> TIER2_SPELL_RUNES = Lists.newArrayList();
    public static List<SpellRuneItem> TIER3_SPELL_RUNES = Lists.newArrayList();
    public static List<SpellRuneItem> TIER4_SPELL_RUNES = Lists.newArrayList();

    public static HashMap<SpellRuneItem, List<ColorRuneItem>> COMBO_MAP = new HashMap<>();

    public static void comboRuneInit() {
        for ( Item item : ITEM_LIST ) if ( item instanceof ColorRuneItem ) COLOR_RUNES.add((ColorRuneItem)item);

        List<List<ColorRuneItem>> comboList1 = Lists.newArrayList();
        List<List<ColorRuneItem>> comboList2 = Lists.newArrayList();
        List<List<ColorRuneItem>> comboList3 = Lists.newArrayList();
        List<List<ColorRuneItem>> comboList4 = Lists.newArrayList();

        for ( int i = 0; i < COLOR_RUNES.size(); i++ ) {
            for ( int j = 0; j < COLOR_RUNES.size(); j++ ) {
                List<ColorRuneItem> tempList1 = Lists.newArrayList();
                tempList1.add(COLOR_RUNES.get(i));
                tempList1.add(COLOR_RUNES.get(j));
                comboList1.add(tempList1);
                for ( int k = 0; k < COLOR_RUNES.size(); k++ ) {
                    List<ColorRuneItem> tempList2 = Lists.newArrayList();
                    tempList2.add(COLOR_RUNES.get(i));
                    tempList2.add(COLOR_RUNES.get(j));
                    tempList2.add(COLOR_RUNES.get(k));
                    comboList2.add(tempList2);
                    for ( int l = 0; l < COLOR_RUNES.size(); l++ ) {
                        List<ColorRuneItem> tempList3 = Lists.newArrayList();
                        tempList3.add(COLOR_RUNES.get(i));
                        tempList3.add(COLOR_RUNES.get(j));
                        tempList3.add(COLOR_RUNES.get(k));
                        tempList3.add(COLOR_RUNES.get(l));
                        comboList3.add(tempList3);
                        for ( int m = 0; m < COLOR_RUNES.size(); m++ ) {
                            List<ColorRuneItem> tempList4 = Lists.newArrayList();
                            tempList4.add(COLOR_RUNES.get(i));
                            tempList4.add(COLOR_RUNES.get(j));
                            tempList4.add(COLOR_RUNES.get(k));
                            tempList4.add(COLOR_RUNES.get(l));
                            tempList4.add(COLOR_RUNES.get(m));
                            comboList4.add(tempList4);
                        }
                    }
                }
            }
        }
        if ( comboList1.size() < TIER1_SPELL_RUNES.size() ) {
            System.out.println("WARN! THERE ARE NOT ENOUGH SPELL COMBINATIONS FOR EVERY TIER 1 SPELL. CONSIDER ADDING MORE COLOR RUNES.");
        }
        else if ( comboList2.size() < TIER2_SPELL_RUNES.size() ) {
            System.out.println("WARN! THERE ARE NOT ENOUGH SPELL COMBINATIONS FOR EVERY TIER 2 SPELL. CONSIDER ADDING MORE COLOR RUNES.");
        }
        else if ( comboList3.size() < TIER3_SPELL_RUNES.size() ) {
            System.out.println("WARN! THERE ARE NOT ENOUGH SPELL COMBINATIONS FOR EVERY TIER 3 SPELL. CONSIDER ADDING MORE COLOR RUNES.");
        }
        else if ( comboList4.size() < TIER4_SPELL_RUNES.size() ) {
            System.out.println("WARN! THERE ARE NOT ENOUGH SPELL COMBINATIONS FOR EVERY TIER 4 SPELL. CONSIDER ADDING MORE COLOR RUNES.");
        }
        else {
            Collections.shuffle(comboList1);
            Collections.shuffle(comboList2);
            Collections.shuffle(comboList3);
            Collections.shuffle(comboList4);
            for ( int i = 0; i < comboList1.size(); i++ ) {
                if ( i < TIER1_SPELL_RUNES.size() ) {
                    COMBO_MAP.put(TIER1_SPELL_RUNES.get(i), comboList1.get(i));
                }
                else break;
            }
            for ( int i = 0; i < comboList2.size(); i++ ) {
                if ( i < TIER2_SPELL_RUNES.size() ) {
                    COMBO_MAP.put(TIER2_SPELL_RUNES.get(i), comboList2.get(i));
                }
                else break;
            }
            for ( int i = 0; i < comboList3.size(); i++ ) {
                if ( i < TIER3_SPELL_RUNES.size() ) {
                    COMBO_MAP.put(TIER3_SPELL_RUNES.get(i), comboList3.get(i));
                }
                else break;
            }
            for ( int i = 0; i < comboList4.size(); i++ ) {
                if ( i < TIER4_SPELL_RUNES.size() ) {
                    COMBO_MAP.put(TIER4_SPELL_RUNES.get(i), comboList4.get(i));
                }
                else break;
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
}
