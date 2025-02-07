package net.mindoth.ancientmagicks.enchantment;

import net.mindoth.ancientmagicks.registries.AncientMagicksEnchantments;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class MagickEnchantmentHelper {

    public static int getArmorMpReg(LivingEntity living) {
        int i = 0;
        for ( ItemStack stack : living.getArmorSlots() ) i += stack.getEnchantmentLevel(AncientMagicksEnchantments.CELESTIAL.get());
        return i;
    }
}
