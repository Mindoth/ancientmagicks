package net.mindoth.ancientmagicks.item.castingitem;

import net.mindoth.ancientmagicks.config.AncientMagicksCommonConfig;
import net.mindoth.ancientmagicks.item.RuneItem;
import net.mindoth.ancientmagicks.network.capabilities.playerspell.PlayerSpellProvider;
import net.mindoth.ancientmagicks.registries.AncientMagicksItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;

public class InvocationStaffItem extends CastingItem {
    public InvocationStaffItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    @Nonnull
    public InteractionResultHolder<ItemStack> use(Level level, Player player, @Nonnull InteractionHand handIn) {
        InteractionResultHolder<ItemStack> result = InteractionResultHolder.fail(player.getItemInHand(handIn));
        if ( !level.isClientSide ) {
            ItemStack staff = player.getItemInHand(handIn);
            if ( isValidCastingItem(staff) ) {
                player.getCapability(PlayerSpellProvider.PLAYER_SPELL).ifPresent(spell -> {
                    Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(spell.getSpell()));
                    if ( item instanceof TabletItem tabletItem && !player.isUsingItem() && !player.getCooldowns().isOnCooldown(tabletItem) ) {
                        if ( player.totalExperience >= tabletItem.tier || player.isCreative() || AncientMagicksCommonConfig.FREE_SPELLS.get() ) player.startUsingItem(handIn);
                        else {
                            addCastingCooldown(player, tabletItem, 20);
                            RuneItem.playWhiffSound(player);
                        }
                    }
                });
            }
        }
        return result;
    }

    @Override
    public void onUseTick(Level level, LivingEntity living, ItemStack wand, int timeLeft) {
        if ( level.isClientSide ) return;
        if ( living instanceof Player player ) {
            if ( isValidCastingItem(wand) ) {
                player.getCapability(PlayerSpellProvider.PLAYER_SPELL).ifPresent(spell -> {
                    Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(spell.getSpell()));
                    if ( item instanceof TabletItem tabletItem ) {
                        if ( getHeldSlateItem(player).getItem() == AncientMagicksItems.STONE_SLATE.get() ) makeTablets(player, player, tabletItem, getHeldSlateItem(player));
                        else doSpell(player, player, wand, tabletItem, getUseDuration(wand) - timeLeft);
                    }
                });
            }
        }
    }
}
