package net.mindoth.ancientmagicks;

import net.mindoth.ancientmagicks.config.AncientMagicksCommonConfig;
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
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

@Mod(AncientMagicks.MOD_ID)
public class AncientMagicks {
    public static final String MOD_ID = "ancientmagicks";
    public static Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

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
        createAnvilRecipes();
    }

    public static HashMap<Item, String> ANVIL_RECIPE_MAP;

    private void createAnvilRecipes() {
        ANVIL_RECIPE_MAP = new HashMap<>();
        ANVIL_RECIPE_MAP.put(Items.ARROW, "am_projectile");
    }
}
