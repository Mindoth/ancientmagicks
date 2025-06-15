package net.mindoth.ancientmagicks.registries;

import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.item.AncientTabletItem;
import net.mindoth.ancientmagicks.item.ColorRuneItem;
import net.mindoth.ancientmagicks.item.ParchmentItem;
import net.mindoth.ancientmagicks.item.SpellBookItem;
import net.mindoth.ancientmagicks.item.armor.AncientMagicksArmorMaterials;
import net.mindoth.ancientmagicks.item.armor.ColorableMagickArmorItem;
import net.mindoth.ancientmagicks.item.castingitem.ColorableStaffItem;
import net.mindoth.ancientmagicks.item.castingitem.StaffItem;
import net.mindoth.ancientmagicks.item.effect.*;
import net.mindoth.ancientmagicks.item.form.ProjectileFormItem;
import net.mindoth.ancientmagicks.item.form.SelfFormItem;
import net.mindoth.ancientmagicks.item.form.TouchFormItem;
import net.mindoth.ancientmagicks.item.modifier.*;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Map;

public class AncientMagicksItems {
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

    public static final RegistryObject<Item> ROBE_HOOD = ITEMS.register("robe_hood",
            () -> new ColorableMagickArmorItem(AncientMagicksArmorMaterials.ROBE, ArmorItem.Type.HELMET, new Item.Properties()));

    public static final RegistryObject<Item> ROBE_TOP = ITEMS.register("robe_top",
            () -> new ColorableMagickArmorItem(AncientMagicksArmorMaterials.ROBE, ArmorItem.Type.CHESTPLATE, new Item.Properties()));

    public static final RegistryObject<Item> ROBE_BOTTOM = ITEMS.register("robe_bottom",
            () -> new ColorableMagickArmorItem(AncientMagicksArmorMaterials.ROBE, ArmorItem.Type.LEGGINGS, new Item.Properties()));

    public static final RegistryObject<Item> SPELL_BOOK = ITEMS.register("spell_book",
            () -> new SpellBookItem(new Item.Properties().stacksTo(1)));


    //Spell Parchments
    public static final RegistryObject<Item> PARCHMENT = ITEMS.register("parchment",
            () -> new ParchmentItem(new Item.Properties(), 3));

    public static final RegistryObject<Item> INFERNAL_PARCHMENT = ITEMS.register("infernal_parchment",
            () -> new ParchmentItem(new Item.Properties(), 6));

    public static final RegistryObject<Item> ARCANE_PARCHMENT = ITEMS.register("arcane_parchment",
            () -> new ParchmentItem(new Item.Properties(), 9));


    //Runes
    public static final RegistryObject<Item> BLANK_RUNE = ITEMS.register("blank_rune",
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
            () -> new ColorRuneItem(new Item.Properties(), "\u00A74"));

    public static final RegistryObject<Item> WHITE_RUNE = ITEMS.register("white_rune",
            () -> new ColorRuneItem(new Item.Properties(), "\u00A7f"));


    //Forms
    public static final RegistryObject<Item> PROJECTILE_SIGIL_ITEM = ITEMS.register("projectile_sigil",
            () -> new ProjectileFormItem(new Item.Properties(), 0, 0));

    public static final RegistryObject<Item> TOUCH_SIGIL_ITEM = ITEMS.register("touch_sigil",
            () -> new TouchFormItem(new Item.Properties(), 0, 0));

    public static final RegistryObject<Item> SELF_SIGIL_ITEM = ITEMS.register("self_sigil",
            () -> new SelfFormItem(new Item.Properties(), 0, 0));

    //Effects
    public static final RegistryObject<Item> BREAK_SIGIL_ITEM = ITEMS.register("break_sigil",
            () -> new BreakEffectItem(new Item.Properties(), 1, 0));

    public static final RegistryObject<Item> CALL_THUNDER_SIGIL_ITEM = ITEMS.register("call_thunder_sigil",
            () -> new CallThunderEffectItem(new Item.Properties(), 1, 0));

    public static final RegistryObject<Item> DESTROY_LIQUIDS_SIGIL_ITEM = ITEMS.register("destroy_liquids_sigil",
            () -> new DestroyLiquidsEffectItem(new Item.Properties(), 1, 0));

