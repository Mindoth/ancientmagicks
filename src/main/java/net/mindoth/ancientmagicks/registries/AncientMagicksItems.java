package net.mindoth.ancientmagicks.registries;

import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.item.StoneTabletItem;
import net.mindoth.ancientmagicks.item.glyph.FireGlyphItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class AncientMagicksItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, AncientMagicks.MOD_ID);

    //Ancient Tablet
    public static final RegistryObject<Item> STONE_TABLET = ITEMS.register("stone_tablet",
            () -> new StoneTabletItem(new Item.Properties()));

    //Glyphs
    public static final RegistryObject<Item> FIRE_GLYPH = ITEMS.register("fire_glyph",
            () -> new FireGlyphItem(new Item.Properties(), "am_fire"));
}
