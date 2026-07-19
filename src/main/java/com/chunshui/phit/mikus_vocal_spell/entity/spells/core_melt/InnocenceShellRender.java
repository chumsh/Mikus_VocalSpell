package com.chunshui.phit.mikus_vocal_spell.entity.spells.core_melt;

import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class InnocenceShellRender extends GeoEntityRenderer<InnocenceShellEntity> {
    public InnocenceShellRender(EntityRendererProvider.Context renderManager) {
        super(renderManager, new InnocenceShellModel());

    }
    @Override
    public boolean shouldRender(@NotNull InnocenceShellEntity entity, @NotNull Frustum frustum, double camX, double camY, double camZ) { return true; }
}
