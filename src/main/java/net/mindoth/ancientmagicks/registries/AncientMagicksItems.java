package net.mindoth.ancientmagicks.registries;

import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.item.AncientTabletItem;
import net.mindoth.ancientmagicks.item.ColorRuneItem;
import net.mindoth.ancientmagicks.item.armor.AncientMagicsArmorMaterials;
import net.mindoth.ancientmagicks.item.armor.ColorableMagicArmorItem;
import net.mindoth.ancientmagicks.item.castingitem.ColorableStaffItem;
import net.mindoth.ancientmagicks.item.castingitem.SpellStorageItem;
import net.mindoth.ancientmagicks.item.spell.acidarrow.AcidArrowItem;
import net.mindoth.ancientmagicks.item.spell.alacrity.AlacrityItem;
import net.mindoth.ancientmagicks.item.spell.blind.BlindItem;
import net.mindoth.ancientmagicks.item.spell.blizzard.BlizzardItem;
import net.mindoth.ancientmagicks.item.spell.burnlance.BurnLanceItem;
import net.mindoth.ancientmagicks.item.spell.callthunder.CallThunderItem;
import net.mindoth.ancientmagicks.item.spell.chaoticpolymorph.ChaoticPolymorphItem;
import net.mindoth.ancientmagicks.item.spell.controlweather.ControlWeatherItem;
import net.mindoth.ancientmagicks.item.spell.darkvision.DarkvisionItem;
import net.mindoth.ancientmagicks.item.spell.dragonbreath.DragonBreathItem;
import net.mindoth.ancientmagicks.item.spell.endlessbreath.EndlessBreathItem;
import net.mindoth.ancientmagicks.item.spell.extinguish.ExtinguishItem;
import net.mindoth.ancientmagicks.item.spell.featherfall.FeatherFallItem;
import net.mindoth.ancientmagicks.item.spell.fireball.FireballItem;
import net.mindoth.ancientmagicks.item.spell.firebolt.FireBoltItem;
import net.mindoth.ancientmagicks.item.spell.firebreath.FireBreathItem;
import net.mindoth.ancientmagicks.item.spell.fly.FlyItem;
import net.mindoth.ancientmagicks.item.spell.freezelance.FreezeLanceItem;
import net.mindoth.ancientmagicks.item.spell.ghostwalk.GhostwalkItem;
import net.mindoth.ancientmagicks.item.spell.greaterinvisibility.GreaterInvisibilityItem;
import net.mindoth.ancientmagicks.item.spell.icewall.IceWallItem;
import net.mindoth.ancientmagicks.item.spell.icicle.IcicleItem;
import net.mindoth.ancientmagicks.item.spell.invisibility.InvisibilityItem;
import net.mindoth.ancientmagicks.item.spell.mindcontrol.MindControlItem;
import net.mindoth.ancientmagicks.item.spell.numbpain.NumbPainItem;
import net.mindoth.ancientmagicks.item.spell.polymorph.PolymorphItem;
import net.mindoth.ancientmagicks.item.spell.sleep.SleepItem;
import net.mindoth.ancientmagicks.item.spell.slimeball.SlimeballItem;
import net.mindoth.ancientmagicks.item.spell.spook.SpookItem;
import net.mindoth.ancientmagicks.item.spell.summonbees.SummonBeesItem;
import net.mindoth.ancientmagicks.item.spell.summondeathknight.SummonDeathKnightItem;
import net.mindoth.ancientmagicks.item.spell.summonzombie.SummonZombieItem;
import net.mindoth.ancientmagicks.item.spell.teleblock.TeleblockItem;
import net.mindoth.ancientmagicks.item.spell.telekineticgrab.TelekineticGrabItem;
import net.mindoth.ancientmagicks.item.spell.teleport.TeleportItem;
import net.mindoth.ancientmagicks.item.spell.thunderball.ThunderballItem;
import net.mindoth.ancientmagicks.item.spell.verminbane.VerminBaneItem;
import net.mindoth.ancientmagicks.item.spell.windburst.WindBurstItem;
import net.mindoth.ancientmagicks.item.spell.witcharmor.WitchArmorItem;
import net.mindoth.ancientmagicks.item.spell.witcharrow.WitchArrowItem;
import net.mindoth.ancientmagicks.registries.attribute.AncientMagicksAttributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Map;
import java.util.UUID;

