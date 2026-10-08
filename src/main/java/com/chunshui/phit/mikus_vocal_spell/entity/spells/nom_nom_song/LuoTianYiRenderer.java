package com.chunshui.phit.mikus_vocal_spell.entity.spells.nom_nom_song;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class LuoTianYiRenderer extends GeoEntityRenderer<LuoTianYiProjectile> {
    public LuoTianYiRenderer(EntityRendererProvider.Context context) {
        super(context, new LuoTianYiModel());
    }

    @Override
    public void actuallyRender(PoseStack poseStack, LuoTianYiProjectile animatable, BakedGeoModel model, @Nullable RenderType renderType, MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        Vec3 motion = animatable.getDeltaMovement();
        if (motion.lengthSqr() > 1e-6) {
            float pitch = (float) Math.asin(motion.y / motion.length());
            float yaw = (float) Math.atan2(motion.x, motion.z);
            poseStack.mulPose(Axis.YP.rotation(yaw));
            poseStack.mulPose(Axis.XP.rotation(-pitch));
        }
        super.actuallyRender(poseStack, animatable, model, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
    }



    @Override
    public boolean shouldRender(@NotNull LuoTianYiProjectile animatable, @NotNull Frustum camera, double camX, double camY, double camZ) {
        return animatable.tickCount > 1;
    }
}
