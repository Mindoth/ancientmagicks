package net.mindoth.ancientmagicks.registries;

import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.item.TabletItem;
import net.mindoth.ancientmagicks.item.glyph.*;
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
    public static final RegistryObject<Item> BLINK_GLYPH = ITEMS.register("blink_glyph",
            () -> new BlinkGlyph(new Item.Properties()));

    public static final RegistryObject<Item> FIRE_GLYPH = ITEMS.register("fire_glyph",
            () -> new FireGlyph(new Item.Properties()));

    public static final RegistryObject<Item> LIGHTNING_GLYPH = ITEMS.register("lightning_glyph",
            () -> new LightningGlyph(new Item.Properties()));

    public static final RegistryObject<Item> MIND_CONTROL_GLYPH = ITEMS.register("mind_control_glyph",
            () -> new MindControlGlyph(new Item.Properties()));

    public static final RegistryObject<Item> NUMB_PAIN_GLYPH = ITEMS.register("numb_pain_glyph",
            () -> new NumbPainGlyph(new Item.Properties()));

    public static final RegistryObject<Item> POLYMORPH_GLYPH = ITEMS.register("polymorph_glyph",
            () -> new PolymorphGlyph(new Item.Properties()));
}
