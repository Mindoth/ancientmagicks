package net.mindoth.ancientmagicks.registries;

import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.item.RuneItem;
import net.mindoth.ancientmagicks.item.modifierrune.*;
import net.mindoth.ancientmagicks.item.spellrune.blackhole.BlackHoleRune;
import net.mindoth.ancientmagicks.item.spellrune.collapse.CollapseRune;
import net.mindoth.ancientmagicks.item.spellrune.dynamite.DynamiteRune;
import net.mindoth.ancientmagicks.item.spellrune.enderbolt.EnderBoltRune;
import net.mindoth.ancientmagicks.item.spellrune.fireball.FireballRune;
import net.mindoth.ancientmagicks.item.spellrune.flight.FlightRune;
import net.mindoth.ancientmagicks.item.spellrune.raisedead.RaiseDeadRune;
import net.mindoth.ancientmagicks.item.spellrune.telekineticgrab.TelekineticGrabRune;
import net.mindoth.ancientmagicks.item.spellrune.witchspark.WitchSparkRune;
import net.mindoth.ancientmagicks.item.weapon.SpellTabletItem;
import net.mindoth.ancientmagicks.item.weapon.StaffItem;
import net.mindoth.ancientmagicks.item.weapon.WandItem;
import net.mindoth.ancientmagicks.item.weapon.WandType;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class AncientMagicksItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, AncientMagicks.MOD_ID);

    //Casting items
    public static final RegistryObject<Item> SPELL_TABLET = ITEMS.register("spell_tablet",
            () -> new SpellTabletItem(WandType.SPELL_TABLET, 60));


    public static final RegistryObject<Item> BEGINNER_WAND = ITEMS.register("beginner_wand",
            () -> new WandItem(WandType.BEGINNER_WAND, 0));

    public static final RegistryObject<Item> BEGINNER_STAFF = ITEMS.register("beginner_staff",
            () -> new StaffItem(WandType.BEGINNER_STAFF, 0));



    //Runes
    public static final RegistryObject<Item> EMPTY_RUNE = ITEMS.register("empty_rune",
            () -> new RuneItem(new Item.Properties(), 0));


    public static final RegistryObject<Item> WITCH_SPARK_RUNE = ITEMS.register("witch_spark_rune",
            () -> new WitchSparkRune(new Item.Properties(), 20));

    public static final RegistryObject<Item> FIREBALL_RUNE = ITEMS.register("fireball_rune",
            () -> new FireballRune(new Item.Properties(), 40));

    //TODO: Implement strength runes into this, maybe?
    public static final RegistryObject<Item> TELEKINETIC_GRAB_RUNE = ITEMS.register("telekinetic_grab_rune",
            () -> new TelekineticGrabRune(new Item.Properties(), 20));

    //TODO: Recolor the earth on the rune texture
    public static final RegistryObject<Item> COLLAPSE_RUNE = ITEMS.register("collapse_rune",
            () -> new CollapseRune(new Item.Properties(), 120));

    public static final RegistryObject<Item> FLIGHT_RUNE = ITEMS.register("flight_rune",
            () -> new FlightRune(new Item.Properties(), 120));

    //TODO: Implement blockPierce rune for DynamiteEntity to go through ground?
    public static final RegistryObject<Item> DYNAMITE_RUNE = ITEMS.register("dynamite_rune",
            () -> new DynamiteRune(new Item.Properties(), 80));

    //TODO: Re-balance health and damage added by runes
    public static final RegistryObject<Item> RAISE_DEAD_RUNE = ITEMS.register("raise_dead_rune",
            () -> new RaiseDeadRune(new Item.Properties(), 120));

    public static final RegistryObject<Item> ENDER_BOLT_RUNE = ITEMS.register("ender_bolt_rune",
            () -> new EnderBoltRune(new Item.Properties(), 60));

    public static final RegistryObject<Item> BLACK_HOLE_RUNE = ITEMS.register("black_hole_rune",
            () -> new BlackHoleRune(new Item.Properties(), 120));


    public static final RegistryObject<Item> AMPLIFY_RUNE = ITEMS.register("amplify_rune",
            () -> new AmplifyRune(new Item.Properties(), 5));

    public static final RegistryObject<Item> DAMPEN_RUNE = ITEMS.register("dampen_rune",
            () -> new DampenRune(new Item.Properties(), 0));

    public static final RegistryObject<Item> QUICKEN_RUNE = ITEMS.register("quicken_rune",
            () -> new QuickenRune(new Item.Properties(), 5));

    public static final RegistryObject<Item> SLACKEN_RUNE = ITEMS.register("slacken_rune",
            () -> new SlackenRune(new Item.Properties(), 0));

    public static final RegistryObject<Item> EXTEND_RUNE = ITEMS.register("extend_rune",
            () -> new ExtendRune(new Item.Properties(), 5));

    public static final RegistryObject<Item> DIMINISH_RUNE = ITEMS.register("diminish_rune",
            () -> new DiminishRune(new Item.Properties(), -5));

    public static final RegistryObject<Item> REACH_RUNE = ITEMS.register("reach_rune",
            () -> new ReachRune(new Item.Properties(), 5));

    public static final RegistryObject<Item> ABRIDGE_RUNE = ITEMS.register("abridge_rune",
            () -> new AbridgeRune(new Item.Properties(), 0));

    public static final RegistryObject<Item> EXPAND_RUNE = ITEMS.register("expand_rune",
            () -> new ExpandRune(new Item.Properties(), 5));

    public static final RegistryObject<Item> COMPACT_RUNE = ITEMS.register("compact_rune",
            () -> new CompactRune(new Item.Properties(), 0));

    public static final RegistryObject<Item> ENEMY_PIERCE_RUNE = ITEMS.register("enemy_pierce_rune",
            () -> new EnemyPierceRune(new Item.Properties(), 10));

    public static final RegistryObject<Item> BLOCK_PIERCE_RUNE = ITEMS.register("block_pierce_rune",
            () -> new BlockPierceRune(new Item.Properties(), 10));

    public static final RegistryObject<Item> HOMING_RUNE = ITEMS.register("homing_rune",
            () -> new HomingRune(new Item.Properties(), 20));
}
