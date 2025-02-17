package net.mindoth.ancientmagicks.item.form;

import com.mojang.blaze3d.vertex.PoseStack;
import net.mindoth.ancientmagicks.AncientMagicks;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class ProjectileRenderer extends EntityRenderer<ProjectileEntity> {

    public ProjectileRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(ProjectileEntity entity) {
        return new ResourceLocation(AncientMagicks.MOD_ID, "textures/particle/clear.png");
    }

    @Override
    public void render(ProjectileEntity entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
    }
}
