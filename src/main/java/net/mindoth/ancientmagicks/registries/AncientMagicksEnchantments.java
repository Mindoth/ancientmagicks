package net.mindoth.ancientmagicks.registries;

import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.enchantment.CelestialEnchantment;
import net.mindoth.ancientmagicks.enchantment.OpenMindEnchantment;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class AncientMagicksEnchantments {
    public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, AncientMagicks.MOD_ID);

    public static RegistryObject<Enchantment> OPEN_MIND = ENCHANTMENTS.register("open_mind",
            () -> new OpenMindEnchantment(Enchantment.Rarity.RARE, EnchantmentCategory.ARMOR_HEAD, EquipmentSlot.HEAD));

    public static RegistryObject<Enchantment> CELESTIAL = ENCHANTMENTS.register("celestial",
            () -> new CelestialEnchantment(Enchantment.Rarity.UNCOMMON, EnchantmentCategory.ARMOR, EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET));
}
