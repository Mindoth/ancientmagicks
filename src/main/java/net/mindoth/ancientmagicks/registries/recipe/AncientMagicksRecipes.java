package net.mindoth.ancientmagicks.registries.recipe;

import net.mindoth.ancientmagicks.AncientMagicks;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class AncientMagicksRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, AncientMagicks.MOD_ID);

    public static final RegistryObject<SimpleCraftingRecipeSerializer<SpellCraftingRecipe>> SPELL_CRAFTING_RECIPE =
            SERIALIZERS.register("spell_crafting", () -> new SimpleCraftingRecipeSerializer<>(SpellCraftingRecipe::new));
}