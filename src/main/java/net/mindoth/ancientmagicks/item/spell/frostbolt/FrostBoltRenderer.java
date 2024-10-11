package net.mindoth.ancientmagicks.item.spell.frostbolt;

import com.mojang.blaze3d.vertex.PoseStack;
import net.mindoth.ancientmagicks.AncientMagicks;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class FrostBoltRenderer extends EntityRenderer<FrostBoltEntity> {

    public FrostBoltRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(FrostBoltEntity entity) {
        return new ResourceLocation(AncientMagicks.MOD_ID, "textures/particle/clear.png");
    }

    @Override
    public void render(FrostBoltEntity entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
    }
}
