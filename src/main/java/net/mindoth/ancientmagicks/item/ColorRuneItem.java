package net.mindoth.ancientmagicks.item;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.network.capabilities.playerspell.PlayerSpell;
import net.mindoth.ancientmagicks.network.capabilities.playerspell.PlayerSpellProvider;
import net.mindoth.ancientmagicks.registries.AncientMagicksItems;
import net.mindoth.shadowizardlib.event.ShadowEvents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber(modid = AncientMagicks.MOD_ID)
public class ColorRuneItem extends RuneItem {
    public String color;

    public ColorRuneItem(Properties pProperties, String color) {
        super(pProperties);
        this.color = color;
    }

    //Serverside string to send in packets. It being a CompoundTag is just a workaround...
    public static CompoundTag CURRENT_COMBO_TAG = new CompoundTag();
    //Server- AND Client-sided Map used in GUI checks
    public static HashMap<SpellTabletItem, List<ColorRuneItem>> CURRENT_COMBO_MAP = new HashMap<>();

    public static SpellTabletItem checkForSpellCombo(List<ColorRuneItem> comboToCheck, @Nullable SpellTabletItem secretSpell) {
        SpellTabletItem spell = null;
        if ( secretSpell == null ) {
            for ( Map.Entry<SpellTabletItem, List<ColorRuneItem>> entry : CURRENT_COMBO_MAP.entrySet() ) {
                SpellTabletItem key = entry.getKey();
                List<ColorRuneItem> value = entry.getValue();
                if ( AncientMagicks.listsMatch(comboToCheck, value) ) spell = key;
            }
        }
        else if ( AncientMagicks.listsMatch(comboToCheck, CURRENT_COMBO_MAP.get(secretSpell)) ) spell = secretSpell;
        return spell;
    }

    //This is some REALLY delicate String parsing. I'm no expert...
    public static HashMap<SpellTabletItem, List<ColorRuneItem>> buildComboMap(String comboString) {
        HashMap<SpellTabletItem, List<ColorRuneItem>> returnMap = new HashMap<>();

        Map<String, String> tempMap = Splitter.on(";").withKeyValueSeparator("=").split(comboString);
        for ( Map.Entry<String, String> entry : tempMap.entrySet() ) {
            SpellTabletItem key = (SpellTabletItem)ForgeRegistries.ITEMS.getValue(new ResourceLocation(entry.getKey()));
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

    @Override
    public int getUseDuration(ItemStack pStack) {
        return 32;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.EAT;
    }

    @Override
    @Nonnull
    public InteractionResultHolder<ItemStack> use(Level level, Player player, @Nonnull InteractionHand handIn) {
        InteractionResultHolder<ItemStack> result = InteractionResultHolder.fail(player.getItemInHand(handIn));
        if ( !level.isClientSide ) {
            if ( !player.isUsingItem() ) player.startUsingItem(handIn);
        }
        return result;
    }

    @SubscribeEvent
    public static void onRuneConsume(final LivingEntityUseItemEvent.Finish event) {
        if ( event.getEntity() instanceof Player player ) {
            if ( !player.level().isClientSide ) {
                ItemStack stack = event.getItem();
                if ( stack.getItem() instanceof ColorRuneItem ) {
                    LazyOptional<PlayerSpell> cap = player.getCapability(PlayerSpellProvider.PLAYER_SPELL);
                    if ( stack.getItem() == AncientMagicksItems.BLUE_RUNE.get() ) cap.ifPresent(rune -> rune.setBlue(!rune.getBlue()));
                    if ( stack.getItem() == AncientMagicksItems.PURPLE_RUNE.get() ) cap.ifPresent(rune -> rune.setPurple(!rune.getPurple()));
                    if ( stack.getItem() == AncientMagicksItems.YELLOW_RUNE.get() ) cap.ifPresent(rune -> rune.setYellow(!rune.getYellow()));
                    if ( stack.getItem() == AncientMagicksItems.GREEN_RUNE.get() ) cap.ifPresent(rune -> rune.setGreen(!rune.getGreen()));
                    if ( stack.getItem() == AncientMagicksItems.BLACK_RUNE.get() ) cap.ifPresent(rune -> rune.setBlack(!rune.getBlack()));
                    if ( stack.getItem() == AncientMagicksItems.WHITE_RUNE.get() ) cap.ifPresent(rune -> rune.setWhite(!rune.getWhite()));
                    if ( stack.getItem() == AncientMagicksItems.BROWN_RUNE.get() ) cap.ifPresent(rune -> rune.setBrown(!rune.getBrown()));
                    if ( stack.getItem() == AncientMagicksItems.RED_RUNE.get() ) cap.ifPresent(rune -> rune.setRed(!rune.getRed()));
                    if ( !player.isCreative() ) event.getResultStack().shrink(1);
                    Vec3 center = ShadowEvents.getEntityCenter(event.getEntity());
                    player.level().playSound(null, center.x, center.y, center.z, SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 0.5F, 0.25F);
                }
            }
        }
    }

    public static List<ItemStack> getColorRuneList(Player player, PlayerSpell spell) {
        List<ItemStack> runeList = Lists.newArrayList();
        if ( (spell.getBlue() || player.isCreative()) && AncientMagicks.isColorRuneEnabled(AncientMagicksItems.BLUE_RUNE.get()) ) {
            runeList.add(new ItemStack(AncientMagicksItems.BLUE_RUNE.get()));
        }
        if ( (spell.getPurple() || player.isCreative()) && AncientMagicks.isColorRuneEnabled(AncientMagicksItems.PURPLE_RUNE.get()) ) {
            runeList.add(new ItemStack(AncientMagicksItems.PURPLE_RUNE.get()));
        }
        if ( (spell.getYellow() || player.isCreative()) && AncientMagicks.isColorRuneEnabled(AncientMagicksItems.YELLOW_RUNE.get()) ) {
            runeList.add(new ItemStack(AncientMagicksItems.YELLOW_RUNE.get()));
        }
        if ( (spell.getGreen() || player.isCreative()) && AncientMagicks.isColorRuneEnabled(AncientMagicksItems.GREEN_RUNE.get()) ) {
            runeList.add(new ItemStack(AncientMagicksItems.GREEN_RUNE.get()));
        }
        if ( (spell.getBlack() || player.isCreative()) && AncientMagicks.isColorRuneEnabled(AncientMagicksItems.BLACK_RUNE.get()) ) {
            runeList.add(new ItemStack(AncientMagicksItems.BLACK_RUNE.get()));
        }
        if ( (spell.getWhite() || player.isCreative()) && AncientMagicks.isColorRuneEnabled(AncientMagicksItems.WHITE_RUNE.get()) ) {
            runeList.add(new ItemStack(AncientMagicksItems.WHITE_RUNE.get()));
        }
        if ( (spell.getBrown() || player.isCreative()) && AncientMagicks.isColorRuneEnabled(AncientMagicksItems.BROWN_RUNE.get()) ) {
            runeList.add(new ItemStack(AncientMagicksItems.BROWN_RUNE.get()));
        }
        if ( (spell.getRed() || player.isCreative()) && AncientMagicks.isColorRuneEnabled(AncientMagicksItems.RED_RUNE.get()) ) {
            runeList.add(new ItemStack(AncientMagicksItems.RED_RUNE.get()));
        }
        return runeList;
    }
}
