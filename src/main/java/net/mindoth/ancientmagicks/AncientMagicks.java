package net.mindoth.ancientmagicks;

import com.google.common.collect.Lists;
import net.mindoth.ancientmagicks.config.AncientMagicksCommonConfig;
import net.mindoth.ancientmagicks.item.AncientMagicksTab;
import net.mindoth.ancientmagicks.item.ColorRuneItem;
import net.mindoth.ancientmagicks.item.castingitem.SpecialCastingItem;
import net.mindoth.ancientmagicks.item.SpellItem;
import net.mindoth.ancientmagicks.item.SpellStorageItem;
import net.mindoth.ancientmagicks.network.AncientMagicksNetwork;
import net.mindoth.ancientmagicks.registries.*;
import net.mindoth.ancientmagicks.registries.attribute.AncientMagicksAttributes;
import net.mindoth.ancientmagicks.registries.recipe.AncientMagicksRecipes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
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
import java.util.*;

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
        AncientMagicksEnchantments.ENCHANTMENTS.register(modEventBus);

        //KEEP THESE LAST
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::addCreative);
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if ( event.getTab() == AncientMagicksTab.ANCIENTMAGICKS_TAB.get() ) {
            for ( RegistryObject<Item> item : AncientMagicksItems.ITEMS.getEntries() ) {
                if ( !(item.get() instanceof SpellItem || item.get() instanceof SpellStorageItem) ) event.accept(item);
            }
            for ( int i = 1; i <= 9; i++ ) {
                for ( RegistryObject<Item> item : AncientMagicksItems.ITEMS.getEntries() ) {
                    if ( item.get() instanceof SpellItem spell && spell.getSpellTier() == i ) event.accept(createSpellScroll(new ItemStack(AncientMagicksItems.SPELL_SCROLL.get()), spell));
                }
            }
        }
    }

    public static ItemStack createSpellScroll(ItemStack stack, SpellItem spell) {
        String spellString = ForgeRegistries.ITEMS.getKey(spell).toString();
        stack.getOrCreateTag().putString(SpecialCastingItem.TAG_STORED_SPELL, spellString);
        if ( spell.getSpellTier() >= 4 && spell.getSpellTier() <= 6 ) stack.getOrCreateTag().putInt("CustomModelData", 1);
        if ( spell.getSpellTier() >= 7 ) stack.getOrCreateTag().putInt("CustomModelData", 2);
        return stack;
    }

    public static List<Item> ITEM_LIST = Lists.newArrayList();

    private void commonSetup(final FMLCommonSetupEvent event) {
        AncientMagicksNetwork.init();
        ITEM_LIST = new ArrayList<>(ForgeRegistries.ITEMS.getValues());
    }

    public static void createLists(Random seededRand) {
        createColorRuneList();
        createSpellList();
        createArcaneDustList(seededRand);
        createSpellComboMap(seededRand);
    }

    public static List<ColorRuneItem> COLOR_RUNE_LIST = Lists.newArrayList();

    private static void createColorRuneList() {
        COLOR_RUNE_LIST = Lists.newArrayList();
        for ( Item item : ITEM_LIST ) if ( item instanceof ColorRuneItem colorRuneItem ) COLOR_RUNE_LIST.add(colorRuneItem);
    }

    public static List<SpellItem> SPELL_LIST = Lists.newArrayList();

    private static void createSpellList() {
        SPELL_LIST = Lists.newArrayList();
        for ( Item item : ITEM_LIST ) if ( item instanceof SpellItem spellItem ) SPELL_LIST.add(spellItem);
    }

    public static boolean isSpellEnabled(SpellItem spell) {
        List<SpellItem> disabledSpells = Lists.newArrayList();
        List<String> configString = AncientMagicksCommonConfig.DISABLED_SPELLS.get();
        configString.forEach(string -> {
            Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(string));
            if ( item instanceof SpellItem spellItem ) disabledSpells.add(spellItem);
        });
        return disabledSpells.isEmpty() || !disabledSpells.contains(spell);
    }

    public static List<Item> ARCANE_DUST_LIST = Lists.newArrayList();

    private static void createArcaneDustList(Random seededRand) {
        ARCANE_DUST_LIST = Lists.newArrayList();
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
            AncientMagicks.ARCANE_DUST_LIST.add(item);
        }
    }

    private static void createTempList(List<List<ColorRuneItem>> comboList, int i, int j, int k, @Nullable Integer l, @Nullable Integer m, @Nullable Integer n) {
        List<ColorRuneItem> tempList = Lists.newArrayList();
        tempList.add(COLOR_RUNE_LIST.get(i));
        tempList.add(COLOR_RUNE_LIST.get(j));
        tempList.add(COLOR_RUNE_LIST.get(k));
        if ( l != null ) tempList.add(COLOR_RUNE_LIST.get(l));
        if ( m != null ) tempList.add(COLOR_RUNE_LIST.get(m));
        if ( n != null ) tempList.add(COLOR_RUNE_LIST.get(n));
        if ( hasNoDupeInList(comboList, tempList) ) comboList.add(tempList);
    }

    public static HashMap<SpellItem, List<ColorRuneItem>> COMBO_MAP = new HashMap<>();

    //TODO remove the ENTIRE system changing COMBO_MAP to string and back in ColorRuneItem. Maybe iterate through itemStacks into a list when sending packets?
    private static void createSpellComboMap(Random seededRand) {
        COMBO_MAP = new HashMap<>();
        AncientMagicks.randomizeSpells(seededRand);

        StringBuilder tempString = new StringBuilder();
        for ( Map.Entry<SpellItem, List<ColorRuneItem>> entry : COMBO_MAP.entrySet() ) {
            tempString.append(ForgeRegistries.ITEMS.getKey(entry.getKey())).append("=").append(entry.getValue()).append(";").append("\n");
        }

        String comboString = tempString.toString().replaceAll("\n", "").replaceAll(".$", "");

        ColorRuneItem. CURRENT_COMBO_TAG = new CompoundTag();
        ColorRuneItem.CURRENT_COMBO_TAG.putString("am_combostring", comboString);
        ColorRuneItem.CURRENT_COMBO_MAP = new HashMap<>();
        ColorRuneItem.CURRENT_COMBO_MAP = ColorRuneItem.buildComboMap(ColorRuneItem.CURRENT_COMBO_TAG.getString("am_combostring"));
    }

    private static void randomizeSpells(Random seededRand) {
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
                Collections.shuffle(comboList, seededRand);
                for ( int i = 0; i < comboList.size(); i++ ) {
                    if ( i < SPELL_LIST.size() ) {
                        if ( isSpellEnabled(SPELL_LIST.get(i)) ) COMBO_MAP.put(SPELL_LIST.get(i), comboList.get(i));
                    }
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
}
