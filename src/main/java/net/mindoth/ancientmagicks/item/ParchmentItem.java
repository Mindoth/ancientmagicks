package net.mindoth.ancientmagicks.item;

import com.google.common.collect.Lists;
import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.event.CastingValidator;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.List;

public class ParchmentItem extends Item {

    public static final String NBT_KEY_SPELL_STRING = "am_spell_string";
    public static final String NBT_KEY_DATA_STRING = "am_encoding_string";
    public static final String NBT_KEY_CODE_STRING = "am_code_string";
    public static final String NBT_KEY_SPELL_NAME = "am_spell_name";
    public static final String NBT_KEY_PAPER_TIER = "am_paper_tier";

    private final int size;
    public int getSize() {
        return this.size;
    }

    public ParchmentItem(Properties pProperties, int size) {
        super(pProperties);
        this.size = size;
    }

    public static List<Item> getScrollComboList(ItemStack stack) {
        if ( !(stack.getItem() instanceof ParchmentItem) ) return null;
        if ( !stack.hasTag() || !stack.getTag().contains(NBT_KEY_CODE_STRING) ) return null;
        CompoundTag tag = stack.getTag();
        List<String> codeList = List.of(tag.getString(NBT_KEY_CODE_STRING).split(","));
        List<Item> runes = Lists.newArrayList();
        for ( String string : codeList ) {
            Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(string));
            if ( item instanceof ColorRuneItem colorRuneItem ) runes.add(colorRuneItem);
        }
        if ( codeList.size() == AncientMagicks.comboSizeCalc() && codeList.size() == codeList.size() ) return runes;
        else return null;
    }

    /*
    //ONLY FOR TESTING
    @Override
    @Nonnull
    public InteractionResultHolder<ItemStack> use(Level level, Player player, @Nonnull InteractionHand hand) {
        InteractionResultHolder<ItemStack> result = InteractionResultHolder.fail(player.getItemInHand(hand));
        if ( !level.isClientSide ) {
            ItemStack stack = player.getItemInHand(hand);
            if ( stack.hasTag() && stack.getTag().contains(NBT_KEY_CODE_STRING) ) CastingValidator.castMagick(player, stack);
        }
        return result;
    }*/
}
