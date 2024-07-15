package net.mindoth.ancientmagicks.network;

import net.mindoth.ancientmagicks.client.particle.EmberParticleProvider;
import net.mindoth.ancientmagicks.client.particle.ParticleColor;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

public class PacketSpellHitBurst {

    public int r;
    public int g;
    public int b;
    public float size;
    public int age;
    public boolean fade;
    public boolean mask;
    public double x;
    public double y;
    public double z;

    public PacketSpellHitBurst(int r, int g, int b, float size, int age, boolean fade, boolean mask, double x, double y, double z) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.size = size;
        this.age = age;
        this.fade = fade;
        this.mask = mask;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public PacketSpellHitBurst(FriendlyByteBuf buf) {
        this.r = buf.readInt();
        this.g = buf.readInt();
        this.b = buf.readInt();
        this.size = buf.readFloat();
        this.age = buf.readInt();
        this.fade = buf.readBoolean();
        this.mask = buf.readBoolean();
        this.x = buf.readDouble();
        this.y = buf.readDouble();
        this.z = buf.readDouble();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(this.r);
        buf.writeInt(this.g);
        buf.writeInt(this.b);
        buf.writeFloat(this.size);
        buf.writeInt(this.age);
        buf.writeBoolean(this.fade);
        buf.writeBoolean(this.mask);
        buf.writeDouble(this.x);
        buf.writeDouble(this.y);
        buf.writeDouble(this.z);
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().enqueueWork(() -> {
            Minecraft minecraft = Minecraft.getInstance();
            for ( int i = 0; i < 10; i++ ) {
                double randX = ThreadLocalRandom.current().nextDouble(-1, 1);
                double randY = ThreadLocalRandom.current().nextDouble(-1, 1);
                double randZ = ThreadLocalRandom.current().nextDouble(-1, 1);
                if ( minecraft.level != null ) minecraft.level.addParticle(EmberParticleProvider.createData(new ParticleColor(this.r, this.g, this.b),
                                this.size, this.age, this.fade, this.mask), this.x, this.y, this.z,
                        randX * 0.5D, randY * 0.5D, randZ * 0.5D);
            }
        });
        contextSupplier.get().setPacketHandled(true);
    }
}
