package net.mindoth.ancientmagicks.registries;

import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.client.gui.inventory.AMBagType;
import net.mindoth.ancientmagicks.item.AncientTabletItem;
import net.mindoth.ancientmagicks.item.ColorRuneItem;
import net.mindoth.ancientmagicks.item.RuneItem;
import net.mindoth.ancientmagicks.item.TabletBag;
import net.mindoth.ancientmagicks.item.castingitem.InvocationStaffItem;
import net.mindoth.ancientmagicks.item.castingitem.SpellPearlItem;
import net.mindoth.ancientmagicks.item.spell.alacrity.AlacrityItem;
import net.mindoth.ancientmagicks.item.spell.blink.BlinkItem;
import net.mindoth.ancientmagicks.item.spell.blizzard.BlizzardItem;
import net.mindoth.ancientmagicks.item.spell.calllightning.CallLightningItem;
import net.mindoth.ancientmagicks.item.spell.chaoticpolymorph.ChaoticPolymorphItem;
import net.mindoth.ancientmagicks.item.spell.dragonbreath.DragonBreathItem;
import net.mindoth.ancientmagicks.item.spell.endlessbreath.EndlessBreathItem;
import net.mindoth.ancientmagicks.item.spell.extinguish.ExtinguishItem;
import net.mindoth.ancientmagicks.item.spell.featherfall.FeatherFallItem;
import net.mindoth.ancientmagicks.item.spell.fireball.FireballItem;
import net.mindoth.ancientmagicks.item.spell.flight.FlightItem;
import net.mindoth.ancientmagicks.item.spell.ghostwalk.GhostwalkItem;
import net.mindoth.ancientmagicks.item.spell.icewall.IceWallItem;
import net.mindoth.ancientmagicks.item.spell.magearmor.MageArmorItem;
import net.mindoth.ancientmagicks.item.spell.mindcontrol.MindControlItem;
import net.mindoth.ancientmagicks.item.spell.numbpain.NumbPainItem;
import net.mindoth.ancientmagicks.item.spell.polymorph.PolymorphItem;
import net.mindoth.ancientmagicks.item.spell.sleep.SleepItem;
import net.mindoth.ancientmagicks.item.spell.spook.SpookItem;
import net.mindoth.ancientmagicks.item.spell.summonzombie.SummonZombieItem;
import net.mindoth.ancientmagicks.item.spell.telekineticgrab.TelekineticGrabItem;
import net.mindoth.ancientmagicks.item.spell.windburst.WindBurstItem;
import net.mindoth.ancientmagicks.item.spell.witchspark.WitchSparkItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class AncientMagicksItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, AncientMagicks.MOD_ID);

    //Casting Items
    public static final RegistryObject<Item> INVOCATION_STAFF = ITEMS.register("invocation_staff",
            () -> new InvocationStaffItem(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> SPELL_PEARL = ITEMS.register("spell_pearl",
            () -> new SpellPearlItem(new Item.Properties()));

    //Tablet Bag
    public static final RegistryObject<Item> TABLET_BAG = ITEMS.register("tablet_bag",
            () -> new TabletBag(AMBagType.TABLET_BAG));

    //Spell Items
    public static final RegistryObject<Item> ANCIENT_TABLET = ITEMS.register("ancient_tablet",
            () -> new AncientTabletItem(new Item.Properties()));

    public static final RegistryObject<Item> SPELL_FRAGMENT = ITEMS.register("spell_fragment",
            () -> new Item(new Item.Properties().fireResistant()));


    //Color Runes
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

    /*public static final RegistryObject<Item> BROWN_RUNE = ITEMS.register("brown_rune",
            () -> new ColorRuneItem(new Item.Properties(), "\u00A76"));

    public static final RegistryObject<Item> RED_RUNE = ITEMS.register("red_rune",
            () -> new ColorRuneItem(new Item.Properties(), "\u00A7c"));*/

    //Empty Rune
    public static final RegistryObject<Item> STONE_SLATE = ITEMS.register("empty_rune",
            () -> new RuneItem(new Item.Properties()));


    //Folk Spells
    public static final RegistryObject<Item> ENDLESS_BREATH_ITEM = ITEMS.register("endless_breath",
            () -> new EndlessBreathItem(new Item.Properties()));

    public static final RegistryObject<Item> FEATHER_FALL_ITEM = ITEMS.register("feather_fall",
            () -> new FeatherFallItem(new Item.Properties()));

    public static final RegistryObject<Item> TELEKINETIC_GRAB_ITEM = ITEMS.register("telekinetic_grab",
            () -> new TelekineticGrabItem(new Item.Properties()));

    public static final RegistryObject<Item> EXTINGUISH_ITEM = ITEMS.register("extinguish",
            () -> new ExtinguishItem(new Item.Properties()));

    public static final RegistryObject<Item> SPOOK_ITEM = ITEMS.register("spook",
            () -> new SpookItem(new Item.Properties()));

    //Combat Spells
    public static final RegistryObject<Item> WITCH_SPARK_ITEM = ITEMS.register("witch_spark",
            () -> new WitchSparkItem(new Item.Properties()));

    public static final RegistryObject<Item> MAGE_ARMOR_ITEM = ITEMS.register("mage_armor",
            () -> new MageArmorItem(new Item.Properties()));

    public static final RegistryObject<Item> WIND_BURST_ITEM = ITEMS.register("wind_burst",
            () -> new WindBurstItem(new Item.Properties()));

    public static final RegistryObject<Item> FIREBALL_ITEM = ITEMS.register("fireball",
            () -> new FireballItem(new Item.Properties()));

    public static final RegistryObject<Item> CALL_LIGHTNING_ITEM = ITEMS.register("call_lightning",
            () -> new CallLightningItem(new Item.Properties()));

    public static final RegistryObject<Item> ALACRITY_ITEM = ITEMS.register("alacrity",
            () -> new AlacrityItem(new Item.Properties()));

    public static final RegistryObject<Item> GHOSTWALK_ITEM = ITEMS.register("ghostwalk",
            () -> new GhostwalkItem(new Item.Properties()));

    public static final RegistryObject<Item> ICE_WALL_ITEM = ITEMS.register("ice_wall",
            () -> new IceWallItem(new Item.Properties()));

    public static final RegistryObject<Item> BLIZZARD_ITEM = ITEMS.register("blizzard",
            () -> new BlizzardItem(new Item.Properties()));

    public static final RegistryObject<Item> SLEEP_ITEM = ITEMS.register("sleep",
            () -> new SleepItem(new Item.Properties()));

    public static final RegistryObject<Item> NUMB_PAIN_ITEM = ITEMS.register("numb_pain",
            () -> new NumbPainItem(new Item.Properties()));

    //Ancient Spells
    public static final RegistryObject<Item> BLINK_ITEM = ITEMS.register("blink",
            () -> new BlinkItem(new Item.Properties()));

    public static final RegistryObject<Item> SUMMON_ZOMBIE_ITEM = ITEMS.register("summon_zombie",
            () -> new SummonZombieItem(new Item.Properties()));

    public static final RegistryObject<Item> FLIGHT_ITEM = ITEMS.register("flight",
            () -> new FlightItem(new Item.Properties()));

    public static final RegistryObject<Item> POLYMORPH_ITEM = ITEMS.register("polymorph",
            () -> new PolymorphItem(new Item.Properties()));

    public static final RegistryObject<Item> CHAOTIC_POLYMORPH_ITEM = ITEMS.register("chaotic_polymorph",
            () -> new ChaoticPolymorphItem(new Item.Properties()));

    public static final RegistryObject<Item> MIND_CONTROL_ITEM = ITEMS.register("mind_control",
            () -> new MindControlItem(new Item.Properties()));

    public static final RegistryObject<Item> DRAGON_BREATH_ITEM = ITEMS.register("dragon_breath",
            () -> new DragonBreathItem(new Item.Properties()));


    /*public static final RegistryObject<Item> COLLAPSE_TABLET = ITEMS.register("collapse",
            () -> new CollapseTablet(new Item.Properties(), false, 60, true));

    public static final RegistryObject<Item> SUMMON_SKELETON_TABLET = ITEMS.register("summon_skeleton",
            () -> new SummonSkeletonTablet(new Item.Properties(), false, 600, true));

    public static final RegistryObject<Item> BLIND_TABLET = ITEMS.register("blind",
            () -> new BlindTablet(new Item.Properties(), false, 400, true));

    public static final RegistryObject<Item> SLIMEBALL_TABLET = ITEMS.register("slimeball",
            () -> new SlimeballTablet(new Item.Properties(), false, 20, true));

    public static final RegistryObject<Item> DYNAMITE_TABLET = ITEMS.register("dynamite",
            () -> new DynamiteTablet(new Item.Properties(), false, 80, true));

    public static final RegistryObject<Item> DEAFENING_BLAST_TABLET = ITEMS.register("deafening_blast",
            () -> new DeafeningBlastTablet(new Item.Properties(), false, 120, true));

    public static final RegistryObject<Item> EXPERIENCE_STREAM_TABLET = ITEMS.register("experience_stream",
            () -> new ExperienceStreamTablet(new Item.Properties(), true, 20, true));

    public static final RegistryObject<Item> ICICLE_TABLET = ITEMS.register("icicle",
            () -> new IcicleTablet(new Item.Properties(), false, 20, true));*/
}
