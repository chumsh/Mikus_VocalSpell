package com.chunshui.phit.mikus_vocal_spell.entity.spells.scallion_dance;

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
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

public class ScallionRender extends EntityRenderer<ScallionEffectArea> {
    private static final ResourceLocation TEXTURE =
            MikusVocalSpellIronsSpellsAddon.id("textures/entity/scallion/scallion_core.png");
    private static final ResourceLocation[] TEXTURES = {
            MikusVocalSpellIronsSpellsAddon.id("textures/entity/scallion/scallion_core.png"),
            MikusVocalSpellIronsSpellsAddon.id("textures/entity/scallion/scallion_outer.png"),
    };

    public ScallionRender(EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius = 0;
    }

    @Override
    public void render(@NotNull ScallionEffectArea entity, float yaw, float partialTicks, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int light) {
        float rotateTime = entity.tickCount + partialTicks;
        float alpha = computeFade(entity, partialTicks);
        float radius = computeRadius(entity, partialTicks);
        if(alpha <= 0) { return; }
        poseStack.pushPose();
        drawScallion(poseStack, bufferSource, radius, alpha, rotateTime, true);
        drawScallion(poseStack, bufferSource, radius, alpha, rotateTime, false);
        poseStack.popPose();

        super.render(entity, yaw, partialTicks, poseStack, bufferSource, light);
    }

    private void drawScallion(PoseStack pose, MultiBufferSource bufferSource, float radius, float alpha, float rotateTime, boolean isCore) {

        float halfRadius = (float) (radius * .5) + 3;
        pose.pushPose();
        pose.translate(0, 0.01, 0);
        pose.mulPose(Axis.ZP.rotationDegrees(90));
        if (isCore) {
            pose.mulPose(Axis.XP.rotationDegrees(rotateTime * 0.8f));
        } else {
            pose.mulPose(Axis.XP.rotationDegrees(-rotateTime));
        }
        Matrix4f matrix4f = pose.last().pose();
        VertexConsumer consumer;
        if(isCore) {
            consumer = bufferSource.getBuffer(RenderType.entityTranslucentEmissive(TEXTURES[0]));
        } else {
            consumer = bufferSource.getBuffer(RenderType.entityTranslucentEmissive(TEXTURES[1]));
        }
        consumer.addVertex(matrix4f, 0, halfRadius, halfRadius).setColor(5, 5, 5, alpha).setUv(1f, 0f).setLight(LightTexture.FULL_BRIGHT).setOverlay(OverlayTexture.NO_OVERLAY).setNormal(0f, 1f, 0f);
        consumer.addVertex(matrix4f, 0, halfRadius, -halfRadius).setColor(5, 5, 5, alpha).setUv(1f, 1f).setLight(LightTexture.FULL_BRIGHT).setOverlay(OverlayTexture.NO_OVERLAY).setNormal(0f, 1f, 0f);
        consumer.addVertex(matrix4f, 0, -halfRadius, -halfRadius).setColor(5, 5, 5, alpha).setUv(0f, 1f).setLight(LightTexture.FULL_BRIGHT).setOverlay(OverlayTexture.NO_OVERLAY).setNormal(0f, 1f, 0f);
        consumer.addVertex(matrix4f, 0, -halfRadius, halfRadius).setColor(5, 5, 5, alpha).setUv(0f, 0f).setLight(LightTexture.FULL_BRIGHT).setOverlay(OverlayTexture.NO_OVERLAY).setNormal(0f, 1f, 0f);
        pose.popPose();
    }

    private float computeFade(ScallionEffectArea entity, float particleTicks) {
        float age = entity.tickCount + particleTicks;
        float lifeTime = entity.getMaxLifeTime();

        float fadeIn = Math.min(1f, age / 10);
        float fadeOut = Math.min(1f, (lifeTime - age) / 10);
        return Mth.clamp(Math.min(fadeIn, fadeOut), 0f, 1f);
    }

    private float computeRadius(ScallionEffectArea entity, float particleTicks) {
        float age = entity.tickCount + particleTicks;
        float lifeTime = entity.getMaxLifeTime();
        float originalRadius = entity.getDetectionRadius();

        float radiusIn = Math.min(originalRadius, originalRadius * age / 10);
        float radiusOut = Math.min(originalRadius, originalRadius * (lifeTime - age) / 10);
        return Mth.clamp(Math.min(radiusIn, radiusOut), 0f, originalRadius);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull ScallionEffectArea entity) { return TEXTURE; }

    @Override
    public boolean shouldRender(@NotNull ScallionEffectArea entity, @NotNull Frustum frustum, double camX, double camY, double camZ) { return true; }
}
