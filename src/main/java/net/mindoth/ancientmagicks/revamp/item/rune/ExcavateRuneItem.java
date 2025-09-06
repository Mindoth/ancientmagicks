package net.mindoth.ancientmagicks.revamp.item.rune;

import com.mojang.authlib.GameProfile;
import net.mindoth.ancientmagicks.revamp.SpellData;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class ExcavateRuneItem extends UseOnBlockTemplate {
    public ExcavateRuneItem(Properties pProperties) {
        super(pProperties);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(Component.translatable("tooltip.ancientmagicks.block").append(Component.literal(" -> "))
                .append(Component.translatable("tooltip.ancientmagicks.excavate")).withStyle(ChatFormatting.GRAY));
        super.appendHoverText(stack, world, tooltip, flagIn);
    }

    private static final GameProfile FAKE_PROFILE = new GameProfile(UUID.fromString("fdc17a6f-5d46-484e-9343-820f43c7b101"), "am_fake_player_profile");

    @Override
    protected SpellData result(Entity caster, SpellData spellData, BlockPos pos, Level level) {
        int power;
        if ( spellData.getIntegers().isEmpty() || spellData.getLatestInteger() == null ) power = 0;
        else power = spellData.getLatestInteger();
        spellData.purgeIntegers(1);
        handleMine(caster, pos, level, power);
        return spellData;
    }

    private void handleMine(Entity caster, BlockPos pos, Level dimension, int power) {
        if ( dimension instanceof ServerLevel serverLevel ) {
            FakePlayer fakePlayer = FakePlayerFactory.get(serverLevel, FAKE_PROFILE);
            Block block = serverLevel.getBlockState(pos).getBlock();
            BlockState blockState = serverLevel.getBlockState(pos);
            fakePlayer.setItemSlot(EquipmentSlot.MAINHAND, getToolFromStrength(power));
            if ( block.canHarvestBlock(blockState, serverLevel, pos, fakePlayer) && block.defaultDestroyTime() >= 0 && !blockState.isAir() ) {
                serverLevel.destroyBlock(pos, !(caster instanceof Player player && player.isCreative()), fakePlayer);
            }
        }
    }

    private ItemStack getToolFromStrength(int power) {
        if ( power < 1 ) return ItemStack.EMPTY;
        else if ( power == 1 ) return new ItemStack(Items.WOODEN_PICKAXE);
        else if ( power == 2 ) return new ItemStack(Items.STONE_PICKAXE);
        else if ( power == 3 ) return new ItemStack(Items.IRON_PICKAXE);
        else if ( power == 4 ) return new ItemStack(Items.DIAMOND_PICKAXE);
        else return new ItemStack(Items.NETHERITE_PICKAXE);
    }
}
