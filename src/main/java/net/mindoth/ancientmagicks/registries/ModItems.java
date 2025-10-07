package net.mindoth.ancientmagicks.registries;

import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.item.AncientTabletItem;
import net.mindoth.ancientmagicks.item.ColorRuneItem;
import net.mindoth.ancientmagicks.item.ParchmentItem;
import net.mindoth.ancientmagicks.item.SpellBookItem;
import net.mindoth.ancientmagicks.item.armor.ColorableMagickArmorItem;
import net.mindoth.ancientmagicks.item.armor.ModArmorMaterials;
import net.mindoth.ancientmagicks.item.castingitem.ColorableStaffItem;
import net.mindoth.ancientmagicks.item.castingitem.StaffItem;
import net.mindoth.ancientmagicks.item.rune.*;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Map;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, AncientMagicks.MOD_ID);

    //Materials
    public static final RegistryObject<Item> ANCIENT_TABLET = ITEMS.register("ancient_tablet",
            () -> new AncientTabletItem(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> WOOL_CLOTH = ITEMS.register("wool_cloth",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> ARCANE_DUST = ITEMS.register("arcane_dust",
            () -> new Item(new Item.Properties()));


    //Equipment
    public static final RegistryObject<Item> WOODEN_STAFF = ITEMS.register("wooden_staff",
            () -> new ColorableStaffItem(new Item.Properties().durability(100), Items.STICK, Map.of(
            )));

    public static final RegistryObject<Item> GOLDEN_STAFF = ITEMS.register("golden_staff",
            () -> new ColorableStaffItem(new Item.Properties().durability(216), Items.GOLD_INGOT,
                    Map.of(
                    )));

    public static final RegistryObject<Item> NETHERITE_STAFF = ITEMS.register("netherite_staff",
            () -> new StaffItem(new Item.Properties().durability(1296), Items.NETHERITE_INGOT,
                    Map.of(
                    )));

    public static final RegistryObject<Item> SPELL_BOOK = ITEMS.register("spell_book",
            () -> new SpellBookItem(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> ROBE_HOOD = ITEMS.register("robe_hood",
            () -> new ColorableMagickArmorItem(ModArmorMaterials.ROBE, ArmorItem.Type.HELMET, new Item.Properties()));

    public static final RegistryObject<Item> ROBE_TOP = ITEMS.register("robe_top",
            () -> new ColorableMagickArmorItem(ModArmorMaterials.ROBE, ArmorItem.Type.CHESTPLATE, new Item.Properties()));

    public static final RegistryObject<Item> ROBE_BOTTOM = ITEMS.register("robe_bottom",
            () -> new ColorableMagickArmorItem(ModArmorMaterials.ROBE, ArmorItem.Type.LEGGINGS, new Item.Properties()));

    public static final RegistryObject<Item> ROBE_BOOTS = ITEMS.register("robe_boots",
            () -> new ColorableMagickArmorItem(ModArmorMaterials.ROBE, ArmorItem.Type.BOOTS, new Item.Properties()));


    //Spell Parchments
    public static final RegistryObject<Item> PARCHMENT = ITEMS.register("parchment",
            () -> new ParchmentItem(new Item.Properties(), 9));

    public static final RegistryObject<Item> INFERNAL_PARCHMENT = ITEMS.register("infernal_parchment",
            () -> new ParchmentItem(new Item.Properties(), 18));

    public static final RegistryObject<Item> ARCANE_PARCHMENT = ITEMS.register("arcane_parchment",
            () -> new ParchmentItem(new Item.Properties(), 27));


    //REVAMPED RUNES
    public static final RegistryObject<Item> RUNE_ESSENCE = ITEMS.register("rune_essence",
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

    //Logic Runes
    public static final RegistryObject<Item> SELF_RUNE_ITEM = ITEMS.register("self_rune",
            () -> new SelfRuneItem(new Item.Properties()));

    public static final RegistryObject<Item> SIGHT_RUNE_ITEM = ITEMS.register("sight_rune",
            () -> new SightRuneItem(new Item.Properties()));

    public static final RegistryObject<Item> ENTITY_POSITION_RUNE_ITEM = ITEMS.register("entity_position_rune",
            () -> new EntityPositionRuneItem(new Item.Properties()));

    public static final RegistryObject<Item> HEAD_POSITION_RUNE_ITEM = ITEMS.register("head_position_rune",
            () -> new HeadPositionRuneItem(new Item.Properties()));

    public static final RegistryObject<Item> BLOCK_POSITION_RUNE_ITEM = ITEMS.register("block_position_rune",
            () -> new BlockPositionRuneItem(new Item.Properties()));

    public static final RegistryObject<Item> BLOCK_FACE_RUNE_ITEM = ITEMS.register("block_face_rune",
            () -> new BlockFaceRuneItem(new Item.Properties()));

    public static final RegistryObject<Item> TOUCH_RUNE_ITEM = ITEMS.register("touch_rune",
            () -> new TouchRuneItem(new Item.Properties()));

    public static final RegistryObject<Item> PROJECTILE_RUNE_ITEM = ITEMS.register("projectile_rune",
            () -> new ProjectileRuneItem(new Item.Properties()));

    public static final RegistryObject<Item> SPATIAL_RUNE_ITEM = ITEMS.register("spatial_rune",
            () -> new SpatialRuneItem(new Item.Properties()));


    public static final RegistryObject<Item> INTEGER_ONE_RUNE_ITEM = ITEMS.register("integer_one_rune",
            () -> new IntegerOneRuneItem(new Item.Properties()));

    public static final RegistryObject<Item> INTEGER_TWO_RUNE_ITEM = ITEMS.register("integer_two_rune",
            () -> new IntegerTwoRuneItem(new Item.Properties()));

    public static final RegistryObject<Item> INTEGER_THREE_RUNE_ITEM = ITEMS.register("integer_three_rune",
            () -> new IntegerThreeRuneItem(new Item.Properties()));

    public static final RegistryObject<Item> DUPLICITY_RUNE_ITEM = ITEMS.register("duplicity_rune",
            () -> new DuplicityRuneItem(new Item.Properties()));

    public static final RegistryObject<Item> NULL_RUNE_ITEM = ITEMS.register("null_rune",
            () -> new NullRuneItem(new Item.Properties()));

    public static final RegistryObject<Item> ADD_RUNE_ITEM = ITEMS.register("add_rune",
            () -> new AddRuneItem(new Item.Properties()));

    public static final RegistryObject<Item> SUBTRACT_RUNE_ITEM = ITEMS.register("subtract_rune",
            () -> new SubtractRuneItem(new Item.Properties()));

    public static final RegistryObject<Item> PURGE_RUNE_ITEM = ITEMS.register("purge_rune",
            () -> new PurgeRuneItem(new Item.Properties()));

    //Power Runes
    public static final RegistryObject<Item> EXCAVATE_RUNE_ITEM = ITEMS.register("excavate_rune",
            () -> new ExcavateRuneItem(new Item.Properties()));

    public static final RegistryObject<Item> ATTACK_RUNE_ITEM = ITEMS.register("attack_rune",
            () -> new AttackRuneItem(new Item.Properties()));

    public static final RegistryObject<Item> TELEPORT_RUNE_ITEM = ITEMS.register("teleport_rune",
            () -> new TeleportRuneItem(new Item.Properties()));

    public static final RegistryObject<Item> EXPLODE_RUNE_ITEM = ITEMS.register("explode_rune",
            () -> new ExplodeRuneItem(new Item.Properties()));

    public static final RegistryObject<Item> FORCE_RUNE_ITEM = ITEMS.register("force_rune",
            () -> new ForceRuneItem(new Item.Properties()));

    public static final RegistryObject<Item> ALCHEMY_RUNE_ITEM = ITEMS.register("alchemy_rune",
            () -> new AlchemyRuneItem(new Item.Properties()));

    /*
    public static final RegistryObject<Item> TARGET_ENTITY_RUNE_ITEM = ITEMS.register("target_entity_rune",
            () -> new TargetEntityRuneItem(new Item.Properties()));

    public static final RegistryObject<Item> AREA_TARGET_ENTITY_RUNE_ITEM = ITEMS.register("area_target_entity_rune",
            () -> new AreaTargetEntityRuneItem(new Item.Properties()));

    public static final RegistryObject<Item> TARGET_BLOCK_RUNE_ITEM = ITEMS.register("target_block_rune",
            () -> new TargetBlockRuneItem(new Item.Properties()));

    public static final RegistryObject<Item> AREA_TARGET_BLOCK_RUNE_ITEM = ITEMS.register("area_target_block_rune",
            () -> new AreaTargetBlockRuneItem(new Item.Properties()));

    public static final RegistryObject<Item> TARGET_FACE_RUNE_ITEM = ITEMS.register("target_face_rune",
            () -> new TargetFaceRuneItem(new Item.Properties()));

    public static final RegistryObject<Item> AREA_TARGET_FACE_RUNE_ITEM = ITEMS.register("area_target_face_rune",
            () -> new AreaTargetFaceRuneItem(new Item.Properties()));

    public static final RegistryObject<Item> TARGET_POSITION_RUNE_ITEM = ITEMS.register("target_position_rune",
            () -> new TargetPositionRuneItem(new Item.Properties()));
     */
}
