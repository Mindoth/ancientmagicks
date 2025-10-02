package net.mindoth.ancientmagicks.registries.recipe;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.mindoth.ancientmagicks.AncientMagicks;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class RuneCraftingRecipe implements Recipe<CraftingContainer> {

    private final NonNullList<Ingredient> input;
    private final ItemStack output;
    private final ResourceLocation id;

    public RuneCraftingRecipe(NonNullList<Ingredient> input, ItemStack output, ResourceLocation id) {
        this.input = input;
        this.output = output;
        this.id = id;
    }

    @Override
    public boolean matches(CraftingContainer container, Level level) {
        if ( level.isClientSide ) return false;
        List<ItemStack> actualItems = Lists.newArrayList();
        for ( ItemStack itemStack : container.getItems() ) if ( !itemStack.isEmpty() ) actualItems.add(itemStack);
        if ( actualItems.size() != this.input.size() ) return false;
        for ( int i = 0; i < this.input.size(); i++ ) if ( !this.input.get(i).test(container.getItem(i)) ) return false;
        return true;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return this.input;
    }

    @Override
    public ItemStack assemble(CraftingContainer pContainer, RegistryAccess pRegistryAccess) {
        return this.output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess pRegistryAccess) {
        return this.output.copy();
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<RuneCraftingRecipe> {
        public static final Type INSTANCE = new Type();
        public static final String ID = "rune_crafting";
    }

    public static class Serializer implements RecipeSerializer<RuneCraftingRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = new ResourceLocation(AncientMagicks.MOD_ID, "rune_crafting");

        @Override
        public RuneCraftingRecipe fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(pSerializedRecipe, "output"));

            JsonArray ingredients = GsonHelper.getAsJsonArray(pSerializedRecipe, "ingredients");
            NonNullList<Ingredient> inputs = NonNullList.withSize(ingredients.size(), Ingredient.EMPTY);

            for ( int i = 0; i < inputs.size(); i++ ) inputs.set(i, Ingredient.fromJson(ingredients.get(i)));

            return new RuneCraftingRecipe(inputs, output, pRecipeId);
        }

        @Override
        public @Nullable RuneCraftingRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            NonNullList<Ingredient> inputs = NonNullList.withSize(pBuffer.readInt(), Ingredient.EMPTY);

            for ( int i = 0; i < inputs.size(); i++ ) inputs.set(i, Ingredient.fromNetwork(pBuffer));

            ItemStack output = pBuffer.readItem();

            return new RuneCraftingRecipe(inputs, output, pRecipeId);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, RuneCraftingRecipe pRecipe) {
            pBuffer.writeInt(pRecipe.input.size());

            for ( Ingredient ingredient : pRecipe.getIngredients() ) ingredient.toNetwork(pBuffer);

            pBuffer.writeItemStack(pRecipe.getResultItem(null), false);
        }
    }
}
