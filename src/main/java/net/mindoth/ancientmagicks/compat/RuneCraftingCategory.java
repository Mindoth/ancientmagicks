package net.mindoth.ancientmagicks.compat;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.registries.ModBlocks;
import net.mindoth.ancientmagicks.registries.recipe.RuneCraftingRecipe;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class RuneCraftingCategory implements IRecipeCategory<RuneCraftingRecipe> {

    public static final ResourceLocation UID = new ResourceLocation(AncientMagicks.MOD_ID, "rune_crafting");
    public static final ResourceLocation TEXTURE = new ResourceLocation(AncientMagicks.MOD_ID, "textures/gui/jei_screen.png");
    public static final RecipeType<RuneCraftingRecipe> RUNE_CRAFTING_TYPE = new RecipeType<>(UID, RuneCraftingRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    public RuneCraftingCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 116, 54);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.RUNE_CRAFTING_TABLE.get()));
    }

    @Override
    public RecipeType<RuneCraftingRecipe> getRecipeType() {
        return RUNE_CRAFTING_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("block.ancientmagicks.rune_crafting_table");
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, RuneCraftingRecipe recipe, IFocusGroup focuses) {
        int slot = 0;
        builder.addSlot(RecipeIngredientRole.INPUT, 48 - 29, 35 - 16).addIngredients(recipe.getIngredients().get(slot));
        slot++;
        for ( int i = 0; i < 2; ++i ) {
            for ( int j = 0; j < 3; ++j ) {
                if ( slot >= recipe.getIngredients().size() ) break;
                int extra0 = i == 1 && j == 1 ? 18 : 0;
                int extra1 = j != 1 ? 9 : 0;
                builder.addSlot(RecipeIngredientRole.INPUT, 30 - 29 + j * 18, 17 - 16 + i * 18 + extra0 + extra1).addIngredients(recipe.getIngredients().get(slot));
                slot++;
            }
        }
        builder.addSlot(RecipeIngredientRole.OUTPUT, 124 - 29, 35 - 16).addItemStack(recipe.getResultItem(null));
    }
}
