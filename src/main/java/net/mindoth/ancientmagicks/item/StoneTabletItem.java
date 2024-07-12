package net.mindoth.ancientmagicks.item;

import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.item.spell.projectile.AbstractSpellEntity;
import net.mindoth.ancientmagicks.item.spell.projectile.WitchSparkEntity;
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
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;

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
                if ( tablet.hasTag() && tablet.getTag() != null && tablet.getTag().contains("am_firemode") ) {
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
        if ( tablet.hasTag() && tag != null ) {
            if ( tag.contains("am_firemode") ) doSpell(player, living, tablet, tag);
        }
    }

    public static void doSpell(Player owner, Entity caster, ItemStack castingItem, CompoundTag tag) {
        Level level = caster.level();
        if ( tag.getString("am_firemode").equals("am_projectile") ) {
            int adjuster = -1;
            float down = 0.0F;
            if ( caster == owner ) {
                adjuster = 1;
                down = -0.2F;
                owner.stopUsingItem();
            }
            Vec3 center = caster.getEyePosition();
            float xRot = caster.getXRot();
            float yRot = caster.getYRot();

            AbstractSpellEntity projectile = new WitchSparkEntity(level, owner, caster);
            projectile.setColor(AbstractSpellEntity.getSpellColor("dark_purple"), 0.3F);
            projectile.setPos(center.x, center.y + down, center.z);
            projectile.anonShootFromRotation(xRot * adjuster, yRot * adjuster, 0F, Math.max(0, projectile.speed), 0.0F);
            level.addFreshEntity(projectile);
        }
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
        }

        super.appendHoverText(stack, world, tooltip, flagIn);
    }

    @SubscribeEvent
    public static void onAnvilRecipe(final AnvilUpdateEvent event) {
        ItemStack left = event.getLeft();
        ItemStack right = event.getRight();
        HashMap<Item, String> map = AncientMagicks.ANVIL_RECIPE_MAP;
        if ( left.getItem() == AncientMagicksItems.STONE_TABLET.get() && map.containsKey(right.getItem()) ) {
            ItemStack result = left.copy();
            CompoundTag tag = result.getOrCreateTag();
            if ( right.getItem() == Items.ARROW ) tag.putString("am_firemode", map.get(right.getItem()));
            else tag.putBoolean(ForgeRegistries.ITEMS.getKey(right.getItem()).toString(), true);
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
