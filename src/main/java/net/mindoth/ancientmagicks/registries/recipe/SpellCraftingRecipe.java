package net.mindoth.ancientmagicks.registries.recipe;

import com.google.common.collect.Lists;
import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.item.ColorRuneItem;
import net.mindoth.ancientmagicks.item.ParchmentItem;
import net.mindoth.ancientmagicks.item.SpellItem;
import net.mindoth.ancientmagicks.registries.AncientMagicksItems;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

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
            else if ( stack.getItem() != Items.AIR ) rest.add(stack.getItem());
        }
        if ( ColorRuneItem.checkForSpellCombo(list) != null && list.size() + rest.size() == AncientMagicks.comboSizeCalc() + 1 ) {
            SpellItem spell = ColorRuneItem.checkForSpellCombo(list);
            if ( spell.isCraftable() && rest.size() == 1 ) return rest.contains(AncientMagicks.getParchmentForSpell(spell.getSpellTier()));
        }
        return false;
    }

    @Override
    public ItemStack assemble(CraftingContainer container, RegistryAccess regAcc) {
        List<ColorRuneItem> list = Lists.newArrayList();
        List<Item> rest = Lists.newArrayList();
        for ( int i = 0; i < container.getContainerSize(); i++ ) {
            ItemStack stack = container.getItem(i);
            if ( stack.getItem() instanceof ColorRuneItem rune ) list.add(rune);
            else if ( stack.getItem() != Items.AIR ) rest.add(stack.getItem());
        }
        if ( ColorRuneItem.checkForSpellCombo(list) != null && list.size() + rest.size() == AncientMagicks.comboSizeCalc() + 1 ) {
            ItemStack spellStack = new ItemStack(AncientMagicksItems.SPELL_SCROLL.get());
            return AncientMagicks.createSpellScroll(spellStack, ColorRuneItem.checkForSpellCombo(list));
        }
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return pWidth * pHeight >= 2;
    }

    /*@Override
    public ItemStack getResultItem(RegistryAccess pRegistryAccess) {
        return ItemStack.EMPTY;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
    }

    @Override
    public boolean isSpecial() {
        return false;
    }*/

    @Override
    public RecipeSerializer<?> getSerializer() {
        return AncientMagicksRecipes.SPELL_CRAFTING_RECIPE.get();
    }
}
