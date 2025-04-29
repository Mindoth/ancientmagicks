package net.mindoth.ancientmagicks.item.modifier;

import net.mindoth.ancientmagicks.item.ComponentItem;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;

public class LocationModifierItem extends SpellModifierItem {

    public LocationModifierItem(Properties pProperties, int manaCost, int cooldown) {
        super(pProperties, manaCost, cooldown);
    }

    @Override
    public boolean isEncodeable() {
        return true;
    }

    @Override
    public boolean usableWithForms() {
        return false;
    }

    @Override
    public boolean usableWithEffects() {
        return true;
    }

    @Override
    public EncodeableData addDataFromEncodeable(String data, Level level, Vec3 posVec) {
        List<String> stringList = List.of(data.split(" "));
        level = level.getServer().getLevel(
                ResourceKey.create(ResourceKey.createRegistryKey(new ResourceLocation(stringList.get(0))),
                        new ResourceLocation(stringList.get(1)))
        );
        posVec = new Vec3(Float.parseFloat(stringList.get(2)), Float.parseFloat(stringList.get(3)), Float.parseFloat(stringList.get(4)));
        return new EncodeableData(level, posVec);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, @Nonnull InteractionHand handIn) {
        InteractionResultHolder<ItemStack> result = InteractionResultHolder.fail(player.getItemInHand(handIn));
        if ( !level.isClientSide ) {
            ItemStack stack = player.getItemInHand(handIn);
            CompoundTag tag = stack.getOrCreateTag();
            if ( player.isCrouching() ) tag.remove(ComponentItem.NBT_KEY_COMPONENT_DATA);
            else {
                StringBuilder stringBuilder = new StringBuilder();
                String xPos = String.valueOf(player.position().x);
                String yPos = String.valueOf(player.position().y);
                String zPos = String.valueOf(player.position().z);
                stringBuilder.append(player.level().dimension().registry()).append(" ").append(player.level().dimension().location()).append(" ").append(xPos).append(" ").append(yPos).append(" ").append(zPos);
                tag.putString(ComponentItem.NBT_KEY_COMPONENT_DATA, stringBuilder.toString());
            }
        }
        return result;
    }

    @Override
    public void decodeTooltipData(List<Component> tooltip, String data, String key, Item item) {
        List<String> stringList = List.of(data.split(" "));

        tooltip.add(Component.translatable(key)
                .append(Component.translatable(item.getDescriptionId()))
                .append(Component.literal(": "))
                .append(Component.literal(new ResourceLocation(stringList.get(1)).getPath()))
                .withStyle(ChatFormatting.GRAY));
    }

    //TODO Set dimension name from lang file and round coordinates in this tooltip
    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flagIn) {
        if ( stack.hasTag() && stack.getTag().contains(NBT_KEY_COMPONENT_DATA) ) {
            CompoundTag tag = stack.getTag();
            List<String> dataList = List.of(tag.getString(NBT_KEY_COMPONENT_DATA).split(" "));
            for ( int i = 1; i < dataList.size(); i++ ) {
                String header = "";
                String string = dataList.get(i);
                if ( i == 1 )  {
                    string = new ResourceLocation(string).getPath();
                    header = "";
                }
                else {
                    string = String.valueOf(Math.round(Float.parseFloat(string)));
                    if ( i == 2 ) header = "x: ";
                    else if ( i == 3 ) header = "y: ";
                    else if ( i == 4 ) header = "z: ";
                }
                tooltip.add(Component.literal(header).append(Component.literal(string)).withStyle(ChatFormatting.GRAY));
            }
        }
        else tooltip.add(Component.translatable("tooltip.ancientmagicks." + stack.getItem()).withStyle(ChatFormatting.GRAY));
    }
}
