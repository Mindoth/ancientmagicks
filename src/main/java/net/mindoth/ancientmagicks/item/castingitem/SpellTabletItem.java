package net.mindoth.ancientmagicks.item.castingitem;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;

public class SpellTabletItem extends CastingItem {

    public SpellTabletItem(WandType tier, int cooldown) {
        super(tier, cooldown);
    }

    @Override
    public void onUseTick(Level level, LivingEntity living, ItemStack tablet, int timeLeft) {
        if ( level.isClientSide ) return;
        if ( living instanceof Player player ) {
            List<ItemStack> wandList = getWandList(tablet);
            if ( timeLeft % 2 == 0 ) doSpell(player, player, tablet, wandList, getUseDuration(tablet) - timeLeft);
            if ( !player.getAbilities().instabuild ) tablet.shrink(1);
        }
    }
}
