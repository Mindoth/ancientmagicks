package net.mindoth.ancientmagicks.item;

import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.client.particle.ParticleColor;
import net.mindoth.ancientmagicks.entity.projectile.AbstractSpell;
import net.mindoth.ancientmagicks.entity.projectile.WitchSpark;
import net.mindoth.ancientmagicks.network.AncientMagicksNetwork;
import net.mindoth.ancientmagicks.network.PacketSpellHitBurst;
import net.mindoth.ancientmagicks.registries.AncientMagicksItems;
import net.mindoth.shadowizardlib.event.ShadowEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber(modid = AncientMagicks.MOD_ID)
public class TabletItem extends Item {
    public TabletItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    @Nonnull
    public InteractionResultHolder<ItemStack> use(Level level, Player player, @Nonnull InteractionHand handIn) {
        InteractionResultHolder<ItemStack> result = InteractionResultHolder.fail(player.getItemInHand(handIn));
        if ( !level.isClientSide ) {
            ItemStack tablet = player.getItemInHand(handIn);
            if ( tablet.getItem() instanceof TabletItem tabletItem && !player.isUsingItem() && !player.getCooldowns().isOnCooldown(tabletItem) ) {
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
        for ( GlyphItem glyph : AncientMagicks.GLYPH_LIST ) if ( tag.contains(ForgeRegistries.ITEMS.getKey(glyph).toString()) ) glyphList.add(glyph);
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
                AbstractSpell projectile = new WitchSpark(level, owner, caster, glyphList);
                projectile.setColor(AbstractSpell.getSpellColor(color), 0.3F);
                projectile.setPos(center.x, center.y + down, center.z);
                projectile.anonShootFromRotation(xRot * adjuster, yRot * adjuster, 0F, Math.max(0, projectile.speed), 0.0F);
                level.addFreshEntity(projectile);
            }
            else if ( tag.getString("am_firemode").equals("am_self") ) {
                for ( GlyphItem glyph : glyphList ) glyph.onEntityHit(null, owner, new EntityHitResult(caster));
                ParticleColor particleColor = AbstractSpell.getSpellColor(color).toParticleColor();
                int r = (int)(particleColor.getRed() * 255F);
                int g = (int)(particleColor.getGreen() * 255F);
                int b = (int)(particleColor.getBlue() * 255F);
                Vec3 pos = ShadowEvents.getEntityCenter(caster);
                AncientMagicksNetwork.sendToNearby(level, caster, new PacketSpellHitBurst(r, g, b, 0.15F,
                        4 + level.random.nextInt(10), true, true, pos.x, pos.y, pos.z));
            }
            else if ( tag.getString("am_firemode").equals("am_touch") ) {
            }
        }

        if ( caster == owner ) owner.stopUsingItem();
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flagIn) {
        if ( !stack.hasTag() || stack.getTag() == null ) return;
        else {
            if ( !Screen.hasShiftDown() ) {
                tooltip.add(Component.translatable("tooltip.ancientmagicks.hold_shift").withStyle(ChatFormatting.GRAY));
            }
            if ( Screen.hasShiftDown() ) {
                CompoundTag tag = stack.getTag();
                if ( tag.contains("am_firemode") ) {
                    tooltip.add(Component.translatable("tooltip.ancientmagicks." + tag.getString("am_firemode")).withStyle(ChatFormatting.GRAY));
                }
                if ( tag.contains("am_color") ) {
                    tooltip.add(Component.translatable("tooltip.ancientmagicks.am_color")
                            .append(Component.literal(StringUtils.capitalize(tag.getString("am_color")
                                    .replaceAll("_", " ")))).withStyle(ChatFormatting.GRAY));
                }
                else {
                    tooltip.add(Component.translatable("tooltip.ancientmagicks.am_color")
                            .append(Component.literal("Purple")).withStyle(ChatFormatting.GRAY));
                }
                for ( Map.Entry<Item, String> entry : AncientMagicks.ANVIL_RECIPE_MAP.entrySet() ) {
                    Item item = entry.getKey();
                    String string = entry.getValue();
                    if ( item instanceof GlyphItem && tag.contains(string) ) {
                        tooltip.add(Component.translatable(item.getDescriptionId()).withStyle(ChatFormatting.GRAY));
                    }
                }
            }
        }

        super.appendHoverText(stack, world, tooltip, flagIn);
    }

    @SubscribeEvent
    public static void onAnvilRecipe(final AnvilUpdateEvent event) {
        ItemStack leftStack = event.getLeft();
        Item rightItem = event.getRight().getItem();
        HashMap<Item, String> map = AncientMagicks.ANVIL_RECIPE_MAP;
        if ( leftStack.getItem() == AncientMagicksItems.STONE_TABLET.get() ) {
            int xpCost = 1;
            if ( (map.containsKey(rightItem) || rightItem instanceof DyeItem) ) {
                ItemStack result = leftStack.copy();
                CompoundTag tag = result.getOrCreateTag();
                if ( rightItem == Items.ARROW || rightItem == Items.GLASS_BOTTLE ) {
                    tag.putString("am_firemode", map.get(rightItem));
                    handleCustomAnvil(event, leftStack, result, tag, xpCost);
                }
                else if ( tag.contains("am_firemode") ) {
                    if ( rightItem instanceof DyeItem dyeItem ) tag.putString("am_color", dyeItem.getDyeColor().toString());
                    else if ( rightItem instanceof GlyphItem glyph ) tag.putBoolean(ForgeRegistries.ITEMS.getKey(glyph).toString(), true);
                    handleCustomAnvil(event, leftStack, result, tag, xpCost);
                }
            }
        }
    }

    private static void handleCustomAnvil(AnvilUpdateEvent event, ItemStack leftStack, ItemStack result, CompoundTag tag, int xpCost) {
        result.setTag(tag);

        if ( event.getName() != null && !Util.isBlank(event.getName()) ) {
            if ( !event.getName().equals(leftStack.getHoverName().getString()) ) {
                result.setHoverName(Component.literal(event.getName()));
            }
        }
        else if ( leftStack.hasCustomHoverName() ) {
            result.resetHoverName();
        }
        event.setMaterialCost(1);
        event.setOutput(result);
        event.setCost(xpCost);
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
