package net.mindoth.ancientmagicks.registries;

import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.item.ColorRuneItem;
import net.mindoth.ancientmagicks.item.RuneItem;
import net.mindoth.ancientmagicks.item.TabletBag;
import net.mindoth.ancientmagicks.item.castingitem.CastingItem;
import net.mindoth.ancientmagicks.item.castingitem.AMBagType;
import net.mindoth.ancientmagicks.item.spell.blink.BlinkTablet;
import net.mindoth.ancientmagicks.item.spell.calllightning.CallLightningTablet;
import net.mindoth.ancientmagicks.item.spell.chaoticpolymorph.ChaoticPolymorphTablet;
import net.mindoth.ancientmagicks.item.spell.collapse.CollapseTablet;
import net.mindoth.ancientmagicks.item.spell.deafeningblast.DeafeningBlastTablet;
import net.mindoth.ancientmagicks.item.spell.dynamite.DynamiteTablet;
import net.mindoth.ancientmagicks.item.spell.fireball.FireballTablet;
import net.mindoth.ancientmagicks.item.spell.flight.FlightTablet;
import net.mindoth.ancientmagicks.item.spell.polymorph.PolymorphTablet;
import net.mindoth.ancientmagicks.item.spell.raisedead.RaiseDeadTablet;
import net.mindoth.ancientmagicks.item.spell.slimeball.SlimeballTablet;
import net.mindoth.ancientmagicks.item.spell.slowfall.SlowFallTablet;
import net.mindoth.ancientmagicks.item.spell.telekineticgrab.TelekineticGrabTablet;
import net.mindoth.ancientmagicks.item.spell.ward.WardTablet;
import net.mindoth.ancientmagicks.item.spell.windburst.WindBurstTablet;
import net.mindoth.ancientmagicks.item.spell.witchspark.WitchSparkTablet;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class AncientMagicksItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, AncientMagicks.MOD_ID);

    //Casting items
    public static final RegistryObject<Item> CASTING_ITEM = ITEMS.register("casting_item",
            () -> new CastingItem(new Item.Properties()));

    //Tablet Bag
    public static final RegistryObject<Item> TABLET_BAG = ITEMS.register("tablet_bag",
            () -> new TabletBag(AMBagType.TABLET_BAG));



    //Runes
    public static final RegistryObject<Item> STONE_SLATE = ITEMS.register("empty_rune",
            () -> new RuneItem(new Item.Properties(), 0));


    public static final RegistryObject<Item> BLUE_RUNE = ITEMS.register("blue_rune",
            () -> new ColorRuneItem(new Item.Properties(), 0, "\u00A7b"));

    public static final RegistryObject<Item> PURPLE_RUNE = ITEMS.register("purple_rune",
            () -> new ColorRuneItem(new Item.Properties(), 0, "\u00A7d"));

    public static final RegistryObject<Item> YELLOW_RUNE = ITEMS.register("yellow_rune",
            () -> new ColorRuneItem(new Item.Properties(), 0, "\u00A7e"));

    public static final RegistryObject<Item> GREEN_RUNE = ITEMS.register("green_rune",
            () -> new ColorRuneItem(new Item.Properties(), 0, "\u00A7a"));

    public static final RegistryObject<Item> WHITE_RUNE = ITEMS.register("white_rune",
            () -> new ColorRuneItem(new Item.Properties(), 0, "\u00A7f"));

    public static final RegistryObject<Item> BLACK_RUNE = ITEMS.register("black_rune",
            () -> new ColorRuneItem(new Item.Properties(), 0, "\u00A78"));


    public static final RegistryObject<Item> WITCH_SPARK_TABLET = ITEMS.register("witch_spark_tablet",
            () -> new WitchSparkTablet(new Item.Properties(), 1));

    public static final RegistryObject<Item> SLIMEBALL_TABLET = ITEMS.register("slimeball_tablet",
            () -> new SlimeballTablet(new Item.Properties(), 1));

    public static final RegistryObject<Item> WARD_TABLET = ITEMS.register("ward_tablet",
            () -> new WardTablet(new Item.Properties(), 1));

    public static final RegistryObject<Item> SLOW_FALL_TABLET = ITEMS.register("slow_fall_tablet",
            () -> new SlowFallTablet(new Item.Properties(), 1));

    public static final RegistryObject<Item> TELEKINETIC_GRAB_TABLET = ITEMS.register("telekinetic_grab_tablet",
            () -> new TelekineticGrabTablet(new Item.Properties(), 1));

    public static final RegistryObject<Item> WIND_BURST_TABLET = ITEMS.register("wind_burst_tablet",
            () -> new WindBurstTablet(new Item.Properties(), 2));

    public static final RegistryObject<Item> DYNAMITE_TABLET = ITEMS.register("dynamite_tablet",
            () -> new DynamiteTablet(new Item.Properties(), 2));

    public static final RegistryObject<Item> BLINK_TABLET = ITEMS.register("blink_tablet",
            () -> new BlinkTablet(new Item.Properties(), 2));

    public static final RegistryObject<Item> FIREBALL_TABLET = ITEMS.register("fireball_tablet",
            () -> new FireballTablet(new Item.Properties(), 3));

    public static final RegistryObject<Item> CALL_LIGHTNING_TABLET = ITEMS.register("call_lightning_tablet",
            () -> new CallLightningTablet(new Item.Properties(), 3));

    public static final RegistryObject<Item> RAISE_DEAD_TABLET = ITEMS.register("raise_dead_tablet",
            () -> new RaiseDeadTablet(new Item.Properties(), 3));

    public static final RegistryObject<Item> COLLAPSE_TABLET = ITEMS.register("collapse_tablet",
            () -> new CollapseTablet(new Item.Properties(), 3));

    public static final RegistryObject<Item> FLIGHT_TABLET = ITEMS.register("flight_tablet",
            () -> new FlightTablet(new Item.Properties(), 4));

    public static final RegistryObject<Item> POLYMORPH_TABLET = ITEMS.register("polymorph_tablet",
            () -> new PolymorphTablet(new Item.Properties(), 4));

    public static final RegistryObject<Item> CHAOTIC_POLYMORPH_TABLET = ITEMS.register("chaotic_polymorph_tablet",
            () -> new ChaoticPolymorphTablet(new Item.Properties(), 5));

    public static final RegistryObject<Item> DEAFENING_BLAST_TABLET = ITEMS.register("deafening_blast_tablet",
            () -> new DeafeningBlastTablet(new Item.Properties(), 5));
}
