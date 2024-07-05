package net.mindoth.ancientmagicks.network;

import com.google.common.collect.Lists;
import net.mindoth.ancientmagicks.item.castingitem.CastingItem;
import net.mindoth.ancientmagicks.network.capabilities.playerspell.PlayerSpellProvider;
import net.mindoth.ancientmagicks.registries.AncientMagicksItems;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.List;
import java.util.function.Supplier;

public class PacketSendRuneData {

    public PacketSendRuneData() {
    }

    public PacketSendRuneData(FriendlyByteBuf buf) {
    }

    public void encode(FriendlyByteBuf buf) {
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        //contextSupplier.get().getSender().refreshContainer(contextSupplier.get().getSender().containerMenu);
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            if ( context.getSender() != null ) {
                ServerPlayer player = context.getSender();
                player.stopUsingItem();
                player.getCapability(PlayerSpellProvider.PLAYER_SPELL).ifPresent(spell -> {
                    boolean isOffHand = !(player.getMainHandItem().getItem() instanceof CastingItem);
                    List<ItemStack> runeList = Lists.newArrayList();
                    if ( spell.getBlue() ) runeList.add(new ItemStack(AncientMagicksItems.BLUE_RUNE.get()));
                    if ( spell.getPurple() ) runeList.add(new ItemStack(AncientMagicksItems.PURPLE_RUNE.get()));
                    if ( spell.getYellow() ) runeList.add(new ItemStack(AncientMagicksItems.YELLOW_RUNE.get()));
                    if ( spell.getGreen() ) runeList.add(new ItemStack(AncientMagicksItems.GREEN_RUNE.get()));
                    if ( spell.getBlack() ) runeList.add(new ItemStack(AncientMagicksItems.BLACK_RUNE.get()));
                    if ( spell.getWhite() ) runeList.add(new ItemStack(AncientMagicksItems.WHITE_RUNE.get()));
                    AncientMagicksNetwork.sendToPlayer(new PacketReceiveRuneData(runeList, null, isOffHand), player);
                });
            }
        });
    }
}
