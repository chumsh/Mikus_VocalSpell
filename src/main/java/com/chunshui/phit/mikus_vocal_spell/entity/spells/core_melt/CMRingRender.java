package com.chunshui.phit.mikus_vocal_spell.entity.spells.core_melt;

import com.chunshui.phit.mikus_vocal_spell.MikusVocalSpellIronsSpellsAddon;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

public class CMRingRender extends EntityRenderer<CoreMeltRing> {
    private final ResourceLocation TEXTURE = MikusVocalSpellIronsSpellsAddon.id("textures/entity/scallion/scallion_outer.png");

    public CMRingRender(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(@NotNull CoreMeltRing entity, float yaw, float partialTicks, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int light) {
        poseStack.pushPose();
        float radius = entity.getRadius(partialTicks);
        drawCMRing(poseStack, bufferSource, radius);
        poseStack.popPose();

    }

    private void drawCMRing(PoseStack pose, MultiBufferSource bufferSource, float radius) {
        pose.pushPose();
        pose.translate(0, 0.01, 0);
        pose.mulPose(Axis.YP.rotationDegrees(90));

        Matrix4f matrix4f = pose.last().pose();
        VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityTranslucentEmissive(TEXTURE));

        consumer.addVertex(matrix4f, radius, 0, radius).setColor(255, 255, 255, 255).setUv(1f, 0f).setLight(LightTexture.FULL_BRIGHT).setOverlay(OverlayTexture.NO_OVERLAY).setNormal(0f, 1f, 0f);
        consumer.addVertex(matrix4f, radius, 0, -radius).setColor(255, 255, 255, 255).setUv(1f, 1f).setLight(LightTexture.FULL_BRIGHT).setOverlay(OverlayTexture.NO_OVERLAY).setNormal(0f, 1f, 0f);
        consumer.addVertex(matrix4f, -radius, 0, -radius).setColor(255, 255, 255, 255).setUv(0f, 1f).setLight(LightTexture.FULL_BRIGHT).setOverlay(OverlayTexture.NO_OVERLAY).setNormal(0f, 1f, 0f);
        consumer.addVertex(matrix4f, -radius, 0, radius).setColor(255, 255, 255, 255).setUv(0f, 0f).setLight(LightTexture.FULL_BRIGHT).setOverlay(OverlayTexture.NO_OVERLAY).setNormal(0f, 1f, 0f);
        pose.popPose();
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull CoreMeltRing entity) { return TEXTURE; }

    @Override
    public boolean shouldRender(@NotNull CoreMeltRing entity, @NotNull Frustum frustum, double camX, double camY, double camZ) { return true; }
}
