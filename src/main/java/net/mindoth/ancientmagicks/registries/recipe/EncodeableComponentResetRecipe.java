package net.mindoth.ancientmagicks.registries.recipe;

import com.google.common.collect.Lists;
import net.mindoth.ancientmagicks.item.ComponentItem;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

import java.util.List;

public class EncodeableComponentResetRecipe extends CustomRecipe {

    public EncodeableComponentResetRecipe(ResourceLocation pId, CraftingBookCategory category) {
        super(pId, category);
    }

    @Override
    public boolean matches(CraftingContainer container, Level level) {
        List<ItemStack> items = Lists.newArrayList();
        for ( int i = 0; i < container.getContainerSize(); i++ ) {
            ItemStack stack = container.getItem(i);
            if ( !stack.isEmpty() && stack.getItem() instanceof ComponentItem component && component.isEncodeable() && stack.hasTag() ) items.add(stack);
        }
        return items.size() == 1;
    }

    @Override
    public ItemStack assemble(CraftingContainer container, RegistryAccess regAcc) {
        ItemStack returnStack = ItemStack.EMPTY;
        List<ItemStack> items = Lists.newArrayList();
        for ( int i = 0; i < container.getContainerSize(); i++ ) {
            ItemStack stack = container.getItem(i);
            if ( !stack.isEmpty() && stack.getItem() instanceof ComponentItem component && component.isEncodeable() ) items.add(stack);
        }
        if ( items.size() == 1 ) returnStack = new ItemStack(items.get(0).getItem());
        return returnStack;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return pWidth * pHeight >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.ENCODEABLE_COMPONENT_RESET_RECIPE.get();
    }
}
