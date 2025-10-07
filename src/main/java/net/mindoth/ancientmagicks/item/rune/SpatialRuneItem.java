package net.mindoth.ancientmagicks.item.rune;

import net.mindoth.ancientmagicks.event.DimVec3;
import net.mindoth.ancientmagicks.event.SpellData;
import net.mindoth.ancientmagicks.item.RuneItem;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class SpatialRuneItem extends RuneItem {
    public SpatialRuneItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public boolean isEncodeable() {
        return true;
    }

    //TODO Set dimension name from lang file and round coordinates in this tooltip
    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(Component.literal("-> ").append(Component.translatable("tooltip.ancientmagicks.position")).withStyle(ChatFormatting.GRAY));
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
                tooltip.add(Component.literal(header).append(Component.literal(string)).withStyle(ChatFormatting.BLUE));
            }
        }
        super.appendHoverText(stack, world, tooltip, flagIn);
    }

    @Override
    public SpellData resolve(Entity caster, SpellData spellData) {
        List<String> stringList = List.of(spellData.getDataList().get(0).split(" "));
        if ( caster != null && stringList.size() == 5 ) {
            Level level = caster.level().getServer().getLevel(
                    ResourceKey.create(ResourceKey.createRegistryKey(new ResourceLocation(stringList.get(0))),
                            new ResourceLocation(stringList.get(1))));
            Vec3 pos = new Vec3(Float.parseFloat(stringList.get(2)), Float.parseFloat(stringList.get(3)), Float.parseFloat(stringList.get(4)));
            spellData.addObject(new DimVec3(pos, level));
        }
        else spellData.addObject(null);
        return spellData;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, @Nonnull InteractionHand handIn) {
        InteractionResultHolder<ItemStack> result = InteractionResultHolder.fail(player.getItemInHand(handIn));
        if ( !level.isClientSide ) {
            ItemStack stack = player.getItemInHand(handIn);
            CompoundTag tag = stack.getOrCreateTag();
            StringBuilder stringBuilder = new StringBuilder();
            String xPos = String.valueOf(player.position().x);
            String yPos = String.valueOf(player.position().y);
            String zPos = String.valueOf(player.position().z);
            stringBuilder.append(player.level().dimension().registry()).append(" ").append(player.level().dimension().location()).append(" ").append(xPos).append(" ").append(yPos).append(" ").append(zPos);
            tag.putString(RuneItem.NBT_KEY_COMPONENT_DATA, stringBuilder.toString());
        }
        return result;
    }
}
