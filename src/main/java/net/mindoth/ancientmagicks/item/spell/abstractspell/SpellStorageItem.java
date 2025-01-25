package net.mindoth.ancientmagicks.item.spell.abstractspell;

import net.mindoth.ancientmagicks.capabilities.playermagic.ClientMagicData;
import net.mindoth.ancientmagicks.capabilities.playermagic.PlayerMagicProvider;
import net.mindoth.ancientmagicks.item.ColorRuneItem;
import net.mindoth.ancientmagicks.item.castingitem.SpecialCastingItem;
import net.mindoth.ancientmagicks.network.AncientMagicksNetwork;
import net.mindoth.ancientmagicks.network.PacketItemActivationAnimation;
import net.mindoth.ancientmagicks.network.PacketUpdateKnownSpells;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

public class SpellStorageItem extends Item {

    public SpellStorageItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public String getDescriptionId(ItemStack stack) {
        SpellItem spell = SpecialCastingItem.getStoredSpell(stack) != null ? SpecialCastingItem.getStoredSpell(stack) : null;
        return spell != null ? spell.getDescriptionId() : this.getDescriptionId();
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flagIn) {
        SpellItem spell = SpecialCastingItem.getStoredSpell(stack) != null ? SpecialCastingItem.getStoredSpell(stack) : null;
        if ( spell != null ) {
            if ( !Screen.hasShiftDown() ) {
                /*String spellSchool = "tooltip.ancientmagicks." + spell.spellSchool.toString();
                tooltip.add(Component.translatable("tooltip.ancientmagicks.spell_school").withStyle(ChatFormatting.GRAY).append(Component.literal(": "))
                        .append(Component.translatable(spellSchool)));*/
                int spellTier = spell.spellTier;
                tooltip.add(Component.translatable("tooltip.ancientmagicks.tier").append(Component.literal(": " + spellTier)).withStyle(ChatFormatting.GRAY));
                if ( spell.isChannel() ) {
                    int manaCost = spell.manaCost * 2;
                    tooltip.add(Component.translatable("tooltip.ancientmagicks.mana_cost").append(Component.literal(": " + manaCost)
                            .append(Component.literal("/s"))).withStyle(ChatFormatting.GRAY));
                }
                else {
                    int manaCost = spell.manaCost;
                    tooltip.add(Component.translatable("tooltip.ancientmagicks.mana_cost").append(Component.literal(": " + manaCost)).withStyle(ChatFormatting.GRAY));
                }
                int cooldown = spell.cooldown / 20;
                tooltip.add(Component.translatable("tooltip.ancientmagicks.cooldown").append(Component.literal(": " + cooldown + "s")).withStyle(ChatFormatting.GRAY));
                tooltip.add(Component.translatable("tooltip.ancientmagicks.shift"));
            }
            else {
                String modid = ForgeRegistries.ITEMS.getKey(spell).toString().split(":")[0];
                if ( modid != null ) tooltip.add(Component.translatable("tooltip." + modid + "." + spell).withStyle(ChatFormatting.GRAY));
            }
            if ( ColorRuneItem.CURRENT_COMBO_MAP.containsKey(spell) && Minecraft.getInstance().player != null ) {
                StringBuilder tooltipString = new StringBuilder();
                List<ColorRuneItem> list = ColorRuneItem.stringListToActualList(ColorRuneItem.CURRENT_COMBO_MAP.get(spell).toString());
                for ( ColorRuneItem rune : list ) {
                    String color = rune.color + "0" + "\u00A7r";
                    tooltipString.append(color);
                }

                tooltip.add(Component.literal(String.valueOf(tooltipString)));
            }
        }

        super.appendHoverText(stack, world, tooltip, flagIn);
    }

    @Override
    @Nonnull
    public InteractionResultHolder<ItemStack> use(Level level, Player player, @Nonnull InteractionHand hand) {
        InteractionResultHolder<ItemStack> result = InteractionResultHolder.fail(player.getItemInHand(hand));
        ItemStack stack = player.getItemInHand(hand);
        if ( !level.isClientSide ) {
            SpellItem spell = SpecialCastingItem.getStoredSpell(stack) != null ? SpecialCastingItem.getStoredSpell(stack) : null;
            if ( spell != null && player instanceof ServerPlayer serverPlayer ) learnSpell(serverPlayer, spell, stack);
        }
        return result;
    }

    public static void learnSpell(ServerPlayer serverPlayer, SpellItem spell, ItemStack vessel) {
        ItemStack stack = new ItemStack(spell);
        final String spellString = ForgeRegistries.ITEMS.getKey(spell).toString();
        serverPlayer.getCapability(PlayerMagicProvider.PLAYER_MAGIC).ifPresent(magic -> {
            CompoundTag tag = new CompoundTag();
            tag.putString("am_secretspell", spellString);
            if ( Objects.equals(magic.getKnownSpells(), "") ) {
                magic.setKnownSpells(spellString);
                AncientMagicksNetwork.sendToPlayer(new PacketUpdateKnownSpells(tag), serverPlayer);
                playLearnEffects(serverPlayer, stack, vessel);
            }
            else if ( !ClientMagicData.stringListToSpellList(magic.getKnownSpells()).contains(spell) ) {
                magic.setKnownSpells(magic.getKnownSpells() + "," + spellString);
                AncientMagicksNetwork.sendToPlayer(new PacketUpdateKnownSpells(tag), serverPlayer);
                playLearnEffects(serverPlayer, stack, vessel);
            }
        });
    }

    public static void playLearnEffects(Player player, ItemStack stack, ItemStack vessel) {
        player.playNotifySound(SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 1.0F, 0.75F);
        player.playNotifySound(SoundEvents.FIREWORK_ROCKET_BLAST_FAR, SoundSource.PLAYERS, 1.0F, 0.75F);
        player.playNotifySound(SoundEvents.FIREWORK_ROCKET_TWINKLE_FAR, SoundSource.PLAYERS, 1.0F, 0.75F);
        if ( player instanceof ServerPlayer ) AncientMagicksNetwork.sendToPlayer(new PacketItemActivationAnimation(stack, player), (ServerPlayer)player);
        vessel.shrink(1);
    }

    public static void playItemActivationAnimation(ItemStack itemStack, Entity entity) {
        Minecraft mc = Minecraft.getInstance();
        if ( entity == mc.player ) mc.gameRenderer.displayItemActivation(itemStack);
    }
}
