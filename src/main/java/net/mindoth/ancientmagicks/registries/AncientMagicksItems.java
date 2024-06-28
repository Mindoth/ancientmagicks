package net.mindoth.ancientmagicks.registries;

import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.item.ColorRuneItem;
import net.mindoth.ancientmagicks.item.RuneItem;
import net.mindoth.ancientmagicks.item.castingitem.CastingItem;
import net.mindoth.ancientmagicks.item.castingitem.WandType;
import net.mindoth.ancientmagicks.item.spell.chaoticpolymorph.ChaoticPolymorphSpell;
import net.mindoth.ancientmagicks.item.spell.collapse.CollapseSpell;
import net.mindoth.ancientmagicks.item.spell.dynamite.DynamiteSpell;
import net.mindoth.ancientmagicks.item.spell.blink.BlinkSpell;
import net.mindoth.ancientmagicks.item.spell.fireball.FireballSpell;
import net.mindoth.ancientmagicks.item.spell.flight.FlightSpell;
import net.mindoth.ancientmagicks.item.spell.calllightning.CallLightningSpell;
import net.mindoth.ancientmagicks.item.spell.polymorph.PolymorphSpell;
import net.mindoth.ancientmagicks.item.spell.raisedead.RaiseDeadSpell;
import net.mindoth.ancientmagicks.item.spell.slimeball.SlimeballSpell;
import net.mindoth.ancientmagicks.item.spell.telekineticgrab.TelekineticGrabSpell;
import net.mindoth.ancientmagicks.item.spell.ward.WardSpell;
import net.mindoth.ancientmagicks.item.spell.windburst.WindBurstSpell;
import net.mindoth.ancientmagicks.item.spell.witchspark.WitchSparkSpell;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class AncientMagicksItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, AncientMagicks.MOD_ID);

    //Casting items
    public static final RegistryObject<Item> CASTING_ITEM = ITEMS.register("casting_item",
            () -> new CastingItem(WandType.CASTING_ITEM));



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

    public static final RegistryObject<Item> WHITE_RUNE = ITEMS.register("white_rune",
            () -> new ColorRuneItem(new Item.Properties(), 0));

    public static final RegistryObject<Item> BLACK_RUNE = ITEMS.register("black_rune",
            () -> new ColorRuneItem(new Item.Properties(), 0));


    public static final RegistryObject<Item> WITCH_SPARK_SPELL = ITEMS.register("witch_spark_spell",
            () -> new WitchSparkSpell(new Item.Properties(), 1));

    public static final RegistryObject<Item> FIREBALL_SPELL = ITEMS.register("fireball_spell",
            () -> new FireballSpell(new Item.Properties(), 3));

    public static final RegistryObject<Item> TELEKINETIC_GRAB_SPELL = ITEMS.register("telekinetic_grab_spell",
            () -> new TelekineticGrabSpell(new Item.Properties(), 2));

    public static final RegistryObject<Item> COLLAPSE_SPELL = ITEMS.register("collapse_spell",
            () -> new CollapseSpell(new Item.Properties(), 4));

    public static final RegistryObject<Item> FLIGHT_SPELL = ITEMS.register("flight_spell",
            () -> new FlightSpell(new Item.Properties(), 4));

    public static final RegistryObject<Item> DYNAMITE_SPELL = ITEMS.register("dynamite_spell",
            () -> new DynamiteSpell(new Item.Properties(), 3));

    public static final RegistryObject<Item> RAISE_DEAD_SPELL = ITEMS.register("raise_dead_spell",
            () -> new RaiseDeadSpell(new Item.Properties(), 4));

    public static final RegistryObject<Item> BLINK_SPELL = ITEMS.register("blink_spell",
            () -> new BlinkSpell(new Item.Properties(), 3));

    public static final RegistryObject<Item> WIND_BURST_SPELL = ITEMS.register("wind_burst_spell",
            () -> new WindBurstSpell(new Item.Properties(), 2));

    public static final RegistryObject<Item> SLIMEBALL_SPELL = ITEMS.register("slimeball_spell",
            () -> new SlimeballSpell(new Item.Properties(), 1));

    public static final RegistryObject<Item> WARD_SPELL = ITEMS.register("ward_spell",
            () -> new WardSpell(new Item.Properties(), 1));

    public static final RegistryObject<Item> CALL_LIGHTNING_SPELL = ITEMS.register("call_lightning_spell",
            () -> new CallLightningSpell(new Item.Properties(), 3));

    public static final RegistryObject<Item> POLYMORPH_SPELL = ITEMS.register("polymorph_spell",
            () -> new PolymorphSpell(new Item.Properties(), 4));

    public static final RegistryObject<Item> CHAOTIC_POLYMORPH_SPELL = ITEMS.register("chaotic_polymorph_spell",
            () -> new ChaoticPolymorphSpell(new Item.Properties(), 5));
}
