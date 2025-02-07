package net.mindoth.ancientmagicks.compat;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.ingredients.subtypes.IIngredientSubtypeInterpreter;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.item.castingitem.SpecialCastingItem;
import net.mindoth.ancientmagicks.item.SpellItem;
import net.mindoth.ancientmagicks.item.SpellStorageItem;
import net.mindoth.ancientmagicks.registries.AncientMagicksItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

@mezz.jei.api.JeiPlugin
public class JeiPlugin implements IModPlugin {

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(AncientMagicks.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration registration) {
        registration.registerSubtypeInterpreter(AncientMagicksItems.SPELL_SCROLL.get(),SCROLL_INTERPRETER);
    }

    private static final IIngredientSubtypeInterpreter<ItemStack> SCROLL_INTERPRETER = (stack, context) -> {
        if ( stack.getItem() instanceof SpellStorageItem && SpecialCastingItem.getStoredSpell(stack) != null ) {
            SpellItem spell = SpecialCastingItem.getStoredSpell(stack);
            return String.format("scroll:%s:%d", spell, spell.getSpellTier());
        }
        return IIngredientSubtypeInterpreter.NONE;
    };
}
