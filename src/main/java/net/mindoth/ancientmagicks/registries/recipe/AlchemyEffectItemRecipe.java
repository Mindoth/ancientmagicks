package net.mindoth.ancientmagicks.registries.recipe;

import com.google.common.collect.Lists;
import net.mindoth.ancientmagicks.item.SpellComponentItem;
import net.mindoth.ancientmagicks.registries.ModItems;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

public class AlchemyEffectItemRecipe extends CustomRecipe {

    public AlchemyEffectItemRecipe(ResourceLocation pId, CraftingBookCategory category) {
        super(pId, category);
    }

    @Override
    public boolean matches(CraftingContainer container, Level level) {
        List<ItemStack> items = Lists.newArrayList();
        ItemStack slate = null;
        Potion potion = null;
        for ( int i = 0; i < container.getContainerSize(); i++ ) {
            ItemStack stack = container.getItem(i);
            if ( !stack.isEmpty() ) items.add(stack);
            if ( stack.getItem() == ModItems.ALCHEMY_SIGIL.get() && (!stack.hasTag() || !stack.getTag().contains(SpellComponentItem.NBT_KEY_COMPONENT_DATA)) ) {
                if ( slate == null ) slate = stack;
                else return false;
            }
            if ( stack.getItem() instanceof PotionItem ) {
                if ( potion == null ) potion = PotionUtils.getPotion(stack);
                else return false;
            }
        }
        return items.size() == 2 && slate != null && potion != null;
    }

    @Override
    public ItemStack assemble(CraftingContainer container, RegistryAccess regAcc) {
        ItemStack returnStack = ItemStack.EMPTY;
        List<ItemStack> items = Lists.newArrayList();
        ItemStack slate = null;
        Potion potion = null;
        for ( int i = 0; i < container.getContainerSize(); i++ ) {
            ItemStack stack = container.getItem(i);
            if ( !stack.isEmpty() ) items.add(stack);
            if ( stack.getItem() == ModItems.ALCHEMY_SIGIL.get() && (!stack.hasTag() || !stack.getTag().contains(SpellComponentItem.NBT_KEY_COMPONENT_DATA)) ) {
                if ( slate == null ) slate = stack;
            }
            if ( stack.getItem() instanceof PotionItem ) {
                if ( potion == null ) potion = PotionUtils.getPotion(stack);
            }
        }
        if ( items.size() == 2 && slate != null && potion != null ) {
            ItemStack newStack = new ItemStack(ModItems.ALCHEMY_SIGIL.get());
            CompoundTag tag = newStack.getOrCreateTag();
            StringBuilder stringBuilder = new StringBuilder();
            for ( int i = 0; i < potion.getEffects().size(); i++ ) {
                if ( i > 0 ) stringBuilder.append(" ");
                MobEffect effect = potion.getEffects().get(i).getEffect();
                stringBuilder.append(ForgeRegistries.MOB_EFFECTS.getKey(effect).toString());
            }
            tag.putString(SpellComponentItem.NBT_KEY_COMPONENT_DATA, stringBuilder.toString());
            returnStack = newStack;
        }
        return returnStack;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return pWidth * pHeight >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.ALCHEMY_EFFECT_ITEM_RECIPE.get();
    }
}
