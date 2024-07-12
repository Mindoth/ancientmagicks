package net.mindoth.ancientmagicks.client.particle;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureManager;

public class ParticleRenderTypes {

    static final ParticleRenderType AMAGICKS_RENDER = new ParticleRenderType() {

        @Override
        public void begin(BufferBuilder buffer, TextureManager textureManager) {
            RenderSystem.enableDepthTest();
            Minecraft.getInstance().gameRenderer.lightTexture().turnOnLightLayer();
            RenderSystem.depthMask(false);
            RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_PARTICLES);
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
            buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
        }

        @Override
        public void end(Tesselator tessellator) {
            tessellator.end();
        }


        @Override
        public String toString() {
            return "ancientmagicks:am_rend";
        }
    };

    static final ParticleRenderType AMAGICKS_RENDER_DARK = new ParticleRenderType() {

        @Override
        public void begin(BufferBuilder buffer, TextureManager textureManager) {
            RenderSystem.depthMask(false);
            textureManager.bindForSetup(TextureAtlas.LOCATION_PARTICLES);
            RenderSystem.enableBlend();

            //This REALLY needs some work, Thanks Mojang...
            /*RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.ONE_MINUS_SRC_COLOR, GlStateManager.DestFactor.DST_COLOR,
                    GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);*/

            //Current placeholder
            RenderSystem.blendFunc(GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ZERO);

            //Old method
            //RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            //RenderSystem.alphaFunc(516, 0.003921569F);
            buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
        }

        @Override
        public void end(Tesselator tessellator) {
            tessellator.end();
            RenderSystem.depthMask(true);
        }

        @Override
        public String toString() {
            return "ancientmagicks:am_rend_dark";
        }
    };
}
