package net.mindoth.ancientmagicks.item.castingitem;

import net.mindoth.ancientmagicks.network.capabilities.playerspell.PlayerSpellProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;

public class CastingItem extends Item {

    public CastingItem(Properties pProperties) {
        super(pProperties.stacksTo(1));
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity living, int pTimeCharged) {
        if ( living instanceof ServerPlayer player ) {
            player.getCapability(PlayerSpellProvider.PLAYER_SPELL).ifPresent(spell -> {
                Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(spell.getCurrentSpell()));
                if ( item instanceof SpellTabletItem spellTabletItem ) {
                    SpellTabletItem.learnSpell((Player)living, spellTabletItem, stack);
                }
            });
        }
    }

    public static boolean isValidCastingItem(ItemStack castingItem) {
        return castingItem.getItem() instanceof CastingItem;
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return 72000;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.BOW;
    }

    @Override
    public boolean isEnchantable(@Nonnull ItemStack stack) {
        return false;
    }
}
