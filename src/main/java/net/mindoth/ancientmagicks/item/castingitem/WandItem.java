package net.mindoth.ancientmagicks.item.castingitem;

import net.mindoth.ancientmagicks.capabilities.playermagic.PlayerMagicProvider;
import net.mindoth.ancientmagicks.item.spell.abstractspell.SpellItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Vanishable;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;

public class WandItem extends SpellStorageItem implements Vanishable {

    public WandItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public boolean isEnchantable(@Nonnull ItemStack stack) {
        return true;
    }

    @Override
    public void onUseTick(Level level, LivingEntity living, ItemStack wand, int timeLeft) {
        if ( level.isClientSide ) return;
        if ( living instanceof Player player ) {
            if ( isValidCastingItem(wand) ) {
                player.getCapability(PlayerMagicProvider.PLAYER_MAGIC).ifPresent(magic -> {
                    Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(magic.getCurrentSpell()));
                    if ( item instanceof SpellItem spell ) doSpell(player, player, wand, spell, getUseDuration(wand) - timeLeft);
                    else living.stopUsingItem();
                });
            }
        }
    }
}
