package net.mindoth.ancientmagicks.registries.recipe;

import com.google.common.collect.Lists;
import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.registries.AncientMagicksItems;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

import java.util.List;

public class ArcaneDustCraftingRecipe extends CustomRecipe {

    public ArcaneDustCraftingRecipe(ResourceLocation pId, CraftingBookCategory category) {
        super(pId, category);
    }

    @Override
    public boolean matches(CraftingContainer container, Level level) {
        List<Item> itemList = Lists.newArrayList();
        for ( int i = 0; i < container.getContainerSize(); i++ ) {
            itemList.add(container.getItem(i).getItem());
        }
        return itemList.equals(AncientMagicks.ARCANE_DUST_LIST);
    }

    @Override
    public ItemStack assemble(CraftingContainer container, RegistryAccess regAcc) {
        List<Item> itemList = Lists.newArrayList();
        for ( int i = 0; i < container.getContainerSize(); i++ ) {
            itemList.add(container.getItem(i).getItem());
        }
        return itemList.equals(AncientMagicks.ARCANE_DUST_LIST) ? getResultItem(regAcc) : ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return pWidth * pHeight >= 2;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess pRegistryAccess) {
        return new ItemStack(AncientMagicksItems.ARCANE_DUST.get());
    }

    /*@Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> list = NonNullList.create();
        AncientMagicks.ARCANE_DUST_LIST.forEach(item -> {
            Ingredient ingredient = Ingredient.of(item);
            list.add(ingredient);
        });
        return list;
    }

    @Override
    public boolean isSpecial() {
        return false;
    }*/

    @Override
    public RecipeSerializer<?> getSerializer() {
        return AncientMagicksRecipes.ARCANE_DUST_CRAFTING_RECIPE.get();
    }
}
