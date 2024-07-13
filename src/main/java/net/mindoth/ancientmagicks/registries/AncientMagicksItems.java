package net.mindoth.ancientmagicks.registries;

import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.item.TabletItem;
import net.mindoth.ancientmagicks.item.glyph.FireGlyphItem;
import net.mindoth.ancientmagicks.item.glyph.MindControlGlyphItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class AncientMagicksItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, AncientMagicks.MOD_ID);

    //Ancient Tablet
    public static final RegistryObject<Item> STONE_TABLET = ITEMS.register("stone_tablet",
            () -> new TabletItem(new Item.Properties()));

    //Glyphs
    public static final RegistryObject<Item> FIRE_GLYPH = ITEMS.register("fire_glyph",
            () -> new FireGlyphItem(new Item.Properties(), "am_fire"));

    public static final RegistryObject<Item> MIND_CONTROL_GLYPH = ITEMS.register("mind_control_glyph",
            () -> new MindControlGlyphItem(new Item.Properties(), "am_mind_control"));
}