public class AncientMagicksItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, AncientMagicks.MOD_ID);

    public static final RegistryObject<Item> STONE_SLATE = ITEMS.register("empty_rune",
            () -> new Item(new Item.Properties()));

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

    public static final RegistryObject<Item> SPELL_SCROLL = ITEMS.register("spell_scroll",
            () -> new SpellStorageItem(new Item.Properties()));

    public static final RegistryObject<Item> SPELL_PEARL = ITEMS.register("spell_pearl",
            () -> new SpellStorageItem(new Item.Properties()));

    public static final RegistryObject<Item> WIZARD_STAFF = ITEMS.register("wizard_staff",
            () -> new ColorableStaffItem(new Item.Properties().stacksTo(1), 0.0D, -3.0D,
            Map.of(AncientMagicksAttributes.MANA_REGENERATION.get(), new AttributeModifier(UUID.fromString("1e043d3d-df87-4519-bd13-7c71552bba2b"),
                    "Weapon modifier", 0.25D, AttributeModifier.Operation.MULTIPLY_BASE),
                    AncientMagicksAttributes.SPELL_POWER.get(), new AttributeModifier(UUID.fromString("1e043d3d-df87-4519-bd13-7c71552bba2b"),
                    "Weapon modifier", 0.10D, AttributeModifier.Operation.MULTIPLY_BASE))));

    public static final RegistryObject<Item> HOOD = ITEMS.register("hood",
            () -> new ColorableMagicArmorItem(AncientMagicsArmorMaterials.CLOTH, ArmorItem.Type.HELMET, new Item.Properties()));

    public static final RegistryObject<Item> CLOAK = ITEMS.register("cloak",
            () -> new ColorableMagicArmorItem(AncientMagicsArmorMaterials.CLOTH, ArmorItem.Type.CHESTPLATE, new Item.Properties()));

    public static final RegistryObject<Item> ARCANE_DUST = ITEMS.register("arcane_dust",
            () -> new Item(new Item.Properties().fireResistant()));

    public static final RegistryObject<Item> ANCIENT_TABLET = ITEMS.register("ancient_tablet",
            () -> new AncientTabletItem(new Item.Properties()));

    //Magic Spells
    public static final RegistryObject<Item> FEATHER_FALL_ITEM = ITEMS.register("feather_fall",
            () -> new FeatherFallItem(new Item.Properties(), 1, 20, 10));

    public static final RegistryObject<Item> TELEKINETIC_GRAB_ITEM = ITEMS.register("telekinetic_grab",
            () -> new TelekineticGrabItem(new Item.Properties(), 1, 4, 1));

    public static final RegistryObject<Item> EXTINGUISH_ITEM = ITEMS.register("extinguish",
            () -> new ExtinguishItem(new Item.Properties(), 1, 4, 2));

    public static final RegistryObject<Item> SPOOK_ITEM = ITEMS.register("spook",
            () -> new SpookItem(new Item.Properties(), 1, 20, 10));

    public static final RegistryObject<Item> WITCH_ARROW_ITEM = ITEMS.register("witch_arrow",
            () -> new WitchArrowItem(new Item.Properties(), 1, 10, 2));

    public static final RegistryObject<Item> WITCH_ARMOR_ITEM = ITEMS.register("witch_armor",
            () -> new WitchArmorItem(new Item.Properties(), 1, 100, 10));

    public static final RegistryObject<Item> SLEEP_ITEM = ITEMS.register("sleep",
            () -> new SleepItem(new Item.Properties(), 1, 30, 10));

    public static final RegistryObject<Item> NUMB_PAIN_ITEM = ITEMS.register("numb_pain",
            () -> new NumbPainItem(new Item.Properties(), 1, 20, 10));

    public static final RegistryObject<Item> ICICLE_ITEM = ITEMS.register("icicle",
            () -> new IcicleItem(new Item.Properties(), 1, 6, 1));

    public static final RegistryObject<Item> FIRE_BOLT_ITEM = ITEMS.register("fire_bolt",
            () -> new FireBoltItem(new Item.Properties(), 1, 6, 1));

    public static final RegistryObject<Item> ENDLESS_BREATH_ITEM = ITEMS.register("endless_breath",
            () -> new EndlessBreathItem(new Item.Properties(), 2, 4, 2));

    public static final RegistryObject<Item> WIND_BURST_ITEM = ITEMS.register("wind_burst",
            () -> new WindBurstItem(new Item.Properties(), 2, 30, 3));

    public static final RegistryObject<Item> BLIND_ITEM = ITEMS.register("blind",
            () -> new BlindItem(new Item.Properties(), 2, 50, 10));

    public static final RegistryObject<Item> DARKVISION_ITEM = ITEMS.register("darkvision",
            () -> new DarkvisionItem(new Item.Properties(), 2, 40, 10));

    public static final RegistryObject<Item> SLIMEBALL_ITEM = ITEMS.register("slimeball",
            () -> new SlimeballItem(new Item.Properties(), 2, 10, 2));

    public static final RegistryObject<Item> INVISIBILITY_ITEM = ITEMS.register("invisibility",
            () -> new InvisibilityItem(new Item.Properties(), 2, 40, 10));

    public static final RegistryObject<Item> SUMMON_BEES_ITEM = ITEMS.register("summon_bees",
            () -> new SummonBeesItem(new Item.Properties(), 2, 40, 10));

    public static final RegistryObject<Item> ACID_ARROW_ITEM = ITEMS.register("acid_arrow",
            () -> new AcidArrowItem(new Item.Properties(), 2, 10, 2));

    public static final RegistryObject<Item> ALACRITY_ITEM = ITEMS.register("alacrity",
            () -> new AlacrityItem(new Item.Properties(), 3, 60, 10));

    public static final RegistryObject<Item> FIREBALL_ITEM = ITEMS.register("fireball",
            () -> new FireballItem(new Item.Properties(), 3, 70, 4));

    public static final RegistryObject<Item> CALL_THUNDER_ITEM = ITEMS.register("call_thunder",
            () -> new CallThunderItem(new Item.Properties(), 3, 100, 4));

    public static final RegistryObject<Item> SUMMON_ZOMBIE_ITEM = ITEMS.register("summon_zombie",
            () -> new SummonZombieItem(new Item.Properties(), 3, 60, 10));

    public static final RegistryObject<Item> FLY_ITEM = ITEMS.register("fly",
            () -> new FlyItem(new Item.Properties(), 3, 60, 10));

    public static final RegistryObject<Item> THUNDERBALL_ITEM = ITEMS.register("thunderball",
            () -> new ThunderballItem(new Item.Properties(), 3, 70, 4));

    public static final RegistryObject<Item> BLIZZARD_ITEM = ITEMS.register("blizzard",
            () -> new BlizzardItem(new Item.Properties(), 4, 8, 2));

    public static final RegistryObject<Item> POLYMORPH_ITEM = ITEMS.register("polymorph",
            () -> new PolymorphItem(new Item.Properties(), 4, 160, 10));

    public static final RegistryObject<Item> FREEZE_LANCE_ITEM = ITEMS.register("freeze_lance",
            () -> new FreezeLanceItem(new Item.Properties(), 4, 80, 3));

    public static final RegistryObject<Item> BURN_LANCE_ITEM = ITEMS.register("burn_lance",
            () -> new BurnLanceItem(new Item.Properties(), 4, 80, 3));

    public static final RegistryObject<Item> FIRE_BREATH_ITEM = ITEMS.register("fire_breath",
            () -> new FireBreathItem(new Item.Properties(), 4, 8, 2));

    public static final RegistryObject<Item> GREATER_INVISIBILITY_ITEM = ITEMS.register("greater_invisibility",
            () -> new GreaterInvisibilityItem(new Item.Properties(), 4, 80, 10));

    public static final RegistryObject<Item> TELEPORT_ITEM = ITEMS.register("teleport",
            () -> new TeleportItem(new Item.Properties(), 5, 100, 3));

    public static final RegistryObject<Item> TELEBLOCK_ITEM = ITEMS.register("teleblock",
            () -> new TeleblockItem(new Item.Properties(), 5, 110, 10));

    public static final RegistryObject<Item> CHAOTIC_POLYMORPH_ITEM = ITEMS.register("chaotic_polymorph",
            () -> new ChaoticPolymorphItem(new Item.Properties(), 5, 200, 10));

    public static final RegistryObject<Item> MIND_CONTROL_ITEM = ITEMS.register("mind_control",
            () -> new MindControlItem(new Item.Properties(), 5, 200, 10));

    public static final RegistryObject<Item> SUMMON_DEATH_KNIGHT_ITEM = ITEMS.register("summon_death_knight",
            () -> new SummonDeathKnightItem(new Item.Properties(), 5, 100, 10));

    public static final RegistryObject<Item> VERMIN_BANE_ITEM = ITEMS.register("vermin_bane",
            () -> new VerminBaneItem(new Item.Properties(), 5, 10, 2));

    public static final RegistryObject<Item> GHOSTWALK_ITEM = ITEMS.register("ghostwalk",
            () -> new GhostwalkItem(new Item.Properties(), 6, 120, 10));

    public static final RegistryObject<Item> DRAGON_BREATH_ITEM = ITEMS.register("dragon_breath",
            () -> new DragonBreathItem(new Item.Properties(), 6, 12, 2));

    public static final RegistryObject<Item> ICE_WALL_ITEM = ITEMS.register("ice_wall",
            () -> new IceWallItem(new Item.Properties(), 6, 12, 2));

    public static final RegistryObject<Item> CONTROL_WEATHER_ITEM = ITEMS.register("control_weather",
            () -> new ControlWeatherItem(new Item.Properties(), 6, 200, 10));
}
