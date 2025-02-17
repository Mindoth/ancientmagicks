package net.mindoth.ancientmagicks.item.temp.abstractspell.spellpearl;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.mindoth.ancientmagicks.AncientMagicks;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class SpellPearlRenderer extends EntityRenderer<SpellPearlEntity> {
    private static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(AncientMagicks.MOD_ID, "textures/item/spell_pearl.png");
    private static final RenderType RENDER_TYPE = RenderType.entityCutoutNoCull(TEXTURE_LOCATION);

    public SpellPearlRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(SpellPearlEntity entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        matrixStackIn.pushPose();
        matrixStackIn.scale(0.4F, 0.4F, 0.4F);
        matrixStackIn.translate(0, 0.25F, 0);
        matrixStackIn.mulPose(this.entityRenderDispatcher.cameraOrientation());
        matrixStackIn.mulPose(Axis.YP.rotationDegrees(180.0F));
        PoseStack.Pose posestack$pose = matrixStackIn.last();
        Matrix4f matrix4f = posestack$pose.pose();
        Matrix3f matrix3f = posestack$pose.normal();
        VertexConsumer vertexconsumer = bufferIn.getBuffer(RENDER_TYPE);
        vertex(vertexconsumer, matrix4f, matrix3f, packedLightIn, 0.0F, 0, 0, 1);
        vertex(vertexconsumer, matrix4f, matrix3f, packedLightIn, 1.0F, 0, 1, 1);
        vertex(vertexconsumer, matrix4f, matrix3f, packedLightIn, 1.0F, 1, 1, 0);
        vertex(vertexconsumer, matrix4f, matrix3f, packedLightIn, 0.0F, 1, 0, 0);
        matrixStackIn.popPose();
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    private static void vertex(VertexConsumer pConsumer, Matrix4f pPose, Matrix3f pNormal, int pLightmapUV, float pX, int pY, int pU, int pV) {
        pConsumer.vertex(pPose, pX - 0.5F, (float)pY - 0.25F, 0.0F).color(255, 255, 255, 255).uv((float)pU, (float)pV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(pLightmapUV).normal(pNormal, 0.0F, 1.0F, 0.0F).endVertex();
    }

    @Override
    public ResourceLocation getTextureLocation(SpellPearlEntity entity) {
        return TEXTURE_LOCATION;
    }
}
