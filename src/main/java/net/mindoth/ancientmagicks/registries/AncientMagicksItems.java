package net.mindoth.ancientmagicks.registries;

import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.item.ColorRuneItem;
import net.mindoth.ancientmagicks.item.RuneItem;
import net.mindoth.ancientmagicks.item.castingitem.StaffItem;
import net.mindoth.ancientmagicks.item.castingitem.WandType;
import net.mindoth.ancientmagicks.item.spellrune.collapse.CollapseRune;
import net.mindoth.ancientmagicks.item.spellrune.dynamite.DynamiteRune;
import net.mindoth.ancientmagicks.item.spellrune.blink.BlinkRune;
import net.mindoth.ancientmagicks.item.spellrune.fireball.FireballRune;
import net.mindoth.ancientmagicks.item.spellrune.flight.FlightRune;
import net.mindoth.ancientmagicks.item.spellrune.raisedead.RaiseDeadRune;
import net.mindoth.ancientmagicks.item.spellrune.slimeball.SlimeballRune;
import net.mindoth.ancientmagicks.item.spellrune.telekineticgrab.TelekineticGrabRune;
import net.mindoth.ancientmagicks.item.spellrune.windburst.WindBurstRune;
import net.mindoth.ancientmagicks.item.spellrune.witchspark.WitchSparkRune;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class AncientMagicksItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, AncientMagicks.MOD_ID);

    //Casting items
    public static final RegistryObject<Item> BEGINNER_STAFF = ITEMS.register("beginner_staff",
            () -> new StaffItem(WandType.BEGINNER_STAFF));



    //Runes
    public static final RegistryObject<Item> EMPTY_RUNE = ITEMS.register("empty_rune",
            () -> new RuneItem(new Item.Properties(), 0));


    public static final RegistryObject<Item> BLUE_RUNE = ITEMS.register("blue_rune",
            () -> new ColorRuneItem(new Item.Properties(), 0));

    public static final RegistryObject<Item> PURPLE_RUNE = ITEMS.register("purple_rune",
            () -> new ColorRuneItem(new Item.Properties(), 0));

    public static final RegistryObject<Item> YELLOW_RUNE = ITEMS.register("yellow_rune",
            () -> new ColorRuneItem(new Item.Properties(), 0));

    public static final RegistryObject<Item> GREEN_RUNE = ITEMS.register("green_rune",
            () -> new ColorRuneItem(new Item.Properties(), 0));


    public static final RegistryObject<Item> WITCH_SPARK_RUNE = ITEMS.register("witch_spark_rune",
            () -> new WitchSparkRune(new Item.Properties(), 1));

    public static final RegistryObject<Item> FIREBALL_RUNE = ITEMS.register("fireball_rune",
            () -> new FireballRune(new Item.Properties(), 3));

    public static final RegistryObject<Item> TELEKINETIC_GRAB_RUNE = ITEMS.register("telekinetic_grab_rune",
            () -> new TelekineticGrabRune(new Item.Properties(), 2));

    public static final RegistryObject<Item> COLLAPSE_RUNE = ITEMS.register("collapse_rune",
            () -> new CollapseRune(new Item.Properties(), 4));

    public static final RegistryObject<Item> FLIGHT_RUNE = ITEMS.register("flight_rune",
            () -> new FlightRune(new Item.Properties(), 4));

    public static final RegistryObject<Item> DYNAMITE_RUNE = ITEMS.register("dynamite_rune",
            () -> new DynamiteRune(new Item.Properties(), 3));

    public static final RegistryObject<Item> RAISE_DEAD_RUNE = ITEMS.register("raise_dead_rune",
            () -> new RaiseDeadRune(new Item.Properties(), 4));

    public static final RegistryObject<Item> BLINK_RUNE = ITEMS.register("blink_rune",
            () -> new BlinkRune(new Item.Properties(), 3));

    public static final RegistryObject<Item> WIND_BURST_RUNE = ITEMS.register("wind_burst_rune",
            () -> new WindBurstRune(new Item.Properties(), 2));

    public static final RegistryObject<Item> SLIMEBALL_RUNE = ITEMS.register("slimeball_rune",
            () -> new SlimeballRune(new Item.Properties(), 1));
}
