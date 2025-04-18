package net.mindoth.ancientmagicks.network;

import net.mindoth.ancientmagicks.client.menu.SpellCraftingMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketDumpSpell {

    public PacketDumpSpell() {
    }

    public PacketDumpSpell(FriendlyByteBuf buf) {
    }

    public void encode(FriendlyByteBuf buf) {
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            if ( context.getSender() != null ) {
                ServerPlayer player = context.getSender();
                if ( player.containerMenu instanceof SpellCraftingMenu) ((SpellCraftingMenu)player.containerMenu).processDumping();
            }
        });
    }
}
