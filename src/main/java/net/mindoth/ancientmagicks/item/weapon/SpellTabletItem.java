package net.mindoth.ancientmagicks.item.weapon;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;

public class SpellTabletItem extends CastingItem {

    public SpellTabletItem(WandType tier, int cooldown) {
        super(tier, cooldown);
    }

    @Override
    public void onUseTick(World level, LivingEntity living, ItemStack tablet, int timeLeft) {
        if ( level.isClientSide ) return;
        if ( living instanceof PlayerEntity ) {
            PlayerEntity player = (PlayerEntity)living;
            List<ItemStack> wandList = getWandList(tablet);
            if ( timeLeft % 2 == 0 ) doSpell(player, player, tablet, wandList, getUseDuration(tablet) - timeLeft);
            if ( !((PlayerEntity)living).abilities.instabuild ) tablet.shrink(1);
        }
    }
}
