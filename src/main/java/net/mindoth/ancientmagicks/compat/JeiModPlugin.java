package net.mindoth.ancientmagicks.compat;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.client.screen.RuneCraftingScreen;
import net.mindoth.ancientmagicks.registries.recipe.RuneCraftingRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.List;

@JeiPlugin
public class JeiModPlugin implements IModPlugin {

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(AncientMagicks.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new RuneCraftingCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();

        List<RuneCraftingRecipe> runeCraftingRecipes = recipeManager.getAllRecipesFor(RuneCraftingRecipe.Type.INSTANCE);
        registration.addRecipes(RuneCraftingCategory.RUNE_CRAFTING_TYPE, runeCraftingRecipes);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        //registration.addRecipeClickArea(RuneCraftingScreen.class, 91, 42, 22, 15, RuneCraftingCategory.RUNE_CRAFTING_TYPE);
    }
}
