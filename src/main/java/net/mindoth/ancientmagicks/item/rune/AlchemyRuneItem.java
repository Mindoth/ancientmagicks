package net.mindoth.ancientmagicks.item.rune;

import net.mindoth.ancientmagicks.event.SpellData;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.StringUtil;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.List;
import java.util.TreeMap;

public class AlchemyRuneItem extends UseOnEntityTemplate {
    public AlchemyRuneItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public boolean isEncodeable() {
        return true;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(Component.translatable("tooltip.ancientmagicks.entity").append(Component.literal(" ->")).withStyle(ChatFormatting.GRAY));
        if ( stack.hasTag() && stack.getTag().contains(NBT_KEY_COMPONENT_DATA) ) {
            CompoundTag tag = stack.getTag();
            List<String> dataList = List.of(tag.getString(NBT_KEY_COMPONENT_DATA).split(" "));
            for ( String string : dataList ) {
                List<String> values = List.of(string.split("/"));
                MobEffect effect = ForgeRegistries.MOB_EFFECTS.getValue(new ResourceLocation(values.get(0)));

                MutableComponent amp = Component.literal("");
                if ( Integer.parseInt(values.get(1)) > 0 ) amp = Component.literal(toRoman(1 + Integer.parseInt(values.get(1)))).append(Component.literal(" "));

                int i = Mth.floor((float)Integer.parseInt(values.get(2)));
                MutableComponent life = Component.literal(StringUtil.formatTickDuration(i));

                ChatFormatting color = ChatFormatting.BLUE;
                if ( !effect.isBeneficial() ) color = ChatFormatting.RED;
                tooltip.add(Component.translatable(effect.getDescriptionId()).append(Component.literal(" ")).append(amp)
                        .append(Component.literal("(")).append(life).append(Component.literal(")")).withStyle(color));
            }
        }
        super.appendHoverText(stack, world, tooltip, flagIn);
    }

    @Override
    protected SpellData result(Entity caster, SpellData spellData, Entity entity) {
        List<String> stringList = List.of(spellData.getDataList().get(0).split(" "));
        if ( entity instanceof LivingEntity living ) {
            for ( String string : stringList ) {
                List<String> values = List.of(string.split("/"));
                MobEffect effect = ForgeRegistries.MOB_EFFECTS.getValue(new ResourceLocation(values.get(0)));
                int amp = Integer.parseInt(values.get(1));
                int life = Integer.parseInt(values.get(2));
                living.addEffect(new MobEffectInstance(effect, life, amp, false, true));
            }
        }
        return spellData;
    }

    //Thanks again Stack Overflow
    //https://stackoverflow.com/questions/12967896/converting-integers-to-roman-numerals-java
    private final static TreeMap<Integer, String> map = new TreeMap<Integer, String>();
    static {
        map.put(1000, "M");
        map.put(900, "CM");
        map.put(500, "D");
        map.put(400, "CD");
        map.put(100, "C");
        map.put(90, "XC");
        map.put(50, "L");
        map.put(40, "XL");
        map.put(10, "X");
        map.put(9, "IX");
        map.put(5, "V");
        map.put(4, "IV");
        map.put(1, "I");
    }
    public final static String toRoman(int number) {
        int l =  map.floorKey(number);
        if ( number == l ) return map.get(number);
        return map.get(l) + toRoman(number-l);
    }
}
