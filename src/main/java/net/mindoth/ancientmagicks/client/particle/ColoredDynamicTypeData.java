package net.mindoth.ancientmagicks.client.particle;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.mindoth.ancientmagicks.registries.AncientMagicksParticles;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;

public class ColoredDynamicTypeData implements IParticleData {

    private ParticleType<ColoredDynamicTypeData> type;
    public ParticleColor color;
    float scale;
    int age;
    boolean fade;
    boolean mask;

    public static final Codec<ColoredDynamicTypeData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    Codec.FLOAT.fieldOf("r").forGetter(d -> d.color.getRed()),
                    Codec.FLOAT.fieldOf("g").forGetter(d -> d.color.getGreen()),
                    Codec.FLOAT.fieldOf("b").forGetter(d -> d.color.getBlue()),
                    Codec.FLOAT.fieldOf("scale").forGetter(d -> d.scale),
                    Codec.INT.fieldOf("age").forGetter(d -> d.age),
                    Codec.BOOL.fieldOf("fade").forGetter(d -> d.fade),
                    Codec.BOOL.fieldOf("mask").forGetter(d -> d.mask)
            )
            .apply(instance, ColoredDynamicTypeData::new));

    @Override
    public ParticleType<?> getType() {
        return type;
    }

    static final IDeserializer<ColoredDynamicTypeData> DESERIALIZER = new IDeserializer<ColoredDynamicTypeData>() {
        @Override
        public ColoredDynamicTypeData fromCommand(ParticleType<ColoredDynamicTypeData> type, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            return new ColoredDynamicTypeData(type, ParticleColor.deserialize(reader.readString()), reader.readFloat(), reader.readInt(), reader.readBoolean(), reader.readBoolean());
        }

        @Override
        public ColoredDynamicTypeData fromNetwork(ParticleType<ColoredDynamicTypeData> type, PacketBuffer buffer) {
            return new ColoredDynamicTypeData(type, ParticleColor.deserialize(buffer.readUtf()), buffer.readFloat(), buffer.readInt(), buffer.readBoolean(), buffer.readBoolean());
        }
    };

    public ColoredDynamicTypeData(float r, float g, float b, float scale, int age, boolean fade, boolean mask) {
        this.type = AncientMagicksParticles.EMBER_TYPE;
        this.color = new ParticleColor(r, g, b);
        this.scale = scale;
        this.age = age;
        this.fade = fade;
        this.mask = mask;
    }

    public ColoredDynamicTypeData(ParticleType<ColoredDynamicTypeData> particleTypeData, ParticleColor color, float scale, int age, boolean fade, boolean mask) {
        this.type = particleTypeData;
        this.color = color;
        this.scale = scale;
        this.age = age;
        this.fade = fade;
        this.mask = mask;
    }

    @Override
    public void writeToNetwork(PacketBuffer buffer) {
        buffer.writeUtf(color.serialize());
        buffer.writeFloat(scale);
        buffer.writeInt(age);
        buffer.writeBoolean(fade);
        buffer.writeBoolean(mask);
    }

    @Override
    public String writeToString() {
        return type.getRegistryName().toString() + " " + color.serialize() + " " + scale + " " + age + " " + fade + " " + mask;
    }
}
