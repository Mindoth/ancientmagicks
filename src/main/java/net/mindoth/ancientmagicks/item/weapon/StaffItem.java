package net.mindoth.ancientmagicks.item.weapon;

import net.mindoth.ancientmagicks.client.gui.inventory.WandData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.List;

public class StaffItem extends CastingItem {

    public StaffItem(WandType tier, int cooldown) {
        super(tier, cooldown);
    }

    @Override
    public void onUseTick(World level, LivingEntity living, ItemStack staff, int timeLeft) {
        if ( level.isClientSide ) return;
        if ( living instanceof PlayerEntity ) {
            PlayerEntity player = (PlayerEntity)living;
            WandData data = CastingItem.getData(staff);
            List<ItemStack> wandList = getWandList(data.getHandler().getStackInSlot(staff.getTag().getInt("staffslot")));
            if ( timeLeft % 2 == 0 ) doSpell(player, player, staff, wandList, getUseDuration(staff) - timeLeft);
        }
    }

    @Override
    @Nonnull
    public ActionResult<ItemStack> use(World level, PlayerEntity player, @Nonnull Hand handIn) {
        ActionResult<ItemStack> result = ActionResult.fail(player.getItemInHand(handIn));
        if ( !level.isClientSide ) {
            ItemStack staff = player.getItemInHand(handIn);
            if ( isValidCastingItem(staff) && staff.getTag().contains("staffslot") ) {
                player.startUsingItem(handIn);
            }
        }
        return result;
    }
}
