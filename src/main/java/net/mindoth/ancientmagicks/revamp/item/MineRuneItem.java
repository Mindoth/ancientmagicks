package net.mindoth.ancientmagicks.revamp.item;

import com.mojang.authlib.GameProfile;
import net.mindoth.ancientmagicks.revamp.SpellData;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;

import java.util.UUID;

public class MineRuneItem extends RuneItem {
    public MineRuneItem(Properties pProperties) {
        super(pProperties);
    }

    private static final GameProfile FAKE_PROFILE = new GameProfile(UUID.fromString("fdc17a6f-5d46-484e-9343-820f43c7b101"), "am_fake_player_profile");

    @Override
    public SpellData resolve(Entity caster, SpellData spellData) {
        if ( !spellData.getBlocks().isEmpty() && !spellData.getDimensions().isEmpty() ) {
            BlockPos blockPos = spellData.getBlocks().get(spellData.getBlocks().size() - 1).getBlockPos();
            spellData.purgeBlocks(1);
            Level dimension = spellData.getDimensions().get(spellData.getDimensions().size() - 1);
            spellData.purgeDimensions(1);
            if ( dimension instanceof ServerLevel serverLevel ) {
                FakePlayer fakePlayer = FakePlayerFactory.get(serverLevel, FAKE_PROFILE);
                Block block = serverLevel.getBlockState(blockPos).getBlock();
                BlockState blockState = serverLevel.getBlockState(blockPos);
                fakePlayer.setItemSlot(EquipmentSlot.MAINHAND, getToolFromStrength(1));
                if ( block.canHarvestBlock(blockState, serverLevel, blockPos, fakePlayer) && block.defaultDestroyTime() >= 0 && !blockState.isAir() ) {
                    serverLevel.destroyBlock(blockPos, !(caster instanceof Player player && player.isCreative()), fakePlayer);
                }
            }
        }
        else spellData.setValid(false);
        return spellData;
    }

    private ItemStack getToolFromStrength(int power) {
        if ( power < 1 ) return ItemStack.EMPTY;
        else if ( power == 1 ) return new ItemStack(Items.WOODEN_PICKAXE);
        else if ( power == 2 ) return new ItemStack(Items.STONE_PICKAXE);
        else if ( power == 3 ) return new ItemStack(Items.IRON_PICKAXE);
        else return new ItemStack(Items.DIAMOND_PICKAXE);
    }
}
