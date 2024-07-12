package net.mindoth.ancientmagicks.item;

import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.entity.projectile.AbstractSpellEntity;
import net.mindoth.ancientmagicks.entity.projectile.WitchSparkEntity;
import net.mindoth.ancientmagicks.item.glyph.GlyphItem;
import net.mindoth.ancientmagicks.registries.AncientMagicksItems;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber(modid = AncientMagicks.MOD_ID)
public class StoneTabletItem extends Item {
    public StoneTabletItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    @Nonnull
    public InteractionResultHolder<ItemStack> use(Level level, Player player, @Nonnull InteractionHand handIn) {
        InteractionResultHolder<ItemStack> result = InteractionResultHolder.fail(player.getItemInHand(handIn));
        if ( !level.isClientSide ) {
            ItemStack tablet = player.getItemInHand(handIn);
            if ( tablet.getItem() instanceof StoneTabletItem tabletItem && !player.isUsingItem() && !player.getCooldowns().isOnCooldown(tabletItem) ) {
                if ( tablet.hasTag() && tablet.getTag() != null ) {
                    player.startUsingItem(handIn);
                }
            }
        }
        return result;
    }

    @Override
    public void onUseTick(Level level, LivingEntity living, ItemStack tablet, int timeLeft) {
        if ( level.isClientSide || !(living instanceof Player player) ) return;
        CompoundTag tag = tablet.getTag();
        if ( tablet.hasTag() && tag != null ) doSpell(player, living, tag);
    }

    public static void doSpell(Player owner, Entity caster, CompoundTag tag) {
        Level level = caster.level();

        List<GlyphItem> glyphList = Lists.newArrayList();
        for ( GlyphItem glyph : AncientMagicks.GLYPH_LIST ) if ( tag.contains(glyph.tag) ) glyphList.add(glyph);
        String color = "purple";
        if ( tag.contains("am_color") ) color = tag.getString("am_color");

        if ( tag.contains("am_firemode") ) {
            if ( tag.getString("am_firemode").equals("am_projectile") ) {
                int adjuster = -1;
                float down = 0.0F;
                if ( caster == owner ) {
                    adjuster = 1;
                    down = -0.2F;
                }
                Vec3 center = caster.getEyePosition();
                float xRot = caster.getXRot();
                float yRot = caster.getYRot();
                AbstractSpellEntity projectile = new WitchSparkEntity(level, owner, caster, glyphList);
                projectile.setColor(AbstractSpellEntity.getSpellColor(color), 0.3F);
                projectile.setPos(center.x, center.y + down, center.z);
                projectile.anonShootFromRotation(xRot * adjuster, yRot * adjuster, 0F, Math.max(0, projectile.speed), 0.0F);
                level.addFreshEntity(projectile);
            }
        }
        else for ( GlyphItem glyph : glyphList ) glyph.onEntityHit(caster.level(), new EntityHitResult(caster));

        if ( caster == owner ) owner.stopUsingItem();
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flagIn) {
        if ( !stack.hasTag() || stack.getTag() == null ) return;
        else {
            CompoundTag tag = stack.getTag();
            if ( tag.contains("am_firemode") ) {
                tooltip.add(Component.translatable("tooltip.ancientmagicks." + tag.getString("am_firemode")).withStyle(ChatFormatting.GRAY) );
            }
            else tooltip.add(Component.translatable("tooltip.ancientmagicks.am_self").withStyle(ChatFormatting.GRAY));
            for ( Map.Entry<Item, String> entry : AncientMagicks.ANVIL_RECIPE_MAP.entrySet() ) {
                Item item = entry.getKey();
                String string = entry.getValue();
                if ( item instanceof GlyphItem && tag.contains(string) ) {
                    tooltip.add(Component.translatable("tooltip.ancientmagicks.am_has_glyph")
                            .append(Component.translatable(item.getDescriptionId())).withStyle(ChatFormatting.GRAY));
                }
            }
            if ( tag.contains("am_color") ) {
                tooltip.add(Component.translatable("tooltip.ancientmagicks.am_color")
                        .append(Component.literal(StringUtils.capitalize(tag.getString("am_color").replaceAll("_", " ")))).withStyle(ChatFormatting.GRAY));
            }
            else {
                tooltip.add(Component.translatable("tooltip.ancientmagicks.am_color")
                        .append(Component.literal("Purple")).withStyle(ChatFormatting.GRAY));
            }
        }

        super.appendHoverText(stack, world, tooltip, flagIn);
    }

    @SubscribeEvent
    public static void onAnvilRecipe(final AnvilUpdateEvent event) {
        ItemStack leftStack = event.getLeft();
        Item rightItem = event.getRight().getItem();
        HashMap<Item, String> map = AncientMagicks.ANVIL_RECIPE_MAP;
        if ( leftStack.getItem() == AncientMagicksItems.STONE_TABLET.get() && (map.containsKey(rightItem) || rightItem instanceof DyeItem) ) {
            ItemStack result = leftStack.copy();
            CompoundTag tag = result.getOrCreateTag();
            if ( rightItem instanceof DyeItem dyeItem ) tag.putString("am_color", dyeItem.getDyeColor().toString());
            else if ( rightItem == Items.ARROW ) tag.putString("am_firemode", map.get(rightItem));
            else if ( rightItem instanceof GlyphItem glyphItem ) tag.putBoolean(glyphItem.tag, true);
            result.setTag(tag);
            event.setOutput(result);
            event.setCost(1);
            event.setMaterialCost(1);
        }
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return 72000;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.BOW;
    }
}
