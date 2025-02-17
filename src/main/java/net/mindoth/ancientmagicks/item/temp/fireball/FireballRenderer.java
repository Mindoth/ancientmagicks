package net.mindoth.ancientmagicks.item.temp.fireball;

import com.mojang.blaze3d.vertex.PoseStack;
import net.mindoth.ancientmagicks.AncientMagicks;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class FireballRenderer extends EntityRenderer<FireballEntity> {

    public FireballRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(FireballEntity entity) {
        return new ResourceLocation(AncientMagicks.MOD_ID, "textures/particle/clear.png");
    }

    @Override
    public void render(FireballEntity entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
    }
}
