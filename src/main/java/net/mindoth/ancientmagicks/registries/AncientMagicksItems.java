package net.mindoth.ancientmagicks.registries;

import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.item.ColorRuneItem;
import net.mindoth.ancientmagicks.item.RuneItem;
import net.mindoth.ancientmagicks.item.TabletBag;
import net.mindoth.ancientmagicks.item.castingitem.AMBagType;
import net.mindoth.ancientmagicks.item.castingitem.InvocationStaffItem;
import net.mindoth.ancientmagicks.item.spell.alacrity.AlacrityTablet;
import net.mindoth.ancientmagicks.item.spell.blind.BlindTablet;
import net.mindoth.ancientmagicks.item.spell.blink.BlinkTablet;
import net.mindoth.ancientmagicks.item.spell.calllightning.CallLightningTablet;
import net.mindoth.ancientmagicks.item.spell.chaoticpolymorph.ChaoticPolymorphTablet;
import net.mindoth.ancientmagicks.item.spell.collapse.CollapseTablet;
import net.mindoth.ancientmagicks.item.spell.deafeningblast.DeafeningBlastTablet;
import net.mindoth.ancientmagicks.item.spell.dynamite.DynamiteTablet;
import net.mindoth.ancientmagicks.item.spell.experiencestream.ExperienceStreamTablet;
import net.mindoth.ancientmagicks.item.spell.extinguish.ExtinguishTablet;
import net.mindoth.ancientmagicks.item.spell.featherfall.FeatherFallTablet;
import net.mindoth.ancientmagicks.item.spell.fireball.FireballTablet;
import net.mindoth.ancientmagicks.item.spell.flight.FlightTablet;
import net.mindoth.ancientmagicks.item.spell.ghostwalk.GhostwalkTablet;
import net.mindoth.ancientmagicks.item.spell.icewall.IceWallTablet;
import net.mindoth.ancientmagicks.item.spell.numbpain.NumbPainTablet;
import net.mindoth.ancientmagicks.item.spell.polymorph.PolymorphTablet;
import net.mindoth.ancientmagicks.item.spell.slimeball.SlimeballTablet;
import net.mindoth.ancientmagicks.item.spell.spook.SpookTablet;
import net.mindoth.ancientmagicks.item.spell.summonskeleton.SummonSkeletonTablet;
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

    //Casting Items
    public static final RegistryObject<Item> INVOCATION_STAFF = ITEMS.register("invocation_staff",
            () -> new InvocationStaffItem(new Item.Properties()));

    //Tablet Bag
    public static final RegistryObject<Item> TABLET_BAG = ITEMS.register("tablet_bag",
            () -> new TabletBag(AMBagType.TABLET_BAG));

    //Stone Slate
    public static final RegistryObject<Item> STONE_SLATE = ITEMS.register("empty_rune",
            () -> new RuneItem(new Item.Properties()));

    //Ancient Tablet
    public static final RegistryObject<Item> ANCIENT_TABLET = ITEMS.register("ancient_tablet",
            () -> new Item(new Item.Properties()));


    //Runes
    public static final RegistryObject<Item> BLUE_RUNE = ITEMS.register("blue_rune",
            () -> new ColorRuneItem(new Item.Properties(), "\u00A7b"));

    public static final RegistryObject<Item> PURPLE_RUNE = ITEMS.register("purple_rune",
            () -> new ColorRuneItem(new Item.Properties(), "\u00A7d"));

    public static final RegistryObject<Item> YELLOW_RUNE = ITEMS.register("yellow_rune",
            () -> new ColorRuneItem(new Item.Properties(), "\u00A7e"));

    public static final RegistryObject<Item> GREEN_RUNE = ITEMS.register("green_rune",
            () -> new ColorRuneItem(new Item.Properties(), "\u00A7a"));

    public static final RegistryObject<Item> BLACK_RUNE = ITEMS.register("black_rune",
            () -> new ColorRuneItem(new Item.Properties(), "\u00A78"));

    public static final RegistryObject<Item> WHITE_RUNE = ITEMS.register("white_rune",
            () -> new ColorRuneItem(new Item.Properties(), "\u00A7f"));


    public static final RegistryObject<Item> WITCH_SPARK_TABLET = ITEMS.register("witch_spark_tablet",
            () -> new WitchSparkTablet(new Item.Properties(), 1, false, 20));

    public static final RegistryObject<Item> SLIMEBALL_TABLET = ITEMS.register("slimeball_tablet",
            () -> new SlimeballTablet(new Item.Properties(), 1, false, 20));

    public static final RegistryObject<Item> WARD_TABLET = ITEMS.register("ward_tablet",
            () -> new WardTablet(new Item.Properties(), 1, false, 200));

    public static final RegistryObject<Item> FEATHER_FALL_TABLET = ITEMS.register("feather_fall_tablet",
            () -> new FeatherFallTablet(new Item.Properties(), 1, false, 200));

    public static final RegistryObject<Item> TELEKINETIC_GRAB_TABLET = ITEMS.register("telekinetic_grab_tablet",
            () -> new TelekineticGrabTablet(new Item.Properties(), 1, false, 20));

    public static final RegistryObject<Item> EXTINGUISH_TABLET = ITEMS.register("extinguish_tablet",
            () -> new ExtinguishTablet(new Item.Properties(), 1, false, 100));

    public static final RegistryObject<Item> SPOOK_TABLET = ITEMS.register("spook_tablet",
            () -> new SpookTablet(new Item.Properties(), 1, false, 1200));

    public static final RegistryObject<Item> BLIND_TABLET = ITEMS.register("blind_tablet",
            () -> new BlindTablet(new Item.Properties(), 1, false, 400));

    public static final RegistryObject<Item> WIND_BURST_TABLET = ITEMS.register("wind_burst_tablet",
            () -> new WindBurstTablet(new Item.Properties(), 2, false, 40));

    public static final RegistryObject<Item> DYNAMITE_TABLET = ITEMS.register("dynamite_tablet",
            () -> new DynamiteTablet(new Item.Properties(), 2, false, 80));

    public static final RegistryObject<Item> BLINK_TABLET = ITEMS.register("blink_tablet",
            () -> new BlinkTablet(new Item.Properties(), 2, false, 40));

    public static final RegistryObject<Item> FIREBALL_TABLET = ITEMS.register("fireball_tablet",
            () -> new FireballTablet(new Item.Properties(), 3, false, 60));

    public static final RegistryObject<Item> CALL_LIGHTNING_TABLET = ITEMS.register("call_lightning_tablet",
            () -> new CallLightningTablet(new Item.Properties(), 3, false, 60));

    public static final RegistryObject<Item> NUMB_PAIN_TABLET = ITEMS.register("numb_pain_tablet",
            () -> new NumbPainTablet(new Item.Properties(), 3, false, 400));

    public static final RegistryObject<Item> SUMMON_SKELETON_TABLET = ITEMS.register("summon_skeleton_tablet",
            () -> new SummonSkeletonTablet(new Item.Properties(), 3, false, 200));

    public static final RegistryObject<Item> COLLAPSE_TABLET = ITEMS.register("collapse_tablet",
            () -> new CollapseTablet(new Item.Properties(), 3, false, 60));

    public static final RegistryObject<Item> ALACRITY_TABLET = ITEMS.register("alacrity_tablet",
            () -> new AlacrityTablet(new Item.Properties(), 3, false, 1200));

    public static final RegistryObject<Item> FLIGHT_TABLET = ITEMS.register("flight_tablet",
            () -> new FlightTablet(new Item.Properties(), 4, false, 1200));

    public static final RegistryObject<Item> POLYMORPH_TABLET = ITEMS.register("polymorph_tablet",
            () -> new PolymorphTablet(new Item.Properties(), 4, false, 200));

    public static final RegistryObject<Item> GHOSTWALK_TABLET = ITEMS.register("ghostwalk_tablet",
            () -> new GhostwalkTablet(new Item.Properties(), 4, false, 1199));

    public static final RegistryObject<Item> ICE_WALL_TABLET = ITEMS.register("ice_wall_tablet",
            () -> new IceWallTablet(new Item.Properties(), 4, true, 80));

    public static final RegistryObject<Item> CHAOTIC_POLYMORPH_TABLET = ITEMS.register("chaotic_polymorph_tablet",
            () -> new ChaoticPolymorphTablet(new Item.Properties(), 5, false, 40));

    public static final RegistryObject<Item> DEAFENING_BLAST_TABLET = ITEMS.register("deafening_blast_tablet",
            () -> new DeafeningBlastTablet(new Item.Properties(), 5, false, 120));

    public static final RegistryObject<Item> EXPERIENCE_STREAM_TABLET = ITEMS.register("experience_stream_tablet",
            () -> new ExperienceStreamTablet(new Item.Properties(), 5, true, 20));
}
