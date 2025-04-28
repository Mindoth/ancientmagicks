package net.mindoth.ancientmagicks.item.form;

import com.google.common.collect.Lists;
import net.mindoth.ancientmagicks.item.ComponentItem;
import net.mindoth.ancientmagicks.item.effect.SpellEffectItem;
import net.mindoth.ancientmagicks.item.modifier.SpellModifierItem;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;

public class LocationFormItem extends SpellFormItem {

    public LocationFormItem(Properties pProperties, int manaCost, int cooldown) {
        super(pProperties, manaCost, cooldown);
    }

    @Override
    public boolean isEncodeable() {
        return true;
    }

    @Override
    public boolean formSpell(LivingEntity owner, Entity caster, List<ComponentItem> spellStack, List<String> data) {
        Level level = caster.level();
        float xPos = 0;
        float yPos = 0;
        float zPos = 0;

        List<ComponentItem> newList = Lists.newArrayList();
        List<String> newData = Lists.newArrayList();
        List<SpellModifierItem> formModifiers = Lists.newArrayList();
        boolean form = false;
        for ( int i = 0; i < spellStack.size(); i++ ) {
            ComponentItem item = spellStack.get(i);
            if ( !form ) {
                if ( item instanceof SpellFormItem ) {
                    form = true;
                    List<String> stringList = List.of(data.get(i).split(" "));
                    level = level.getServer().getLevel(
                            ResourceKey.create(ResourceKey.createRegistryKey(new ResourceLocation(stringList.get(0))),
                                    new ResourceLocation(stringList.get(1)))
                    );
                    xPos = Float.valueOf(stringList.get(2));
                    yPos = Float.valueOf(stringList.get(3));
                    zPos = Float.valueOf(stringList.get(4));
                }
                else if ( item instanceof SpellModifierItem modifier ) formModifiers.add(modifier);
            }
            else {
                newList.add(item);
                newData.add(data.get(i));
            }
        }
        HashMap<String, Float> formStats = SpellEffectItem.createSpellStats(formModifiers);
        float aoe = formStats.get(AOE);
        Vec3 vecPos = new Vec3(xPos, yPos, zPos);
        BlockPos blockPos = new BlockPos(Mth.floor(xPos), Mth.floor(yPos), Mth.floor(zPos));
        List<Entity> list = level.getEntities(null, new AABB(blockPos));
        HitResult hitResult;
        System.out.println(blockPos.getY());
        if ( list.isEmpty() ) hitResult = new BlockHitResult(vecPos, Direction.DOWN, blockPos.above(), false);
        else hitResult = new EntityHitResult(list.get(0), vecPos);

        List<SpellModifierItem> modifiers = Lists.newArrayList();
        HashMap<String, Float> stats = SpellEffectItem.createDefaultStats();
        List<Boolean> boolist = Lists.newArrayList();
        for ( int i = 0; i < newList.size(); i++ ) {
            ComponentItem item = newList.get(i);
            if ( item instanceof SpellModifierItem modifier ) modifiers.add(modifier);
            if ( item instanceof SpellEffectItem effect ) {
                for ( SpellModifierItem modifier : modifiers ) modifier.addStatsToMap(stats);
                boolist.add(effect.castSpell(level, owner, caster, hitResult, aoe, stats, newData.get(i)));
                modifiers = Lists.newArrayList();
                stats = SpellEffectItem.createDefaultStats();
            }
        }
        for ( boolean bool : boolist ) if ( bool ) return true;
        return false;
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
                String string = dataList.get(i);
                String header = "";
                if ( i == 1 )  {
                    string = new ResourceLocation(string).getPath();
                    header = "";
                }
                else if ( i == 2 ) header = "x: ";
                else if ( i == 3 ) header = "y: ";
                else if ( i == 4 ) header = "z: ";
                tooltip.add(Component.literal(header).append(Component.literal(string)).withStyle(ChatFormatting.GRAY));
            }
        }
        else tooltip.add(Component.translatable("tooltip.ancientmagicks." + stack.getItem()).withStyle(ChatFormatting.GRAY));
    }
}
