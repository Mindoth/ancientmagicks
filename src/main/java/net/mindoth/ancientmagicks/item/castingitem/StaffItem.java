package net.mindoth.ancientmagicks.item.castingitem;

import net.mindoth.ancientmagicks.item.SpellItem;
import net.mindoth.ancientmagicks.network.capabilities.playermagic.PlayerMagicProvider;
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

public class StaffItem extends CastingItem {

    public StaffItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    @Nonnull
    public InteractionResultHolder<ItemStack> use(Level level, Player player, @Nonnull InteractionHand hand) {
        InteractionResultHolder<ItemStack> result = InteractionResultHolder.fail(player.getItemInHand(hand));
        if ( !level.isClientSide ) {
            ItemStack staff = player.getItemInHand(hand);
            if ( isValidCastingItem(staff) ) {
                player.getCapability(PlayerMagicProvider.PLAYER_MAGIC).ifPresent(magic -> {
                    Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(magic.getCurrentSpell()));
                    if ( item instanceof SpellItem spell && !player.isUsingItem() && !player.getCooldowns().isOnCooldown(spell) ) {
                        if ( magic.getCurrentMana() >= spell.manaCost || player.isCreative() ) player.startUsingItem(hand);
                        else {
                            addCastingCooldown(player, spell, 20);
                            SpellItem.playWhiffSound(player);
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
                player.getCapability(PlayerMagicProvider.PLAYER_MAGIC).ifPresent(magic -> {
                    Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(magic.getCurrentSpell()));
                    if ( item instanceof SpellItem spell && (magic.getCurrentMana() >= spell.manaCost || player.isCreative()) ) {
                        doSpell(player, player, wand, spell, getUseDuration(wand) - timeLeft);
                    }
                    else living.stopUsingItem();
                });
            }
        }
    }
}
