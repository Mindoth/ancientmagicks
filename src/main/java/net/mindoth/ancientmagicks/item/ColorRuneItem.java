package net.mindoth.ancientmagicks.item;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.item.spell.abstractspell.SpellItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber(modid = AncientMagicks.MOD_ID)
public class ColorRuneItem extends Item {
    public String color;

    public ColorRuneItem(Properties pProperties, String color) {
        super(pProperties);
        this.color = color;
    }

    //Serverside string to send in packets. It being a CompoundTag is just a workaround...
    public static CompoundTag CURRENT_COMBO_TAG = new CompoundTag();
    //Server- AND Client-sided Map used in GUI checks
    public static HashMap<SpellItem, List<ColorRuneItem>> CURRENT_COMBO_MAP = new HashMap<>();

    @Nullable
    public static SpellItem checkForSpellCombo(List<ColorRuneItem> comboToCheck/*, @Nullable SpellItem secretSpell*/) {
        for ( Map.Entry<SpellItem, List<ColorRuneItem>> entry : CURRENT_COMBO_MAP.entrySet() ) {
            SpellItem key = entry.getKey();
            List<ColorRuneItem> value = entry.getValue();
            if ( AncientMagicks.listsMatch(comboToCheck, value) ) return key;
        }
        return null;
        /*SpellItem spell = null;
        if ( secretSpell == null ) {
            for ( Map.Entry<SpellItem, List<ColorRuneItem>> entry : CURRENT_COMBO_MAP.entrySet() ) {
                SpellItem key = entry.getKey();
                List<ColorRuneItem> value = entry.getValue();
                if ( AncientMagicks.listsMatch(comboToCheck, value) ) spell = key;
            }
        }
        else if ( AncientMagicks.listsMatch(comboToCheck, CURRENT_COMBO_MAP.get(secretSpell)) ) spell = secretSpell;
        return spell;*/
    }

    //This is some REALLY delicate String parsing. I'm no expert...
    public static HashMap<SpellItem, List<ColorRuneItem>> buildComboMap(String comboString) {
        HashMap<SpellItem, List<ColorRuneItem>> returnMap = new HashMap<>();

        Map<String, String> tempMap = Splitter.on(";").withKeyValueSeparator("=").split(comboString);
        for ( Map.Entry<String, String> entry : tempMap.entrySet() ) {
            SpellItem key = (SpellItem)ForgeRegistries.ITEMS.getValue(new ResourceLocation(entry.getKey()));
            if ( AncientMagicks.isSpellEnabled(key) ) {
                List<ColorRuneItem> tempList = Lists.newArrayList();
                for ( String string : List.of(entry.getValue().replaceAll("[\\[\\]]", "").split(",")) ) {
                    tempList.add((ColorRuneItem)ForgeRegistries.ITEMS.getValue(new ResourceLocation(AncientMagicks.MOD_ID, string.replaceAll(" ", ""))));
                }
                returnMap.put(key, tempList);
            }
        }

        return returnMap;
    }

    public static List<ColorRuneItem> stringListToActualList(String comboString) {
        List<ColorRuneItem> tempList = Lists.newArrayList();
        for ( String string : List.of(comboString.replaceAll("[\\[\\]]", "").split(",")) ) {
            tempList.add((ColorRuneItem)ForgeRegistries.ITEMS.getValue(new ResourceLocation(AncientMagicks.MOD_ID, string.replaceAll(" ", ""))));
        }
        return tempList;
    }

    /*@Override
    public int getUseDuration(ItemStack pStack) {
        return 32;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.EAT;
    }*/

    /*@Override
    @Nonnull
    public InteractionResultHolder<ItemStack> use(Level level, Player player, @Nonnull InteractionHand handIn) {
        InteractionResultHolder<ItemStack> result = InteractionResultHolder.fail(player.getItemInHand(handIn));
        if ( !level.isClientSide ) {
            if ( !player.isUsingItem() ) player.startUsingItem(handIn);
        }
        return result;
    }*/

    /*@SubscribeEvent
    public static void onRuneConsume(final LivingEntityUseItemEvent.Finish event) {
        if ( event.getEntity() instanceof Player player ) {
            if ( !player.level().isClientSide ) {
                ItemStack stack = event.getItem();
                if ( stack.getItem() instanceof ColorRuneItem ) {
                    CompoundTag playerData = player.getPersistentData();
                    CompoundTag data = playerData.getCompound(Player.PERSISTED_NBT_TAG);

                    data.putBoolean(stack.getItem().toString(), !data.getBoolean(stack.getItem().toString()));
                    playerData.put(Player.PERSISTED_NBT_TAG, data);

                    if ( !player.isCreative() ) event.getResultStack().shrink(1);
                    Vec3 center = ShadowEvents.getEntityCenter(event.getEntity());
                    player.level().playSound(null, center.x, center.y, center.z, SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 0.5F, 0.25F);
                }
            }
        }
    }*/

    public static List<ItemStack> getColorRuneList(Player player) {
        List<ItemStack> runeList = Lists.newArrayList();
        for ( ColorRuneItem rune : AncientMagicks.COLOR_RUNE_LIST ) runeList.add(new ItemStack(rune));
        /*CompoundTag playerData = player.getPersistentData();
        CompoundTag data = playerData.getCompound(Player.PERSISTED_NBT_TAG);*/
        /*if ( data.getBoolean("blue_rune") || player.isCreative() ) runeList.add(new ItemStack(AncientMagicksItems.BLUE_RUNE.get()));
        if ( data.getBoolean("purple_rune") || player.isCreative() ) runeList.add(new ItemStack(AncientMagicksItems.PURPLE_RUNE.get()));
        if ( data.getBoolean("yellow_rune") || player.isCreative() ) runeList.add(new ItemStack(AncientMagicksItems.YELLOW_RUNE.get()));
        if ( data.getBoolean("green_rune") || player.isCreative() ) runeList.add(new ItemStack(AncientMagicksItems.GREEN_RUNE.get()));
        if ( data.getBoolean("black_rune") || player.isCreative() ) runeList.add(new ItemStack(AncientMagicksItems.BLACK_RUNE.get()));
        if ( data.getBoolean("white_rune") || player.isCreative() ) runeList.add(new ItemStack(AncientMagicksItems.WHITE_RUNE.get()));*/
        return runeList;
    }
}
