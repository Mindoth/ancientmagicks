package net.mindoth.ancientmagicks.registries.recipe;

import com.google.common.collect.Lists;
import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.item.SpellBookItem;
import net.mindoth.ancientmagicks.registries.AncientMagicksItems;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

import java.util.List;

public class SpellBookResettingRecipe extends CustomRecipe {

    public SpellBookResettingRecipe(ResourceLocation pId, CraftingBookCategory category) {
        super(pId, category);
    }

    @Override
    public boolean matches(CraftingContainer container, Level level) {
        List<ItemStack> itemList = Lists.newArrayList();
        for ( int i = 0; i < container.getContainerSize(); i++ ) {
            if ( container.getItem(i).getItem() != Items.AIR ) itemList.add(container.getItem(i));
        }
        return itemList.size() == 1 && itemList.get(0).getItem() instanceof SpellBookItem;
    }

    @Override
    public ItemStack assemble(CraftingContainer container, RegistryAccess regAcc) {
        List<ItemStack> itemList = Lists.newArrayList();
        for ( int i = 0; i < container.getContainerSize(); i++ ) {
            if ( container.getItem(i).getItem() != Items.AIR ) itemList.add(container.getItem(i));
        }
        return itemList.size() == 1 && itemList.get(0).getItem() instanceof SpellBookItem ? new ItemStack(AncientMagicksItems.SPELL_BOOK.get()) : ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    /*@Override
    public ItemStack getResultItem(RegistryAccess pRegistryAccess) {
        return new ItemStack(AncientMagicksItems.SPELL_BOOK.get());
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> list = NonNullList.create();
        list.add(Ingredient.of(AncientMagicksItems.SPELL_BOOK.get()));
        return list;
    }*/

    @Override
    public boolean isSpecial() {
        return false;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return AncientMagicksRecipes.SPELL_BOOK_RESETTING_RECIPE.get();
    }
}
