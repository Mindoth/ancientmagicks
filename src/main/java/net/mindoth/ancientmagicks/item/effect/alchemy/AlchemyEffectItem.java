package net.mindoth.ancientmagicks.item.effect.alchemy;

import net.mindoth.ancientmagicks.item.effect.EntityTargetEffect;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;

public class AlchemyEffectItem extends EntityTargetEffect {

    public AlchemyEffectItem(Properties pProperties, int manaCost, int cooldown) {
        super(pProperties, manaCost, cooldown);
    }

    @Override
    public boolean isEncodeable() {
        return true;
    }

    @Override
    protected boolean doSpell(Level level, LivingEntity owner, Entity caster, HitResult result, HashMap<String, Float> stats, String data) {
        LivingEntity target = (LivingEntity)((EntityHitResult)result).getEntity();
        addEnchantParticles(target, getParticleColor().r, getParticleColor().g, getParticleColor().b, 0.15F, 8);
        int amp = Mth.floor(Math.max(0, (stats.get(POWER) - 1) / 4));
        int life = 600 * Mth.floor(stats.get(LIFE));
        for ( String string : List.of(data.split(" ")) ) {
            MobEffect effect = ForgeRegistries.MOB_EFFECTS.getValue(new ResourceLocation(string));
            if ( effect != null ) {
                if ( effect.isInstantenous() ) life = 1;
                target.addEffect(new MobEffectInstance(effect, life, amp, false, !effect.isBeneficial()));
            }
        }
        return true;
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
        else tooltip.add(Component.translatable("tooltip.ancientmagicks.empty").withStyle(ChatFormatting.GRAY));
    }
}
