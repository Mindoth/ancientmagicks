package net.mindoth.ancientmagicks.registries.recipe;

import net.mindoth.ancientmagicks.AncientMagicks;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, AncientMagicks.MOD_ID);

    public static final RegistryObject<SimpleCraftingRecipeSerializer<ArcaneDustCraftingRecipe>> ARCANE_DUST_CRAFTING_RECIPE =
            SERIALIZERS.register("arcane_dust_crafting", () -> new SimpleCraftingRecipeSerializer<>(ArcaneDustCraftingRecipe::new));

    public static final RegistryObject<SimpleCraftingRecipeSerializer<SpellBookAddRecipe>> SPELL_BOOK_ADD_RECIPE =
            SERIALIZERS.register("spell_book_add_crafting", () -> new SimpleCraftingRecipeSerializer<>(SpellBookAddRecipe::new));

    public static final RegistryObject<SimpleCraftingRecipeSerializer<AlchemyEffectItemRecipe>> ALCHEMY_EFFECT_ITEM_RECIPE =
            SERIALIZERS.register("alchemy_effect_item_crafting", () -> new SimpleCraftingRecipeSerializer<>(AlchemyEffectItemRecipe::new));

    public static final RegistryObject<SimpleCraftingRecipeSerializer<EncodeableComponentResetRecipe>> ENCODEABLE_COMPONENT_RESET_RECIPE =
            SERIALIZERS.register("encodeable_component_reset_crafting", () -> new SimpleCraftingRecipeSerializer<>(EncodeableComponentResetRecipe::new));


    public static final RegistryObject<RecipeSerializer<RuneCraftingRecipe>> RUNE_CRAFTING_RECIPE =
            SERIALIZERS.register("rune_crafting", () -> RuneCraftingRecipe.Serializer.INSTANCE);
}
