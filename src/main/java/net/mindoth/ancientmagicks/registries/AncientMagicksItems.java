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
import net.mindoth.ancientmagicks.item.effect.AlchemyEffectItem;
import net.mindoth.ancientmagicks.item.effect.CallThunderEffectItem;
import net.mindoth.ancientmagicks.item.effect.CreateWaterEffectItem;
import net.mindoth.ancientmagicks.item.effect.DestroyLiquidsEffectItem;
import net.mindoth.ancientmagicks.item.effect.MindControlEffectItem;
import net.mindoth.ancientmagicks.item.effect.BreakEffectItem;
import net.mindoth.ancientmagicks.item.effect.TeleportEffectItem;
import net.mindoth.ancientmagicks.item.form.SpatialFormItem;
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

    public static final RegistryObject<Item> WHITE_RUNE = ITEMS.register("white_rune",
            () -> new ColorRuneItem(new Item.Properties(), "\u00A7f"));

    public static final RegistryObject<Item> BLUE_RUNE = ITEMS.register("blue_rune",
            () -> new ColorRuneItem(new Item.Properties(), "\u00A7b"));

    public static final RegistryObject<Item> GREEN_RUNE = ITEMS.register("green_rune",
            () -> new ColorRuneItem(new Item.Properties(), "\u00A7a"));

    public static final RegistryObject<Item> PURPLE_RUNE = ITEMS.register("purple_rune",
            () -> new ColorRuneItem(new Item.Properties(), "\u00A7d"));

    public static final RegistryObject<Item> RED_RUNE = ITEMS.register("red_rune",
            () -> new ColorRuneItem(new Item.Properties(), "\u00A74"));

    public static final RegistryObject<Item> YELLOW_RUNE = ITEMS.register("yellow_rune",
            () -> new ColorRuneItem(new Item.Properties(), "\u00A7e"));


    //Forms
    public static final RegistryObject<Item> PROJECTILE_FORM_ITEM = ITEMS.register("projectile_form",
            () -> new ProjectileFormItem(new Item.Properties(), 0, 0));

    public static final RegistryObject<Item> TOUCH_FORM_ITEM = ITEMS.register("touch_form",
            () -> new TouchFormItem(new Item.Properties(), 0, 0));

    public static final RegistryObject<Item> SELF_FORM_ITEM = ITEMS.register("self_form",
            () -> new SelfFormItem(new Item.Properties(), 0, 0));

    public static final RegistryObject<Item> SPATIAL_FORM_ITEM = ITEMS.register("spatial_form",
            () -> new SpatialFormItem(new Item.Properties(), 0, 0));

    //Effects
    public static final RegistryObject<Item> BREAK_EFFECT_ITEM = ITEMS.register("break_effect",
            () -> new BreakEffectItem(new Item.Properties(), 1, 0));

    public static final RegistryObject<Item> CALL_THUNDER_EFFECT_ITEM = ITEMS.register("call_thunder_effect",
            () -> new CallThunderEffectItem(new Item.Properties(), 1, 0));

    public static final RegistryObject<Item> DESTROY_LIQUIDS_EFFECT_ITEM = ITEMS.register("destroy_liquids_effect",
            () -> new DestroyLiquidsEffectItem(new Item.Properties(), 1, 0));

    public static final RegistryObject<Item> CREATE_WATER_EFFECT_ITEM = ITEMS.register("create_water_effect",
            () -> new CreateWaterEffectItem(new Item.Properties(), 1, 0));

    public static final RegistryObject<Item> TELEPORT_EFFECT_ITEM = ITEMS.register("teleport_effect",
            () -> new TeleportEffectItem(new Item.Properties(), 1, 0));

    public static final RegistryObject<Item> ALCHEMY_EFFECT_ITEM = ITEMS.register("alchemy_effect",
            () -> new AlchemyEffectItem(new Item.Properties(), 1, 0));

    public static final RegistryObject<Item> MIND_CONTROL_EFFECT_ITEM = ITEMS.register("mind_control_effect",
            () -> new MindControlEffectItem(new Item.Properties(), 1, 0));

    //Modifiers
    public static final RegistryObject<Item> AMPLIFY_MODIFIER_ITEM = ITEMS.register("amplify_modifier",
            () -> new AmplifyModifierItem(new Item.Properties(), 4, 0));

    public static final RegistryObject<Item> DAMPEN_MODIFIER_ITEM = ITEMS.register("dampen_modifier",
            () -> new DampenModifierItem(new Item.Properties(), 0, 0));

    public static final RegistryObject<Item> EXPAND_MODIFIER_ITEM = ITEMS.register("expand_modifier",
            () -> new ExpandModifierItem(new Item.Properties(), 9, 0));

    public static final RegistryObject<Item> SHRINK_MODIFIER_ITEM = ITEMS.register("shrink_modifier",
            () -> new ShrinkModifierItem(new Item.Properties(), 0, 0));

    public static final RegistryObject<Item> REACH_MODIFIER_ITEM = ITEMS.register("reach_modifier",
            () -> new ReachModifierItem(new Item.Properties(), 1, 0));

    public static final RegistryObject<Item> CONTRACT_MODIFIER_ITEM = ITEMS.register("contract_modifier",
            () -> new ContractModifierItem(new Item.Properties(), 0, 0));

    public static final RegistryObject<Item> QUICKEN_MODIFIER_ITEM = ITEMS.register("quicken_modifier",
            () -> new QuickenModifierItem(new Item.Properties(), 1, 0));

    public static final RegistryObject<Item> SLACKEN_MODIFIER_ITEM = ITEMS.register("slacken_modifier",
            () -> new SlackenModifierItem(new Item.Properties(), 1, 0));

    public static final RegistryObject<Item> EXTEND_MODIFIER_ITEM = ITEMS.register("extend_modifier",
            () -> new ExtendModifierItem(new Item.Properties(), 1, 0));

    public static final RegistryObject<Item> DIMINISH_MODIFIER_ITEM = ITEMS.register("diminish_modifier",
            () -> new DiminishModifierItem(new Item.Properties(), 1, 0));

    public static final RegistryObject<Item> ENTITY_PIERCE_MODIFIER_ITEM = ITEMS.register("entity_pierce_modifier",
            () -> new EntityPierceModifierItem(new Item.Properties(), 4, 0));

    public static final RegistryObject<Item> BLOCK_PIERCE_MODIFIER_ITEM = ITEMS.register("block_pierce_modifier",
            () -> new BlockPierceModifierItem(new Item.Properties(), 4, 0));

    public static final RegistryObject<Item> BOUNCING_MODIFIER_ITEM = ITEMS.register("bounce_modifier",
            () -> new BouncingModifierItem(new Item.Properties(), 1, 0));

    public static final RegistryObject<Item> HOMING_MODIFIER_ITEM = ITEMS.register("homing_modifier",
            () -> new HomingModifierItem(new Item.Properties(), 6, 0));

    public static final RegistryObject<Item> GRAVITY_MODIFIER_ITEM = ITEMS.register("gravity_modifier",
            () -> new GravityModifierItem(new Item.Properties(), 0, 0));

    public static final RegistryObject<Item> LOCATION_MODIFIER_ITEM = ITEMS.register("location_modifier",
            () -> new LocationModifierItem(new Item.Properties(), 6, 0));
}
