package net.mindoth.ancientmagicks.item.effect;

import com.google.common.collect.Lists;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.List;

public class AlchemyEffectItem extends PotionEffectItem {

    public AlchemyEffectItem(Properties pProperties, int manaCost, int cooldown) {
        super(pProperties, manaCost, cooldown);
    }

    @Override
    public boolean isEncodeable() {
        return true;
    }

    @Override
    public boolean isHarmful(@Nullable String data) {
        if ( data == null || data.isEmpty() ) return true;
        for ( MobEffect effect : getEffects(data) ) if ( !effect.isBeneficial() ) return true;
        return false;
    }

    @Override
    protected List<MobEffect> getEffects(String data) {
        List<MobEffect> effects = Lists.newArrayList();
        for ( String string : List.of(data.split(" ")) ) {
            MobEffect effect = ForgeRegistries.MOB_EFFECTS.getValue(new ResourceLocation(string));
            if ( effect != null ) effects.add(effect);
        }
        return effects;
    }

    @Override
    public void decodeTooltipData(List<Component> tooltip, String data, String key, Item item) {
        List<String> stringList = List.of(data.split(" "));
        StringBuilder stringBuilder = new StringBuilder();
        for ( int i = 0; i < stringList.size(); i++ ) {
            if ( i > 0 ) stringBuilder.append(", ");
            String string = ForgeRegistries.MOB_EFFECTS.getValue(new ResourceLocation(stringList.get(i))).getDescriptionId();
            stringBuilder.append(I18n.get(string));
        }

        tooltip.add(Component.translatable(key)
                .append(Component.translatable(item.getDescriptionId()))
                .append(Component.literal(": "))
                .append(Component.literal(stringBuilder.toString()))
                .withStyle(ChatFormatting.GRAY));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flagIn) {
        if ( stack.hasTag() && stack.getTag().contains(NBT_KEY_COMPONENT_DATA) ) {
            CompoundTag tag = stack.getTag();
            List<String> dataList = List.of(tag.getString(NBT_KEY_COMPONENT_DATA).split(" "));
            for ( String string : dataList ) {
                String effect = ForgeRegistries.MOB_EFFECTS.getValue(new ResourceLocation(string)).getDescriptionId();
                tooltip.add(Component.translatable(effect).withStyle(ChatFormatting.GRAY));
            }
        }
        else tooltip.add(Component.translatable("tooltip.ancientmagicks." + stack.getItem()).withStyle(ChatFormatting.GRAY));
    }
}
