package net.mindoth.ancientmagicks.item.spell.thunderball;

import com.mojang.blaze3d.vertex.PoseStack;
import net.mindoth.ancientmagicks.AncientMagicks;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class ThunderballRenderer extends EntityRenderer<ThunderballEntity> {

    public ThunderballRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
    }

    @Override
    public ResourceLocation getTextureLocation(ThunderballEntity entity) {
        return new ResourceLocation(AncientMagicks.MOD_ID, "textures/particle/clear.png");
    }

    @Override
    public void render(ThunderballEntity entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
    }

    /*protected int getBlockLightLevel(ThunderballEntity pEntity, BlockPos pPos) {
        return 15;
    }
    
    private static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(AncientMagicks.MOD_ID, "textures/entity/thunderball.png");
    private static final RenderType RENDER_TYPE = RenderType.itemEntityTranslucentCull(TEXTURE_LOCATION);

    public void render(ThunderballEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        pPoseStack.pushPose();
        pPoseStack.scale(0.8F, 0.8F, 0.8F);
        pPoseStack.translate(0, -0.25F, 0);
        pPoseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
        pPoseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
        PoseStack.Pose posestack$pose = pPoseStack.last();
        Matrix4f matrix4f = posestack$pose.pose();
        Matrix3f matrix3f = posestack$pose.normal();
        VertexConsumer vertexconsumer = pBuffer.getBuffer(RENDER_TYPE);
        vertex(vertexconsumer, matrix4f, matrix3f, pPackedLight, 0.0F, 0, 0, 1, 150, 255, 255);
        vertex(vertexconsumer, matrix4f, matrix3f, pPackedLight, 1.0F, 0, 1, 1, 150, 255, 255);
        vertex(vertexconsumer, matrix4f, matrix3f, pPackedLight, 1.0F, 1, 1, 0, 150, 255, 255);
        vertex(vertexconsumer, matrix4f, matrix3f, pPackedLight, 0.0F, 1, 0, 0, 150, 255, 255);
        pPoseStack.popPose();
        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
    }

    private static void vertex(VertexConsumer pConsumer, Matrix4f pMatrix, Matrix3f pMatrixNormal, int pPackedLight, float pX, float pY, float pU, float pV, int pRed, int pGreen, int pBlue) {
        pConsumer.vertex(pMatrix, pX - 0.5F, pY - 0.25F, 0.0F).color(pRed, pGreen, pBlue, 200).uv(pU, pV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(pPackedLight).normal(pMatrixNormal, 0.0F, 1.0F, 0.0F).endVertex();
    }

    public ResourceLocation getTextureLocation(ThunderballEntity pEntity) {
        return TEXTURE_LOCATION;
    }*/
}
