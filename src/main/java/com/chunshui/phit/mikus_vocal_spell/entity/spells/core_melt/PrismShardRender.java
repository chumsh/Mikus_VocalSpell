package com.chunshui.phit.mikus_vocal_spell.entity.spells.core_melt;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class PrismShardRender extends GeoEntityRenderer<PrismShardEntity> {


    public PrismShardRender(EntityRendererProvider.Context renderManager) {
        super(renderManager, new PrismShardModel());

        addRenderLayer(new AutoGlowingGeoLayer<>(this));
    }

    @Override
    public void render(
            PrismShardEntity entity, float entityYaw, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight
    ) {
        Vec3 motion = entity.getDeltaMovement();
        if (motion.lengthSqr() > 1e-6) {
            float pitch = (float) Math.asin(motion.y / motion.length());
            float yaw = (float) Math.atan2(motion.x, motion.z);
            poseStack.mulPose(Axis.YP.rotation(yaw));
            poseStack.mulPose(Axis.XP.rotation(-pitch));
        }

        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }


    @Override
    public boolean shouldRender(
            @NotNull PrismShardEntity entity, @NotNull Frustum frustum, double camX, double camY, double camZ
    ) { return true; }
}
