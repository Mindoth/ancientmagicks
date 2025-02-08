package net.mindoth.ancientmagicks.item.armor;

import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.registries.AncientMagicksItems;
import net.mindoth.ancientmagicks.registries.attribute.AncientMagicksAttributes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.Map;
import java.util.function.Supplier;

public enum AncientMagicksArmorMaterials implements CustomArmorMaterial {

    SIMPLE("simple", 4, new int[]{ 1, 1, 1, 1 }, 25, SoundEvents.ARMOR_EQUIP_LEATHER,
            0, 0, () -> Ingredient.of(AncientMagicksItems.WOOL_CLOTH.get()), Map.of(
            AncientMagicksAttributes.MP_MAX.get(), new AttributeModifier("Mana Maximum", 50.0D, AttributeModifier.Operation.ADDITION)
    )),
    TRIMMED("trimmed", 4, new int[]{ 1, 1, 1, 1 }, 25, SoundEvents.ARMOR_EQUIP_LEATHER,
            0, 0, () -> Ingredient.of(AncientMagicksItems.WOOL_CLOTH.get()), Map.of(
            AncientMagicksAttributes.MP_MAX.get(), new AttributeModifier("Mana Maximum", 75.0D, AttributeModifier.Operation.ADDITION),
            AncientMagicksAttributes.MP_REG.get(), new AttributeModifier("Mana Regeneration", 1.0D, AttributeModifier.Operation.ADDITION)
    )),
    ARCANE("arcane", 4, new int[]{ 1, 1, 1, 1 }, 25, SoundEvents.ARMOR_EQUIP_LEATHER,
            0, 0, () -> Ingredient.of(AncientMagicksItems.WOOL_CLOTH.get()), Map.of(
            AncientMagicksAttributes.MP_MAX.get(), new AttributeModifier("Mana Maximum", 100.0D, AttributeModifier.Operation.ADDITION),
            AncientMagicksAttributes.MP_REG.get(), new AttributeModifier("Mana Regeneration", 2.0D, AttributeModifier.Operation.ADDITION)
    ));

    private final String name;
    private final int durabilityModifier;
    private final int[] protectionAmounts;
    private final int enchantmentValue;
    private final SoundEvent equipSound;
    private final float toughness;
    private final float knockbackResistance;
    private final Supplier<Ingredient> repairIngredient;
    private final Map<Attribute, AttributeModifier> additionalAttributes;

    private static final int[] BASE_DURABILITY = { 11, 16, 15, 13 };

    AncientMagicksArmorMaterials(String name, int durabilityModifier, int[] protectionAmounts, int enchantmentValue, SoundEvent equipSound, float toughness, float knockbackResistance, Supplier<Ingredient> repairIngredient, Map<Attribute, AttributeModifier> additionalAttributes) {
        this.name = name;
        this.durabilityModifier = durabilityModifier;
        this.protectionAmounts = protectionAmounts;
        this.enchantmentValue = enchantmentValue;
        this.equipSound = equipSound;
        this.toughness = toughness;
        this.knockbackResistance = knockbackResistance;
        this.repairIngredient = repairIngredient;
        this.additionalAttributes = additionalAttributes;
    }

    @Override
    public int getDurabilityForType(ArmorItem.Type pType) {
        return BASE_DURABILITY[pType.ordinal()] * this.durabilityModifier;
    }

    @Override
    public int getDefenseForType(ArmorItem.Type pType) {
        return this.protectionAmounts[pType.ordinal()];
    }

    @Override
    public int getEnchantmentValue() {
        return this.enchantmentValue;
    }

    @Override
    public SoundEvent getEquipSound() {
        return this.equipSound;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return this.repairIngredient.get();
    }

    @Override
    public String getName() {
        return AncientMagicks.MOD_ID + ":" + this.name;
    }

    @Override
    public float getToughness() {
        return this.toughness;
    }

    @Override
    public float getKnockbackResistance() {
        return this.knockbackResistance;
    }

    @Override
    public Map<Attribute, AttributeModifier> getAdditionalAttributes() {
        return this.additionalAttributes;
    }
}
