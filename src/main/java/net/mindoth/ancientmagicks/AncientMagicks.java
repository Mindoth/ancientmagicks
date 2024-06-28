package net.mindoth.ancientmagicks;

import com.google.common.collect.Lists;
import net.mindoth.ancientmagicks.config.AncientMagicksCommonConfig;
import net.mindoth.ancientmagicks.item.AncientMagicksTab;
import net.mindoth.ancientmagicks.item.ColorRuneItem;
import net.mindoth.ancientmagicks.item.SpellItem;
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
                if ( !(item.get() instanceof SpellItem) ) event.accept(item);
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

    public static List<SpellItem> SPELL_RUNES = Lists.newArrayList();
    public static List<ColorRuneItem> COLOR_RUNES = Lists.newArrayList();

    public static List<SpellItem> TIER1_SPELL_RUNES = Lists.newArrayList();
    public static List<SpellItem> TIER2_SPELL_RUNES = Lists.newArrayList();
    public static List<SpellItem> TIER3_SPELL_RUNES = Lists.newArrayList();
    public static List<SpellItem> TIER4_SPELL_RUNES = Lists.newArrayList();
    public static List<SpellItem> TIER5_SPELL_RUNES = Lists.newArrayList();

    public static HashMap<SpellItem, List<ColorRuneItem>> COMBO_MAP = new HashMap<>();

    public static void clearLists() {
        if ( !SPELL_RUNES.isEmpty() ) SPELL_RUNES.clear();
        if ( !COLOR_RUNES.isEmpty() ) COLOR_RUNES.clear();
        if ( !TIER1_SPELL_RUNES.isEmpty() ) TIER1_SPELL_RUNES.clear();
        if ( !TIER2_SPELL_RUNES.isEmpty() ) TIER2_SPELL_RUNES.clear();
        if ( !TIER3_SPELL_RUNES.isEmpty() ) TIER3_SPELL_RUNES.clear();
        if ( !TIER4_SPELL_RUNES.isEmpty() ) TIER4_SPELL_RUNES.clear();
        if ( !TIER5_SPELL_RUNES.isEmpty() ) TIER5_SPELL_RUNES.clear();
        if ( !COMBO_MAP.isEmpty() ) COMBO_MAP.clear();
    }

    public static void createSpellLists() {
        for ( Item item : ITEM_LIST ) if ( item instanceof SpellItem) SPELL_RUNES.add((SpellItem)item);
        for ( SpellItem spellItem : SPELL_RUNES ) {
            if ( spellItem.tier == 1 ) TIER1_SPELL_RUNES.add(spellItem);
            if ( spellItem.tier == 2 ) TIER2_SPELL_RUNES.add(spellItem);
            if ( spellItem.tier == 3 ) TIER3_SPELL_RUNES.add(spellItem);
            if ( spellItem.tier == 4 ) TIER4_SPELL_RUNES.add(spellItem);
            if ( spellItem.tier == 5 ) TIER5_SPELL_RUNES.add(spellItem);
        }
    }

    public static void comboRuneInit() {
        for ( Item item : ITEM_LIST ) if ( item instanceof ColorRuneItem ) COLOR_RUNES.add((ColorRuneItem)item);

        List<List<ColorRuneItem>> comboList1 = Lists.newArrayList();
        List<List<ColorRuneItem>> comboList2 = Lists.newArrayList();
        List<List<ColorRuneItem>> comboList3 = Lists.newArrayList();
        List<List<ColorRuneItem>> comboList4 = Lists.newArrayList();
        List<List<ColorRuneItem>> comboList5 = Lists.newArrayList();

        for ( int i = 0; i < COLOR_RUNES.size(); i++ ) {
            for ( int j = 0; j < COLOR_RUNES.size(); j++ ) {
                List<ColorRuneItem> tempList1 = Lists.newArrayList();
                tempList1.add(COLOR_RUNES.get(i));
                tempList1.add(COLOR_RUNES.get(j));
                if ( !hasDupeInList(comboList1, tempList1) ) comboList1.add(tempList1);
                for ( int k = 0; k < COLOR_RUNES.size(); k++ ) {
                    List<ColorRuneItem> tempList2 = Lists.newArrayList();
                    tempList2.add(COLOR_RUNES.get(i));
                    tempList2.add(COLOR_RUNES.get(j));
                    tempList2.add(COLOR_RUNES.get(k));
                    if ( !hasDupeInList(comboList2, tempList2) ) comboList2.add(tempList2);
                    for ( int l = 0; l < COLOR_RUNES.size(); l++ ) {
                        List<ColorRuneItem> tempList3 = Lists.newArrayList();
                        tempList3.add(COLOR_RUNES.get(i));
                        tempList3.add(COLOR_RUNES.get(j));
                        tempList3.add(COLOR_RUNES.get(k));
                        tempList3.add(COLOR_RUNES.get(l));
                        if ( !hasDupeInList(comboList3, tempList3) ) comboList3.add(tempList3);
                        for ( int m = 0; m < COLOR_RUNES.size(); m++ ) {
                            List<ColorRuneItem> tempList4 = Lists.newArrayList();
                            tempList4.add(COLOR_RUNES.get(i));
                            tempList4.add(COLOR_RUNES.get(j));
                            tempList4.add(COLOR_RUNES.get(k));
                            tempList4.add(COLOR_RUNES.get(l));
                            tempList4.add(COLOR_RUNES.get(m));
                            if ( !hasDupeInList(comboList4, tempList4) ) comboList4.add(tempList4);
                            for ( int n = 0; n < COLOR_RUNES.size(); n++ ) {
                                List<ColorRuneItem> tempList5 = Lists.newArrayList();
                                tempList5.add(COLOR_RUNES.get(i));
                                tempList5.add(COLOR_RUNES.get(j));
                                tempList5.add(COLOR_RUNES.get(k));
                                tempList5.add(COLOR_RUNES.get(l));
                                tempList5.add(COLOR_RUNES.get(m));
                                tempList5.add(COLOR_RUNES.get(n));
                                if ( !hasDupeInList(comboList5, tempList5) ) comboList5.add(tempList5);
                            }
                        }
                    }
                }
            }
        }

        if ( comboList1.size() < TIER1_SPELL_RUNES.size() ) System.out.println("WARN! THERE ARE NOT ENOUGH SPELL COMBINATIONS FOR EVERY TIER 1 SPELL.");
        else if ( comboList2.size() < TIER2_SPELL_RUNES.size() ) System.out.println("WARN! THERE ARE NOT ENOUGH SPELL COMBINATIONS FOR EVERY TIER 2 SPELL.");
        else if ( comboList3.size() < TIER3_SPELL_RUNES.size() ) System.out.println("WARN! THERE ARE NOT ENOUGH SPELL COMBINATIONS FOR EVERY TIER 3 SPELL.");
        else if ( comboList4.size() < TIER4_SPELL_RUNES.size() ) System.out.println("WARN! THERE ARE NOT ENOUGH SPELL COMBINATIONS FOR EVERY TIER 4 SPELL.");
        else if ( comboList5.size() < TIER5_SPELL_RUNES.size() ) System.out.println("WARN! THERE ARE NOT ENOUGH SPELL COMBINATIONS FOR EVERY TIER 5 SPELL.");
        else {
            Collections.shuffle(comboList1);
            Collections.shuffle(comboList2);
            Collections.shuffle(comboList3);
            Collections.shuffle(comboList4);
            Collections.shuffle(comboList5);
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
            for ( int i = 0; i < comboList5.size(); i++ ) {
                if ( i < TIER5_SPELL_RUNES.size() ) {
                    COMBO_MAP.put(TIER5_SPELL_RUNES.get(i), comboList5.get(i));
                }
                else break;
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
