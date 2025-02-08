package net.mindoth.ancientmagicks.enchantment;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

public class MagickEnchantmentHelper {

    public static int getArmorEnchantLevels(LivingEntity living, Enchantment enchantment) {
        int i = 0;
        for ( ItemStack stack : living.getArmorSlots() ) i += stack.getEnchantmentLevel(enchantment);
        return i;
    }
}
