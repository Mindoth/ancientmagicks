package net.mindoth.ancientmagicks;

import com.google.common.collect.Lists;
import net.mindoth.ancientmagicks.config.AncientMagicksCommonConfig;
import net.mindoth.ancientmagicks.item.GlyphItem;
import net.mindoth.ancientmagicks.network.AncientMagicksNetwork;
import net.mindoth.ancientmagicks.registries.AncientMagicksEffects;
import net.mindoth.ancientmagicks.registries.AncientMagicksEntities;
import net.mindoth.ancientmagicks.registries.AncientMagicksItems;
import net.mindoth.ancientmagicks.registries.AncientMagicksParticles;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
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

import java.util.HashMap;
import java.util.List;

@Mod(AncientMagicks.MOD_ID)
public class AncientMagicks {
    public static final String MOD_ID = "ancientmagicks";

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

        modEventBus.addListener(this::addCreative);
        modEventBus.addListener(this::commonSetup);
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if ( event.getTab() == AncientMagicksTab.ANCIENTMAGICKS_TAB.get() ) {
            for ( RegistryObject<Item> item : AncientMagicksItems.ITEMS.getEntries() ) {
                event.accept(item);
            }
        }
    }

    public void commonSetup(final FMLCommonSetupEvent event) {
        AncientMagicksNetwork.init();
        createGlyphList();
        createAnvilRecipes();
    }

    public static List<GlyphItem> GLYPH_LIST;

    private void createGlyphList() {
        GLYPH_LIST = Lists.newArrayList();
        for ( Item item : ForgeRegistries.ITEMS.getValues() ) {
            if ( item instanceof GlyphItem glyphItem ) GLYPH_LIST.add(glyphItem);
        }
    }

    public static HashMap<Item, String> ANVIL_RECIPE_MAP;

    private void createAnvilRecipes() {
        ANVIL_RECIPE_MAP = new HashMap<>();
        ANVIL_RECIPE_MAP.put(Items.ARROW, "am_projectile");
        ANVIL_RECIPE_MAP.put(Items.GLASS_BOTTLE, "am_self");
        for ( Item item : ForgeRegistries.ITEMS.getValues()) {
            if ( item instanceof GlyphItem glyph ) ANVIL_RECIPE_MAP.put(glyph, ForgeRegistries.ITEMS.getKey(glyph).toString());
        }
    }
}
