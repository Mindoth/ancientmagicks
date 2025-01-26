package net.mindoth.ancientmagicks;

import com.google.common.collect.Lists;
import net.mindoth.ancientmagicks.config.AncientMagicksCommonConfig;
import net.mindoth.ancientmagicks.item.AncientMagicksTab;
import net.mindoth.ancientmagicks.item.ColorRuneItem;
import net.mindoth.ancientmagicks.item.castingitem.SpecialCastingItem;
import net.mindoth.ancientmagicks.item.spell.abstractspell.SpellItem;
import net.mindoth.ancientmagicks.item.spell.abstractspell.SpellStorageItem;
import net.mindoth.ancientmagicks.network.AncientMagicksNetwork;
import net.mindoth.ancientmagicks.registries.*;
import net.mindoth.ancientmagicks.registries.attribute.AncientMagicksAttributes;
import net.mindoth.ancientmagicks.registries.recipe.AncientMagicksRecipes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
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

import javax.annotation.Nullable;
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
        AncientMagicksModifiers.LOOT_MODIFIER_SERIALIZERS.register(modEventBus);
        AncientMagicksModifiers.LOOT_FUNCTIONS.register(modEventBus);
        AncientMagicksAttributes.ATTRIBUTES.register(modEventBus);
        AncientMagicksRecipes.SERIALIZERS.register(modEventBus);

        //KEEP THESE LAST
        modEventBus.addListener(this::addCreative);
        modEventBus.addListener(this::commonSetup);
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if ( event.getTab() == AncientMagicksTab.ANCIENTMAGICKS_TAB.get() ) {
            for ( RegistryObject<Item> item : AncientMagicksItems.ITEMS.getEntries() ) {
                if ( !(item.get() instanceof SpellItem || item.get() instanceof SpellStorageItem) ) event.accept(item);
            }
            for ( SpellItem spell : AncientMagicks.SPELL_LIST ) event.accept(createSpellScroll(new ItemStack(AncientMagicksItems.SPELL_SCROLL.get()), spell));
        }
    }

    public static ItemStack createSpellScroll(ItemStack stack, SpellItem spell) {
        String spellString = ForgeRegistries.ITEMS.getKey(spell).toString();
        stack.getOrCreateTag().putString(SpecialCastingItem.TAG_STORED_SPELL, spellString);
        if ( spell.spellTier >= 4 && spell.spellTier <= 6 ) stack.getOrCreateTag().putInt("CustomModelData", 1);
        return stack;
    }

    //List stuff
    public static List<Item> ITEM_LIST = Lists.newArrayList();
    public static List<EntityType<?>> MOB_LIST = Lists.newArrayList();
    public static List<EntityType<?>> DISABLED_POLYMOBS = Lists.newArrayList();

    public void commonSetup(final FMLCommonSetupEvent event) {
        ITEM_LIST = new ArrayList<>(ForgeRegistries.ITEMS.getValues());
        AncientMagicksNetwork.init();
        createLists();
        createPolymobDisableList();
    }

    private static void createLists() {
        if ( !SPELL_LIST.isEmpty() ) SPELL_LIST.clear();
        if ( !COLOR_RUNE_LIST.isEmpty() ) COLOR_RUNE_LIST.clear();
        if ( !COMBO_MAP.isEmpty() ) COMBO_MAP.clear();
        createSpellDisableList();
        for ( Item item : ITEM_LIST ) if ( item instanceof SpellItem spellItem && isSpellEnabled(spellItem) ) SPELL_LIST.add(spellItem);
        for ( Item item : ITEM_LIST ) if ( item instanceof ColorRuneItem colorRuneItem ) COLOR_RUNE_LIST.add(colorRuneItem);
    }

    public static boolean isSpellEnabled(SpellItem spell) {
        return DISABLED_SPELLS.isEmpty() || !DISABLED_SPELLS.contains(spell);
    }

    private static void createSpellDisableList() {
        String configString = AncientMagicksCommonConfig.DISABLED_SPELLS.get();
        for ( String string : List.of(configString.replaceAll("[\\[\\]]", "").replaceAll(" ", "").replaceAll("\n", "").split(",")) ) {
            Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(string));
            if ( item instanceof SpellItem spellItem) DISABLED_SPELLS.add(spellItem);
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
                if ( tempEntity instanceof Mob ) MOB_LIST.add(entityType);
            }
        }
    }

    public static List<ColorRuneItem> COLOR_RUNE_LIST = Lists.newArrayList();
    public static List<SpellItem> SPELL_LIST = Lists.newArrayList();
    public static List<SpellItem> DISABLED_SPELLS = Lists.newArrayList();
    public static HashMap<SpellItem, List<ColorRuneItem>> COMBO_MAP = new HashMap<>();

    private static List<List<ColorRuneItem>> createTempList(List<List<ColorRuneItem>> comboList, int i, int j, int k, @Nullable Integer l, @Nullable Integer m, @Nullable Integer n) {
        List<ColorRuneItem> tempList = Lists.newArrayList();
        tempList.add(COLOR_RUNE_LIST.get(i));
        tempList.add(COLOR_RUNE_LIST.get(j));
        tempList.add(COLOR_RUNE_LIST.get(k));
        if ( l != null ) tempList.add(COLOR_RUNE_LIST.get(l));
        if ( m != null ) tempList.add(COLOR_RUNE_LIST.get(m));
        if ( n != null ) tempList.add(COLOR_RUNE_LIST.get(n));
        if ( hasNoDupeInList(comboList, tempList) ) comboList.add(tempList);
        return comboList;
    }

    //Check how many Color Runes should be in a Spell Code. The amount increases depending on how many Spells are registered.
    public static int comboSizeCalc() {
        //return (n * (n + 2) * (n + 1)) >= (6 * SPELL_LIST.size());
        int returnValue = 0;
        if ( (56 * (56 + 2) * (56 + 1)) >= (6 * SPELL_LIST.size()) ) returnValue = 3;
        else if ( (126 * (126 + 2) * (126 + 1)) >= (6 * SPELL_LIST.size()) ) returnValue = 4;
        else if ( (252 * (252 + 2) * (252 + 1)) >= (6 * SPELL_LIST.size()) ) returnValue = 5;
        else if ( (462 * (462 + 2) * (462 + 1)) >= (6 * SPELL_LIST.size()) ) returnValue = 6;
        return returnValue;
    }

    public static void randomizeSpells() {
        Logger logger = getLogger();

        List<List<ColorRuneItem>> comboList = Lists.newArrayList();
        for ( int i = 0; i < COLOR_RUNE_LIST.size(); i++ ) {
            for ( int j = 0; j < COLOR_RUNE_LIST.size(); j++ ) {
                for ( int k = 0; k < COLOR_RUNE_LIST.size(); k++ ) {
                    if ( comboSizeCalc() == 3 ) createTempList(comboList, i, j, k, null, null, null);
                    for ( int l = 0; l < COLOR_RUNE_LIST.size(); l++ ) {
                        if ( comboSizeCalc() == 3 ) break;
                        else if ( comboSizeCalc() == 4 ) createTempList(comboList, i, j, k, l, null, null);
                        for ( int m = 0; m < COLOR_RUNE_LIST.size(); m++ ) {
                            if ( comboSizeCalc() == 4 ) break;
                            else if ( comboSizeCalc() == 5 ) createTempList(comboList, i, j, k, l, m, null);
                            for ( int n = 0; n < COLOR_RUNE_LIST.size(); n++ ) {
                                if ( comboSizeCalc() == 5 ) break;
                                else if ( comboSizeCalc() == 6 ) createTempList(comboList, i, j, k, l, m, n);
                            }
                        }
                    }
                }
            }
        }
        if ( comboList.size() < SPELL_LIST.size() ) {
            logger.warn("WARN! THERE ARE NOT ENOUGH SPELL COMBINATIONS FOR EVERY SPELL. SOMETHING'S WRONG");
        }
        else {
            if ( !SPELL_LIST.isEmpty() ) {
                Collections.shuffle(comboList);
                for ( int i = 0; i < comboList.size(); i++ ) {
                    if ( i < SPELL_LIST.size() && isSpellEnabled(SPELL_LIST.get(i)) ) COMBO_MAP.put(SPELL_LIST.get(i), comboList.get(i));
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
