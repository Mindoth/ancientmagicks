package net.mindoth.ancientmagicks;

import com.google.common.collect.Lists;
import net.mindoth.ancientmagicks.config.ModCommonConfig;
import net.mindoth.ancientmagicks.item.ModCreativeTab;
import net.mindoth.ancientmagicks.network.ModNetwork;
import net.mindoth.ancientmagicks.registries.*;
import net.mindoth.ancientmagicks.registries.attribute.ModAttributes;
import net.mindoth.ancientmagicks.registries.recipe.ModRecipes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Mod(AncientMagicks.MOD_ID)
public class AncientMagicks {
    public static final String MOD_ID = "ancientmagicks";

    public AncientMagicks() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        if ( FMLEnvironment.dist == Dist.CLIENT ) AncientMagicksClient.registerHandlers();
        addRegistries(modEventBus);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ModCommonConfig.SPEC, "ancientmagicks-common.toml");
    }

    private void addRegistries(final IEventBus modEventBus) {
        ModCreativeTab.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModBlocks.BLOCKS.register(modEventBus);
        ModEntities.ENTITIES.register(modEventBus);
        ModParticles.PARTICLES.register(modEventBus);
        ModModifiers.LOOT_MODIFIER_SERIALIZERS.register(modEventBus);
        ModModifiers.LOOT_FUNCTIONS.register(modEventBus);
        ModAttributes.ATTRIBUTES.register(modEventBus);
        ModRecipes.SERIALIZERS.register(modEventBus);
        ModMenus.MENUS.register(modEventBus);

        //KEEP THESE LAST
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::addCreative);
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if ( event.getTab() == ModCreativeTab.ANCIENTMAGICKS_TAB.get() ) {
            for ( RegistryObject<Block> block : ModBlocks.BLOCKS.getEntries() ) event.accept(block);
            for ( RegistryObject<Item> item : ModItems.ITEMS.getEntries() ) event.accept(item);
        }
    }

    public static List<Item> ITEM_LIST = Lists.newArrayList();

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            ModNetwork.init();
            ITEM_LIST = new ArrayList<>(ForgeRegistries.ITEMS.getValues());
        });
    }

    public static void createLists(Random seededRand) {
        createArcaneDustList(seededRand);
    }

    public static List<Item> ARCANE_DUST_LIST = Lists.newArrayList();

    private static void createArcaneDustList(Random seededRand) {
        ARCANE_DUST_LIST = Lists.newArrayList();
        List<Item> vanillaList = Lists.newArrayList();
        List<Item> disabledList = Lists.newArrayList();
        List<String> configString = ModCommonConfig.DISABLED_ARCANE_DUST_RECIPE_ENTRIES.get();
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
    public static boolean listsMatch(List<Item> firstList, List<Item> secondList) {
        if ( firstList == secondList ) return true;
        if ( firstList != null && secondList != null ) {
            if ( firstList.isEmpty() && secondList.isEmpty() ) return true;
            if ( firstList.size() != secondList.size() ) return false;
            List<Item> tmpSecondList = new ArrayList<>(secondList);
            Object currFirstObject;
            for ( int i = 1 ; i <= firstList.size() ; i++ ) {
                currFirstObject = firstList.get(i - 1);
                boolean removed = tmpSecondList.remove(currFirstObject);
                if ( !removed ) return false;
                if ( i != firstList.size() ) if ( tmpSecondList.isEmpty() ) return false;
            }
            return tmpSecondList.isEmpty();
        }
        return false;
    }

    //How many Color Runes should be in a Spell Code.
    public static int comboSizeCalc() {
        return 3;
    }
}
