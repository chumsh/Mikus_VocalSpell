package com.chunshui.phit.mikus_vocal_spell.entity.spells.core_melt;

import com.chunshui.phit.mikus_vocal_spell.client.ClientRenderers;
import com.chunshui.phit.mikus_vocal_spell.utils.NBTKeyHelper;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.renderer.layer.vanilla.AttachedAnimatableRenderLayer;

import java.util.function.Function;

public class InnocenceShellLayer<T extends Entity, M extends EntityModel<T>> extends AttachedAnimatableRenderLayer<InnocenceShellEntity, T, M, InnocenceShellRender> {
    /**
     * Create a new {@link RenderLayer} instance
     *
     * @param renderer        The vanilla renderer instance that the layer is being added to
     * @param instanceFactory A factory that creates a new GeoAnimatable instance for rendering
     */
    public InnocenceShellLayer(RenderLayerParent<T, M> renderer, Function<Level, InnocenceShellEntity> instanceFactory) {
        super(renderer, instanceFactory);
        //MikusVocalSpellIronsSpellsAddon.LOGGER.info("Creating Innocence Shell Layer");
    }


    @Override
    public boolean shouldRender(T entity) {
//        MikusVocalSpellIronsSpellsAddon.LOGGER.info("Innocence Shell Layer: {}",  hasInnocence);
        return entity.getPersistentData().getBoolean(NBTKeyHelper.HAS_INNOCENCE);
    }
    @Override
    protected void renderAnimatableOnModel(T entity, InnocenceShellEntity animatable, M model, InnocenceShellRender renderer, PoseStack poseStack, MultiBufferSource bufferSource, float partialTick, int packedLight, float ageInTicks, float limbSwing, float limbSwingAmount, float netHeadYaw, float headPitch) {
        // MikusVocalSpellIronsSpellsAddon.LOGGER.info("Rendering Innocence Shell");
        poseStack.mulPose(Axis.YP.rotationDegrees(90));
        poseStack.translate(0, -0.6, 0);
        renderer.render(animatable, netHeadYaw, partialTick, poseStack, bufferSource, packedLight);
        }

    @Override
    public @Nullable InnocenceShellRender getRenderer(InnocenceShellEntity animatable) {
        return ClientRenderers.INNOCENCE_SHELL_RENDER;
    }
}