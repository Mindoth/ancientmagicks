package net.mindoth.ancientmagicks.network;

import net.mindoth.ancientmagicks.client.gui.menu.SpellCraftingMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketCraftSpell {

    public String name;

    public PacketCraftSpell(String name) {
        this.name = name;
    }

    public PacketCraftSpell(FriendlyByteBuf buf) {
        this.name = buf.readUtf();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(this.name);
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            if ( context.getSender() != null ) {
                ServerPlayer player = context.getSender();
                if ( player.containerMenu instanceof SpellCraftingMenu ) ((SpellCraftingMenu)player.containerMenu).processCrafting(this.name);
            }
        });
    }
}
