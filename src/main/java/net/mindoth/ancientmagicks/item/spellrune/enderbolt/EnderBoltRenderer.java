package net.mindoth.ancientmagicks.item.spellrune.enderbolt;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.mindoth.ancientmagicks.AncientMagicks;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;

public class EnderBoltRenderer extends EntityRenderer<EnderBoltEntity> {

    public EnderBoltRenderer(EntityRendererManager renderManager) {
        super(renderManager);
    }

    @Override
    public ResourceLocation getTextureLocation(EnderBoltEntity entity) {
        return new ResourceLocation(AncientMagicks.MOD_ID, "textures/particle/clear.png");
    }

    @Override
    public void render(EnderBoltEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        matrixStackIn.pushPose();
        matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(MathHelper.lerp(partialTicks, entityIn.yRotO, entityIn.yRot) - 90.0F));
        matrixStackIn.mulPose(Vector3f.ZP.rotationDegrees(MathHelper.lerp(partialTicks, entityIn.xRotO, entityIn.xRot)));
        matrixStackIn.scale(1.0F, 1.0F, 1.0F);
        matrixStackIn.translate(0, 0, 0);
        matrixStackIn.popPose();
    }
}
