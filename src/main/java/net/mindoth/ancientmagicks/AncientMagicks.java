package net.mindoth.ancientmagicks;

import com.google.common.collect.Lists;
import net.mindoth.ancientmagicks.config.AncientMagicksCommonConfig;
import net.mindoth.ancientmagicks.item.AncientMagicksTab;
import net.mindoth.ancientmagicks.item.modifier.ColorModifierItem;
import net.mindoth.ancientmagicks.network.AncientMagicksNetwork;
import net.mindoth.ancientmagicks.registries.*;
import net.mindoth.ancientmagicks.registries.attribute.AncientMagicksAttributes;
import net.mindoth.ancientmagicks.registries.recipe.AncientMagicksRecipes;
import net.mindoth.ancientmagicks.registries.recipe.BetterBrewingRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
        AncientMagicksBlocks.BLOCKS.register(modEventBus);
        AncientMagicksEntities.ENTITIES.register(modEventBus);
        AncientMagicksEffects.EFFECTS.register(modEventBus);
        AncientMagicksPotions.POTIONS.register(modEventBus);
        AncientMagicksParticles.PARTICLES.register(modEventBus);
        AncientMagicksModifiers.LOOT_MODIFIER_SERIALIZERS.register(modEventBus);
        AncientMagicksModifiers.LOOT_FUNCTIONS.register(modEventBus);
        AncientMagicksAttributes.ATTRIBUTES.register(modEventBus);
        AncientMagicksRecipes.SERIALIZERS.register(modEventBus);
        AncientMagicksMenus.MENUS.register(modEventBus);

        //KEEP THESE LAST
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::addCreative);
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if ( event.getTab() == AncientMagicksTab.ANCIENTMAGICKS_TAB.get() ) {
            for ( RegistryObject<Block> block : AncientMagicksBlocks.BLOCKS.getEntries() ) event.accept(block);
            for ( RegistryObject<Item> item : AncientMagicksItems.ITEMS.getEntries() ) event.accept(item);
        }
    }

    public static List<Item> ITEM_LIST = Lists.newArrayList();

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            AncientMagicksNetwork.init();
            ITEM_LIST = new ArrayList<>(ForgeRegistries.ITEMS.getValues());

            BrewingRecipeRegistry.addRecipe(new BetterBrewingRecipe(Potions.AWKWARD, Items.ELYTRA, AncientMagicksPotions.FLIGHT_POTION.get()));
            BrewingRecipeRegistry.addRecipe(new BetterBrewingRecipe(AncientMagicksPotions.FLIGHT_POTION.get(), Items.REDSTONE, AncientMagicksPotions.LONG_FLIGHT_POTION.get()));
            BrewingRecipeRegistry.addRecipe(new BetterBrewingRecipe(Potions.AWKWARD, Items.FEATHER, AncientMagicksPotions.FALL_CONTROL_POTION.get()));
            BrewingRecipeRegistry.addRecipe(new BetterBrewingRecipe(AncientMagicksPotions.FALL_CONTROL_POTION.get(), Items.REDSTONE, AncientMagicksPotions.LONG_FALL_CONTROL_POTION.get()));
            BrewingRecipeRegistry.addRecipe(new BetterBrewingRecipe(Potions.AWKWARD, Items.POTATO, AncientMagicksPotions.SLEEP_POTION.get()));
            BrewingRecipeRegistry.addRecipe(new BetterBrewingRecipe(AncientMagicksPotions.SLEEP_POTION.get(), Items.REDSTONE, AncientMagicksPotions.LONG_SLEEP_POTION.get()));
            BrewingRecipeRegistry.addRecipe(new BetterBrewingRecipe(Potions.AWKWARD, Items.ENDER_PEARL, AncientMagicksPotions.TELEBLOCK_POTION.get()));
            BrewingRecipeRegistry.addRecipe(new BetterBrewingRecipe(AncientMagicksPotions.TELEBLOCK_POTION.get(), Items.REDSTONE, AncientMagicksPotions.LONG_TELEBLOCK_POTION.get()));
        });
    }

    public static void createLists(Random seededRand) {
        createArcaneDustList(seededRand);
    }

    /*public static boolean isSpellEnabled(SpellItem spell) {
        List<SpellItem> disabledSpells = Lists.newArrayList();
        List<String> configString = AncientMagicksCommonConfig.DISABLED_SPELLS.get();
        configString.forEach(string -> {
            Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(string));
            if ( item instanceof SpellItem spellItem ) disabledSpells.add(spellItem);
        });
        return disabledSpells.isEmpty() || !disabledSpells.contains(spell);
    }*/

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

    //Thank god for Stack Overflow (the website)
    //https://stackoverflow.com/questions/1075656/simple-way-to-find-if-two-different-lists-contain-exactly-the-same-elements/67986292#67986292
    public static boolean listsMatch(List<ColorModifierItem> firstList, List<ColorModifierItem> secondList) {
        if ( firstList == secondList ) return true;
        if ( firstList != null && secondList != null ) {
            if ( firstList.isEmpty() && secondList.isEmpty() ) return true;
            if ( firstList.size() != secondList.size() ) return false;
            List<ColorModifierItem> tmpSecondList = new ArrayList<>(secondList);
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

    //Check how many Color Runes should be in a Spell Code. The amount increases depending on how many Spells are registered.
    public static int comboSizeCalc() {
        //return (n * (n + 2) * (n + 1)) >= (6 * SPELL_LIST.size());
        /*int returnValue = 0;
        if ( (56 * (56 + 2) * (56 + 1)) >= (6 * SPELL_LIST.size()) ) returnValue = 3;
        else if ( (126 * (126 + 2) * (126 + 1)) >= (6 * SPELL_LIST.size()) ) returnValue = 4;
        else if ( (252 * (252 + 2) * (252 + 1)) >= (6 * SPELL_LIST.size()) ) returnValue = 5;
        else if ( (462 * (462 + 2) * (462 + 1)) >= (6 * SPELL_LIST.size()) ) returnValue = 6;
        return returnValue;*/
        return 3;
    }
}
