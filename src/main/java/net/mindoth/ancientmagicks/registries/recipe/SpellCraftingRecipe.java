package net.mindoth.ancientmagicks.registries.recipe;

import com.google.common.collect.Lists;
import net.mindoth.ancientmagicks.item.ColorRuneItem;
import net.mindoth.ancientmagicks.item.ParchmentItem;
import net.mindoth.ancientmagicks.item.castingitem.SpecialCastingItem;
import net.mindoth.ancientmagicks.item.spell.abstractspell.SpellItem;
import net.mindoth.ancientmagicks.registries.AncientMagicksItems;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

public class SpellCraftingRecipe extends CustomRecipe {

    public SpellCraftingRecipe(ResourceLocation pId, CraftingBookCategory category) {
        super(pId, category);
    }

    @Override
    public boolean matches(CraftingContainer container, Level level) {
        List<ColorRuneItem> list = Lists.newArrayList();
        List<Item> rest = Lists.newArrayList();
        for ( int i = 0; i < container.getContainerSize(); i++ ) {
            ItemStack stack = container.getItem(i);
            if ( stack.getItem() instanceof ColorRuneItem rune ) list.add(rune);
            else if ( stack.getItem() instanceof ParchmentItem item ) rest.add(item);
        }
        if ( ColorRuneItem.checkForSpellCombo(list) != null ) {
            SpellItem spell = ColorRuneItem.checkForSpellCombo(list);
            if ( spell.isCraftable() && rest.size() == 1 ) {
                if ( spell.spellTier >= 1 && spell.spellTier <= 3 && rest.contains(AncientMagicksItems.PARCHMENT.get()) ) return true;
                else if ( spell.spellTier >= 4 && spell.spellTier <= 6 && rest.contains(AncientMagicksItems.INFERNAL_PARCHMENT.get()) ) return true;
            }
        }
        return false;
    }

    @Override
    public ItemStack assemble(CraftingContainer container, RegistryAccess regAcc) {
        List<ColorRuneItem> list = Lists.newArrayList();
        for ( int i = 0; i < container.getContainerSize(); i++ ) {
            ItemStack stack = container.getItem(i);
            if ( stack.getItem() instanceof ColorRuneItem rune ) list.add(rune);
        }
        ItemStack spellStack = new ItemStack(AncientMagicksItems.SPELL_SCROLL.get());
        return createSpellScroll(spellStack, ColorRuneItem.checkForSpellCombo(list));
    }

    public static ItemStack createSpellScroll(ItemStack stack, SpellItem spell) {
        String spellString = ForgeRegistries.ITEMS.getKey(spell).toString();
        stack.getOrCreateTag().putString(SpecialCastingItem.TAG_STORED_SPELL, spellString);
        if ( spell.spellTier >= 4 && spell.spellTier <= 6 ) stack.getOrCreateTag().putInt("CustomModelData", 1);
        return stack;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return pWidth * pHeight >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return AncientMagicksRecipes.SPELL_CRAFTING_RECIPE.get();
    }
}
