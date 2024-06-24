package net.mindoth.ancientmagicks.item.castingitem;

import com.google.common.collect.Lists;
import net.mindoth.ancientmagicks.client.gui.inventory.WandData;
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
import java.util.List;

public class StaffItem extends CastingItem {

    public StaffItem(WandType tier, int cooldown) {
        super(tier, cooldown);
    }

    @Override
    public void onUseTick(Level level, LivingEntity living, ItemStack staff, int timeLeft) {
        if ( level.isClientSide ) return;
        if ( living instanceof Player player ) {
            //WandData data = CastingItem.getData(staff);
            //List<ItemStack> wandList = getWandList(data.getHandler().getStackInSlot(staff.getTag().getInt("staffslot")));
            List<ItemStack> wandList = Lists.newArrayList();
            if ( staff.hasTag() && staff.getTag().contains("spellRune") ) {
                Item spellRune = ForgeRegistries.ITEMS.getValue(new ResourceLocation(staff.getTag().getString("spellRune")));
                wandList.add(new ItemStack(spellRune));
            }
            if ( timeLeft % 2 == 0 ) doSpell(player, player, staff, wandList, getUseDuration(staff) - timeLeft);
        }
    }

    @Override
    @Nonnull
    public InteractionResultHolder<ItemStack> use(Level level, Player player, @Nonnull InteractionHand handIn) {
        InteractionResultHolder<ItemStack> result = InteractionResultHolder.fail(player.getItemInHand(handIn));
        if ( !level.isClientSide ) {
            ItemStack staff = player.getItemInHand(handIn);
            if ( isValidCastingItem(staff) && staff.getTag().contains("spellRune") ) {
                player.startUsingItem(handIn);
            }
        }
        return result;
    }
}
