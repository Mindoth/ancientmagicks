package net.mindoth.ancientmagicks.client.particle;

import net.mindoth.ancientmagicks.registries.AncientMagicksParticles;
import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particles.IParticleData;

public class EmberParticleData implements IParticleFactory<ColoredDynamicTypeData> {

    private final IAnimatedSprite spriteSet;
    public static final String NAME = "ember";

    public EmberParticleData(IAnimatedSprite sprite) {
        this.spriteSet = sprite;
    }

    @Override
    public Particle createParticle(ColoredDynamicTypeData data, ClientWorld worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        return new ParticleEmber(worldIn, x, y, z, xSpeed, ySpeed, zSpeed,
                data.color.getRed(), data.color.getGreen(), data.color.getBlue(), data.scale, data.age, data.fade, data.mask, this.spriteSet);
    }

    public static IParticleData createData(ParticleColor color, float scale, int age, boolean fade, boolean mask) {
        return new ColoredDynamicTypeData(AncientMagicksParticles.EMBER_TYPE, color, scale, age, fade, mask);
    }
}
