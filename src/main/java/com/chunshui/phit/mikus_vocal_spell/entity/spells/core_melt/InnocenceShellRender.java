package com.chunshui.phit.mikus_vocal_spell.entity.spells.core_melt;

import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class InnocenceShellRender extends GeoEntityRenderer<InnocenceShellEntity> {
    public InnocenceShellRender(EntityRendererProvider.Context renderManager) {
        super(renderManager, new InnocenceShellModel());

        addRenderLayer(new AutoGlowingGeoLayer<>(this));
    }

    @Override
    public boolean shouldRender(@NotNull InnocenceShellEntity entity, @NotNull Frustum frustum, double camX, double camY, double camZ) { return true; }
}