    public static final RegistryObject<Item> CREATE_WATER_SIGIL_ITEM = ITEMS.register("create_water_sigil",
            () -> new CreateWaterEffectItem(new Item.Properties(), 1, 0));

    public static final RegistryObject<Item> TELEPORT_SIGIL_ITEM = ITEMS.register("teleport_sigil",
            () -> new TeleportEffectItem(new Item.Properties(), 1, 0));

    public static final RegistryObject<Item> TELEBRING_SIGIL_ITEM = ITEMS.register("telebring_sigil",
            () -> new TelebringEffectItem(new Item.Properties(), 1, 0));

    public static final RegistryObject<Item> ALCHEMY_SIGIL_ITEM = ITEMS.register("alchemy_sigil",
            () -> new AlchemyEffectItem(new Item.Properties(), 1, 0));

    public static final RegistryObject<Item> MIND_CONTROL_SIGIL_ITEM = ITEMS.register("mind_control_sigil",
            () -> new MindControlEffectItem(new Item.Properties(), 1, 0));

    //Modifiers
    public static final RegistryObject<Item> AMPLIFY_SIGIL_ITEM = ITEMS.register("amplify_sigil",
            () -> new AmplifyModifierItem(new Item.Properties(), 1, 0));

    public static final RegistryObject<Item> DAMPEN_SIGIL_ITEM = ITEMS.register("dampen_sigil",
            () -> new DampenModifierItem(new Item.Properties(), 1, 0));

    public static final RegistryObject<Item> EXPAND_SIGIL_ITEM = ITEMS.register("expand_sigil",
            () -> new ExpandModifierItem(new Item.Properties(), 1, 0));

    public static final RegistryObject<Item> SHRINK_SIGIL_ITEM = ITEMS.register("shrink_sigil",
            () -> new ShrinkModifierItem(new Item.Properties(), 1, 0));

    public static final RegistryObject<Item> REACH_SIGIL_ITEM = ITEMS.register("reach_sigil",
            () -> new ReachModifierItem(new Item.Properties(), 1, 0));

    public static final RegistryObject<Item> CONTRACT_SIGIL_ITEM = ITEMS.register("contract_sigil",
            () -> new ContractModifierItem(new Item.Properties(), 1, 0));

    public static final RegistryObject<Item> QUICKEN_SIGIL_ITEM = ITEMS.register("quicken_sigil",
            () -> new QuickenModifierItem(new Item.Properties(), 1, 0));

    public static final RegistryObject<Item> SLACKEN_SIGIL_ITEM = ITEMS.register("slacken_sigil",
            () -> new SlackenModifierItem(new Item.Properties(), 1, 0));

    public static final RegistryObject<Item> EXTEND_SIGIL_ITEM = ITEMS.register("extend_sigil",
            () -> new ExtendModifierItem(new Item.Properties(), 1, 0));

    public static final RegistryObject<Item> DIMINISH_SIGIL_ITEM = ITEMS.register("diminish_sigil",
            () -> new DiminishModifierItem(new Item.Properties(), 1, 0));

    public static final RegistryObject<Item> ENTITY_PIERCE_SIGIL_ITEM = ITEMS.register("entity_pierce_sigil",
            () -> new EntityPierceModifierItem(new Item.Properties(), 1, 0));

    public static final RegistryObject<Item> BLOCK_PIERCE_SIGIL_ITEM = ITEMS.register("block_pierce_sigil",
            () -> new BlockPierceModifierItem(new Item.Properties(), 1, 0));

    public static final RegistryObject<Item> BOUNCING_SIGIL_ITEM = ITEMS.register("bounce_sigil",
            () -> new BouncingModifierItem(new Item.Properties(), 1, 0));

    public static final RegistryObject<Item> HOMING_SIGIL_ITEM = ITEMS.register("homing_sigil",
            () -> new HomingModifierItem(new Item.Properties(), 1, 0));

    public static final RegistryObject<Item> GRAVITY_SIGIL_ITEM = ITEMS.register("gravity_sigil",
            () -> new GravityModifierItem(new Item.Properties(), 1, 0));

    public static final RegistryObject<Item> LOCATION_SIGIL_ITEM = ITEMS.register("location_sigil",
            () -> new LocationModifierItem(new Item.Properties(), 1, 0));
}
